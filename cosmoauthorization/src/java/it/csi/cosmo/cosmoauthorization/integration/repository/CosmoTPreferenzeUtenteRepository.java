/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import it.csi.cosmo.common.entities.CosmoTPreferenzeUtente;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTPreferenzeUtente"
 */

public interface CosmoTPreferenzeUtenteRepository
    extends CosmoTRepository<CosmoTPreferenzeUtente, Long> {

  CosmoTPreferenzeUtente findByVersioneAndCosmoTUtente(String versione, CosmoTUtente utente);
}
