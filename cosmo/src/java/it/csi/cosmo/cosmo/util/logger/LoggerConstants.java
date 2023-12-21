/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.util.logger;

import it.csi.cosmo.cosmo.config.Constants;

public class LoggerConstants {

  private LoggerConstants() {
    throw new IllegalStateException("LoggerConstants class");
  }

  public static final String ROOT_LOG_CATEGORY = Constants.PRODUCT + "." + Constants.COMPONENT_NAME;

}
