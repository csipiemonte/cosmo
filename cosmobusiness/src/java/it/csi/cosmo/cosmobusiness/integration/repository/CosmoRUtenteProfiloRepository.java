/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import it.csi.cosmo.common.entities.CosmoRUtenteProfilo;
import it.csi.cosmo.common.repository.CosmoRRepository;

/**
 * Spring Data JPA repository per "CosmoRUtenteProfilo"
 */

public interface CosmoRUtenteProfiloRepository
    extends CosmoRRepository<CosmoRUtenteProfilo, String> {


}
