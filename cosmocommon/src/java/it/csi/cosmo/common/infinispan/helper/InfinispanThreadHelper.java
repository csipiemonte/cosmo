/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.infinispan.helper;

import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import it.csi.cosmo.common.config.Constants;


public class InfinispanThreadHelper {

  private Logger logger;

  private InfinispanFingerprintHelper fsHelper;

  public InfinispanThreadHelper(String loggingPrefix) {
    logger = Logger.getLogger((loggingPrefix != null ? loggingPrefix : Constants.PRODUCT)
        + ".cluster.InfinispanThreadHelper");
    fsHelper = new InfinispanFingerprintHelper(loggingPrefix);
  }

  public void fixedDelay(int amount) {
    try {
      logger.trace("sleeping for " + amount + " ms");
      Thread.sleep(amount);
    } catch (Exception e) {
      logger.warn("fiexdDelay interrupted");
    }
  }

  public void randomDelay(int min, int max) {
    try {
      long amount = fsHelper.getRandomGenerator().nextInt((max - min)) + (long) min;
      logger.trace("sleeping for " + amount + " ms");
      Thread.sleep(amount);
    } catch (Exception e) {
      logger.warn("fiexdDelay interrupted");
    }
  }

  public Long toMs(TimeUnit timeUnit, int duration) {
    if (timeUnit == null) {
      return Long.valueOf(duration);
    }

    return timeUnit.toMillis(duration);
  }
}
