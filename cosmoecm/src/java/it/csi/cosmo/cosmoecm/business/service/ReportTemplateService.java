/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateRequest;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateReport;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateReportResponse;

public interface ReportTemplateService {

  /**
  * Metodo che restituisce un'insieme di template di report
  * @return TemplateResponse: oggetto contenente un insieme di template e informazioni relative alla paginazione
  */
  TemplateReportResponse getReportTemplate(String filter);

  /**
   * Metodo per inserire un template report dal database
   *
   * @param request: contiene le informazioni da inserire per creare il template report
   */

  TemplateReport creaReportTemplate(CreaTemplateRequest request);

  /**
   * Metodo per eliminare un template report dal database
   *
   * @param id e' l'identificativo numerico del template report da eliminare
   */
  void deleteReportTemplate(Long id);

  /**
   * Metodo che restituisce un template report dato l'id
   *
   * @param id e' l'identificativo numerico del template report
   */
  TemplateReport getReportTemplateId(Long id);

  /**
   * Metodo per aggiornare un template report dato l'id
   *
   * @param id e' l'identificativo numerico del template report
   * @param body contiene le informazioni da inserire per l'aggiornamento del template report
   */
  TemplateReport updateReportTemplate(Long id, CreaTemplateRequest body);


}
