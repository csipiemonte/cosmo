/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;

/**
 * Servizio per la gestione dell'utente, della profilazione e della sessione
 */
public interface PreferenzeEnteService {

  /**
   * * Crea le preferenze dell'ente in sessione.
   *
   * @param idEnte
   * @param versione
   * @return le preferenze dell'utente
   */
  Preferenza createPreferenzeEnte(Integer idEnte, Preferenza body);

  /**
   * Restituisce l'ultima versione delle preferenze ente in sessione. La versione delle
   * preferenze e' salvata nella tabella di configurazione.
   *
   * @param idEnte
   * @param versione
   * @return le preferenze dell'utente
   */
  Preferenza getPreferenzeEnte(Integer idEnte, String versione);

  /**
   * Aggiorna le preferenze dell'ente
   *
   * @param idEnte
   * @param preferenzeEnte da aggiornare
   * @return le preferenze dell'ente appena aggiornate
   */
  Preferenza updatePreferenzeEnte(Integer idEnte, Preferenza body);

}
