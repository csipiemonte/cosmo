/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.annotations.interception.HeaderDecoratorPrecedence;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;
import it.csi.cosmo.common.security.model.IdentitaDTO;
import it.csi.cosmo.common.security.model.LivelloAutenticazione;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.business.service.UserService;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.security.SecurityUtils;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;


/**
 * Filtro delegato all'inserimento in sessione:
 * <ul>
 * <li>dell'identit&agrave; digitale relativa all'utente autenticato.
 * <li>dell'oggetto <code>UserInfo</code> contenente la profilazione per l'utente corrente
 * </ul>
 * Funge da adapter tra il filter del metodo di autenticaizone previsto e la logica applicativa.
 *
 * @author CSIPiemonte
 */
@Provider
@HeaderDecoratorPrecedence
@Priority(11000)
public class AuthenticationFilter implements ContainerRequestFilter, ContainerResponseFilter {

  protected static final CosmoLogger LOG =
      LoggerFactory.getLogger(LogCategory.FILTER_LOG_CATEGORY, "AuthenticationFilter");

  public static final String IRIDE_ID_SESSIONATTR = "iride2_id";

  public static final String AUTH_ID_MARKER = "Shib-Iride-IdentitaDigitale";

  public static final String USERINFO_SESSIONATTR = "appDatacurrentUser";

  private static final String HEADER_ENTE_SELEZIONATO = Constants.HEADERS_PREFIX + "Ente";

  private static final String HEADER_PROFILO_SELEZIONATO = Constants.HEADERS_PREFIX + "Profilo";

  private boolean isEnabled() {
    ConfigurazioneService configurazioneService = ConfigurazioneService.getInstance();
    return configurazioneService.getConfig(ParametriApplicativo.ENABLE_AUTHENTICATION_FILTER)
        .asBoolean();
  }

  private boolean isBypassed() {
    ConfigurazioneService configurazioneService = ConfigurazioneService.getInstance();
    return configurazioneService.getConfig(ParametriApplicativo.BYPASS_AUTHENTICATION_FILTER)
        .asBoolean();
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String methodName = "filter";
    if (!isEnabled()) {
      return;
    }

    HttpServletRequest hreq = RequestUtils.getCurrentRequest().orElse(null);

    Long idEnte = getIdEnteSelezionato(hreq).orElse(null);
    Long idProfilo = getIdProfiloSelezionato(hreq).orElse(null);
    String marker = getToken(hreq);
    IdentitaDTO identita = null;

    if (marker != null) {
      try {
        identita = getIdentitaFromToken(marker);
      } catch (Exception e) {
        throw new UnauthorizedException("Errore nella lettura del token iride", e);
      }
    }

    if (hreq.getSession().getAttribute(IRIDE_ID_SESSIONATTR) == null) {
      if (marker != null) {
        LOG.info(methodName, "loading identity for the first time");
        loadIdentityInSession(hreq, identita, idEnte, idProfilo);
      } else {
        LOG.debug(methodName, "no identity in session and no marker token");
      }
    } else {
      if (marker != null) {
        UserInfoDTO userInfoCorrente = UserService.getInstance().getUtenteCorrente(hreq);
        if (checkChanged(userInfoCorrente, identita, idEnte, idProfilo)) {
          LOG.info(methodName, "reloading identity");
          loadIdentityInSession(hreq, identita, idEnte, idProfilo);
        }
      } else {
        // accesso a Shibbolet scaduto
        LOG.warn(methodName, "IDENTITA IRIDE SCADUTA");
        removeIdentityFromSession(hreq);
      }
    }
  }

  private boolean checkChanged(UserInfoDTO userInfoCorrente, IdentitaDTO identita, Long idEnte,
      Long idProfilo) {
    final String methodName = "checkChanged";

    if (userInfoCorrente.getAnonimo() != null && userInfoCorrente.getAnonimo().booleanValue()
        && identita != null && !StringUtils.isBlank(identita.getCodFiscale())) {
      LOG.warn(methodName, "IDENTITA' IRIDE INDIVIDUATA (come iniziale)");
      return true;
    } else if (!userInfoCorrente.getCodiceFiscale()
        .equals(identita == null ? null : identita.getCodFiscale())) {
      // identita' cambiata
      LOG.warn(methodName, "IDENTITA' IRIDE CAMBIATA");
      return true;
    } else if (idEnte != null && ObjectUtils.differ(idEnte,
        userInfoCorrente.getEnte() != null ? userInfoCorrente.getEnte().getId() : null)) {
      LOG.info(methodName, "ENTE SELEZIONATO CAMBIATO");
      return true;
    } else if (idProfilo != null && ObjectUtils.differ(idProfilo,
        userInfoCorrente.getProfilo() != null ? userInfoCorrente.getProfilo().getId() : null)) {
      LOG.info(methodName, "PROFILO SELEZIONATO CAMBIATO");
      return true;
    }

    return false;
  }

