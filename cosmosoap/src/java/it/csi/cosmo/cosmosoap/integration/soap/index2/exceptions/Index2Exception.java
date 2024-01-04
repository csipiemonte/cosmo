/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions;

/**
 *
 */

public class Index2Exception extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = -2181361259574109555L;

  public Index2Exception() {
    // NOP
  }

  public Index2Exception(String message) {
    super(message);
    // NOP
  }

  public Index2Exception(Throwable cause) {
    super(cause);
    // NOP
  }

  public Index2Exception(String message, Throwable cause) {
    super(message, cause);
    // NOP
  }

  public Index2Exception(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // NOP
  }

}
