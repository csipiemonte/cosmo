/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository;

import it.csi.cosmo.common.entities.CosmoLEsecuzioneBatch;
import it.csi.cosmo.common.repository.CosmoLRepository;

/**
 * Spring Data JPA repository per "CosmoLEsecuzioneBatch"
 */

public interface CosmoLEsecuzioneBatchRepository
    extends CosmoLRepository<CosmoLEsecuzioneBatch, Long> {

}
