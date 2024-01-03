/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.util.logger;

import it.csi.cosmo.common.logger.CosmoLogger;

public interface LoggerFactory {

  public static CosmoLogger getLogger(String logCategory) {
    return new CosmoLogger(logCategory);
  }

  public static CosmoLogger getLogger(LogCategory logCategory) {
    return new CosmoLogger(logCategory.getCategory());
  }

  public static CosmoLogger getLogger(LogCategory logCategory, String className) {
    return new CosmoLogger(logCategory.getCategory(), className);
  }

}
