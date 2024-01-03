/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

import java.math.BigDecimal;
import java.util.List;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticaInRelazione;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoRelazionePraticaPratica;

/**
 *
 */

public interface RelazionePraticaPraticaService {

  /**
   * Metodo che restituisce tutti i possibili tipi di relazione che si possono creare tra due
   * pratiche
   *
   * @return una lista con tutti i tipi di relazione tra pratiche
   */
  List<TipoRelazionePraticaPratica> getTipiRelazionePraticaPratica();

  /**
   * Metodo che mette in relazione una pratica con un'altra pratica
   *
   * @param idPraticaDa e' la pratica che deve essere messa in relazione
   * @param idPraticaA e' la pratica verso cui viene creata la relazione
   * @param tipoRelazione indica il tipo di relazione
   *
   * @return la pratica che viene messa in relazione
   */

  /**
   * Metodo che mette in relazione le pratiche
   *
   * @param idPratica e' la pratica che deve essere associata
   * @param tipoRelazione e' il codice del tipo relazione che deve essere fatto
   * @param idPraticheDaRelazionare contiene gli id delle pratiche che devono essere messi in
   *        relazione con la pratica con id idPratica
   *
   * @return la pratica con id idPratica
   */
  Pratica creaAggiornaRelazioni(String idPratica, String codiceTipoRelazione,
      List<BigDecimal> idPraticheDaRelazionare);

  /**
   * Metodo che restituisce tutte le pratiche che sono in relazione con la pratica di cui si passa
   * l'id
   *
   * @param idPraticaDa e' l'id della pratica di cui si vogliono le relazioni
   *
   * @return la lista con le pratiche in relazione e il tipo di relazione per ogni pratica
   */
  List<PraticaInRelazione> getPraticheInRelazione(Long idPratica);

}
