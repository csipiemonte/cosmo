/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.repository;

import it.csi.cosmo.common.entities.CosmoRFormatoFileProfiloFeqTipoFirma;
import it.csi.cosmo.common.repository.CosmoRRepository;

/**
 *
 */

public interface CosmoRFormatoFileProfiloFeqTipoFirmaRepository
    extends CosmoRRepository<CosmoRFormatoFileProfiloFeqTipoFirma, Long> {

  CosmoRFormatoFileProfiloFeqTipoFirma findOneByCosmoDFormatoFileCodiceAndCosmoDProfiloFeqCodice(
      String codiceFormatoFile, String codiceProfiloFeq);
}
