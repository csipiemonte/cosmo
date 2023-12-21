/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async.exception;

import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;

/**
 *
 */

/**
 *
 *
 */
public class LongTaskAwaitException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private transient LongTaskPersistableEntry task;

  /**
   *
   */
  public LongTaskAwaitException(LongTaskPersistableEntry task) {
    // Auto-generated constructor stub
    super(task.getErrorMessage());
    this.task = task;
  }

  /**
   * @param message
   */
  public LongTaskAwaitException(LongTaskPersistableEntry task, String message) {
    super(message);
    // Auto-generated constructor stub
    this.task = task;
  }

  /**
   * @param cause
   */
  public LongTaskAwaitException(LongTaskPersistableEntry task, Throwable cause) {
    super(cause);
    // Auto-generated constructor stub
    this.task = task;
  }

  /**
   * @param message
   * @param cause
   */
  public LongTaskAwaitException(LongTaskPersistableEntry task, String message, Throwable cause) {
    super(message, cause);
    // Auto-generated constructor stub
    this.task = task;
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public LongTaskAwaitException(LongTaskPersistableEntry task, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // Auto-generated constructor stub
    this.task = task;
  }

  /**
   * @return the task
   */
  public LongTaskPersistableEntry getTask() {
    return task;
  }

}
