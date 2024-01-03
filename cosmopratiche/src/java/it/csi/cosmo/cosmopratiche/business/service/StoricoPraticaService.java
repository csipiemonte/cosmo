/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPraticaRequest;

/**
 *
 */

public interface StoricoPraticaService {

  /**
   * @param idpratica
   * @return
   */
  StoricoPratica getStoricoAttivita(Long idpratica);

  void logEvent(CosmoLStoricoPratica entry);

  void logEvent(StoricoPraticaRequest request);

}
