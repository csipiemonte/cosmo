/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.async;

import java.io.Closeable;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import org.springframework.core.task.TaskRejectedException;
import it.csi.cosmo.common.logger.CosmoLogger;

public class BatchProcessingQueue<T> implements Closeable {

  private final CosmoLogger log;

  private LinkedList<T> queue;

  private int dispatchInterval = 2000;

  private Integer maxSize = 100;

  private ExecutorService executor;

  private ScheduledExecutorService pollExecutor;

  private Consumer<List<T>> consumer;

  private boolean parallel = true;

  private boolean fixedDelay = false;

  private AtomicLong runningCounter = new AtomicLong(0);

  private boolean disposing = false;

  public long getQueuedItems() {
    if (this.queue == null) {
      return 0;
    } else {
      return this.queue.size();
    }
  }

  public Double getOccupationPercentage() {
    if (this.queue == null) {
      return null;
    } else if (maxSize != null) {
      int queueSize = this.queue.size();
      return 100.0 * queueSize / this.maxSize;
    } else {
      return 0.0;
    }
  }

  private void dequeuePeriodically() {
    final var method = "dequeuePeriodically";

    if (!parallel && this.runningCounter.get() > 0L) {
      log.warn(method,
          "skipping batch execution as another is in progress and overlapping executions are disabled");
      return;
    }

    try {
      this.runningCounter.incrementAndGet();
      runTask();
    } finally {
      this.runningCounter.decrementAndGet();
    }
  }

  private void runTask() {
    final var method = "runTask";
    if (this.queue.isEmpty()) {
      return;
    }

    List<T> tasks = new LinkedList<>();

    synchronized (this.queue) {
      if (!this.queue.isEmpty()) {
        T x;
        while ((x = this.queue.pollFirst()) != null) {
          tasks.add(x);
        }
      }
    }

    if (!tasks.isEmpty()) {
      if (parallel) {
        log.debug(method, "running {} tasks from queue in parallel mode", tasks.size());
        this.executor.submit(() -> executeBatch(tasks));
      } else {
        log.debug(method, "running {} tasks from queue in sequential mode", tasks.size());
        executeBatch(tasks);
      }
    }
  }

  private void executeBatch(List<T> payloads) {
    try {
      this.consumer.accept(payloads);
    } catch (Throwable t) { // NOSONAR
      log.error("wrapTask", "error in task dispatched by BatchProcessingQueue", t);
    }
  }

  public void submit(T payload) {
    final var method = "submit";
    if (disposing) {
      throw new TaskRejectedException("Queue is disposing");
    }

    log.debug(method, "submitting task to queue");
    synchronized (this.queue) {
      if (maxSize != null && this.queue.size() >= this.maxSize) {
        throw new TaskRejectedException("Queue reached max size");
      }

      this.queue.add(payload);
    }
  }

  private BatchProcessingQueue(Builder<T> builder) {
    this.log = builder.logger != null ? builder.logger
        : new CosmoLogger("cosmo.test", this.getClass().getSimpleName());

    this.dispatchInterval = builder.dispatchInterval;
    this.maxSize = builder.maxSize;
    this.consumer = builder.consumer;
    this.parallel = builder.parallel;
    this.fixedDelay = builder.fixedDelay;

    if ((this.maxSize != null && this.maxSize < 1) || this.dispatchInterval < 10) {
      throw new InvalidParameterException();
    }

    this.queue = new LinkedList<>();

    if (parallel) {
      this.executor = Executors.newCachedThreadPool();
    }

    this.pollExecutor = Executors.newScheduledThreadPool(1);

    if (fixedDelay) {
      this.pollExecutor.scheduleWithFixedDelay(this::dequeuePeriodically, this.dispatchInterval,
          this.dispatchInterval, TimeUnit.MILLISECONDS);
    } else {
      this.pollExecutor.scheduleAtFixedRate(this::dequeuePeriodically, this.dispatchInterval,
          this.dispatchInterval, TimeUnit.MILLISECONDS);
    }
  }

  public void shutdown(long timeout, TimeUnit unit, boolean attemptExecution)
      throws InterruptedException {
    this.disposing = true;

    if (pollExecutor != null) {
      pollExecutor.shutdownNow();
    }

    if (executor != null) {
      executor.shutdown();
      if (!executor.awaitTermination(timeout, unit)) {
        executor.shutdownNow();
      }
    }

    if (attemptExecution) {
      synchronized (queue) {
        if (!queue.isEmpty()) {
          List<T> tasks = new LinkedList<>();
          T x;
          while ((x = this.queue.pollFirst()) != null) {
            tasks.add(x);
          }

          if (!tasks.isEmpty()) {
            log.warn("shutdown",
                "batch processing queue will attempt to consume {} remaining tasks before shutdown",
                tasks.size());
            executeBatch(tasks);
          }
        }
      }
    }
  }

  public void shutdownNow(boolean attemptExecution) {
    this.disposing = true;

    if (pollExecutor != null) {
      pollExecutor.shutdownNow();
    }

    if (executor != null) {
      executor.shutdownNow();
    }

    if (attemptExecution) {
      synchronized (queue) {
        if (!queue.isEmpty()) {
          List<T> tasks = new LinkedList<>();
          T x;
          while ((x = this.queue.pollFirst()) != null) {
            tasks.add(x);
          }

          if (!tasks.isEmpty()) {
            log.warn("shutdown",
                "batch processing queue will attempt to consume {} remaining tasks before shutdown",
                tasks.size());
            executeBatch(tasks);
          }
        }
      }
    }
  }

  @Override
  public void close() throws IOException {
    try {
      this.shutdown(5, TimeUnit.SECONDS, true);
    } catch (InterruptedException e) {
      log.warn("close", "error cleaning up queue", e);
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Creates builder to build {@link BatchProcessingQueue}.
   * 
   * @return created builder
   */
  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  /**
   * Builder to build {@link BatchProcessingQueue}.
   */
  public static final class Builder<T> {

    private CosmoLogger logger;

    private int dispatchInterval = 5000;

    private Integer maxSize = null;

    private Consumer<List<T>> consumer;

    private boolean parallel = false;

    private boolean fixedDelay = true;

    private Builder() {}

    public Builder<T> withFixedDelay(boolean fixedDelay) {
      this.fixedDelay = fixedDelay;
      return this;
    }

    public Builder<T> withParallel(boolean parallel) {
      this.parallel = parallel;
      return this;
    }

    public Builder<T> withConsumer(Consumer<List<T>> consumer) {
      this.consumer = consumer;
      return this;
    }

    public Builder<T> withLogger(CosmoLogger logger) {
      this.logger = logger;
      return this;
    }

    public Builder<T> withDispatchInterval(int dispatchInterval) {
      this.dispatchInterval = dispatchInterval;
      return this;
    }

    public Builder<T> withMaxSize(Integer maxSize) {
      this.maxSize = maxSize;
      return this;
    }

    public BatchProcessingQueue<T> build() {
      return new BatchProcessingQueue<>(this);
    }
  }

}
