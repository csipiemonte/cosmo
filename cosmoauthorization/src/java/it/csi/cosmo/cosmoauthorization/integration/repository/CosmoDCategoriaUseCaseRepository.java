/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import it.csi.cosmo.common.entities.CosmoDCategoriaUseCase;
import it.csi.cosmo.common.repository.CosmoDRepository;

/**
 * Spring Data JPA repository per "CosmoDCategoriaUseCase"
 */

public interface CosmoDCategoriaUseCaseRepository
extends CosmoDRepository<CosmoDCategoriaUseCase, String> {

}
