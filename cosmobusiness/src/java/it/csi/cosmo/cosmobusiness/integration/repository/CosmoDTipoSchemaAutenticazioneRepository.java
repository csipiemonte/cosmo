/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import it.csi.cosmo.common.entities.CosmoDTipoSchemaAutenticazione;
import it.csi.cosmo.common.repository.CosmoDRepository;

/**
 * Spring Data JPA repository
 */

public interface CosmoDTipoSchemaAutenticazioneRepository
    extends CosmoDRepository<CosmoDTipoSchemaAutenticazione, String> {

}
