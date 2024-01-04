/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.exception;

/**
 *
 */

public class ActaModelTranslationException extends ActaRuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ActaModelTranslationException() {
    super();

  }

  public ActaModelTranslationException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public ActaModelTranslationException(String message, Throwable cause) {
    super(message, cause);

  }

  public ActaModelTranslationException(String message) {
    super(message);

  }

  public ActaModelTranslationException(Throwable cause) {
    super(cause);

  }

}
