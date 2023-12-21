/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.exception;

/**
 *
 */

public class MisconfigurationException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = -6851348418618093003L;

  public MisconfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  public MisconfigurationException(String message) {
    super(message);
  }
}
