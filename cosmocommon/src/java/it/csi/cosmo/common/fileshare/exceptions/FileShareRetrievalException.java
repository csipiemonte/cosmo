/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.exceptions;

/**
 *
 */

public class FileShareRetrievalException extends FileShareException {

  /**
   *
   */
  private static final long serialVersionUID = 7438621493162065944L;

  public FileShareRetrievalException() {
  }

  public FileShareRetrievalException(String message) {
    super(message);
  }

  public FileShareRetrievalException(Throwable cause) {
    super(cause);
  }

  public FileShareRetrievalException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileShareRetrievalException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
