/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaEnteRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEnteRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.Ente;
import it.csi.cosmo.cosmoauthorization.dto.rest.EntiResponse;

/**
 *
 */

public interface EntiService {

  /**
   * Metodo per eliminare un ente dal database
   *
   * @param id e' l'identificativo numerico dell'ente da eliminare
   */
  void deleteEnte(Long id);

  /**
   * Metodo che restituisce una lista di enti validi. Questa lista ha un numero di elementi pari al
   * valore indicato da filter o, se filter non e' valorizzato, un numero di elementi pari al valore
   * definito nella tabella di configurazione
   *
   * @param filter indica il numero di enti che deve essere restituito
   * @return una lista di enti validi
   */
  EntiResponse getEnti(String filter);


  /**
   * Metodo che restituisce un ente specifico
   *
   * @param id dell'ente che deve essere cercato nel database
   * @return ente presente nel database
   */
  Ente getEnte(Long id);

  /**
   * Metodo che crea un ente sul database
   *
   * @param ente e' l'ente che deve essere creato sul database
   * @return l'ente creato sul database
   */
  Ente createEnte(CreaEnteRequest ente);

  /**
   * Metodo che aggiorna un ente gia' esistente nel database
   *
   * @param ente e' l'ente che deve essere aggiornato
   * @return l'ente aggiornato nel database
   */
  Ente updateEnte(Long id, AggiornaEnteRequest ente);

}
