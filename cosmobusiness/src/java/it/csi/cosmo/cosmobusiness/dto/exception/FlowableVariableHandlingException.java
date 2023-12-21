/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.dto.exception;

/**
 *
 */

public class FlowableVariableHandlingException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = -2671119359335236040L;

  public FlowableVariableHandlingException() {
    super();
  }

  public FlowableVariableHandlingException(String message) {
    super(message);
  }

  public FlowableVariableHandlingException(String message, Throwable cause) {
    super(message, cause);
  }

  public FlowableVariableHandlingException(Throwable cause) {
    super(cause);
  }

}
