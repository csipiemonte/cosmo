/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.infinispan.connector.InfinispanResourceConnector;
import it.csi.cosmo.common.infinispan.exceptions.InfinispanResourceBusyException;
import it.csi.cosmo.common.infinispan.helper.InfinispanFingerprintHelper;
import it.csi.cosmo.common.infinispan.manager.InfinispanLockManager;
import it.csi.cosmo.common.infinispan.manager.InfinispanNegotiationManager;
import it.csi.cosmo.common.infinispan.model.InfinispanDistributedLockToken;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.AsyncUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ClusterService;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.dto.exception.NotReadyException;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerConstants;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;


/**
 * Servizio per la gestione del cluster INFINISPAN
 *
 */
@Service
public class ClusterServiceImpl implements ClusterService, InitializingBean {

  private static final String MASTER_INSTANCE_RESOURCE_CODE = "AUTHORIZATION_MASTER_INSTANCE_LEASE";

  private static final int MASTER_INSTANCE_LEASE_INTERVAL = 5 * 1000;

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "ClusterServiceImpl");

  private InfinispanLockManager lockManager;

  private InfinispanNegotiationManager masterInstanceNegotiator;

  @Autowired
  private InfinispanResourceConnector connector;

  @Autowired
  private ConfigurazioneService configurazioneService;

  private InfinispanFingerprintHelper fingerprintHelper =
      new InfinispanFingerprintHelper(LoggerConstants.ROOT_LOG_CATEGORY);

  @Override
  public void afterPropertiesSet() throws Exception {
    AsyncUtils.when("INIT_CLUSTER_SERVICE", LoggerConstants.ROOT_LOG_CATEGORY,
        configurazioneService::isReady, this::initNegotiator, 5000l, null);
  }

  private void initNegotiator() {

    lockManager =
        new InfinispanLockManager(connector.getCache(), LoggerConstants.ROOT_LOG_CATEGORY);

    logger.info("afterPropertiesSet", "instantiating cluster member " + getInstanceId());

    masterInstanceNegotiator =
        new InfinispanNegotiationManager(MASTER_INSTANCE_RESOURCE_CODE, TimeUnit.MILLISECONDS,
            MASTER_INSTANCE_LEASE_INTERVAL, lockManager, LoggerConstants.ROOT_LOG_CATEGORY);

    masterInstanceNegotiator
    .onAcquire(() -> logger.info("leaseEvent",
        "CURRENT INSTANCE ELECTED TO MASTER OF CLUSTER (" + getInstanceId() + ")"))
    .onLose(() -> logger.info("leaseEvent",
        "i am no longer master of cluster (" + getInstanceId() + ")"));
  }

  @Override
  public Map<String, Object> getSharedCache() {
    return connector.getCache();
  }

  private String getInstanceId() {
    return fingerprintHelper.getInstanceId(false);
  }

  @Override
  public boolean isMaster() {
    if (masterInstanceNegotiator == null) {
      logger.warn("isMaster",
          "istance reports as SLAVE because no negotiator is enabled at the moment. This probably means you have NO MASTER INSTANCES at the moment.");
      return false;
    }
    return masterInstanceNegotiator.getLeaseStatus();
  }

  @Override
  public InfinispanDistributedLockToken acquireLock(String code, TimeUnit timeUnit, int duration) {

    requireLockManager();

    return lockManager.acquireLock(code, timeUnit, duration);
  }

  @Override
  public void releaseLock(InfinispanDistributedLockToken lock) {

    requireLockManager();

    lockManager.releaseLock(lock);
  }

  @Override
  public InfinispanDistributedLockToken waitLock(String code, TimeUnit timeUnit, int duration,
      TimeUnit timeUnitTimeOut, int timeOut) {

    requireLockManager();

    return lockManager.waitLock(code, timeUnit, duration, timeUnitTimeOut, timeOut);
  }

  @Override
  public <T> T executeLocking(String code, Supplier<T> task, TimeUnit timeUnit,
      int executionTimeout, int lockAcquisitionTimeout) throws InfinispanResourceBusyException {

    requireLockManager();

    return lockManager.executeLocking(code, task, timeUnit, executionTimeout,
        lockAcquisitionTimeout);
  }

  private void requireLockManager() {
    if (lockManager == null) {
      throw new NotReadyException(
          "Lock manager still not initialized. Is configuration service ready?");
    }
  }

  @Override
  public void close() throws Exception {

    String methodName = "close";
    if (masterInstanceNegotiator != null) {
      try {
        masterInstanceNegotiator.close();
      } catch (Exception t) {
        logger.error(methodName, "Error closing master instance negotiator", t);
      }
    }

    if (lockManager != null) {
      try {
        lockManager.close();
      } catch (Exception t) {
        logger.error(methodName, "Error closing distributed executor", t);
      }
    }

    try {
      connector.close();
    } catch (Exception t) {
      logger.error(methodName, "Error closing distributed executor", t);
    }
  }

}
