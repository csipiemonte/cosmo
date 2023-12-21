/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.exception;

/**
 *
 */

public class FeignClientRegistrationException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1625842305991694374L;

  public FeignClientRegistrationException() {
    // NOP
  }

  public FeignClientRegistrationException(String message) {
    super(message);
    // NOP
  }

  public FeignClientRegistrationException(Throwable cause) {
    super(cause);
    // NOP
  }

  public FeignClientRegistrationException(String message, Throwable cause) {
    super(message, cause);
    // NOP
  }

  public FeignClientRegistrationException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    // NOP
  }

}
