/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.exception;

/**
 *
 */

public class ActaTooManyResultsException extends ActaRuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ActaTooManyResultsException() {
    super();

  }

  public ActaTooManyResultsException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public ActaTooManyResultsException(String message, Throwable cause) {
    super(message, cause);

  }

  public ActaTooManyResultsException(String message) {
    super(message);

  }

  public ActaTooManyResultsException(Throwable cause) {
    super(cause);

  }

}
