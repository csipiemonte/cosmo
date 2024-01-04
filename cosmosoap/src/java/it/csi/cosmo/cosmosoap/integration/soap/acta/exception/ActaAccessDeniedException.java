/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.exception;

/**
 *
 */

public class ActaAccessDeniedException extends ActaRuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ActaAccessDeniedException() {
    super();

  }

  public ActaAccessDeniedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public ActaAccessDeniedException(String message, Throwable cause) {
    super(message, cause);

  }

  public ActaAccessDeniedException(String message) {
    super(message);

  }

  public ActaAccessDeniedException(Throwable cause) {
    super(cause);

  }

}
