/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions;

/**
 *
 */

public class Index2DuplicateNodeException extends Index2Exception implements Index2FatalException {

  /**
   *
   */
  private static final long serialVersionUID = -2181361259574109555L;

  public Index2DuplicateNodeException() {
    // NOP
  }

  public Index2DuplicateNodeException(String message) {
    super(message);
    // NOP
  }

  public Index2DuplicateNodeException(Throwable cause) {
    super(cause);
    // NOP
  }

  public Index2DuplicateNodeException(String message, Throwable cause) {
    super(message, cause);
    // NOP
  }

  public Index2DuplicateNodeException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // NOP
  }

}
