/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.util.logger;

/**
 * Enumerazione contenente tutte le categorie da indicare nei log
 */
public enum LogCategory {

  //@formatter:off
  INFINISPAN_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".cluster"),
  MESSAGING_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".messaging"),
  WEBSOCKET_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".websocket"),
  BUSINESS_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".business"),
  MAIL_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".mail"),
  FILESHARE_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".fileshare"),
  DISCOVERY_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".discovery"),
  SECURITY_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".security"),
  FILTER_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".filter"),
  REST_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".rest"),
  UTIL_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".util"),
  SOAP_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY + ".soap"),
  PROXY_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY +  ".proxy"),
  AUDIT_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY +  ".audit"),
  BATCH_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY +  ".batch"),
  TEST_LOG_CATEGORY(LoggerConstants.ROOT_LOG_CATEGORY +  ".test"),;
  //@formatter:on

  private String category;

  private LogCategory(String category) {
    this.category = category;
  }

  public String getCategory() {
    return category;
  }
}
