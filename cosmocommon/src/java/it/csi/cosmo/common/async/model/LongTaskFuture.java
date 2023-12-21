/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async.model;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

/**
 *
 */

public class LongTaskFuture<T extends Serializable> {

  private String taskId;

  private LongTask<T> task;

  private CompletableFuture<T> future;

  private LongTaskFuture(Builder<T> builder) {
    this.task = builder.task;
    this.future = builder.future;
    this.taskId = builder.task.getUuid();
  }

  public String getTaskId() {
    return taskId;
  }

  public LongTask<T> getTask() {
    return task;
  }

  public CompletableFuture<T> getFuture() {
    return future;
  }

  /**
   * Creates builder to build {@link LongTaskFuture}.
   * 
   * @return created builder
   */
  public static <T extends Serializable> Builder<T> builder() {
    return new Builder<>();
  }

  /**
   * Builder to build {@link LongTaskFuture}.
   */
  public static final class Builder<T extends Serializable> {
    private LongTask<T> task;
    private CompletableFuture<T> future;

    private Builder() {}

    public Builder<T> withTask(LongTask<T> task) {
      this.task = task;
      return this;
    }

    public Builder<T> withFuture(CompletableFuture<T> future) {
      this.future = future;
      return this;
    }

    public LongTaskFuture<T> build() {
      return new LongTaskFuture<>(this);
    }
  }

}
