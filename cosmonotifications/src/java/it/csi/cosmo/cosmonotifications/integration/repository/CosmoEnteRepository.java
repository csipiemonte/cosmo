/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmonotifications.integration.repository;

import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.repository.CosmoTRepository;

public interface CosmoEnteRepository extends CosmoTRepository<CosmoTEnte, Long> {

  CosmoTEnte findByCodiceIpa(String codiceIpa);


}
