/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.exception;

import org.springframework.web.client.RestClientException;
import it.csi.cosmo.common.feignclient.model.FeignClientRequestContext;

/**
 *
 */

/**
 *
 *
 */
public class FeignClientRestException extends RestClientException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private FeignClientRequestContext requestContext;

  private Exception innerException;

  /**
   * @param statusCode
   */
  public FeignClientRestException(FeignClientRequestContext requestContext, Exception e) {
    super(e.getMessage(), e.getCause());
    this.requestContext = requestContext;
    this.innerException = e;
  }

  /**
   * @return the requestContext
   */
  public FeignClientRequestContext getRequestContext() {
    return requestContext;
  }

  /**
   * @return the innerException
   */
  public Exception getInnerException() {
    return innerException;
  }

}
