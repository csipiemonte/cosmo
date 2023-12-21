/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.exception;

import java.nio.charset.StandardCharsets;
import org.springframework.web.client.HttpServerErrorException;
import it.csi.cosmo.common.dto.common.ErrorMessageDTO;
import it.csi.cosmo.common.feignclient.model.FeignClientRequestContext;

/**
 *
 */

/**
 *
 *
 */
public class FeignClientServerErrorException extends HttpServerErrorException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private FeignClientRequestContext requestContext;

  private HttpServerErrorException innerException;

  private ErrorMessageDTO response;

  /**
   * @param statusCode
   */
  public FeignClientServerErrorException(FeignClientRequestContext requestContext,
      HttpServerErrorException e, ErrorMessageDTO response) {

    super(e.getStatusCode(),
        response != null && response.getTitle() != null ? response.getTitle() : e.getStatusText(),
            e.getResponseHeaders(),
            e.getResponseBodyAsByteArray(), StandardCharsets.UTF_8);

    this.requestContext = requestContext;
    this.innerException = e;
    this.response = response;
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
  public HttpServerErrorException getInnerException() {
    return innerException;
  }

  /**
   * @return the response
   */
  public ErrorMessageDTO getResponse() {
    return response;
  }

}
