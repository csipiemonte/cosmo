/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTEnte"
 */

public interface CosmoTEnteRepository extends CosmoTRepository<CosmoTEnte, Long> {

  CosmoTEnte findByNome(String nome);
  CosmoTEnte findByCodiceIpaOrCodiceFiscale(String codiceIpa,String codiceFiscale);
}
