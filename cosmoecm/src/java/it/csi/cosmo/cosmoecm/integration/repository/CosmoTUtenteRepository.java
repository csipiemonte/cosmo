/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository;

import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTUtente"
 */

public interface CosmoTUtenteRepository
    extends CosmoTRepository<CosmoTUtente, Long> {

  CosmoTUtente findByCodiceFiscale(String codiceFiscale);

}
