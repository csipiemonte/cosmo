/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTPratica"
 */

public interface CosmoTPraticaRepository extends CosmoTRepository<CosmoTPratica, Long> {

  CosmoTPratica findOneByAttivitaIdAndDtCancellazioneIsNull(Long id);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  CosmoTPratica findWithLockingByIdAndDtCancellazioneIsNull(Long id);
}
