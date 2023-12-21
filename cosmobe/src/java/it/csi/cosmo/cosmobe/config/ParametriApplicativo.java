/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.config;

/**
 * Enumerazione con i parametri dell'applicativo da configurare
 */
public enum ParametriApplicativo {

  //@formatter:off
  // internal microservice security
  INTERNAL_JWT_SECRET("security.internal.jwt.secret", ExposurePolicy.SENSIBLE),
  INTERNAL_JWT_ISSUER("security.internal.jwt.issuer"),
  INTERNAL_JWT_ALGORITHM("security.internal.jwt.algorithm"),

  // discovery configuration
  DISCOVERY_SERVER_LOCATION(ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY),

  DISCOVERY_FETCH_ENABLE("discovery.fetch.enable"),
  DISCOVERY_FETCH_INTERVAL("discovery.fetch.interval"),

  DISCOVERY_FETCH_FALLBACK_ENABLE("discovery.fetch.fallback.enable"),
  DISCOVERY_FETCH_FALLBACK_LOCATIONS("discovery.fetch.fallback.locations"),

  DISCOVERY_CLIENT_ENABLE("discovery.client.enable"),
  DISCOVERY_CLIENT_USERNAME("discovery.client.username"),
  DISCOVERY_CLIENT_PASSWORD("discovery.client.password", ExposurePolicy.SENSIBLE),
  DISCOVERY_CLIENT_LOCATION("discovery.client.location"),
  DISCOVERY_CLIENT_HEARTBEAT_INTERVAL("discovery.client.heartbeat.interval"),

  // filters
  AUTHENTICATION_CLIENT_MOCK_ENABLE("authentication.client.mock.enable"),
  AUTHENTICATION_CLIENT_BYPASS("authentication.client.bypass"),
  AUTHENTICATION_BASIC_API_MANAGER_USERNAME("authentication.basic.apimanager.username"),
  AUTHENTICATION_BASIC_API_MANAGER_PASSWORD("authentication.basic.apimanager.password"),
  AUTHENTICATION_BASIC_STARDAS_USERNAME("authentication.basic.stardas.username"),
  AUTHENTICATION_BASIC_STARDAS_PASSWORD("authentication.basic.stardas.username"),

  // file share
  FILE_SHARE_PATH("fileshare.path"),



  UPLOAD_EXCEL_PATH("upload.excel.path"),
  UPLOAD_EXCEL_PATH_MAXROWS("upload.excel.maxrows"),

  // misc
  NOME_AMBIENTE("nome.ambiente", ExposurePolicy.EXTERNAL),
  CORS_ENABLE("cors.enable", ExposurePolicy.SENSIBLE),
  TESTMODE_ENABLED("testmode.enabled", ExposurePolicy.EXTERNAL);



  //@formatter:on

  public static final String DISCOVERY_SERVER_LOCATION_KEY = "discovery.server.location";

  private String codice;

  private boolean obbligatorio;

  private ExposurePolicy policyEsposizione = ExposurePolicy.PUBLIC;

  public String getCodice() {
    return codice;
  }

  public boolean isObbligatorio() {
    return obbligatorio;
  }

  public ExposurePolicy getPolicyEsposizione() {
    return policyEsposizione;
  }

  private ParametriApplicativo(String codice) {
    this.codice = codice;
    obbligatorio = true;
  }

  private ParametriApplicativo(String codice, ExposurePolicy exposurePolicy) {
    this.codice = codice;
    obbligatorio = true;
    policyEsposizione = exposurePolicy;
  }

  public enum ExposurePolicy {
    /**
     * Valore di default: parametro non riservato, disponibile solo a backend
     */
    PUBLIC,

    /**
     * Valore disponibile solo a backend e considerato sensibile. Non verra' mostrato in nessun log
     * ne' esposto all'esterno dell'applicativo
     */
    SENSIBLE,

    /**
     * Valore disponibile ed esposto anche a frontend
     */
    EXTERNAL
  }
}

