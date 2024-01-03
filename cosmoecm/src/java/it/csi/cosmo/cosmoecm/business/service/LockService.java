/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.function.Function;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.cosmoecm.business.service.impl.LockServiceImpl.LockAcquisitionAttemptResult;
import it.csi.cosmo.cosmoecm.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmoecm.dto.LockPolicyConfiguration;

public interface LockService {

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
  boolean release(CosmoTLock cosmoTLock);

}
