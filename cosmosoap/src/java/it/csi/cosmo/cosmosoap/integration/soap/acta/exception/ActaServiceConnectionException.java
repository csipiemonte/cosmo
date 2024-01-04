/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.exception;

/**
 *
 */

public class ActaServiceConnectionException extends ActaClientException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ActaServiceConnectionException() {
    super();

  }

  public ActaServiceConnectionException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public ActaServiceConnectionException(String message, Throwable cause) {
    super(message, cause);

  }

  public ActaServiceConnectionException(String message) {
    super(message);

  }

  public ActaServiceConnectionException(Throwable cause) {
    super(cause);

  }

}
