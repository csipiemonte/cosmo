/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.testbed.repository;

import org.springframework.stereotype.Repository;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 *
 */

@Repository
public interface CosmoTPraticaTestbedRepository extends CosmoTRepository<CosmoTPratica, Long> {

}
