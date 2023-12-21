/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.infinispan.helper;

import java.util.Random;
import java.util.UUID;
import org.apache.log4j.Logger;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.exception.BadConfigurationException;


public class InfinispanFingerprintHelper {

  private Logger logger;

  public InfinispanFingerprintHelper(String loggingPrefix) {
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".cluster.InfinispanFingerprintHelper");
  }

  private static String instanceId;

  private static final Object instanceIdLock = new Object();

  private static Random randomGenerator;

  public Random getRandomGenerator() {
    return randomGenerator;
  }

  public String getInstanceId() {
    return getInstanceId(true);
  }

  public String getInstanceId(boolean threadDependent) {

    synchronized (instanceIdLock) {
      if (instanceId == null) {
        try {
          instanceId = computeInstanceId();
          logger.info("computed instance ID at " + instanceId);
        } catch (Exception e) {
          throw new BadConfigurationException("Error computing unique instance ID", e);
        }
        randomGenerator = new Random(instanceId.hashCode());
      }
    }

    if (threadDependent) {
      return instanceId + "-" + Thread.currentThread().getName();
    } else {
      return instanceId;
    }
  }

  private static String computeInstanceId() {

    StringBuilder sb = new StringBuilder().append(UUID.randomUUID().toString()).append("-")
        .append(System.currentTimeMillis());

    return sb.toString();
  }

}
