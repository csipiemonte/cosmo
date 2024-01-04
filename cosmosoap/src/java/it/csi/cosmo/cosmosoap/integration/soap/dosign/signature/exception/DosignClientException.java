/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.dosign.signature.exception;

import it.csi.cosmo.cosmosoap.integration.dto.DosignOutcomeDTO;

/**
 *
 */
public class DosignClientException extends Exception {
  private static final long serialVersionUID = 1L;

  private DosignOutcomeDTO exceptionData;

  public DosignClientException() {
    super();
  }

  public DosignClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, DosignOutcomeDTO exceptionData) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.exceptionData = exceptionData;
  }

  public DosignClientException(String message, Throwable cause, DosignOutcomeDTO exceptionData) {
    super(message, cause);
    this.exceptionData = exceptionData;
  }

  public DosignClientException(String message, DosignOutcomeDTO exceptionData) {
    super(message);
  }

  public DosignClientException(String message) {
    super(message);
  }

  public DosignClientException(Throwable cause, DosignOutcomeDTO exceptionData) {
    super(cause);
    this.exceptionData = exceptionData;
  }

  /**
   * @return the exceptionData
   */
  public DosignOutcomeDTO getExceptionData() {
    return exceptionData;
  }
}
