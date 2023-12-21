/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoTApplicazioneEsterna;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTApplicazioneEsterna"
 */

public interface CosmoTApplicazioneEsternaRepository
extends CosmoTRepository<CosmoTApplicazioneEsterna, Long> {

  List<CosmoTApplicazioneEsterna> findAllByCosmoREnteApplicazioneEsternasCosmoTEnteIdAndDtCancellazioneNullOrderByDescrizione(
      Long idEnte);

  List<CosmoTApplicazioneEsterna> findAllByCosmoREnteApplicazioneEsternasCosmoTEnteIdOrderByDescrizione(
      Long idEnte);

  List<CosmoTApplicazioneEsterna> findAllByDtCancellazioneNullOrderByDescrizione();
}
