/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.exception;

/**
 *
 */

public class ActaModificationFailedException extends ActaRuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ActaModificationFailedException() {
    super();

  }

  public ActaModificationFailedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public ActaModificationFailedException(String message, Throwable cause) {
    super(message, cause);

  }

  public ActaModificationFailedException(String message) {
    super(message);

  }

  public ActaModificationFailedException(Throwable cause) {
    super(cause);

  }

}
