/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.service.impl;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.IdentitaDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmo.business.service.ProfilazioneService;
import it.csi.cosmo.cosmo.business.service.UserService;
import it.csi.cosmo.cosmo.filter.AuthenticationFilter;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;

/**
 * Implementazione del servizio per la gestione dell'utente, della profilazione e della sessione
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "UserServiceImpl");

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
  public UserInfoDTO getUtenteCorrente(HttpServletRequest req) {
    if ( req == null ) {
      return ProfilazioneServiceImpl.getUtenteSistema ();
    }

    UserInfoDTO output =
        (UserInfoDTO) req.getSession().getAttribute(AuthenticationFilter.USERINFO_SESSIONATTR);
    if ( output == null ) {
      return ProfilazioneServiceImpl.getUtenteAnonimo ();
    } else {
      return output;
    }
  }

  @Override
  public UserInfoDTO getUtenteCorrente() {
    return getUtenteCorrente(RequestUtils.getCurrentRequest().orElse(null));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void invalidaSessione ( HttpServletRequest req ) {
    String method = "invalidaSessione";

    req.getSession().removeAttribute(AuthenticationFilter.IRIDE_ID_SESSIONATTR);
    logger.info(method, "rimosso da sessione marcatore IRIDE");

    req.getSession().removeAttribute(AuthenticationFilter.USERINFO_SESSIONATTR);
    logger.info(method, "rimosso da sessione UserInfo");

    req.getSession ().invalidate ();
    logger.info(method, "sessione locale invalidata");
  }

  @Override
  public void caricaIdentityInSessione(HttpServletRequest hreq, IdentitaDTO identita, Long idEnte,
      Long idProfilo) {
    String method = "caricaIdentityInSessione";

    UserInfoDTO userInfo =
        ProfilazioneService.getInstance().caricaUserInfo(hreq, identita, idEnte, idProfilo);

    hreq.getSession().setAttribute(AuthenticationFilter.IRIDE_ID_SESSIONATTR, identita);
    logger.info(method, "inserito in sessione marcatore IRIDE: " + identita);

    hreq.getSession().setAttribute(AuthenticationFilter.USERINFO_SESSIONATTR, userInfo);
    logger.info(method, "inserito in sessione UserInfo: " + userInfo);

  }

  @Override
  public boolean isAccessoDiretto(Utente utente) {

    return utente.getEnti().size() == 1 && utente.getProfili().size() == 1
        && !ObjectUtils.isEmpty(utente.getProfili().get(0).getEnte());
  }

}
