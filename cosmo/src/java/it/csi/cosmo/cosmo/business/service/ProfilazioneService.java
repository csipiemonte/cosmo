/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.service;

import javax.servlet.http.HttpServletRequest;
import it.csi.cosmo.common.security.model.IdentitaDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmo.business.service.impl.ProfilazioneServiceImpl;
import it.csi.cosmo.cosmo.util.listener.SpringApplicationContextHelper;


/**
 * Servizio per la gestione dell'utente, della profilazione e della sessione
 */
public interface ProfilazioneService {

  /**
   * Costruisco l'oggetto UserInfo, ovvero la profilazione dell'utente, a partire dall'identita' Iride fornita.
   *
   * @param identita l'identita' Iride ottenuta da Shibbolet
   * @param codiceRuolo codice del ruolo selezionato
   * @return UserInfo le informazioni derivate sull'utente corrente
   * @see it.csi.cosmo.cosmo.security.UserInfo
   */
  UserInfoDTO caricaUserInfo(HttpServletRequest request, IdentitaDTO identita, Long idEnte,
      Long idProfilo);

  /**
   * Ritorna il singleton che implementa UserService registrato nello spring context.
   * Metodo di utilita' inserito per esporre in modo pulito lo UserService a tutte le componenti che ne hanno necessita' al di fuori
   * dello stretto contesto di Spring es. AuthenticationFilter, SecurityInterceptor, ResponseHeaderInjectorFilter.
   *
   * @return UserService bean
   */
  static ProfilazioneService getInstance () {
    return (ProfilazioneService) SpringApplicationContextHelper.getBean ( ProfilazioneServiceImpl.class );
  }


}
