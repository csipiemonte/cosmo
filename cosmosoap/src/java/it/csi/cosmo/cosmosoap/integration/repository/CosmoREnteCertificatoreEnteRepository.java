/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.repository;

import it.csi.cosmo.common.entities.CosmoREnteCertificatoreEnte;
import it.csi.cosmo.common.repository.CosmoRRepository;

/**
 *
 */

public interface CosmoREnteCertificatoreEnteRepository
    extends CosmoRRepository<CosmoREnteCertificatoreEnte, Long> {

  CosmoREnteCertificatoreEnte findOneByCosmoTEnteIdAndCosmoDEnteCertificatoreCodiceAndAnno(
      Long idEnte, String codiceEnteCertificatore, Long Anno);

}
