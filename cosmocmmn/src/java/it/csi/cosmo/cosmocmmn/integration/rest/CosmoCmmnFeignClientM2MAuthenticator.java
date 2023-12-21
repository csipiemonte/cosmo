/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.integration.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.feignclient.model.FeignClientContext;
import it.csi.cosmo.common.feignclient.proto.BasicAuthenticationInterceptor;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmocmmn.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmocmmn.config.Constants;
import it.csi.cosmo.cosmocmmn.config.ParametriApplicativo;

/**
 *
 */

@Component
public class CosmoCmmnFeignClientM2MAuthenticator extends BasicAuthenticationInterceptor {

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  protected String getPassword() {
    return configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENT_PASSWORD)
        .asString();
  }

  @Override
  protected String getUsername() {
    return configurazioneService.requireConfig(ParametriApplicativo.DISCOVERY_CLIENT_USERNAME)
        .asString();
  }

  @Override
  protected void addAdditionalHeaders(HttpRequest request, FeignClientContext context) {
    // forward current user header
    RequestUtils.getCurrentRequest().ifPresent(currentRequest -> {

      request.getHeaders().add(it.csi.cosmo.common.config.Constants.HEADERS_PREFIX + "Sender-Name",
          Constants.COMPONENT_NAME);
    });
  }
}
