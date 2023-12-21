/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTGruppo"
 */

public interface CosmoTGruppoRepository extends CosmoTRepository<CosmoTGruppo, Long> {

  CosmoTGruppo findByCodiceAndEnte(String codice,CosmoTEnte ente);
}

