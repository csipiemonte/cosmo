/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;
import it.csi.cosmo.common.repository.CosmoCRepository;

/**
 * Spring Data JPA repository per "CosmoCConfigurazioneEnte"
 */

public interface CosmoCConfigurazioneEnteRepository
    extends CosmoCRepository<CosmoCConfigurazioneEnte, String> {

}
