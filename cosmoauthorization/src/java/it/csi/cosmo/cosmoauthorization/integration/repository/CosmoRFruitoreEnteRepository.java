/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoRFruitoreEnte;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.repository.CosmoRRepository;

/**
 * Spring Data JPA repository per "CosmoRFruitoreEnte"
 */

public interface CosmoRFruitoreEnteRepository
extends CosmoRRepository<CosmoRFruitoreEnte, String> {

  CosmoRFruitoreEnte findByCosmoTEnteAndCosmoTFruitore(CosmoTEnte ente, CosmoTFruitore fruitore);

  List<CosmoRFruitoreEnte> findAllByCosmoTFruitore(CosmoTFruitore fruitore);

}
