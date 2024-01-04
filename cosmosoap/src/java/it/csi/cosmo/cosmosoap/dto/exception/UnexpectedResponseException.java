/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.dto.exception;

/**
 *
 */

public class UnexpectedResponseException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = -7538251044491266283L;

  public UnexpectedResponseException() {
    // nop
  }

  public UnexpectedResponseException(String message) {
    super(message);

  }

  public UnexpectedResponseException(Throwable cause) {
    super(cause);

  }

  public UnexpectedResponseException(String message, Throwable cause) {
    super(message, cause);

  }

  public UnexpectedResponseException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

}
