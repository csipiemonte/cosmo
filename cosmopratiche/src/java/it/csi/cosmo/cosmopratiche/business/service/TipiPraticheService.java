/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service;

import java.util.List;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.TipiPraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;

public interface TipiPraticheService {

  /**
   * Metodo che restituisce una lista di pratiche
   *
   * @param filter
   * @return una lista di pratiche
   */
  TipiPraticheResponse getTipiPratica(String filter);

  /**
   * @param codice
   * @return
   */
  TipoPratica getTipoPratica(String codice);

  /**
   * @param codice
   */
  void deleteTipoPratica(String codice);

  /**
   * @param body
   * @return
   */
  TipoPratica postTipoPratica(CreaTipoPraticaRequest body);

  /**
   * @param body
   * @return
   */
  TipoPratica putTipoPratica(String codice, AggiornaTipoPraticaRequest body);

  /**
   * Metodo che restituisce la lista di tipi pratiche, per l'ente passato come parametro, se
   * presente, altrimenti per l'utente connesso
   *
   * @param idEnte e' l'ente di cui si vogliono i tipi pratica
   * @param conEnte: se valorizzato a true ritorna anche, per ciascun tipo pratica, il proprio ente
   *        associato
   * @return
   */
  List<TipoPratica> getTipiPraticaPerEnte(Integer idEnte, Boolean creazionePratica,
      Boolean conEnte);




}
