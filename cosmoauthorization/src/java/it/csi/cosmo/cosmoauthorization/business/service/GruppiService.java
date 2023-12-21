/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import java.util.List;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.GruppiResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.Gruppo;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoGruppo;

/**
 *
 */

public interface GruppiService {

  /**
   * Restituisce il gruppo il cui id e' quello inserito nel database
   *
   * @param id del gruppo da cercare sul database
   * @return il gruppo del database
   */
  Gruppo getGruppo(Long id);


  /**
   * Restituisce il gruppo il cui codice e' quello inserito nel database
   *
   * @param codice del gruppo da cercare sul database
   * @return il gruppo del database
   */

  Gruppo getGruppoPerCodice(String codice);
  /**
   * Restituisce una lista di gruppo validi, il cui numero di elementi e' pari al valore indicato da
   * filter o, se filter non e' valorizzato, un numero di elementi pari al valore definito nella
   * tabella di configurazione
   *
   * @param filter indica i filtri per la ricerca dei gruppo
   * @return una lista di gruppo validi
   */
  GruppiResponse getGruppi(String filter);

  /**
   * Metodo che crea un gruppo sul database
   *
   * @param gruppo e' il gruppo che deve essere creato sul database
   * @return il gruppo creato sul database
   */
  Gruppo createGruppo(CreaGruppoRequest gruppo);

  /**
   * Metodo che aggiorna un gruppo sul database
   *
   * @param gruppo e' il gruppo che deve essere aggiornato sul database
   * @return il gruppo aggiornato sul database
   */
  Gruppo updateGruppo(Long id, AggiornaGruppoRequest gruppo);

  /**
   * @param id
   */
  void deleteGruppo(Long id);

  List<RiferimentoGruppo> getGruppiUtenteCorrente();
}
