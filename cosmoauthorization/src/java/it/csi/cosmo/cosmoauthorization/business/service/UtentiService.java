/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteCampiTecnici;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtentiResponse;

/**
 *
 */

public interface UtentiService {

  /**
   * Metodo per eliminare un utente dal database
   *
   * @param id e' l'identificativo numerico dell'utente da eliminare
   */
  Utente deleteUtente(String id);

  /**
   * Metodo che restituisce una lista di utenti validi. Questa lista ha un numero di elementi pari
   * al valore indicato da filter o, se filter non e' valorizzato, un numero di elementi pari al
   * valore definito nella tabella di configurazione
   *
   * @param filter indica il numero di utenti che deve essere restituito
   * @return una lista di utenti validi
   */
  UtentiResponse getUtenti(String filter);

  /**
   * Metodo che restituisce una lista di utenti validi associati all'ente di cui viene passato l'id.
   *
   * @param filter ha le informazioni per la paginazione degli utenti e l'idEnte per restituire
   *        tutti gli utenti ad esso associati
   * @return una lista di utenti validi paginati
   */
  UtentiResponse getUtentiEnte(String filter);


  /**
   * Metodo che restituisce un utente specifico
   *
   * @param id dell'utente che deve essere cercato nel database
   * @return utente presente nel database
   */
  Utente getUtenteDaId(String id);

  /**
   * Metodo che restituisce un utente specifico
   *
   * @param codice fiscale dell'utente che deve essere cercato nel database
   * @return utente presente nel database
   */
  Utente getUtenteDaCodiceFiscale(String codiceFiscale);

  /**
   * Metodo che restituisce l'utente corrente
   *
   * @return utente presente nel database
   */
  Utente getUtenteCorrente();

  /**
   * Metodo che crea un utente sul database
   *
   * @param utente e' l'utente che deve essere creato sul database
   * @return l'utente creato sul database
   */
  Utente createUtente(UtenteCampiTecnici utente);

  /**
   * Metodo che aggiorna un utente gia' esistente nel database
   *
   * @param ente e' l'utente che deve essere aggiornato
   * @return l'utente aggiornato nel database
   */
  Utente updateUtente(UtenteCampiTecnici utente);

  /**
   * Metodo che restituisce un utente con anche i campi di validazione
   *
   * @param id e' l'id del utente
   * @return utente di cui si ha l'id
   */
  UtenteCampiTecnici getUtenteConValidita(String idUtente, String idEnte);
}
