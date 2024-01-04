/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.repository;

import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 *
 */

public interface CosmoTDocumentoRepository extends CosmoTRepository<CosmoTDocumento, Long> {

  CosmoTDocumento findOneByContenutiIdAndDtCancellazioneNull(Long contenutoId);

}
