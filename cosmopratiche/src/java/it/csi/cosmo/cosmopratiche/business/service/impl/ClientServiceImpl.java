/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.util.Arrays;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;
import it.csi.cosmo.common.security.handler.CosmoJWTTokenDecoder;
import it.csi.cosmo.common.security.model.AuthenticationToken;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmopratiche.business.service.ClientService;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.security.Scopes;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerConstants;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

/**
 * Implementazione del servizio per la gestione del client
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

  public static final String CLIENTINFO_REQUESTATTR = "appDataCurrentClient";

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, "ClientServiceImpl");

  //@formatter:off
  private static ClientInfoDTO clientAnonimo = ClientInfoDTO.builder()
      .withNome("GUEST")
      .withCodice("GUEST")
      .withScopes(Collections.emptyList())
      .withAnonimo(true)
      .build();
  //@formatter:on

  //@formatter:off
  private static ClientInfoDTO clientSistema = ClientInfoDTO.builder()
      .withNome("COSMOPRATICHE")
      .withCodice("COSMOPRATICHE")
      .withScopes(Collections.emptyList())
      .withAnonimo(false)
      .build();
  //@formatter:on

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  public ClientInfoDTO getClientCorrente() {
    HttpServletRequest req = RequestUtils.getCurrentRequest().orElse(null);
    logger.trace("getClientCorrente", "getClientCorrente from request");

    if (req == null) {
      return clientSistema;
    }

    ClientInfoDTO output =
        (ClientInfoDTO) req.getAttribute(CLIENTINFO_REQUESTATTR);

    if (output == null) {
      output = caricaDaRequest(req);
      req.setAttribute(CLIENTINFO_REQUESTATTR, output);
    }

    return output;
  }

  private ClientInfoDTO caricaDaRequest(HttpServletRequest req) {
    final ClientInfoDTO output;

    if (isMocked()) {
      output = caricaClientInfoMocked();
    } else {
      output = caricaClientInfo(req);
    }

    if (output != null) {
      return output;
    } else {
      return clientAnonimo;
    }
  }

  private boolean isMocked() {
    return configurazioneService.getConfig(ParametriApplicativo.AUTHENTICATION_CLIENT_MOCK_ENABLE)
        .asBoolean();
  }

  protected ClientInfoDTO caricaClientInfo(HttpServletRequest request) {
    String methodName = "caricaClientInfo";
    logger.debug(methodName, "carico client info da header");

    String userAuthHeader = request.getHeader(CosmoAuthenticationConfig.TRANSMISSION_HEADER_CLIENT);

    if (!StringUtils.isEmpty(userAuthHeader)) {
      AuthenticationToken<ClientInfoDTO> authToken = new CosmoJWTTokenDecoder(
          configurazioneService.requireConfig(ParametriApplicativo.INTERNAL_JWT_SECRET).asString(),
          LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY).decodeAndVerify(userAuthHeader,
              ClientInfoDTO.class);

      ClientInfoDTO loaded = authToken.getPayload().getContent();
      logger.debug(methodName, "caricato user info " + loaded.getCodice() + " da header");

      return loaded;
    } else {
      return null;
    }
  }

  protected ClientInfoDTO caricaClientInfoMocked() {
    //@formatter:off
    return ClientInfoDTO.builder()
        .withNome("Test client")
        .withCodice("TEST")
        .withScopes(Arrays.asList(
            fromScope(Scopes.MONITORING),
            fromScope(Scopes.DEFAULT_CLIENT))
            )
        .withAnonimo(false)
        .build();
    //@formatter:on
  }

  private ScopeDTO fromScope(Scopes auth) {
    //@formatter:off
    return ScopeDTO.builder ()
        .withCodice ( auth.name () )
        .withDescrizione ( auth.getDescrizione () )
        .build ();
    //@formatter:on
  }

}
