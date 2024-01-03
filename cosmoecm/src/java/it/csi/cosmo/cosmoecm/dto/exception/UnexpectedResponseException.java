/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto.exception;

/**
 *
 */

public class UnexpectedResponseException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = -7538251044491266283L;

  public UnexpectedResponseException() {
    // Auto-generated constructor stub
  }

  public UnexpectedResponseException(String message) {
    super(message);
    // Auto-generated constructor stub
  }

  public UnexpectedResponseException(Throwable cause) {
    super(cause);
    // Auto-generated constructor stub
  }

  public UnexpectedResponseException(String message, Throwable cause) {
    super(message, cause);
    // Auto-generated constructor stub
  }

  public UnexpectedResponseException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // Auto-generated constructor stub
  }

}
