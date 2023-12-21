/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.exception;

import org.springframework.http.HttpStatus;

/**
 *
 */

public class PayloadTooLargeException extends ManagedException {

  /**
   *
   */

  private static final long serialVersionUID = 642356998753386173L;

  private static final HttpStatus STATUS = HttpStatus.PAYLOAD_TOO_LARGE;

  public PayloadTooLargeException(String message, Throwable cause) {
    super(STATUS.value(), STATUS.name(), message, cause, null);
  }

  public PayloadTooLargeException(String message) {
    super(STATUS.value(), STATUS.name(), message, null, null);
  }

  public PayloadTooLargeException() {
    super(STATUS.value(), STATUS.name(), STATUS.getReasonPhrase(), null, null);
  }

}
