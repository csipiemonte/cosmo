/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service;

import java.util.List;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.PaginaTask;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheNoLinkResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;

public interface PracticeService {

  Void deletePracticesId(String id);

  Pratica getPracticesId(String id, Boolean annullata);

  Pratica postPractices(CreaPraticaRequest body);

  Pratica putPracticesId(String id, Pratica body, Boolean aggiornaTask);

  RiassuntoStatoPratica getPraticheStatoIdPraticaExt(String idPraticaExt);

  PaginaTask getPraticheTaskIdTaskSubtasks(String idTask);

  /**
   * Metodo che restituisce la lista di tipi pratiche, per l'utente connesso
   *
   * @return List<TipoPratica>
   */

  /**
   * Metodo che restituisce la lista di tipi pratiche, per ente passato come parametro, se
   * presente, altrimenti per l'utente connesso
   *
   * @param idEnte e' l'ente di cui si vogliono i tipi pratica
   * @return
   */
  List<TipoPratica> getTipiPratica(Integer idEnte);


  /**
   * Metodo che restituisce una lista di pratiche. Questa lista ha un numero di elementi pari al
   * valore indicato da filter
   *
   * @param filter
   * @return PraticheResponse
   */
  PraticheResponse getPratiche(String filter, Boolean export);

  List<StatoPratica> getStatiPerTipo(String tipoPratica);

  /**
   * Metodo che restituisce una pratica a partire dal suo id, a seconda che sia visibile dall'utente
   * che la richiede
   *
   * @param idPratica
   * @return Pratica
   */
  Pratica getVisibilitaPraticaById(Long idPratica);

  /**
   * Metodo che restituisce una pratica a partire dall' idTask correlato, a seconda che sia visibile
   * dall'utente che lo richiede
   *
   *
   * @param idTask
   * @return Pratica
   */
  Pratica getVisibilitaPraticaByIdTask(String idTask);

  /**
   * @param idPratica
   * @return
   */
  byte[] getPraticheIdPraticaDiagramma(Integer idPratica);

  PraticheNoLinkResponse getPraticheNoLink();
}
