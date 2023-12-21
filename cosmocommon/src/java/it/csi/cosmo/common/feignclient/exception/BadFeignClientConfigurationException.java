/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.exception;

/**
 *
 */

public class BadFeignClientConfigurationException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1625842305991694374L;

  public BadFeignClientConfigurationException() {
    // NOP
  }

  public BadFeignClientConfigurationException(String message) {
    super(message);
    // NOP
  }

  public BadFeignClientConfigurationException(Throwable cause) {
    super(cause);
    // NOP
  }

  public BadFeignClientConfigurationException(String message, Throwable cause) {
    super(message, cause);
    // NOP
  }

  public BadFeignClientConfigurationException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // NOP
  }

}
