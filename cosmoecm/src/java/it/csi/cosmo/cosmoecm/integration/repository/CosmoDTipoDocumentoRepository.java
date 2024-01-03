/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.repository.CosmoDRepository;

/**
 */

public interface CosmoDTipoDocumentoRepository
    extends CosmoDRepository<CosmoDTipoDocumento, String> {

  List<CosmoDTipoDocumento> findAllByCodiceIn(List<String> codici);
}
