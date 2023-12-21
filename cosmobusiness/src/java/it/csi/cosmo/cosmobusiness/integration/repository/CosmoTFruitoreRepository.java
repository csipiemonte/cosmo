/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 *
 */

public interface CosmoTFruitoreRepository extends CosmoTRepository<CosmoTFruitore, Long> {

  CosmoTFruitore findByApiManagerId(String apiManagerId);
}
