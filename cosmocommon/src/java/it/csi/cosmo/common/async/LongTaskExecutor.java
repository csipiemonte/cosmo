/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.async;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import it.csi.cosmo.common.async.internal.ContextAwarePoolExecutor;
import it.csi.cosmo.common.async.internal.LongTaskFeedbackBridge;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;
import it.csi.cosmo.common.async.model.LongTaskAwaitCallbacks;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.async.store.LongTaskInMemoryStore;
import it.csi.cosmo.common.async.store.LongTaskStorageAdapter;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;

public class LongTaskExecutor implements AutoCloseable {

  private final CosmoLogger logger;

  private static final Map<String, LongTask<?>> taskMap = new HashMap<>();

  private static final Map<String, Boolean> idToPublish = new ConcurrentHashMap<>();

  private static final Map<String, LocalDateTime> idToRemove = new ConcurrentHashMap<>();

  private LongTaskStepExecutor taskExecutor;

  private ContextAwarePoolExecutor executor;

  private ScheduledExecutorService scheduledExecutor;

  private LongTaskInMemoryStore localStorageAdapter;

  private LongTaskStorageAdapter storageAdapter;

  private LongTaskFeedbackBridge bridge;

  private RateLimitedProcessingQueue publishQueue;

  private LongTaskExecutor(Builder builder) {
    this.logger = builder.log;
    this.executor = new ContextAwarePoolExecutor();
    this.scheduledExecutor = Executors.newScheduledThreadPool(1);
    this.createBridge();

    this.localStorageAdapter = LongTaskInMemoryStore.builder().withLogger(logger).build();

    this.storageAdapter = builder.storageAdapter;

    if (builder.taskExecutor != null) {
      this.taskExecutor = builder.taskExecutor;
    } else {
      this.taskExecutor = DefaultLongTaskStepExecutor.builder().withLogger(this.logger).build();
    }

    publishQueue = RateLimitedProcessingQueue.builder().withLogger(this.logger)
        .withDispatchInterval(300).withMaxSize(1000).build();

    this.scheduledExecutor.scheduleWithFixedDelay(this::deleteExpired, 120, 120, TimeUnit.SECONDS);
  }

  private <T extends Serializable> LongTask<T> start(String name) {
    final var method = "start";
    LongTask<T> step = LongTask.start(this.bridge, name);
    taskMap.put(step.getUuid(), step);
    logger.debug(method, "initiated long task {}", step.getUuid());
    updated(step.getUuid(), true);
    return step;
  }

  public <T extends Serializable> LongTaskFuture<T> start(String name,
      Function<LongTask<T>, T> executor) {
    final var method = "start";
    LongTask<T> task = this.start(name);

    CompletableFuture<T> future = new CompletableFuture<>();

    this.executor.submit(() -> {
      T result;
      try {
        result = this.taskExecutor.apply(executor, task);
        task.complete(result);
        future.complete(result);
      } catch (Throwable e) { // NOSONAR
        task.fail(e);
        logger.error(method, "Error in long task", e);
        future.completeExceptionally(e);
      }
    });

    return LongTaskFuture.<T>builder().withTask(task).withFuture(future).build();
  }

  public LongTaskPersistableEntry get(String id) {
    final var method = "get";
    logger.debug(method, "getting status of task {}", id);

    synchronized (localStorageAdapter) {
      logger.debug(method, "getting status of task {} from local storage", id);
      var local = localStorageAdapter.get(id);
      if (local != null) {
        return toPersistableEntry(local);
      }
      if (storageAdapter != null) {
        logger.debug(method, "fetching status of task {} from storage adapter", id);
        var external = storageAdapter.get(id);
        if (external != null) {
          return external;
        }
      }

      logger.debug(method, "no task with identifier {} found", id);
      return null;
    }
  }

  public void deleteDelayed(String id) {
    int delay = 30;
    if (!idToRemove.containsKey(id)) {
      idToRemove.put(id, LocalDateTime.now().plusSeconds(5));
      logger.debug("deleteDelayed", "scheduling task {} for removal in {} seconds", delay);
    }
  }

  public void delete(String id) {
    final var method = "delete";
    logger.debug(method, "removing task with identifier {}", id);
    synchronized (localStorageAdapter) {
      if (taskMap.containsKey(id)) {
        taskMap.remove(id);
      }
      localStorageAdapter.delete(id);
      if (storageAdapter != null) {
        storageAdapter.delete(id);
      }
      idToRemove.remove(id);
    }
  }

  public void updated(String id, boolean immediate) {
    final var method = "updated";
    logger.debug(method, "received updated signal for task {}", id);

    synchronized (localStorageAdapter) {
      if (immediate) {
        logger.debug(method, "attempting to force publish task {}", id);
        publish(id);
      } else {
        if (!idToPublish.containsKey(id)) {
          logger.debug(method, "pushed to publish queue for task {}", id);
          idToPublish.put(id, true);
          publishQueue.submit(this::dequeueUpdates);
        } else {
          logger.debug(method, "task {} is queued already", id);
        }
      }
    }
  }

