/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.cosmoecm.dto.index2.RisultatoMigrazioneDocumento;

public interface StreamDataToIndexService {

  /**
   * @param daMigrare
   * @return
   */
  RisultatoMigrazioneDocumento migraDocumento(CosmoTDocumento daMigrare, String link);



}
