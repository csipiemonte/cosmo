/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.exception;

import org.springframework.http.HttpStatus;

/**
 *
 */

public class NotFoundException extends ManagedException {

  /**
   *
   */
  private static final long serialVersionUID = 1979539207711562092L;

  private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

  public NotFoundException(String message, Throwable cause) {
    super(STATUS.value(), STATUS.name(), message, cause, null);
  }

  public NotFoundException(String message) {
    super(STATUS.value(), STATUS.name(), message, null, null);
  }

  public NotFoundException() {
    super(STATUS.value(), STATUS.name(), STATUS.getReasonPhrase(), null, null);
  }

}