  public Future<LongTaskPersistableEntry> watch(String uuid, Long timeout, LongTaskAwaitCallbacks callbacks) {
    if (timeout == null) {
      timeout = 600L;
    }

    LongTaskWatcher watcher = LongTaskWatcher.builder()
        .withLogger(logger)
        .withLongTaskExecutor(this)
        .withTimeout(timeout)
        .withCallbacks(callbacks)
        .withUuid(uuid)
        .build();

    return watcher.start();
  }

  public Future<LongTaskPersistableEntry> watch(String uuid, Long timeout) {
    return watch(uuid, timeout, null);
  }

  private void dequeueUpdates() {
    final var method = "dequeueUpdates";
    logger.debug(method, "dequeueing updates from queue");
    synchronized (localStorageAdapter) {
      if (idToPublish.isEmpty()) {
        return;
      }
      var keys = idToPublish.keySet().stream().collect(Collectors.toSet());
      for (String key : keys) {
        logger.debug(method, "removing from publish queue for task {}", key);
        idToPublish.remove(key);
        publish(key);
      }
    }
  }

  private void publish(String id) {
    final var method = "publish";
    try {
      logger.debug(method, "attempting to publish task {}", id);
      LongTask<?> task = taskMap.getOrDefault(id, null);
      if (task != null) {
        logger.debug(method, "publishing task {} to local storage", id);
        localStorageAdapter.save(task.copy());
        if (storageAdapter != null) {
          logger.debug(method, "publishing task {} to remote storage", id);
          storageAdapter.save(toPersistableEntry(task));
        }
        if (task.getStatus() != null && task.getStatus() != LongTaskStatus.STARTED) {
          // task is finished
          scheduleForRemoval(task);
        }
      } else {
        logger.warn(method, "publishing task {} failed: task not found", id);
      }
    } catch (Exception e) {
      logger.error(method, "error publishing task", e);
    }
  }

  private void scheduleForRemoval(LongTask<?> task) {
    final var method = "scheduleForRemoval";
    if (!idToRemove.containsKey(task.getUuid())) {
      idToRemove.put(task.getUuid(), LocalDateTime.now().plusSeconds(3600));
      logger.debug(method, "scheduling task {} for removal");
    }
  }

  private void deleteExpired() {
    if (!idToRemove.isEmpty()) {
      var toRemove = idToRemove.entrySet().stream()
          .filter(e -> e.getValue().isBefore(LocalDateTime.now())).collect(Collectors.toSet());
      logger.debug("deleteExpired", "deleting {} expired tasks", toRemove.size());
      toRemove.forEach(e -> delete(e.getKey()));
    }
  }

  public static LongTaskPersistableEntry toPersistableEntry(LongTask<?> input) {
    LongTaskPersistableEntry output = new LongTaskPersistableEntry();

    output.setErrorDetails(input.getErrorDetails());
    output.setErrorMessage(input.getErrorMessage());
    output.setFinishedAt(input.getFinishedAt());
    output.setUuid(input.getUuid());
    output.setMessages(input.getMessages());
    output.setName(input.getName());
    output.setResult(ObjectUtils.toJson(input.getResult()));
    output.setStartedAt(input.getStartedAt());
    output.setStatus(input.getStatus());

    if (input.getSteps() != null) {
      synchronized (input.getSteps()) {
        output.setSteps(input.getSteps() == null ? null
            : input.getSteps().stream().map(LongTaskExecutor::toPersistableEntry)
                .collect(Collectors.toCollection(LinkedList::new)));
      }
    }

    output.setVersion(input.getVersion());

    return output;
  }

  private void createBridge() {
    @SuppressWarnings("resource")
    final var parent = this;
    this.bridge = new LongTaskFeedbackBridge() {

      @Override
      public <T extends Serializable> void updated(LongTask<T> task, boolean immediate) {
        parent.updated(task.getUuid(), immediate);
      }

      @Override
      public <V extends Serializable> V applyStep(Function<LongTask<V>, V> executor,
          LongTask<V> step) {
        return parent.taskExecutor.apply(executor, step);
      }
    };
  }

  /**
   * Creates builder to build {@link LongTaskExecutor}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link LongTaskExecutor}.
   */
  public static final class Builder {
    private CosmoLogger log;
    private LongTaskStepExecutor taskExecutor;
    private LongTaskStorageAdapter storageAdapter;

    private Builder() {}

    public Builder withStorageAdapter(LongTaskStorageAdapter storageAdapter) {
      this.storageAdapter = storageAdapter;
      return this;
    }

    public Builder withLogger(CosmoLogger log) {
      this.log = log;
      return this;
    }

    public Builder withTaskExecutor(LongTaskStepExecutor taskExecutor) {
      this.taskExecutor = taskExecutor;
      return this;
    }

    public LongTaskExecutor build() {
      return new LongTaskExecutor(this);
    }
  }

  @Override
  public void close() throws Exception {
    if (this.executor != null) {
      this.executor.shutdown();
    }
    if (this.scheduledExecutor != null) {
      this.scheduledExecutor.shutdownNow();
    }
    if (this.publishQueue != null) {
      this.publishQueue.close();
    }
  }

}
