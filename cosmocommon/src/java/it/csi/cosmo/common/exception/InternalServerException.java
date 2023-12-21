/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.exception;

import org.springframework.http.HttpStatus;

/**
 *
 */

public class InternalServerException extends ManagedException {

  /**
   *
   */
  private static final long serialVersionUID = 1979539207711562091L;

  private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

  public InternalServerException(String message, Throwable cause) {
    super(STATUS.value(), STATUS.name(), message, cause, null);
  }

  public InternalServerException(String message) {
    super(STATUS.value(), STATUS.name(), message, null, null);
  }

  public InternalServerException() {
    super(STATUS.value(), STATUS.name(), STATUS.getReasonPhrase(), null, null);
  }

}
