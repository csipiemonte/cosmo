/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare.exceptions;

/**
 *
 */

public class FileShareBadConfigurationException extends FileShareException {

  /**
   *
   */
  private static final long serialVersionUID = 7438621493162065944L;

  public FileShareBadConfigurationException() {
  }

  public FileShareBadConfigurationException(String message) {
    super(message);
  }

  public FileShareBadConfigurationException(Throwable cause) {
    super(cause);
  }

  public FileShareBadConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileShareBadConfigurationException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
