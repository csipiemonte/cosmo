/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import it.csi.cosmo.cosmoauthorization.business.service.impl.PreferenzeUtenteServiceImpl;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;
import it.csi.cosmo.cosmoauthorization.util.listener.SpringApplicationContextHelper;

/**
 *
 */

public interface PreferenzeUtenteService {

  /**
   * Restituisce l'ultima versione delle preferenze dell'utente in sessione. La versione delle
   * preferenze e' salvata nella tabella di configurazione.
   *
   * @return le preferenze dell'utente
   */
  Preferenza getPreferenzeUtente();

  /**
   * Restituisce l'ultima versione delle preferenze dell'utente di cui si e' passato l'id. La
   * versione delle preferenze e' salvata nella tabella di configurazione.
   *
   * @return le preferenze dell'utente
   */
  Preferenza getPreferenzeUtente(String id);

  /**
   * Crea le preferenze dell'utente in sessione.
   *
   * @param preferenzeUtente da creare
   * @return le preferenze dell'utente create
   */
  Preferenza createPreferenzeUtente(Preferenza preferenzeUtente);

  /**
   * Aggiorna le preferenze dell'utente in sessione.
   *
   * @param preferenzeUtente da aggiornare
   * @return le preferenze dell'utente appena aggiornate
   */
  Preferenza updatePreferenzeUtente(Preferenza preferenzeUtente);

  static PreferenzeUtenteService getInstance() {
    return (PreferenzeUtenteService) SpringApplicationContextHelper
        .getBean(PreferenzeUtenteServiceImpl.class.getSimpleName());
  }
}
