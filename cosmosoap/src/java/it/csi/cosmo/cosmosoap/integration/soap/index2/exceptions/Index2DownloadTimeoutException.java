/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions;

/**
 *
 */

public class Index2DownloadTimeoutException extends Index2Exception {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public Index2DownloadTimeoutException() {
    super();
    // nop
  }

  public Index2DownloadTimeoutException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);

  }

  public Index2DownloadTimeoutException(String message, Throwable cause) {
    super(message, cause);

  }

  public Index2DownloadTimeoutException(String message) {
    super(message);

  }

  public Index2DownloadTimeoutException(Throwable cause) {
    super(cause);

  }

}
