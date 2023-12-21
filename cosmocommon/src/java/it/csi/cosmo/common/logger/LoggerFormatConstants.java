/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.logger;

public class LoggerFormatConstants {

  private LoggerFormatConstants() {
    throw new IllegalStateException("LoggerFormatConstants class");
  }

  public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

  public static final String DATE_PATTERN = "yyyy-MM-dd";

  public static final String TIME_PATTERN = "HH:mm:ss.SSS";

  public static final String DEFAULT_LOG = "[%s::%s] %s";

  public static final String BEGIN = "BEGIN.";

  public static final String END = "END.";

  public static final String LOG_PARAM_PLACEHOLDER = "{}";
}
