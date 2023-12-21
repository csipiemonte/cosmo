/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import java.util.function.Function;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.cosmobusiness.business.service.impl.LockServiceImpl.LockAcquisitionAttemptResult;
import it.csi.cosmo.cosmobusiness.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmobusiness.dto.LockPolicyConfiguration;
import it.csi.cosmo.cosmobusiness.dto.rest.AcquisizioneLockRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Lock;
import it.csi.cosmo.cosmobusiness.dto.rest.RilascioLockRequest;

public interface LockService {

  Lock getLock(String codiceRisorsa);

  Lock postLock(AcquisizioneLockRequest body);

  boolean deleteLock(RilascioLockRequest body);

  void checkLock(String codiceRisorsa, boolean required);

  void reconfigure(LockPolicyConfiguration policy);

  /**
   * @param <T>
   * @param task
   * @param locking
   * @return
   */
  <T> T executeLocking(Function<CosmoTLock, T> task, LockAcquisitionRequest locking);

  /**
   * @param request
   * @return
   */
  LockAcquisitionAttemptResult acquire(LockAcquisitionRequest request);

  /**
   * @param request
   * @return
   */
  boolean release(CosmoTLock request);

}
