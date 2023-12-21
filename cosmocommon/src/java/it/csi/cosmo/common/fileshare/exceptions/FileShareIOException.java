/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.exceptions;

/**
 *
 */

public class FileShareIOException extends FileShareException {

  /**
   *
   */
  private static final long serialVersionUID = 7438621493162065944L;

  public FileShareIOException() {
  }

  public FileShareIOException(String message) {
    super(message);
  }

  public FileShareIOException(Throwable cause) {
    super(cause);
  }

  public FileShareIOException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileShareIOException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
