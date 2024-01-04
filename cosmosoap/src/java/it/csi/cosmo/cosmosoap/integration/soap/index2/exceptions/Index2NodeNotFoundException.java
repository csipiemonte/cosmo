/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions;

/**
 *
 */

public class Index2NodeNotFoundException extends Index2Exception implements Index2FatalException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public Index2NodeNotFoundException() {
    super();

  }

  public Index2NodeNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public Index2NodeNotFoundException(String message, Throwable cause) {
    super(message, cause);

  }

  public Index2NodeNotFoundException(String message) {
    super(message);

  }

  public Index2NodeNotFoundException(Throwable cause) {
    super(cause);

  }

}
