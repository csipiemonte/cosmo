/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.cosmoecm.dto.rest.FormatoFileResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RaggruppamentoFormatiFileResponse;

/**
 *
 */

public interface FormatoFileService {

  /**
   * Metodo che restituisce i formati file
   *
   * @param filter filtro
   * @return una lista di formati file
   */
  FormatoFileResponse getFormatiFile(String filter);

  RaggruppamentoFormatiFileResponse getFormatiFileRaggruppati(String filter);

}
