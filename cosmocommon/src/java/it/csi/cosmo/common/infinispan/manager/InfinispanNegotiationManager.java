/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.infinispan.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.infinispan.model.InfinispanDistributedLockToken;


/**
 * Servizio per la gestione long-time automatica dei lock con INFINISPAN
 *
 */
public class InfinispanNegotiationManager implements AutoCloseable {

  private Logger logger;

  private TimeUnit timeUnit;

  private int renewalInterval;

  private int lockInterval;

  private InfinispanLockManager lockExecutor;

  private String resourceCode;

  private ScheduledExecutorService executor = null;

  private volatile boolean leaseStatus = false;

  private InfinispanDistributedLockToken lastAcquiredLock = null;

  private List<Runnable> acquireCallbacks;

  private List<Runnable> loseCallbacks;

  public InfinispanNegotiationManager(String resourceCode, TimeUnit timeUnit, int renewalInterval,
      InfinispanLockManager lockExecutor, String loggingPrefix) {
    super();
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".cluster.InfinispanNegotiationManager");
    this.timeUnit = timeUnit;
    this.renewalInterval = renewalInterval;
    lockInterval = (int) Math.round(this.renewalInterval * 1.25);
    this.lockExecutor = lockExecutor;
    this.resourceCode = resourceCode;
    this.acquireCallbacks = new ArrayList<>();
    this.loseCallbacks = new ArrayList<>();

    executor = Executors.newSingleThreadScheduledExecutor();

    executor.scheduleAtFixedRate(this::negotiateTick, 0, renewalInterval, timeUnit);
  }

  public InfinispanNegotiationManager onAcquire(Runnable callback) {
    this.acquireCallbacks.add(callback);
    return this;
  }

  public InfinispanNegotiationManager onLose(Runnable callback) {
    this.loseCallbacks.add(callback);
    return this;
  }

  public boolean getLeaseStatus() {
    return leaseStatus;
  }

  private void negotiateTick() {
    try {
      this.negotiate();
    } catch (Throwable t) { // NOSONAR
      logger.error("error in negotiate task", t);
    }
  }

  private synchronized boolean negotiate() {
    logger.debug("negotiating lease for resource " + resourceCode);

    lastAcquiredLock = lockExecutor.acquireLock(resourceCode, timeUnit, lockInterval);

    if (lastAcquiredLock == null) {
      if (leaseStatus) {
        logger.warn("lease LOST for resource " + resourceCode);
        this.loseCallbacks.forEach(Runnable::run);
      } else {
        logger.debug("lease still missing for resource " + resourceCode);
      }
      leaseStatus = false;
    } else {
      if (!leaseStatus) {
        logger.info("lease ACQUIRED for resource " + resourceCode);
        this.acquireCallbacks.forEach(Runnable::run);
      } else {
        logger.debug("lease still owned for resource " + resourceCode);
      }
      leaseStatus = true;
    }

    return leaseStatus;
  }

  @Override
  public void close() throws Exception {

    if (lastAcquiredLock != null) {
      logger.debug(
          "releasing resouce lease " + resourceCode + " before negotiator destruction");
      lockExecutor.releaseLock(lastAcquiredLock, true);
    }

    executor.shutdownNow();
  }
}
