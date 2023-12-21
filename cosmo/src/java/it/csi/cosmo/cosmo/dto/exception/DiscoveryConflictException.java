/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto.exception;

import org.springframework.http.HttpStatus;
import it.csi.cosmo.common.exception.ManagedException;

/**
 *
 */

public class DiscoveryConflictException extends ManagedException {

  /**
   *
   */
  private static final long serialVersionUID = 1979539207711562091L;

  private static final HttpStatus STATUS = HttpStatus.CONFLICT;

  public DiscoveryConflictException(String message) {
    super(STATUS.value(), STATUS.name(), message, null, null);
  }

}
