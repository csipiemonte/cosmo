/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import it.csi.cosmo.common.entities.CosmoRUtenteEnte;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.repository.CosmoRRepository;

/**
 * Spring Data JPA repository per "CosmoRUtenteEnte"
 */

public interface CosmoRUtenteEnteRepository extends CosmoRRepository<CosmoRUtenteEnte, String> {

  CosmoRUtenteEnte findByCosmoTEnteAndCosmoTUtente(CosmoTEnte ente, CosmoTUtente utente);

}
