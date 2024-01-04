/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.exception;

/**
 *
 */

public class ActaQueryFailedException extends ActaRuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ActaQueryFailedException() {
    super();

  }

  public ActaQueryFailedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public ActaQueryFailedException(String message, Throwable cause) {
    super(message, cause);

  }

  public ActaQueryFailedException(String message) {
    super(message);

  }

  public ActaQueryFailedException(Throwable cause) {
    super(cause);

  }

}
