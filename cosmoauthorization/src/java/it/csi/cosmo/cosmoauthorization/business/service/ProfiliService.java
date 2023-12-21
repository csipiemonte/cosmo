/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiliResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;

/**
 *
 */

public interface ProfiliService {

  /**
   * Restituisce il profilo il cui id e' quello inserito nel database
   *
   * @param id del profilo da cercare sul database
   * @return il profilo del database
   */
  Profilo getProfilo(String id);

  /**
   * Restituisce una lista di profili validi, il cui numero di elementi e' pari al valore indicato
   * da filter o, se filter non e' valorizzato, un numero di elementi pari al valore definito nella
   * tabella di configurazione
   *
   * @param filter indica i filtri per la ricerca dei profili
   * @return una lista di profili validi
   */
  ProfiliResponse getProfili(String filter);

  /**
   * Metodo che crea un profilo sul database
   *
   * @param profilo e' il profilo che deve essere creato sul database
   * @return il profilo creato sul database
   */
  Profilo createProfilo(Profilo profilo);

  /**
   * Metodo che aggiorna un profilo sul database
   *
   * @param profilo e' il profilo che deve essere aggiornato sul database
   * @return il profilo aggiornato sul database
   */
  Profilo updateProfilo(String id, Profilo profilo);

  void deleteProfilo(String id);
}
