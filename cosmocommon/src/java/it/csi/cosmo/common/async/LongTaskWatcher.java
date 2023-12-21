/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import it.csi.cosmo.common.async.exception.LongTaskFailedException;
import it.csi.cosmo.common.async.exception.LongTaskTimedOutException;
import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;
import it.csi.cosmo.common.async.model.LongTaskAwaitCallbacks;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.logger.CosmoLogger;

/**
 *
 *
 */
public class LongTaskWatcher implements AutoCloseable {

  private static final long STARTING_FETCH_INTERVAL = 1000L;

  private static final long MIN_FETCH_INTERVAL = 1000L;

  private static final long MAX_FETCH_INTERVAL = 5000L;

  private static final long FETCH_INTERVAL_FACTOR_ON_CHANGE = -2000;

  private static final long FETCH_INTERVAL_FACTOR_ON_NO_CHANGE = 1500;

  private final CosmoLogger logger;

  private final String uuid;

  private final LongTaskExecutor longTaskExecutor;

  private final LongTaskAwaitCallbacks callbacks;

  private final Long timeout;

  private long currentFetchInterval = 0L;

  private Long lastVersion = null;

  private ScheduledExecutorService scheduledExecutor;

  private boolean fetching = false;

  private boolean done = false;

  private LocalDateTime startedAt = null;

  private LocalDateTime finishedAt = null;

  private CompletableFuture<LongTaskPersistableEntry> output = null;

  private LongTaskWatcher(Builder builder) {
    this.logger = builder.logger;
    this.longTaskExecutor = builder.longTaskExecutor;
    this.callbacks = builder.callbacks;
    this.uuid = builder.uuid;
    this.timeout = builder.timeout;
    validate();
  }

  protected void validate() {
    if (this.logger == null) {
      throw new InvalidParameterException("Logger is required");
    }
    if (this.longTaskExecutor == null) {
      throw new InvalidParameterException("LongTaskExecutor is required");
    }
    if (this.uuid == null) {
      throw new InvalidParameterException("UUID is required");
    }
    if (this.timeout == null) {
      throw new InvalidParameterException("Timeout is required");
    }
  }

  public Future<LongTaskPersistableEntry> start() {
    final var method = "start";
    logger.debug(method, "starting watcher for task {}", this.uuid);

    this.scheduledExecutor = Executors.newScheduledThreadPool(1);

    output = new CompletableFuture<>();
    startedAt = LocalDateTime.now();
    currentFetchInterval = STARTING_FETCH_INTERVAL;

    attemptFetchChanges();

    return output;
  }

  protected void attemptFetchChanges() {
    final var method = "attemptFetchChanges";
    if (done) {
      return;
    }
    if (fetching) {
      logger.warn(method, "fetching of changes skipped because another one is in progress");
      return;
    }

    // attempt to fetch
    this.fetching = true;
    LongTaskPersistableEntry fetched = null;
    try {
      fetched = fetchChanges();
    } catch (Exception e) {
      logger.error(method, "error fetching changes for async task " + uuid, e);
    } finally {
      this.fetching = false;
    }

    if (fetched != null && fetched.getStatus() != null
        && fetched.getStatus() != LongTaskStatus.STARTED) {
      done = true;
    }

    if (!done) {
      if (startedAt.until(LocalDateTime.now(), ChronoUnit.SECONDS) >= timeout) {
        // timed out
        timedOut(fetched);
      } else {
        // reschedule
        rescheduleFetch();
      }
    } else {
      if (fetched != null && fetched.getStatus() == LongTaskStatus.COMPLETED) {
        succeeded(fetched);
      } else {
        failed(fetched);
      }
    }
  }

  protected void finished() {
    final var method = "finished";
    logger.debug(method, "waiting for async task {} has finished", uuid);

    finishedAt = LocalDateTime.now();
    this.disposeResources();

    try {
      this.longTaskExecutor.deleteDelayed(uuid);
    } catch (Exception e) {
      logger.error(method, "error deleting async task entry " + uuid, e);
    }
  }

