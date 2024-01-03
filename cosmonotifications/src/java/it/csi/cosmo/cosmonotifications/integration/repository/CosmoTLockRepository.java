/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.repository;

import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTLock"
 */

public interface CosmoTLockRepository extends CosmoTRepository<CosmoTLock, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT c FROM CosmoTLock c WHERE c.id = ?1")
  public CosmoTLock lock(Long id);

}
