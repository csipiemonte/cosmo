/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * Classe di centralizzazione dei log.
 */
public class CosmoLogger {

  private Logger log;
  private String className;

  /**
   * Costrutture da usare solo negli aspect.
   *
   * @param logCategory
   */
  public CosmoLogger(String logCategory) {
    this.log = Logger.getLogger(logCategory);
  }

  public CosmoLogger(String logCategory, String className) {
    this.log = Logger.getLogger(logCategory);
    this.className = className;
  }

  public void beginForClass(String className, String method) {
    this.debug(className, method, LoggerFormatConstants.BEGIN);
  }

  public void endForClass(String className, String method) {
    this.debug(className, method, LoggerFormatConstants.END);
  }

  public void debug(String method, String message) {
    if (log.isDebugEnabled()) {
      log.debug(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message));
    }
  }

  public void debug(String method, String message, Object... params) {
    if (log.isDebugEnabled()) {
      log.debug(
          String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
              format(message, params)));
    }
  }

  public void debugForClass(String className, String method, String message) {
    if (log.isDebugEnabled()) {
      log.debug(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message));
    }
  }

  public void debugForClass(String className, String method, String message, Object... params) {
    if (log.isDebugEnabled()) {
      log.debug(
          String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
              format(message, params)));
    }
  }

  public void trace(String method, String message) {
    if (log.isTraceEnabled()) {
      log.trace(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message));
    }
  }

  public void trace(String method, String message, Object... params) {
    if (log.isTraceEnabled()) {
      log.trace(
          String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
              format(message, params)));
    }
  }

  public void warn(String method, String message) {
    log.warn(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message));
  }

  public void warnForClass(String className, String method, String message) {
    log.warn(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message));
  }

  public void warn(String method, String message, Object... params) {
    log.warn(
        String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
            format(message, params)));
  }

  public void warn(String method, String message, Throwable ex) {
    log.warn(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message), ex);
  }

  public void warn(String method, String message, Throwable ex, Object... params) {
    log.warn(
        String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
            format(message, params)),
        ex);
  }

  public void warnForClass(String className, String method, String message, Throwable ex) {
    log.warn(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message), ex);
  }

  public void warnForClass(String className, String method, String message, Throwable ex,
      Object... params) {
    log.warn(
        String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
            format(message, params)),
        ex);
  }

  public void info(String method, String message) {
    log.info(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message));
  }

  public void info(String method, String message, Object... params) {
    log.info(
        String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
            format(message, params)));
  }

  public void infoForClass(String className, String method, String message) {
    log.info(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message));
  }

  public void infoForClass(String className, String method, String message, Object... params) {
    log.info(
        String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
            format(message, params)));
  }

  public void error(String method, String message) {
    log.error(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message));
  }

  public void error(String method, String message, Object... params) {
    log.error(
        String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
            format(message, params)));
  }

  public void error(String method, String message, Throwable ex) {
    log.error(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message), ex);
  }

  public void error(String method, String message, Throwable ex, Object... params) {
    log.error(
        String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
            format(message, params)),
        ex);
  }

  public void errorForClass(String className, String method, String message, Throwable ex) {
    log.error(String.format(LoggerFormatConstants.DEFAULT_LOG, className, method, message), ex);
  }

  public void errorForClass(String className, String method, String message, Throwable ex,
      Object... params) {
    log.error(
        String.format(LoggerFormatConstants.DEFAULT_LOG, className, method,
            format(message, params)),
        ex);
  }

  public boolean isDebugEnabled() {
    return log.isDebugEnabled();
  }

  public boolean isTraceEnabled() {
    return log.isTraceEnabled();
  }

  public boolean isErrorEnabled() {
    return true;
  }

  private String format(String message, Object... params) {
    if (params.length < 1) {
      return message;
    }
    if (message.contains(LoggerFormatConstants.LOG_PARAM_PLACEHOLDER) && StringUtils
        .countMatches(message, LoggerFormatConstants.LOG_PARAM_PLACEHOLDER) == params.length) {
      // using slf4j format specifiers
      Object[] asStr = new String[params.length];
      for (int i = 0; i < params.length; i++) {
        asStr[i] = String.valueOf(params[i]);
      }
      return String.format(message.replace(LoggerFormatConstants.LOG_PARAM_PLACEHOLDER, "%s"),
          asStr);
    } else {
      return String.format(message, params);
    }
  }
}