  protected void rescheduleFetch() {
    logger.debug("rescheduleFetch", "rescheduling status check for task {} in {} ms", uuid,
        currentFetchInterval);
    scheduledExecutor.schedule(this::attemptFetchChanges, currentFetchInterval,
        TimeUnit.MILLISECONDS);
  }

  protected void succeeded(LongTaskPersistableEntry fetched) {
    final var method = "succeeded";
    logger.debug(method, "waiting for async task {} has succeeded", uuid);
    this.finished();

    output.complete(fetched);

    if (callbacks != null) {
      callbacks.completed(fetched);
      callbacks.finalizing(fetched);
    }
  }

  protected void failed(LongTaskPersistableEntry fetched) {
    final var method = "failed";
    logger.error(method, "waiting for async task {} has failed", uuid);
    this.finished();

    output.completeExceptionally(new LongTaskFailedException(fetched));

    if (callbacks != null) {
      callbacks.failed(fetched);
      callbacks.finalizing(fetched);
    }
  }

  protected void timedOut(LongTaskPersistableEntry fetched) {
    final var method = "timedOut";
    logger.error(method, "waiting for async task {} has timed out", uuid);
    this.finished();

    output.completeExceptionally(new LongTaskTimedOutException(fetched));

    if (callbacks != null) {
      callbacks.timedOut(fetched);
      callbacks.finalizing(fetched);
    }
  }

  protected synchronized LongTaskPersistableEntry fetchChanges() {
    final var method = "fetchChanges";
    logger.debug(method, "fetching changes for task {}", uuid);

    LongTaskPersistableEntry fetched = this.longTaskExecutor.get(uuid);

    boolean changed = fetched.getVersion() != null
        && (lastVersion == null || !fetched.getVersion().equals(lastVersion));

    if (!changed) {
      logger.debug(method, "no changes for task {}", uuid);
      currentFetchInterval += FETCH_INTERVAL_FACTOR_ON_NO_CHANGE;
      if (currentFetchInterval > MAX_FETCH_INTERVAL) {
        currentFetchInterval = MAX_FETCH_INTERVAL;
      }

    } else {
      logger.debug(method, "detected some changes for task {}", uuid);
      currentFetchInterval += FETCH_INTERVAL_FACTOR_ON_CHANGE;
      if (currentFetchInterval < MIN_FETCH_INTERVAL) {
        currentFetchInterval = MIN_FETCH_INTERVAL;
      }

      if (callbacks != null) {
        callbacks.updated(fetched);
      }
    }

    lastVersion = fetched.getVersion();

    return fetched;
  }

  /**
   * Creates builder to build {@link LongTaskWatcher}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link LongTaskWatcher}.
   */
  public static final class Builder {
    private CosmoLogger logger;
    private LongTaskExecutor longTaskExecutor;
    private String uuid;
    private Long timeout;
    private LongTaskAwaitCallbacks callbacks;

    private Builder() {}

    public Builder withCallbacks(LongTaskAwaitCallbacks callbacks) {
      this.callbacks = callbacks;
      return this;
    }

    public Builder withTimeout(Long timeout) {
      this.timeout = timeout;
      return this;
    }

    public Builder withUuid(String uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder withLogger(CosmoLogger logger) {
      this.logger = logger;
      return this;
    }

    public Builder withLongTaskExecutor(LongTaskExecutor longTaskExecutor) {
      this.longTaskExecutor = longTaskExecutor;
      return this;
    }

    public LongTaskWatcher build() {
      return new LongTaskWatcher(this);
    }
  }

  private void disposeResources() {
    logger.debug("disposeResources", "disposing watcher resources for task {}", this.uuid);
    if (this.scheduledExecutor != null) {
      this.scheduledExecutor.shutdownNow();
    }
  }

  @Override
  public void close() throws Exception {
    disposeResources();
  }

  /**
   * @return the done
   */
  public boolean isDone() {
    return done;
  }

  /**
   * @param done the done to set
   */
  public void setDone(boolean done) {
    this.done = done;
  }

  /**
   * @return the uuid
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * @return the startedAt
   */
  public LocalDateTime getStartedAt() {
    return startedAt;
  }

  /**
   * @return the finishedAt
   */
  public LocalDateTime getFinishedAt() {
    return finishedAt;
  }

}
