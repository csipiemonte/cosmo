/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository;

import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTProfilo"
 */

public interface CosmoTProfiloRepository
    extends CosmoTRepository<CosmoTProfilo, Long> {

}
