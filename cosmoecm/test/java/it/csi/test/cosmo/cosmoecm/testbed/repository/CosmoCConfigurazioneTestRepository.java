/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.testbed.repository;

import it.csi.cosmo.common.entities.CosmoCConfigurazione;
import it.csi.cosmo.common.repository.BasicRepository;

/**
 * Spring Data JPA repository per "CosmoCConfigurazione"
 */

public interface CosmoCConfigurazioneTestRepository
    extends BasicRepository<CosmoCConfigurazione, String> {

}
