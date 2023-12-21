/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import it.csi.cosmo.common.entities.CosmoDUseCase;
import it.csi.cosmo.common.repository.CosmoDRepository;

/**
 * Spring Data JPA repository per "CosmoDUseCase"
 */

public interface CosmoDUseCaseRepository extends CosmoDRepository<CosmoDUseCase, String> {

}
