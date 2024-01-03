/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFea;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFeaResponse;

/**
 *
 */

public interface TemplateFeaService {

  /**
   * Metodo che restituisce un'insieme di template fea
   * @return TemplateFeaResponse: oggetto contenente un insieme di template fea e la loro paginazione
   */
  TemplateFeaResponse getTemplateFea(String filter, Boolean tutti);

  /**
   * Metodo per eliminare un template fea dal database
   *
   * @param id e' l'identificativo numerico del template fea da eliminare
   */
  void deleteTemplateFea(Long id);

  /**
   * Metodo che restituisce un template fea dato l'id
   *
   * @param id e' l'identificativo numerico del template fea
   */
  TemplateFea getTemplateFeaId(Long id);

  /**
   * Metodo per inserire un template fea dal database
   *
   * @param request: contiene le informazioni da inserire per creare il template fea
   */

  TemplateFea creaTemplateFea(CreaTemplateFeaRequest request);

  /**
   * Metodo per aggiornare un template fea dato l'id
   *
   * @param id e' l'identificativo numerico del template fea
   * @param body contiene le informazioni da inserire per l'aggiornamento del template fea
   */
  TemplateFea updateTemplateFea(Long id, CreaTemplateFeaRequest body);
}
