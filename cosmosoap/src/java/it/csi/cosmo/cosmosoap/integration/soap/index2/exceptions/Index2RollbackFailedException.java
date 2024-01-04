/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions;

/**
 *
 */

public class Index2RollbackFailedException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public Index2RollbackFailedException() {
    super();

  }

  public Index2RollbackFailedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public Index2RollbackFailedException(String message, Throwable cause) {
    super(message, cause);

  }

  public Index2RollbackFailedException(String message) {
    super(message);

  }

  public Index2RollbackFailedException(Throwable cause) {
    super(cause);

  }

}
