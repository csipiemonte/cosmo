/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.exception;

/**
 *
 */

public abstract class ActaRuntimeException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ActaRuntimeException() {
    super();

  }

  public ActaRuntimeException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public ActaRuntimeException(String message, Throwable cause) {
    super(message, cause);

  }

  public ActaRuntimeException(String message) {
    super(message);

  }

  public ActaRuntimeException(Throwable cause) {
    super(cause);

  }

}
