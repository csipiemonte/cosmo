/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.feignclient.model.FeignClientBeforeRequestInterceptor;
import it.csi.cosmo.common.feignclient.model.FeignClientContext;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;
import it.csi.cosmo.cosmobe.config.Constants;
import it.csi.cosmo.cosmobe.security.AuthenticationTokenManager;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;

/**
 *
 */

@Component
public class FeignClientM2MConfigurator implements FeignClientBeforeRequestInterceptor {

  private static final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.SECURITY_LOG_CATEGORY, "FeignClientM2MConfigurator");

  @Autowired
  private AuthenticationTokenManager authenticationTokenManager;

  @Override
  public void beforeRequest(HttpRequest request, FeignClientContext context) {

    String methodName = "intercept";
    LOGGER.debug(methodName, "aggiungo authentication header a chiamata feign");

    request.getHeaders().add(CosmoAuthenticationConfig.TRANSMISSION_HEADER_CLIENT,
        authenticationTokenManager.getSignedTokenForOrchestrator());

    request.getHeaders().add(it.csi.cosmo.common.config.Constants.HEADERS_PREFIX + "Sender-Name",
        Constants.COMPONENT_NAME);

    // nota: NON TRASMETTO ORIGINAL-CLIENT
  }

}
