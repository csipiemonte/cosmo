/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.security.handler.CosmoJWTTokenEncoder;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.config.Constants;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.security.InMemoryClientRegistry.InMemoryClientRegistryEntry;
import it.csi.cosmo.cosmo.util.logger.LoggerConstants;


@Component
public class AuthenticationTokenManager {

  @Autowired
  private InMemoryClientRegistry clientRegistry;

  @Autowired
  private ConfigurazioneService configurazioneService;

  private String getConfiguredSecret() {
    return configurazioneService.requireConfig(ParametriApplicativo.INTERNAL_JWT_SECRET).asString();
  }

  private String getConfiguredIssuer() {
    return configurazioneService.requireConfig(ParametriApplicativo.INTERNAL_JWT_ISSUER).asString();
  }

  private String getConfiguredAlgorithm() {
    return configurazioneService.requireConfig(ParametriApplicativo.INTERNAL_JWT_ALGORITHM)
        .asString();
  }

  public String getSignedTokenForCurrentUser() {
    UserInfoDTO currentUser = SecurityUtils.getUtenteCorrente();

    return new CosmoJWTTokenEncoder(getConfiguredAlgorithm(), getConfiguredSecret(),
        LoggerConstants.ROOT_LOG_CATEGORY)
        .buildAndSign(currentUser, getConfiguredIssuer(), currentUser.getCodiceFiscale());
  }

  public String getSignedTokenForOrchestrator() {
    ClientInfoDTO currentClient = clientRegistry.find(Constants.ORCHESTRATOR_USERNAME)
        .map(InMemoryClientRegistryEntry::getClientInfo).orElseThrow(InternalServerException::new);

    return new CosmoJWTTokenEncoder(getConfiguredAlgorithm(), getConfiguredSecret(),
        LoggerConstants.ROOT_LOG_CATEGORY)
        .buildAndSign(currentClient, getConfiguredIssuer(), currentClient.getCodice());
  }

  public String getSignedTokenForCurrentClient() {
    ClientInfoDTO currentClient = SecurityUtils.getClientCorrente();

    return new CosmoJWTTokenEncoder(getConfiguredAlgorithm(), getConfiguredSecret(),
        LoggerConstants.ROOT_LOG_CATEGORY)
        .buildAndSign(currentClient, getConfiguredIssuer(), currentClient.getCodice());
  }

}
