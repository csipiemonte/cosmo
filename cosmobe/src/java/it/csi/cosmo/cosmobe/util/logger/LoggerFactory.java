/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmobe.util.logger;

import it.csi.cosmo.common.logger.CosmoLogger;

/**
 * Classe di centralizzazione della creazione dei log.
 */
public interface LoggerFactory {

  static CosmoLogger getLogger(String logCategory) {
    return new CosmoLogger(logCategory);
  }

  static CosmoLogger getLogger(LogCategory logCategory) {
    return new CosmoLogger(logCategory.getCategory());
  }

  static CosmoLogger getLogger(LogCategory logCategory, String className) {
    return new CosmoLogger(logCategory.getCategory(), className);
  }

}
