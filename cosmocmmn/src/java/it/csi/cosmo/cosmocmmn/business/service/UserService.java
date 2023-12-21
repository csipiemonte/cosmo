/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.service;

import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmocmmn.business.service.impl.UserServiceImpl;
import it.csi.cosmo.cosmocmmn.util.listener.SpringApplicationContextHelper;



/**
 * Servizio per la gestione dell'utente, della profilazione e della sessione
 */
public interface UserService {

  /**
   * Ritorna l'utente corrente, salvato in sessione.
   *
   * @return UserInfo le informazioni sull'utente corrente
   * @see it.csi.cosmo.cosmocmmn.dto.security.UserInfo
   */
  UserInfoDTO getUtenteCorrente();

  /**
   * Ritorna il singleton che implementa UserService registrato nello spring context. Metodo di
   * utilita' inserito per esporre in modo pulito lo UserService a tutte le componenti che ne hanno
   * necessita' al di fuori dello stretto contesto di Spring es. SecurityInterceptor,
   * ResponseHeaderInjectorFilter.
   *
   * @return UserService bean
   */
  static UserService getInstance() {
    return (UserService) SpringApplicationContextHelper
        .getBean(UserServiceImpl.class.getSimpleName());
  }

}
