/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.infinispan.manager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.apache.log4j.Logger;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.infinispan.exceptions.InfinispanResourceBusyException;
import it.csi.cosmo.common.infinispan.helper.InfinispanFingerprintHelper;
import it.csi.cosmo.common.infinispan.helper.InfinispanThreadHelper;
import it.csi.cosmo.common.infinispan.model.InfinispanDistributedLockToken;


/**
 * Servizio per la gestione dei lock con INFINISPAN
 *
 */
public class InfinispanLockManager implements AutoCloseable {

  private Logger logger;

  private Map<String, Object> cache;

  private InfinispanThreadHelper threadHelper;

  private InfinispanFingerprintHelper fpHelper;

  public InfinispanLockManager(Map<String, Object> cache, String loggingPrefix) {
    super();
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".cluster.InfinispanLockManager");
    threadHelper = new InfinispanThreadHelper(loggingPrefix);
    fpHelper = new InfinispanFingerprintHelper(loggingPrefix);
    this.cache = cache;
  }

  public InfinispanDistributedLockToken acquireLock(String code, TimeUnit timeUnit,
      int durationTU) {
    Long duration = threadHelper.toMs(timeUnit, durationTU);

    String instanceId = fpHelper.getInstanceId();

    logger.debug("negotiating lock " + code + " for " + duration + " ms");

    threadHelper.randomDelay(1, 100);

    logger.trace("starting acquisition procedure");

    LocalDateTime now = LocalDateTime.now();

    // costruisco il token con il lock DESIDERATO
    InfinispanDistributedLockToken lockToken = new InfinispanDistributedLockToken();
    lockToken.setCode(code);
    lockToken.setAcquired(now);
    lockToken.setExpires(now.plus(duration, ChronoUnit.MILLIS));
    lockToken.setInstanceId(instanceId);

    logger.trace("generated request lock token " + lockToken.getLockId());

    String lockCode = "RESOURCE_LOCK_" + code;
    InfinispanDistributedLockToken previousLock =
        (InfinispanDistributedLockToken) cache.putIfAbsent(lockCode, lockToken);

    // non c'era un token precedente
    if (previousLock == null) {
      return acquireLockNoPrevious(lockCode, lockToken);
    }

    logger.debug("found existing token for lock code with lock ID " + previousLock.getLockId());

    threadHelper.randomDelay(10, 100);

    // c'era un token precedente. controllo se e' scaduto
    if (previousLock.isExpired()) {
      return acquireLockOnExpired(lockCode, lockToken, previousLock);
    }

    logger.debug("previous token is not expired");

    // c'e' un token precedente non ancora scaduto
    if (previousLock.getInstanceId().equals(instanceId)) {
      // il token precedente appartiene alla stessa istanza. lo ritorno

      logger
      .debug("previous not expired token is owned by current instance. renewing current lock.");

      previousLock.setExpires(lockToken.getExpires());

      cache.put(lockCode, previousLock);

      return previousLock;
    }

    logger.debug(
        "previous not expired token is owned by another instance. acquisition failed because of busy resource");

    // c'e' un token precedente non ancora scaduto associato ad un altra istanza.
    // acquisizione del lock fallita
    return null;
  }

  private InfinispanDistributedLockToken acquireLockOnExpired(String lockCode,
      InfinispanDistributedLockToken lockToken, InfinispanDistributedLockToken previousLock) {
    // il token precedente e' scaduto.

    logger.debug("detected expired lock. checking ID consistency");

    String expiredId = previousLock.getLockId();

    threadHelper.randomDelay(10, 100);

    InfinispanDistributedLockToken tokenNow = readCurrentLockIfAny(lockCode);
    if (tokenNow == null || !tokenNow.getLockId().equals(expiredId)) {
      logger.warn("concurrency acquisition mismatch detected at step 2. failing acquisition");
      return null;
    }

    logger.debug(
        "detected expired lock passed ID consistency check. acquiring lock for current instance");

    // forzo l'inserimento del lock desiderato
    tokenNow = (InfinispanDistributedLockToken) cache.put(lockCode, lockToken);

    if (tokenNow == null || !tokenNow.getLockId().equals(expiredId)) {
      logger.warn("concurrency acquisition mismatch detected at step 3. failing acquisition");
      return null;
    }

    threadHelper.randomDelay(10, 100);

    tokenNow = readCurrentLockIfAny(lockCode);
    if (tokenNow != null && tokenNow.getLockId().equals(lockToken.getLockId())) {
      logger.trace("consistency check passed");
      return lockToken;
    } else {
      logger.warn("concurrency acquisition mismatch detected at step 4. failing acquisition");
      return null;
    }
  }

  private InfinispanDistributedLockToken acquireLockNoPrevious(String lockCode,
      InfinispanDistributedLockToken lockToken) {
    // ritorno il lock acquisito

    logger.debug("no previous token found");

    threadHelper.randomDelay(10, 100);

    InfinispanDistributedLockToken tokenNow = readCurrentLockIfAny(lockCode);
    if (tokenNow != null && tokenNow.getLockId().equals(lockToken.getLockId())) {
      logger.trace("consistency check passed");
      return lockToken;
    } else {
      logger.warn("concurrency acquisition mismatch detected at step 1. failing acquisition");
      return null;
    }
  }

  public void releaseLock(InfinispanDistributedLockToken token) {
    releaseLock(token, false);
  }

  public void releaseLock(InfinispanDistributedLockToken token, boolean forceCurrentThread) {
    String instanceId = fpHelper.getInstanceId();

    logger.debug("releasing lock " + token.getCode());

    String lockCode = "RESOURCE_LOCK_" + token.getCode();
    InfinispanDistributedLockToken tokenNow = readCurrentLockIfAny(lockCode);

    if (tokenNow == null) {
      logger.warn("no current lock with such code.");
      return;
    }

    logger.debug("existing lock with such code with ID " + tokenNow.getLockId());

    if (!tokenNow.getInstanceId().equals(instanceId)) {
      if (forceCurrentThread) {
        logger.debug(
            "current lock is owned by another instance BUT thread forcing has been requested");
      } else {
        logger.warn("current lock is owned by another instance. nothing to release");
        return;
      }
    }

    String idBefore = tokenNow.getLockId();

    threadHelper.randomDelay(10, 100);

    tokenNow = readCurrentLockIfAny(lockCode);
    if (tokenNow == null) {
      logger.warn("lock released unexpectedly");
      return;
    }

    if (!tokenNow.getLockId().equals(idBefore)) {
      logger.warn("concurrency acquisition mismatch detected at step 1. failing release");
      return;
    }

    logger.trace("current token consistency check passed");
    cache.remove(lockCode);

    logger.debug("lock " + token.getCode() + " released");
  }

  private InfinispanDistributedLockToken readCurrentLockIfAny(String code) {
    return (InfinispanDistributedLockToken) cache.getOrDefault(code, null);
  }

  public InfinispanDistributedLockToken waitLock(String code, TimeUnit timeUnit, int durationTU,
      TimeUnit timeUnitTimeOut, int timeOutTU) {

    logger.debug("attempting to acquire lock " + code + " for " + durationTU + " " + timeUnit);

    Long timeOut = threadHelper.toMs(timeUnitTimeOut, timeOutTU);
    Long timePassed = 0L;
    Long timeStep = timeOut / 10;

    while (timePassed < timeOut) {

      InfinispanDistributedLockToken lock = acquireLock(code, timeUnit, durationTU);

      if (lock != null) {
        logger.debug("lock acquisition attempt succeeded");
        return lock;
      } else {
        logger.debug("lock acquisition attempt failed");
      }

      threadHelper.fixedDelay(timeStep.intValue());
      timePassed += timeStep;
      logger.debug(timePassed + " / " + timeOut + " ms elapsed");
    }

    logger.debug("lock acquisition failed after " + timePassed + " ms");
    return null;
  }

  public <T> T executeLocking(String code, Supplier<T> task, TimeUnit timeUnit,
      Integer executionTimeout, Integer lockAcquisitionTimeout)
          throws InfinispanResourceBusyException {

    logger.debug("attempting to acquire lock " + code + " for task execution");

    InfinispanDistributedLockToken lock =
        waitLock(code, timeUnit, executionTimeout, timeUnit, lockAcquisitionTimeout);

    if (lock == null) {
      throw new InfinispanResourceBusyException(
          "Could not acquire lock " + code + " within specified timeout for execution");
    }

    try {
      return task.get();
    } finally {
      releaseLock(lock);
    }
  }

  @Override
  public void close() throws Exception {
    // NOP
  }

}
