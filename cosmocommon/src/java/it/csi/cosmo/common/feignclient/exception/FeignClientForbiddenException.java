/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.exception;

import org.springframework.web.client.HttpClientErrorException;
import it.csi.cosmo.common.dto.common.ErrorMessageDTO;
import it.csi.cosmo.common.feignclient.model.FeignClientRequestContext;

/**
 *
 */

/**
 *
 *
 */
public class FeignClientForbiddenException extends FeignClientClientErrorException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param statusCode
   */
  public FeignClientForbiddenException(FeignClientRequestContext requestContext,
      HttpClientErrorException e, ErrorMessageDTO response) {

    super(requestContext, e, response);
  }
}
