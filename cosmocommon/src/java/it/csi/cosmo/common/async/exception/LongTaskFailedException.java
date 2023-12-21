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
public class LongTaskFailedException extends LongTaskAwaitException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public Throwable getOriginalException() {
    Throwable e = this;
    while (e.getCause() != null && e.getCause() instanceof LongTaskFailedException) {
      e = e.getCause();
    }
    return e;
  }

  public LongTaskFailedException(LongTaskPersistableEntry task) {
    // Auto-generated constructor stub
    super(task);
  }

  /**
   * @param message
   */
  public LongTaskFailedException(LongTaskPersistableEntry task, String message) {
    super(task, message);
    // Auto-generated constructor stub
  }

  /**
   * @param cause
   */
  public LongTaskFailedException(LongTaskPersistableEntry task, Throwable cause) {
    super(task, cause);
    // Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   */
  public LongTaskFailedException(LongTaskPersistableEntry task, String message, Throwable cause) {
    super(task, message, cause);
    // Auto-generated constructor stub
  }

  /**
   * @param message
   * @param cause
   * @param enableSuppression
   * @param writableStackTrace
   */
  public LongTaskFailedException(LongTaskPersistableEntry task, String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(task, message, cause, enableSuppression, writableStackTrace);
    // Auto-generated constructor stub
  }

}
