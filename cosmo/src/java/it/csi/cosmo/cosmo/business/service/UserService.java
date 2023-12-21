/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.service;

import javax.servlet.http.HttpServletRequest;
import it.csi.cosmo.common.security.model.IdentitaDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmo.business.service.impl.UserServiceImpl;
import it.csi.cosmo.cosmo.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;


/**
 * Servizio per la gestione dell'utente, della profilazione e della sessione
 */
public interface UserService {

  /**
   * Ritorna l'utente corrente, salvato in sessione.
   *
   * @param req la ServletRequest dalla cui sessione si vuole estrarre l'utente
   * @return UserInfo le informazioni sull'utente corrente
   * @see it.csi.cosmo.cosmo.security.UserInfo
   */
  UserInfoDTO getUtenteCorrente(HttpServletRequest req);

  UserInfoDTO getUtenteCorrente();

  /**
   * Invalido la sessione dell'utente corrente
   *
   * @param req la ServletRequest dalla cui sessione si vuole estrarre l'utente
   */
  void invalidaSessione ( HttpServletRequest req );

  /**
   * Ritorna il singleton che implementa UserService registrato nello spring context.
   * Metodo di utilita' inserito per esporre in modo pulito lo UserService a tutte le componenti che ne hanno necessita' al di fuori
   * dello stretto contesto di Spring es. AuthenticationFilter, SecurityInterceptor, ResponseHeaderInjectorFilter.
   *
   * @return UserService bean
   */
  static UserService getInstance () {
    return (UserService) SpringApplicationContextHelper.getBean ( UserServiceImpl.class );
  }

  void caricaIdentityInSessione(HttpServletRequest hreq, IdentitaDTO identita, Long idEnte,
      Long idProfilo);

  boolean isAccessoDiretto(Utente utente);

}
