/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTFruitore"
 */

public interface CosmoTFruitoreRepository extends CosmoTRepository<CosmoTFruitore, Long> {

  CosmoTFruitore findByApiManagerId(String apiManagerId);


}
