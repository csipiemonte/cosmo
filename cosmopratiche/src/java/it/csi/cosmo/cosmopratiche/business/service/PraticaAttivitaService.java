/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

import java.util.List;
import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
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
  /**
   * Metodo che salva una pratica e le relative attivita'
   *
   * @param pratica e' la pratica che e' stata salvata
   * @return
   */
  Pratica salva(Pratica pratica);

  /**
   * Pratica che aggiorna una pratica e salva le relative attivita' Nel caso in cui le attivita' con
   * evento completato o aggiornato, vengono eliminate dal db. Successivamente vengono salvate le
   * attivita' che hanno l'evento completato
   *
   * @param pratica
   * @return
   */
  Pratica aggiorna(Pratica pratica);

  Boolean putAttivitaIdAttivita(String idAttivita);

  Attivita getPraticheAttivitaIdAttivita(String idAttivita);

  List<Attivita> getAttivitaIdPratica(String idPratica);

}
