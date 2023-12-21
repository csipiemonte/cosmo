/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import java.util.List;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsternaConValidita;

/**
 *
 */

public interface FunzionalitaApplicazioneEsternaService {

  /**
   * Metodo che elimina logicamente la funzionalita' di cui e' passato l'id, impostando la data di
   * cancellazione e l'utente di cancellazione.
   *
   * Inoltre imposta anche la data di fine validita' per gli utenti che hanno associata questa
   * funzionalita'
   *
   * @param idApplicazione e' l'id dell'applicazione a cui appartiene la funzionalita'
   * @param idFunzionalita e' l'id della funzionalita' che si vuole eliminare
   */
  void eliminaFunzionalita(String idApplicazione, String idFunzionalita);

  /**
   * Metodo che restituisce tutte le funzionalita associate ad un'applicazione di cui si passa l'id.
   *
   * Inoltre, se configurata e' true, vengono selezionate solo le funzionalita' che sono associate
   * all'utente in sessione (quindi le cosidette funzionalita' configurate), se invece configurata
   * e' false o null, vengono restituire tutte le funzionalita' associate a quella applicazione
   * (quindi tutte le funzionalita configurabili)
   *
   * @param idApplicazione
   * @param configurata
   * @return
   */
  List<FunzionalitaApplicazioneEsternaConValidita> getFunzionalita(String idApplicazione);

  /**
   * Metodo che restituisce la funzionalita' di cui si e' passato l'id come parametro
   *
   * @param idApplicazione e' l'id dell'applicazione a cui appartiene la funzionalita'
   * @param idFunzionalita e' l'id della funzionalita' che si vuole come risultato
   *
   * @return e' il dto con la funzionalita' richiesta
   */
  FunzionalitaApplicazioneEsternaConValidita getSingolaFunzionalita(String idApplicazione,
      String idFunzionalita);

  /**
   * Metodo per salvare una singola funzionalita' associata ad un'applicazione di cui viene passato
   * l'id.
   *
   * La funzionalita' in questione sara' una funzionalita' con default uguale a false
   *
   * @param idApplicazione e' l'id dell'applicazione a cui sara' collegata la funzionalita'
   * @param body contiene le informazioni della funzionalita' da salvare
   * @return un dto con la funzionalita' salvata
   */
  FunzionalitaApplicazioneEsternaConValidita salvaSingolaFunzionalita(String idApplicazione,
      FunzionalitaApplicazioneEsternaConValidita body);

  /**
   * Metodo che aggiorna la funzionalita' di cui si passa l'id e che e' associata all'applicazione
   * di cui si passa l'id. Si aggiornano tutti i campi che vengono passati nel body.
   *
   * @param idApplicazione e' l'id dell'applicazione a cui appartiene la funzionalita'
   * @param idFunzionalita e' l'id della funzionalita' che si vuole aggiornare
   * @param body contiene i dati della funzionalita' che devono essere aggiornati
   * @return la funzionalita' aggiornata
   */
  FunzionalitaApplicazioneEsternaConValidita aggiornaSingolaFunzionalita(String idApplicazione,
      String idFunzionalita, FunzionalitaApplicazioneEsternaConValidita body);

}
