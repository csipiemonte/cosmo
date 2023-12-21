/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.exception;

import org.springframework.http.HttpStatus;


/**
 *
 */

public class ManagedException extends RuntimeException {

  private final int status;

  private final String codice;

  private final Object response;

  private static final long serialVersionUID = -7478790655529042486L;

  public static ManagedException withStatus(HttpStatus status) {
    return new ManagedException(status.value(), status.name(), status.getReasonPhrase(), null,
        null);
  }

  public static ManagedException withMessage(HttpStatus status, String message) {
    return new ManagedException(status.value(), status.name(), message, null, null);
  }

  public static ManagedException withCause(HttpStatus status, String message, Throwable cause) {
    return new ManagedException(status.value(), status.name(), message, cause, null);
  }

  public static ManagedException withResponse(HttpStatus status, Object response) {
    return new ManagedException(status.value(), status.name(), null, null, response);
  }

  protected ManagedException(int status, String codice, String message, Throwable cause,
      Object response) {
    // private constructor
    super(message, cause);
    this.status = status;
    this.codice = codice;
    this.response = response;
  }

  public Object getResponse() {
    return response;
  }

  public int getStatus() {
    return status;
  }

  public String getCodice() {
    return codice;
  }

}
