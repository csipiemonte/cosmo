/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.proto;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */

public class FeignClientLoggingInterceptor implements ClientHttpRequestInterceptor {

  private Logger logger;

  public FeignClientLoggingInterceptor(String loggingPrefix) {
    super();
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".feignclient.FeignClientLoggingInterceptor");
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {

    String feignClientRayId = request.getHeaders().get(Constants.FEIGN.FEIGN_RAY_ID_HEADER).stream()
        .findFirst().orElse("<unknown>");

    String prefix = "[feign " + feignClientRayId + "] ";


    if (logger.isDebugEnabled()) {
      logger
      .debug(prefix + "-> preparing to send http feign request " + request.getMethod() + " to "
          + request.getURI());

      if (request.getHeaders() != null) {
        for (Entry<String, List<String>> header : request.getHeaders().entrySet()) {
          logger.debug(prefix + "-> request header: [" + header.getKey() + "] = ["
              + header.getValue() + "]");
        }
      }

    }

    logger.debug("sending http feign request " + request.getMethod() + " to " + request.getURI());

    try {
      ClientHttpResponse r = execution.execute(request, body);

      logger.debug("got HTTP " + r.getStatusCode() + " in response from " + request.getURI());

      if (logger.isDebugEnabled()) {
        logger.debug(prefix + "<- " + r.getStatusCode() + " from " + request.getURI());
        logger.debug(prefix + "<- raw response from " + request.getURI() + ": "
            + ObjectUtils.represent(r.getBody()));
        for (Entry<String, List<String>> header : r.getHeaders().entrySet()) {
          logger.debug(prefix + "<- response header: [" + header.getKey() + "] = ["
              + header.getValue() + "]");
        }
      }

      return r;
    } catch (HttpStatusCodeException e) {

      logger
      .error(
          prefix + "error (HttpStatusCodeException) in feign request to " + request.getURI()
          + ": " + e.getStatusCode() + " - " + e.getStatusText() + " - " + e.getMessage(),
          e);

      if (logger.isDebugEnabled()) {
        logger.debug(prefix + "<- " + e.getStatusCode() + " - " + e.getStatusText() + " from "
            + request.getURI());
        logger.debug(prefix + "<- error response from " + request.getURI() + ": "
            + e.getResponseBodyAsString());
      }
      throw e;
    } catch (RestClientException e) {
      logger.error(prefix + "error (RestClientException) in feign request to " + request.getURI()
      + ": " + e.getMessage(), e);

      throw e;
    } catch (Exception e) {
      logger.error(prefix + "error (generic Exception) in feign request to " + request.getURI()
      + ": " + e.getMessage(), e);
      throw e;
    }
  }
}
