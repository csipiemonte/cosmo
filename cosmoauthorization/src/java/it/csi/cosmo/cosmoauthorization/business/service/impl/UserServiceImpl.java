/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;
import it.csi.cosmo.common.security.handler.CosmoJWTTokenDecoder;
import it.csi.cosmo.common.security.model.AuthenticationToken;
import it.csi.cosmo.common.security.model.IdentitaDTO;
import it.csi.cosmo.common.security.model.LivelloAutenticazione;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.business.service.UserService;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerConstants;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

/**
 * Implementazione del servizio per la gestione dell'utente, della profilazione e della sessione
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "UserServiceImpl");

  public static final String USERINFO_REQUESTATTR = "appDataCurrentUser";

  //@formatter:off
  private static UserInfoDTO utenteAnonimo = UserInfoDTO.builder ()
      .withIdentita ( null )
      .withNome ( "GUEST" )
      .withCognome ( "" )
      .withCodiceFiscale ( "GUEST" )
      .withAnonimo ( true )
      .build ();
  //@formatter:on

  //@formatter:off
  private static UserInfoDTO utenteSistema = UserInfoDTO.builder ()
      .withIdentita ( null )
      .withNome ( "SYSTEM" )
      .withCognome ( "" )
      .withCodiceFiscale ( "SYSTEM" )
      .withAnonimo ( true )
      .build ();
  //@formatter:on

  @Autowired
  private ConfigurazioneService configurazioneService;

  /**
   * {@inheritDoc}
   *
   * Se nessun utente e' correntemente profilato (utente anonimo),
   * viene ritornato un oggetto UserInfo costruito appositamente per l'utente anonimo, avente
   * una lista di Authorities vuota e un codice ruolo pari alla costante Constants.RUOLI.ANONIMO;
   * In questo modo posso utilizzare l'entita' 'utente corrente'
   * coerentemente in tutto l'applicativo, sapendo che non sara' mai NULL,
   * semplificando notevolmente i controlli delle autorizzazioni basati sulle Authorities
   */
  @Override
  public synchronized UserInfoDTO getUtenteCorrente() {
    HttpServletRequest req = RequestUtils.getCurrentRequest().orElse(null);
    if ( req == null ) {
      return utenteSistema;
    }

    UserInfoDTO output =
        (UserInfoDTO) req.getAttribute(USERINFO_REQUESTATTR);

    if (output == null) {
      output = caricaDaRequest(req);
      req.setAttribute(USERINFO_REQUESTATTR, output);
    }

    return output;
  }

  private UserInfoDTO caricaDaRequest(HttpServletRequest req) {
    final UserInfoDTO output;

    if (isMocked()) {
      output = caricaUserInfoMocked();
    } else {
      output = caricaUserInfo(req);
    }

    if (output != null) {
      return output;
    } else {
      return utenteAnonimo;
    }
  }

  private boolean isMocked() {
    return configurazioneService.getConfig(ParametriApplicativo.AUTHENTICATION_USER_MOCK_ENABLE)
        .asBoolean();
  }

  protected UserInfoDTO caricaUserInfo(HttpServletRequest request) {

    String methodName = "caricaUserInfo";
    logger.debug(methodName, "carico user info da header");

    String userAuthHeader = request.getHeader(CosmoAuthenticationConfig.TRANSMISSION_HEADER_USER);

    if (!StringUtils.isEmpty(userAuthHeader)) {
      AuthenticationToken<UserInfoDTO> authToken = new CosmoJWTTokenDecoder(
          configurazioneService.requireConfig(ParametriApplicativo.INTERNAL_JWT_SECRET).asString(),
          LoggerConstants.ROOT_LOG_CATEGORY).decodeAndVerify(userAuthHeader, UserInfoDTO.class);

      UserInfoDTO loaded = authToken.getPayload().getContent();
      logger.debug(methodName, "caricato user info " + loaded.getCodiceFiscale() + " da header");

      return loaded;
    } else {
      return null;
    }
  }

  protected UserInfoDTO caricaUserInfoMocked() {
    //@formatter:off
    return UserInfoDTO.builder()
        .withId(1234L)
        .withNome("Test")
        .withCognome("User")
        .withCodiceFiscale("AAAAAA00B77B000F")
        .withAnonimo(false)
        .withIdentita(IdentitaDTO.builder()
            .withCodFiscale("AAAAAA00B77B000F")
            .withCognome("User")
            .withIdProvider("MOCK")
            .withLivelloAutenticazione(LivelloAutenticazione.NON_RICONOSCIUTO)
            .withMac("NONE")
            .withNome("Test")
            .withRappresentazioneInterna("NONE")
            .withTimestamp(String.valueOf(Instant.now().getEpochSecond()))
            .build()
            )
        .build();
    //@formatter:on
  }
}
