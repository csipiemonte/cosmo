/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import java.util.Optional;
import it.csi.cosmo.common.entities.CosmoTVariabile;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTCommento"
 */

public interface CosmoTVariabileRepository extends CosmoTRepository<CosmoTVariabile, Long> {

  Optional<CosmoTVariabile> findByNomeAndCosmoTPraticaIdAndDtCancellazioneIsNull(String nome,
      Long idPratica);


}
