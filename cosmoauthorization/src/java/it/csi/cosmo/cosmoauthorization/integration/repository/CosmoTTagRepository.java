/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTTag"
 */

public interface CosmoTTagRepository extends CosmoTRepository<CosmoTTag, Long> {

}
