/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.exceptions;

/**
 *
 */

public abstract class FileShareException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 7438621493162065944L;

  public FileShareException() {
  }

  public FileShareException(String message) {
    super(message);
  }

  public FileShareException(Throwable cause) {
    super(cause);
  }

  public FileShareException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileShareException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
