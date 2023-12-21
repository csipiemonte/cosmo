/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.exception;

import org.springframework.http.HttpStatus;

/**
 *
 */

public class ConflictException extends ManagedException {

  /**
   *
   */
  private static final long serialVersionUID = 1979539207711562091L;

  private static final HttpStatus STATUS = HttpStatus.CONFLICT;

  public ConflictException(String message, Throwable cause) {
    super(STATUS.value(), STATUS.name(), message, cause, null);
  }

  public ConflictException(String message) {
    super(STATUS.value(), STATUS.name(), message, null, null);
  }

  public ConflictException() {
    super(STATUS.value(), STATUS.name(), STATUS.getReasonPhrase(), null, null);
  }

}
