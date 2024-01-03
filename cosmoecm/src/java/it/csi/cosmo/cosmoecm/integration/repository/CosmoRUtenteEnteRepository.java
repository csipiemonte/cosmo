/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository;

import it.csi.cosmo.common.entities.CosmoRUtenteEnte;
import it.csi.cosmo.common.entities.CosmoRUtenteEntePK;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.repository.CosmoRRepository;

/**
 * Spring Data JPA repository per "CosmoRUtenteEnte"
 */

public interface CosmoRUtenteEnteRepository
extends CosmoRRepository<CosmoRUtenteEnte, CosmoRUtenteEntePK> {

  CosmoRUtenteEnte findByCosmoTEnteAndCosmoTUtente(CosmoTEnte ente, CosmoTUtente utente);

}
