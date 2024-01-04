/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.exception;

/**
 *
 */

public abstract class ActaClientException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ActaClientException() {
    super();

  }

  public ActaClientException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public ActaClientException(String message, Throwable cause) {
    super(message, cause);

  }

  public ActaClientException(String message) {
    super(message);

  }

  public ActaClientException(Throwable cause) {
    super(cause);

  }

}
