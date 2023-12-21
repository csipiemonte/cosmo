/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoDAutorizzazioneFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.repository.CosmoDRepository;

/**
 * Spring Data JPA repository per "CosmoDAutorizzazioneFruitore"
 */

public interface CosmoDAutorizzazioneFruitoreRepository
extends CosmoDRepository<CosmoDAutorizzazioneFruitore, String> {

  CosmoDAutorizzazioneFruitore findByCodice(String codice);

  List<CosmoDAutorizzazioneFruitore> findAllByCosmoTFruitores(CosmoTFruitore fruitore);

}
