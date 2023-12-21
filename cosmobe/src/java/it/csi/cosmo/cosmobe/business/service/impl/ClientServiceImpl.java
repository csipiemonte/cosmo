/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.business.service.impl;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.CredenzialiAutenticazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.Fruitore;
import it.csi.cosmo.cosmobe.business.service.ClientService;
import it.csi.cosmo.cosmobe.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobe.config.ParametriApplicativo;
import it.csi.cosmo.cosmobe.integration.apimgr.handler.ApiMgrJWTAssertionDecoder;
import it.csi.cosmo.cosmobe.integration.apimgr.model.ApiMgrConfig;
import it.csi.cosmo.cosmobe.integration.apimgr.model.ApiMgrJWTAssertion;
import it.csi.cosmo.cosmobe.integration.rest.CosmoAuthorizationFruitoriFeignClient;
import it.csi.cosmo.cosmobe.security.InMemoryClientRegistry;
import it.csi.cosmo.cosmobe.security.InMemoryClientRegistry.InMemoryClientRegistryEntry;
import it.csi.cosmo.cosmobe.security.Scopes;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;


/**
 * Implementazione del servizio per la gestione del client
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

  public static final String CLIENTINFO_REQUESTATTR = "appDataCurrentClient";

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "ClientServiceImpl");

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
      .withNome("COSMOBE")
      .withCodice("COSMOBE")
      .withScopes(Collections.emptyList())
      .withAnonimo(false)
      .build();
  //@formatter:on

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoAuthorizationFruitoriFeignClient fruitoriClient;

  @Autowired
  private ApiMgrJWTAssertionDecoder apiMgrJWTAssertionDecoder;

  @Autowired
  private InMemoryClientRegistry clientRegistry;

  @Override
  public ClientInfoDTO getClientCorrente() {
    HttpServletRequest req = RequestUtils.getCurrentRequest().orElse(null);
    logger.trace("getClientCorrente", "getClientCorrente from request");

    if (req == null) {
      return clientSistema;
    }

    ClientInfoDTO output = (ClientInfoDTO) req.getAttribute(CLIENTINFO_REQUESTATTR);

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

  private boolean isBypassed() {
    return configurazioneService.getConfig(ParametriApplicativo.AUTHENTICATION_CLIENT_BYPASS)
        .asBoolean();
  }

  protected ClientInfoDTO caricaClientInfo(HttpServletRequest request) {
    String methodName = "caricaClientInfo";
    logger.debug(methodName, "carico client info da header");

    // prima di tutto verifico se sono state fornite delle credenziali in basic authentication
    var basicAuthCredentials = getBasicAuthCredentials(request);
    ClientInfoDTO authenticated = null;

    if (basicAuthCredentials.isPresent()) {
      // verifico se posso autenticare in-memory
      authenticated = caricaClientInfoViaBasicAuthInMemory(request, basicAuthCredentials.get());
      if (authenticated != null) {
        return authenticated;
      }

      // verifica dell'autenticazione basic con credenziali dinamiche (interrogazione a
      // cosmoauthorization)
      authenticated =
          caricaClientInfoViaBasicAuthFromCosmoAuthorization(basicAuthCredentials.get());
      if (authenticated != null) {
        return authenticated;
      }

    } else if (isBypassed()) {
      // nessuna credenziale di autenticazione in input, se permesso il bypass carico le credenziali
      // mocked
      return caricaClientInfoMock(request);
    }

    // non e' stato possibile autenticare in nessun modo. ritorno null
    return null;
  }

  private ClientInfoDTO caricaClientInfoViaBasicAuthFromCosmoAuthorization(
      String[] basicAuthCredentials) {
    final var methodName = "caricaClientInfoViaBasicAuthFromCosmoAuthorization";

    var autenticaRequest = new CredenzialiAutenticazioneFruitore();
    autenticaRequest.setUsername(basicAuthCredentials[0]);
    autenticaRequest.setPassword(basicAuthCredentials[1]);
    var autenticaResponse = fruitoriClient.postFruitoriAutentica(autenticaRequest);

    var output = map(autenticaResponse);
    logger.debug(methodName, "caricato client {} da basic authentication con credenziali dinamiche",
        output.getCodice());

    return output;
  }

  private ClientInfoDTO caricaClientInfoViaBasicAuthInMemory(HttpServletRequest request,
      String[] basicAuthCredentials) {
    final var methodName = "caricaClientInfoViaBasicAuthInMemory";

    InMemoryClientRegistryEntry entry = clientRegistry.find(basicAuthCredentials[0]).orElse(null);

    if (entry == null) {
      return null;
    }

    try {
      // verifica della password in-memory
      clientRegistry.checkPassword(entry, basicAuthCredentials[1]);

    } catch (Exception e) {
      // non autenticabile in memory, continuo con altri metodi
      logger.warn(methodName, "request basic authentication as " + basicAuthCredentials[0]
          + " in-memory failed: " + e.getMessage());

      return null;
    }

    // verifica della password in-memory riuscita
    var basicAuthenticatedClient = entry.getClientInfo();

    // profilo il fruitore corrente
    logger.debug(methodName, "request basic authenticated in-memory as "
        + basicAuthenticatedClient.getNome() + " (" + basicAuthenticatedClient.getCodice() + ")");

    if (basicAuthenticatedClient.hasScope(Scopes.API_MANAGER.name())) {
      return caricaClientInfoDaApiManager(request, basicAuthenticatedClient);
    } else {
      return caricaClientInfoDiretto(basicAuthenticatedClient);
    }
  }

  private ClientInfoDTO caricaClientInfoMock(HttpServletRequest request) {
    final var method = "caricaClientInfoMock";

    var bypassHeader =
        RequestUtils.streamHeaders(request, Constants.HEADERS_PREFIX + "Fruitore", ",").findFirst();

    if (bypassHeader.isPresent()) {
      Fruitore fruitore = getFruitoreByApiManagerId(bypassHeader.get());

      ClientInfoDTO loaded = map(fruitore);
      logger.debug(method, "caricato user info " + loaded.getCodice() + " da header");

      return loaded;
    }

    return null;
  }

  private ClientInfoDTO caricaClientInfoDaApiManager(HttpServletRequest request,
      ClientInfoDTO basicAuthenticatedClient) {
    final var method = "caricaClientInfoDaApiManager";
    logger.debug(method,
        "carico client info da api manager per fruitore " + basicAuthenticatedClient.getNome());

    ClientInfoDTO output = basicAuthenticatedClient;
    Optional<String> clientAuthHeader = RequestUtils
        .streamHeaders(request, ApiMgrConfig.JWT_ASSERTION_HEADER_NAME, ",").findFirst();

    if (clientAuthHeader.isPresent()) {

      ApiMgrJWTAssertion decoded =
          apiMgrJWTAssertionDecoder.decodeAndVerify(clientAuthHeader.get());
      Fruitore fruitore = getFruitoreByApiManagerJWTAssertion(decoded);

      output = map(fruitore);
      logger.debug(method, "caricato user info " + output.getCodice() + " da header ApiManager");
    }

    return output;
  }

  private ClientInfoDTO caricaClientInfoDiretto(ClientInfoDTO basicAuthenticatedClient) {
    final var method = "caricaClientInfoDiretto";

    // verifico configurazione su cosmoauthorization
    Fruitore result = null;
    try {
      result = fruitoriClient.getFruitoriAmiApiManagerId(basicAuthenticatedClient.getCodice());
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        logger.warn("Fruitore con identificativo %s non configurato su Cosmo",
            basicAuthenticatedClient.getCodice());
      } else {
        throw e;
      }
    }

    // eseguo il merge della profilazione su DB + profilazione in - memory.
    // la profilazione su DB ha la precedenza.
    ClientInfoDTO output = result != null ? map(result) : basicAuthenticatedClient;
    if (result != null) {
      for (ScopeDTO inMemoryGrantedScope : basicAuthenticatedClient.getScopes()) {
        if (!output.hasScope(inMemoryGrantedScope.getCodice())) {
          output.getScopes().add(inMemoryGrantedScope);
        }
      }
    }

    logger.debug(method, "caricato client info per fruitore " + output.getNome());

    return output;
  }

  private Fruitore getFruitoreByApiManagerJWTAssertion(ApiMgrJWTAssertion decoded) {
    if (decoded == null || decoded.getPayload() == null
        || StringUtils.isBlank(decoded.getPayload().getSubscriber())) {
      throw new UnauthorizedException("No subscriber passed from ApiManager");
    }

    String idFruitore = decoded.getPayload().getApplicationName();

    return getFruitoreByApiManagerId(idFruitore);
  }

  private Fruitore getFruitoreByApiManagerId(String apiManagerId) {
    if (StringUtils.isBlank(apiManagerId)) {
      throw new UnauthorizedException("No apiManagerId passed");
    }

    Fruitore result;

    try {
      result = fruitoriClient.getFruitoriAmiApiManagerId(apiManagerId);
    } catch (HttpClientErrorException e) {
      if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
        throw new ForbiddenException(String.format(
            "Fruitore con identificativo %s non autorizzato dal sistema di Cosmo", apiManagerId));
      }
      throw e;
    }

    if (result == null) {
      throw new UnauthorizedException("Unrecognized client");
    }

    return result;
  }

  private ClientInfoDTO map(Fruitore fruitore) {
    var clientInfo =
        ClientInfoDTO.builder().withCodice(fruitore.getApiManagerId()).withAnonimo(false)
        .withScopes(fruitore.getAutorizzazioni().stream()
            .map(auth -> ScopeDTO.builder().withCodice(auth.getCodice().trim())
                .withDescrizione(auth.getDescrizione().trim()).build())
            .collect(Collectors.toList()))
        .withNome(fruitore.getNomeApp()).build();

    return clientInfo;
  }

  protected ClientInfoDTO caricaClientInfoMocked() {
    //@formatter:off
    return ClientInfoDTO.builder()
        .withNome("Test client")
        .withCodice("TEST")
        .withScopes(Arrays.asList(
            fromScope(Scopes.MONITORING)
            ))
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

  private Optional<String[]> getBasicAuthCredentials(HttpServletRequest hreq) {
    String methodName = "getBasicAuthCredentials";

    if (hreq == null) {
      logger.error(methodName, "Richiesta non verificabile - request = null");
      throw new InternalServerException("Richiesta non verificabile");
    }

    String basicHeader = RequestUtils.streamHeaders(hreq, "Authorization", ",")
        .filter(h -> h.toUpperCase().startsWith("BASIC ")).findFirst().orElse(null);

    if (basicHeader == null) {
      return Optional.empty();
    }


    String[] splitted = new String(Base64.getDecoder().decode(basicHeader.replace("Basic ", "")),
        CosmoAuthenticationConfig.ENCODING).split("\\:");

    return Optional.of(splitted);
  }
}
