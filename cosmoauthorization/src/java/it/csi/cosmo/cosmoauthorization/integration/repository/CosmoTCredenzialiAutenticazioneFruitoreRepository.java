/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTCredenzialiAutenticazioneFruitore"
 */

public interface CosmoTCredenzialiAutenticazioneFruitoreRepository
    extends CosmoTRepository<CosmoTCredenzialiAutenticazioneFruitore, Long> {

}
