/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service;

import java.util.List;
import java.util.Map;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.EsportaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.ImportaTipoPraticaRequest;

public interface ImportExportService {

  /**
   * @param codiceTipoPratica
   * @param tenantId
   * @return
   * @throws Exception
   */
  byte[] esporta(EsportaTipoPraticaRequest request);

  /**
   * @return Map<String, Object>
   */
  Map<String, Object> importa(ImportaTipoPraticaRequest request);

  List<Object> getOpzioniEsportazioneTenant(String codiceTipoPratica);
}
