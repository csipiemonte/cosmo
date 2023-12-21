/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import it.csi.cosmo.common.entities.CosmoREnteApplicazioneEsterna;
import it.csi.cosmo.common.repository.CosmoRRepository;

/**
 * Spring Data JPA repository per "CosmoREnteApplicazioneEsterna"
 */

public interface CosmoREnteApplicazioneEsternaRepository
extends CosmoRRepository<CosmoREnteApplicazioneEsterna, String> {

  CosmoREnteApplicazioneEsterna findOneByCosmoTApplicazioneEsternaIdAndCosmoTEnteId(
      Long idApplicazione, Long idEnte);

}
