/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.repository;

import it.csi.cosmo.common.entities.CosmoLSegnalazioneBatch;
import it.csi.cosmo.common.repository.CosmoLRepository;

/**
 * Spring Data JPA repository per "CosmoLSegnalazioneBatch"
 */

public interface CosmoLSegnalazioneBatchRepository
    extends CosmoLRepository<CosmoLSegnalazioneBatch, Long> {

}
