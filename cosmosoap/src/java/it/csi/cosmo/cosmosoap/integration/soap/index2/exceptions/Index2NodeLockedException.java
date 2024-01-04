/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions;

/**
 *
 */

public class Index2NodeLockedException extends Index2Exception
    implements Index2RetryiableException {

  /**
   *
   */
  private static final long serialVersionUID = -2181361259574109555L;

  public Index2NodeLockedException() {
    // NOP
  }

  public Index2NodeLockedException(String message) {
    super(message);
    // NOP
  }

  public Index2NodeLockedException(Throwable cause) {
    super(cause);
    // NOP
  }

  public Index2NodeLockedException(String message, Throwable cause) {
    super(message, cause);
    // NOP
  }

  public Index2NodeLockedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // NOP
  }

}
