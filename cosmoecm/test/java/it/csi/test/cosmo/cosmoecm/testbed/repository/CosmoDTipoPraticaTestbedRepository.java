/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.testbed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;

/**
 *
 */
@Repository
public interface CosmoDTipoPraticaTestbedRepository extends JpaRepository<CosmoDTipoPratica, Long> {

}