  private void loadIdentityInSession(HttpServletRequest hreq, IdentitaDTO identita, Long idEnte,
      Long idProfilo) {
    String method = "caricaUserInfo";
    try {
      UserService.getInstance().caricaIdentityInSessione(hreq, identita, idEnte, idProfilo);

    } catch (Exception e) {
      if (isBypassed()) {
        LOG.error(method, "Errore nella profilazione dell'utente, l'utente non verra' profilato.",
            e);
        return;
      }
      throw new UnauthorizedException(
          "Errore nella profilazione dell'utente (in fase di caricamento identity)", e);
    }
  }

  private void removeIdentityFromSession(HttpServletRequest hreq) {
    try {
      UserService.getInstance().invalidaSessione(hreq);
    } catch (Exception e) {
      throw new InternalServerException(
          "Errore nella profilazione dell'utente (in fase di logout automatico)", e);
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) throws IOException {

    if (!isEnabled()) {
      return;
    }

    UserInfoDTO user = SecurityUtils.getUtenteCorrente();
    responseContext.getHeaders().add(CosmoAuthenticationConfig.HEADER_IDENTITA_ATTIVA,
        user.getCodiceFiscale() + (user.getEnte() != null ? ".E" + user.getEnte().getId() : "")
        + (user.getProfilo() != null ? ".P" + user.getProfilo().getId() : ""));
  }

  private String getToken(HttpServletRequest httpreq) {
    if (isBypassed()) {
      return getTokenDevMode(httpreq);
    } else {
      return getTokenFromHeader(httpreq);
    }
  }

  private String getTokenDevMode(HttpServletRequest httpreq) {
    String marker = httpreq.getParameter(AUTH_ID_MARKER);

    if (StringUtils.isEmpty(marker)) {
      marker = getTokenFromHeader(httpreq);
    }

    if (StringUtils.isEmpty(marker)) {
      // mock
      marker =
          "AAAAAA00B77B000F/CSI PIEMONTE/DEMO 20/IPA/20190521145421/2/3CORaY75yebZdAFjdmFFmA==";
    }

    return marker;
  }

  private Optional<Long> getIdEnteSelezionato(HttpServletRequest httpreq) {
    if (isBypassed()) {
      return getIdEnteSelezionatoDevMode(httpreq);
    } else {
      return RequestUtils.extractHeaderId(httpreq, HEADER_ENTE_SELEZIONATO);
    }
  }

  private Optional<Long> getIdProfiloSelezionato(HttpServletRequest httpreq) {
    if (isBypassed()) {
      return getIdProfiloSelezionatoDevMode(httpreq);
    } else {
      return RequestUtils.extractHeaderId(httpreq, HEADER_PROFILO_SELEZIONATO);
    }
  }

  private Optional<Long> getIdEnteSelezionatoDevMode(HttpServletRequest httpreq) {
    return RequestUtils.extractRequestParamId(httpreq, "idEnte")
        .or(() -> RequestUtils.extractHeaderId(httpreq, HEADER_ENTE_SELEZIONATO));
  }

  private Optional<Long> getIdProfiloSelezionatoDevMode(HttpServletRequest httpreq) {
    return RequestUtils.extractRequestParamId(httpreq, "idProfilo")
        .or(() -> RequestUtils.extractHeaderId(httpreq, HEADER_PROFILO_SELEZIONATO));
  }

  private String getTokenFromHeader(HttpServletRequest httpreq) {
    String marker = httpreq.getHeader(AUTH_ID_MARKER);
    LOG.debug("getTokenFromHeader", "---> MARKER IS: " + marker + " <---");
    if (marker != null) {
      marker = new String(marker.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
    return marker;
  }

  // Metodo di utility
  public static IdentitaDTO getIdentitaFromToken(String token) {

    String[] splitted = token.split("/");
    if (splitted.length < 7) {
      throwTokenNonAutentico();
    }

    //@formatter:off
    return IdentitaDTO.builder()
        .withCodFiscale(splitted[0])
        .withNome(splitted[1])
        .withCognome(splitted[2])
        .withIdProvider(splitted[3])
        .withTimestamp(splitted[4])
        .withLivelloAutenticazione(
            LivelloAutenticazione.fromValore(Integer.parseInt(splitted[5])))
        .withMac(splitted[6])
        .build();
    //@formatter:on
  }

  private static void throwTokenNonAutentico() {
    throw new ForbiddenException("Token di identita' non autentico");
  }

}
