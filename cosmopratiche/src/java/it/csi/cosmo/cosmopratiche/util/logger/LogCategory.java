/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.util.logger;

/**
 * Enumerazione contenente tutte le categorie da indicare nei log
 */
public enum LogCategory {

  //@formatter:off
  COSMOPRATICHE_INFINISPAN_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".cluster"),
  COSMOPRATICHE_MESSAGING_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".messaging"),
  COSMOPRATICHE_WEBSOCKET_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".websocket"),
  COSMOPRATICHE_BUSINESS_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".business"),
  COSMOPRATICHE_MAIL_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".mail"),
  COSMOPRATICHE_FILESHARE_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".fileshare"),
  COSMOPRATICHE_DISCOVERY_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".discovery"),
  COSMOPRATICHE_SECURITY_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".security"),
  COSMOPRATICHE_FILTER_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".filter"),
  COSMOPRATICHE_REST_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".rest"),
  COSMOPRATICHE_UTIL_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY + ".util"),
  COSMOPRATICHE_PROXY_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY +  ".proxy"),
  COSMOPRATICHE_AUDIT_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY +  ".audit"),
  COSMOPRATICHE_BATCH_LOG_CATEGORY(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY +  ".batch");
  //@formatter:on

  private String category;

  private LogCategory(String category) {
    this.category = category;
  }

  public String getCategory() {
    return category;
  }
}
