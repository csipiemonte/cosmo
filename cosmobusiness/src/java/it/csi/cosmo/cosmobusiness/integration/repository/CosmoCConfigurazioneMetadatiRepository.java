/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import it.csi.cosmo.common.entities.CosmoCConfigurazioneMetadati;
import it.csi.cosmo.common.repository.CosmoCRepository;

/**
 * Spring Data JPA repository per "CosmoCConfigurazione"
 */

public interface CosmoCConfigurazioneMetadatiRepository
    extends CosmoCRepository<CosmoCConfigurazioneMetadati, String> {

}
