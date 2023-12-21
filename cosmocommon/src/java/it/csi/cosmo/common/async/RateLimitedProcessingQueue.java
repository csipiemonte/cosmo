/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.async;

import java.io.Closeable;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.core.task.TaskRejectedException;
import it.csi.cosmo.common.logger.CosmoLogger;

public class RateLimitedProcessingQueue implements Closeable {

  private final CosmoLogger log;

  private LinkedList<Runnable> queue;

  private int dispatchInterval = 2000;

  private int maxSize = 100;

  private ExecutorService executor;

  private ScheduledExecutorService pollExecutor;

  public Double getOccupationPercentage() {
    if (this.queue == null) {
      return null;
    } else {
      int queueSize = this.queue.size();
      return 100.0 * queueSize / this.maxSize;
    }
  }

  public void runTask() {
    final var method = "runTask";
    if (this.queue.isEmpty()) {
      return;
    }

    synchronized (this.queue) {
      if (!this.queue.isEmpty()) {
        log.debug(method, "running task from queue");
        Runnable task = this.queue.removeFirst();
        this.executor.submit(wrapTask(task));
      }
    }
  }

  private Runnable wrapTask(Runnable task) {
    return () -> {
      try {
        task.run();
      } catch (Throwable t) { // NOSONAR
        log.error("wrapTask", "error in task dispatched by RateLimitedProcessingQueue", t);
      }
    };
  }

  public void submit(Runnable task) {
    final var method = "submit";
    log.debug(method, "submitting task to queue");
    synchronized (this.queue) {
      if (this.queue.size() >= this.maxSize) {
        throw new TaskRejectedException("Queue reached max size");
      }

      this.queue.add(task);
    }
  }

  private RateLimitedProcessingQueue(Builder builder) {
    this.log = builder.logger;
    this.dispatchInterval = builder.dispatchInterval;
    this.maxSize = builder.maxSize;

    if (this.maxSize < 1 || this.dispatchInterval < 10) {
      throw new InvalidParameterException();
    }

    this.queue = new LinkedList<>();
    this.executor = Executors.newCachedThreadPool();
    this.pollExecutor = Executors.newScheduledThreadPool(1);

    this.pollExecutor.scheduleAtFixedRate(this::runTask, this.dispatchInterval,
        this.dispatchInterval, TimeUnit.MILLISECONDS);
  }

  /**
   * Creates builder to build {@link RateLimitedProcessingQueue}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link RateLimitedProcessingQueue}.
   */
  public static final class Builder {

    private CosmoLogger logger;

    private int dispatchInterval = 1000;

    private int maxSize = 100;

    private Builder() {}

    public Builder withLogger(CosmoLogger logger) {
      this.logger = logger;
      return this;
    }

    public Builder withDispatchInterval(int dispatchInterval) {
      this.dispatchInterval = dispatchInterval;
      return this;
    }

    public Builder withMaxSize(int maxSize) {
      this.maxSize = maxSize;
      return this;
    }

    public RateLimitedProcessingQueue build() {
      return new RateLimitedProcessingQueue(this);
    }
  }

  @Override
  public void close() throws IOException {
    if (this.pollExecutor != null) {
      this.pollExecutor.shutdownNow();
    }

    if (this.executor != null) {
      this.executor.shutdownNow();
    }
  }
}
