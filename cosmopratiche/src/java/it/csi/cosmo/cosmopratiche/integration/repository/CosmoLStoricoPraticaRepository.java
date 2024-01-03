/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository;

import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.repository.CosmoLRepository;

/**
 * Spring Data JPA repository per "CosmoLStoricoPratica"
 */

public interface CosmoLStoricoPraticaRepository
    extends CosmoLRepository<CosmoLStoricoPratica, Long> {

}
