/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.feignclient.model.FeignClientBeforeRequestInterceptor;
import it.csi.cosmo.common.feignclient.model.FeignClientContext;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;
import it.csi.cosmo.cosmo.config.Constants;
import it.csi.cosmo.cosmo.security.AuthenticationTokenManager;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;

/**
 *
 */

@Component
public class FeignClientJWTConfigurator implements FeignClientBeforeRequestInterceptor {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.SECURITY_LOG_CATEGORY, "FeignClientJWTConfigurator");

  @Autowired
  private AuthenticationTokenManager authenticationTokenManager;

  @Override
  public void beforeRequest(HttpRequest request, FeignClientContext context) {

    String methodName = "intercept";
    LOGGER.debug(methodName, "aggiungo authentication header a chiamata feign");

    // request.getHeaders().add(CosmoAuthenticationConfig.TRANSMISSION_HEADER_USER,
    // authenticationTokenManager.getSignedTokenForCurrentUser());

    request.getHeaders().add(CosmoAuthenticationConfig.TRANSMISSION_HEADER_CLIENT,
        authenticationTokenManager.getSignedTokenForOrchestrator());

    // request.getHeaders().add(CosmoAuthenticationConfig.TRANSMISSION_HEADER_ORIGINAL_CLIENT,
    // authenticationTokenManager.getSignedTokenForCurrentClient());

    request.getHeaders().add(it.csi.cosmo.common.config.Constants.HEADERS_PREFIX + "Sender-Name",
        Constants.COMPONENT_NAME);
  }

}
