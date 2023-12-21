/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;


import javax.jms.MapMessage;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

/**
 *
 */

public interface PraticaAttivitaService {
  /**
   * Metodo che crea una pratica (con relative attivita') dal messaggio inviato da CMMN e
   * successivamente la invia al componente pratiche per poterla salvare o aggiornare su db (a
   * seconda che sia gia' presente o meno)
   *
   * @param mapMessage e' il messaggio mappato che viene inviato da CMMN quando e' completato un
   *        task su flowable
   * @return la pratica salvata o aggiornata sul database
   */
  Pratica salvaPraticaAttivita(MapMessage mapMessage);


  /**
   * Metodo che indica se un'attivita sta per essere riassegnata o meno
   *
   * @param mapMessage e' il messaggio mappato, contenente il nuovo codice fiscale da aggiornare
   * @return booleana uguale a true se l'attivita, gia' esistente su db, ha un codice fiscale
   *         diverso rispetto a quello dell'aggiornamento, altrimenti false
   */
  boolean getAttivitaAncoraNonAggiornata(MapMessage mapMessage);
}
