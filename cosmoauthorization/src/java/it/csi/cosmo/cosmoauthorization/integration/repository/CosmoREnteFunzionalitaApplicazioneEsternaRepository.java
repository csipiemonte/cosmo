/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoREnteFunzionalitaApplicazioneEsterna;
import it.csi.cosmo.common.repository.CosmoRRepository;

/**
 * Spring Data JPA repository per "CosmoREnteFunzionalitaApplicazioneEsternaRepository"
 */

public interface CosmoREnteFunzionalitaApplicazioneEsternaRepository
extends CosmoRRepository<CosmoREnteFunzionalitaApplicazioneEsterna, String> {

  List<CosmoREnteFunzionalitaApplicazioneEsterna> findAllByCosmoTEnteIdAndDtFineValIsNull(
      Long idEnte);

}
