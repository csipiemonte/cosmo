/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.dto.exception;

/**
 *
 */

public class FlowableEventMessageException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 7683782720641391013L;

  public FlowableEventMessageException() {
    super();
  }

  public FlowableEventMessageException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public FlowableEventMessageException(String message, Throwable cause) {
    super(message, cause);
  }

  public FlowableEventMessageException(String message) {
    super(message);
  }

  public FlowableEventMessageException(Throwable cause) {
    super(cause);
  }


}
