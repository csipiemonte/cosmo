/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.service.impl;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.cosmo.business.service.ProfilazioneClientService;
import it.csi.cosmo.cosmo.security.InMemoryClientRegistry;
import it.csi.cosmo.cosmo.security.InMemoryClientRegistry.InMemoryClientRegistryEntry;
import it.csi.cosmo.cosmo.security.Scopes;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;

/**
 * Implementazione del servizio per la gestione della profilazione del client
 */
@Service
@Transactional
public class ProfilazioneClientServiceImpl implements ProfilazioneClientService {

  public static final String CLIENTINFO_REQUESTATTR = "appDataCurrentClient";

  private static final CosmoLogger logger = LoggerFactory
      .getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "ProfilazioneClientServiceImpl");

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
      .withNome("COSMO")
      .withCodice("COSMO")
      .withScopes(Collections.emptyList())
      .withAnonimo(false)
      .build();
  //@formatter:on

  public static ClientInfoDTO getClientAnonimo() {
    return clientAnonimo;
  }

  public static ClientInfoDTO getClientSistema() {
    return clientSistema;
  }

  @Autowired
  private InMemoryClientRegistry clientRegistry;

  @Override
  public ClientInfoDTO caricaClientInfo(HttpServletRequest request) {
    String methodName = "caricaClientInfo";
    logger.debug(methodName, "carico client info da header");

    String userAuthHeader = request.getHeader("Authorization");
    if (!StringUtils.isEmpty(userAuthHeader)) {
      String[] splitted =
          new String(Base64.getDecoder().decode(userAuthHeader.replace("Basic ", "")),
              CosmoAuthenticationConfig.ENCODING).split("\\:");

      InMemoryClientRegistryEntry entry = clientRegistry.find(splitted[0])
          .orElseThrow(() -> new ForbiddenException("Fruitore non riconosciuto"));

      clientRegistry.checkPassword(entry, splitted[1]);

      return entry.getClientInfo();

    } else {
      return null;
    }
  }

  @Override
  public ClientInfoDTO caricaClientInfoTest(HttpServletRequest request) {
    //@formatter:off
    return ClientInfoDTO.builder()
        .withNome("Test client")
        .withCodice("TEST")
        .withScopes(Arrays.asList(
            fromScope(Scopes.MONITORING),
            fromScope(Scopes.DISCOVERY_REGISTER))
            )
        .withAnonimo(false)
        .build();
    //@formatter:on
  }

  private ScopeDTO fromScope(String auth) {
    //@formatter:off
    return ScopeDTO.builder ()
        .withCodice ( auth )
        .withDescrizione ( auth )
        .build ();
    //@formatter:on
  }

}
