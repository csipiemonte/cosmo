/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto;

/**
 *
 */

/**
 *
 *
 */
public class TransactionExecutionResult<T> {

  private boolean success;
  private Throwable error;
  private T result;

  public static <T> TransactionExecutionResult<T> forSuccess(T result) {
    return TransactionExecutionResult.<T>builder().withResult(result).withSuccess(true).build();
  }

  public static <T> TransactionExecutionResult<T> forFailure(Throwable t) {
    return TransactionExecutionResult.<T>builder().withError(t).withSuccess(false).build();
  }

  private TransactionExecutionResult(Builder<T> builder) {
    this.success = builder.success;
    this.error = builder.error;
    this.result = builder.result;
  }

  /**
   * @return the success
   */
  public boolean success() {
    return success;
  }

  public boolean failed() {
    return !success;
  }

  /**
   * @return the error
   */
  public Throwable getError() {
    return error;
  }

  /**
   * @return the result
   */
  public T getResult() {
    return result;
  }

  @Override
  public String toString() {
    return "TransactionExecutionResult [success=" + success + ", error=" + error + "]";
  }

  /**
   * Creates builder to build {@link TransactionExecutionResult}.
   *
   * @return created builder
   */
  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  /**
   * Builder to build {@link TransactionExecutionResult}.
   */
  public static final class Builder<T> {
    private boolean success;
    private Throwable error;
    private T result;

    private Builder() {}

    public Builder<T> withSuccess(boolean success) {
      this.success = success;
      return this;
    }

    public Builder<T> withError(Throwable error) {
      this.error = error;
      return this;
    }

    public Builder<T> withResult(T result) {
      this.result = result;
      return this;
    }

    public TransactionExecutionResult<T> build() {
      return new TransactionExecutionResult<>(this);
    }
  }

}
