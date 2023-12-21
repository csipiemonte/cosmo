/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoTFunzionalitaApplicazioneEsterna;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTFunzionalitaApplicazioneEsternaRepository"
 */

public interface CosmoTFunzionalitaApplicazioneEsternaRepository
extends CosmoTRepository<CosmoTFunzionalitaApplicazioneEsterna, Long> {

  List<CosmoTFunzionalitaApplicazioneEsterna> findAllByCosmoREnteApplicazioneEsternaCosmoTApplicazioneEsternaIdAndCosmoREnteApplicazioneEsternaCosmoTEnteIdAndDtCancellazioneNullOrderByDescrizione(
      Long idApplicazione, Long idEnte);
}
