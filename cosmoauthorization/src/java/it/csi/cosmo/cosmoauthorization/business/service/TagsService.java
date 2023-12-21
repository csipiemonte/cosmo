/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaTagRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.TagResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.TagsResponse;

public interface TagsService {

  /**
   * Metodo per eliminare un tag dal database
   *
   * @param id e' l'identificativo numerico del tag da eliminare
   */
  void deleteTag(Long id);

  /**
   * Metodo che restituisce una lista di tags validi
   *
   * @param filter
   * @return una lista di tags validi
   */
  TagsResponse getTags(String filter);

  /**
   * Metodo che restituisce un tag specifico
   *
   * @param id del tag che deve essere cercato nel database
   * @return tag presente nel database
   */
  TagResponse getTagById(Long id);

  /**
   * Metodo che crea un tag sul database
   *
   * @param body e' il tag che deve essere creato sul database
   * @return il tag creato sul database
   */
  TagResponse postTag(CreaTagRequest body);

  /**
   * Metodo che aggiorna un tag gia' esistente nel database
   *
   * @param id e' l'id fisico del tag che deve essere aggiornato
   * @param body e' il tag che deve essere aggiornato
   * @return il tag aggiornato nel database
   */
  TagResponse updateTag(Long id, AggiornaTagRequest body);
}
