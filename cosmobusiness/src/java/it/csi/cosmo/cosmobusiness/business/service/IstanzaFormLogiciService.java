/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.util.ValoreParametroFormLogicoWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaIstanzaFunzionalitaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaIstanzaFunzionalitaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzeFormLogiciResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologiaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologieParametroFormLogicoResponse;

/**
 *
 */

public interface IstanzaFormLogiciService {

  /**
   * Metodo per eliminare logicamente l'istanza di cui si passa l'id come parametro. Inoltre bisogna
   * impostare la fine validita' alle tabelle di relazione (r_form_logico_istanza_funzionalita' e
   * r_istanza_form_logico_parametro_valore)
   *
   * @param id e' l'id dell'istanza che si vuole eliminare
   */
  void deleteFormLogiciIstanzeId(Long id);

  /**
   * Metodo che restituisce una lista paginata di istanze dei form logici, ricercate in base ai
   * filtri passati come parametro
   *
   * @param filter contiene tutte le informazioni per effettuare una ricerca delle istanze dei form
   *        logici
   * @return un dto con le informazioni sulla paginazione delle istanze trovate ed una lista delle
   *         istanze
   */
  IstanzeFormLogiciResponse getFormLogiciIstanze(String filter);

  /**
   * Metodo che restituisce l'istanza di cui si e' passato l'id come parametro
   *
   * @param id dell'istanza da cercare
   * @return istanca che ha l'id come il parametro
   */
  IstanzaFunzionalitaFormLogico getFormLogiciIstanzeId(Long id);

  /**
   * Metodo per salvare un'istanza del form logico
   *
   * @param body contiene tutte le informazioni per salvare l'istanza del form logico
   *
   * @return un dto con l'istanza salvata
   */
  IstanzaFunzionalitaFormLogico postFormLogiciIstanze(
      CreaIstanzaFunzionalitaFormLogicoRequest body);

  /**
   * Metodo per aggiornare un'istanza di un form logico
   *
   * @param id e' l'id dell'istanza che si vuole aggiornare
   * @param body contiene tutti i campi dell'istanza che si vuole aggiornare, aggiornati o meno
   *
   * @return l'istanza aggiornata
   */
  IstanzaFunzionalitaFormLogico putFormLogiciIstanzeId(Long id,
      AggiornaIstanzaFunzionalitaFormLogicoRequest body);

  List<TipologiaFunzionalitaFormLogico> getAllTipologie();

  /**
   * @param id
   * @return
   */
  TipologieParametroFormLogicoResponse getFormLogiciTipologieIstanzeFunzionalitaParametri(
      String codice);

  /**
   * @param attivita
   * @param codiceFunzionalita
   * @return
   */
  CosmoTIstanzaFunzionalitaFormLogico ricercaIstanzaAttiva(CosmoTAttivita attivita,
      String codiceFunzionalita);

  /**
   * @param istanza
   * @param codiceParametro
   * @return
   */
  Optional<ValoreParametroFormLogicoWrapper> getValoreParametro(
      CosmoTIstanzaFunzionalitaFormLogico istanza,
      String codiceParametro);

  /**
   * @param istanza
   * @param codiceParametro
   * @return
   */
  ValoreParametroFormLogicoWrapper requireValoreParametro(
      CosmoTIstanzaFunzionalitaFormLogico istanza,
      String codiceParametro);

  /**
   * @param istanza
   * @return
   */
  Map<String, ValoreParametroFormLogicoWrapper> getValoriParametri(
      CosmoTIstanzaFunzionalitaFormLogico istanza);

}
