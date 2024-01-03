/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository;

import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTEnte"
 */

public interface CosmoTEnteRepository
    extends CosmoTRepository<CosmoTEnte, Long> {

  CosmoTEnte findByCodiceIpa(String codice);
}

