/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.exception;

/**
 *
 */

public class ActaContextBuildingException extends ActaRuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ActaContextBuildingException() {
    super();

  }

  public ActaContextBuildingException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public ActaContextBuildingException(String message, Throwable cause) {
    super(message, cause);

  }

  public ActaContextBuildingException(String message) {
    super(message);

  }

  public ActaContextBuildingException(Throwable cause) {
    super(cause);

  }

}
