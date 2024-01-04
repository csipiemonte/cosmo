/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.dto.exception;

/**
 *
 */

public class UnknownSignatureException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = -7538251044491266283L;

  public UnknownSignatureException() {
    // nop
  }

  public UnknownSignatureException(String message) {
    super(message);

  }

  public UnknownSignatureException(Throwable cause) {
    super(cause);

  }

  public UnknownSignatureException(String message, Throwable cause) {
    super(message, cause);

  }

  public UnknownSignatureException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

}
