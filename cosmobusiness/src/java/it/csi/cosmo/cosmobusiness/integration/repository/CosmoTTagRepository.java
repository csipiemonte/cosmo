/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 *
 */

public interface CosmoTTagRepository extends CosmoTRepository<CosmoTTag, Long> {

  CosmoTTag findOneByCosmoTEnteAndCodiceAndDtCancellazioneIsNull(CosmoTEnte ente, String codice);

}
