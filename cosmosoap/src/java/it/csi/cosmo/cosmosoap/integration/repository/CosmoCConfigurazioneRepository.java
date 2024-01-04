/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.repository;

import it.csi.cosmo.common.entities.CosmoCConfigurazione;
import it.csi.cosmo.common.repository.CosmoCRepository;

/**
 * Spring Data JPA repository per "CosmoCConfigurazione"
 */

public interface CosmoCConfigurazioneRepository
    extends CosmoCRepository<CosmoCConfigurazione, String> {

}
