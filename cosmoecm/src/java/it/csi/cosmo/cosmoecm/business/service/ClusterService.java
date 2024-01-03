/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import it.csi.cosmo.common.infinispan.exceptions.InfinispanResourceBusyException;
import it.csi.cosmo.common.infinispan.model.InfinispanDistributedLockToken;


/**
 * Servizio per la gestione del cluster
 *
 */
public interface ClusterService extends AutoCloseable {

  boolean isMaster();

  InfinispanDistributedLockToken acquireLock(String code, TimeUnit timeUnit, int duration);

  void releaseLock(InfinispanDistributedLockToken lock);

  InfinispanDistributedLockToken waitLock(String code, TimeUnit timeUnit, int duration,
      TimeUnit timeUnitTimeOut, int timeOut);

  <T> T executeLocking(String code, Supplier<T> task, TimeUnit timeUnit, int executionTimeout,
      int lockAcquisitionTimeout)
          throws InfinispanResourceBusyException;

  Map<String, Object> getSharedCache();

}
