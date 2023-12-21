/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.config;

public enum ParametriApplicativo {

  //@formatter:off

  //logout urls
  LOGOUT_URL("logout.url"),
  LOGOUT_INTERNET_URL("logout.internet.url"),
  LOGOUT_INTRANET_URL("logout.intranet.url"),
  LOGOUT_INTRACOM_URL("logout.intracom.url"),

  // internal microservice security
  INTERNAL_JWT_SECRET("security.internal.jwt.secret", ExposurePolicy.SENSIBLE),
  INTERNAL_JWT_ISSUER("security.internal.jwt.issuer"),
  INTERNAL_JWT_ALGORITHM("security.internal.jwt.algorithm"),

  // discovery configuration
  UI_CLIENT_PASSWORD("ui.client.password", ExposurePolicy.SENSIBLE),
  DISCOVERY_CLIENTS_GENERIC_PASSWORD("discovery.clients.cosmoclient.password", ExposurePolicy.SENSIBLE),
  DISCOVERY_CLIENTS_COSMOBE_PASSWORD("discovery.clients.cosmobe.password", ExposurePolicy.SENSIBLE),
  DISCOVERY_CLIENTS_COSMOCMMN_PASSWORD("discovery.clients.cosmocmmn.password", ExposurePolicy.SENSIBLE),
  DISCOVERY_CLIENTS_COSMONOTIFICATIONS_PASSWORD("discovery.clients.cosmonotifications.password", ExposurePolicy.SENSIBLE),
  DISCOVERY_STORE_JNDI_ENABLE("discovery.store.infinispan.jndi.enable"),

  // jms messaging
  MESSAGING_ENABLE("messaging.jms.enable"),

  // file share
  FILE_SHARE_PATH("fileshare.path"),
  UPLOAD_PRATICHE_ROOT_PATH("upload.pratiche.root.path"),

  UPLOAD_PRATICHE_EXCEL_VALIDA_MAXROWS("upload.pratiche.excel.valida.maxrows"),
  UPLOAD_PRATICHE_EXCEL_MAXROWS("upload.pratiche.excel.maxrows"),

  // filters
  ENABLE_AUTHENTICATION_FILTER("authfilter.enable"),
  BYPASS_AUTHENTICATION_FILTER("authfilter.bypass"),
  ENABLE_CLIENT_AUTHENTICATION_FILTER("clientauthfilter.enable"),
  BYPASS_CLIENT_AUTHENTICATION_FILTER("clientauthfilter.bypass"),

  // mailing
  MAIL_ENABLE("mail.enable"),
  MAIL_SERVER("mail.server"),
  MAIL_PORT("mail.port"),
  MAIL_TRANSPORT_PROTOCOL("mail.transport"),
  MAIL_ENABLE_AUTHENTICATION("mail.authentication.enable"),
  MAIL_ENABLE_START_TLS("mail.authentication.starttls"),
  MAIL_USERNAME("mail.authentication.username", false),
  MAIL_PASSWORD("mail.authentication.password", false),
  MAIL_RETRY_MAX_RETRIES("mail.retry.maxretries"),
  MAIL_RETRY_DELAY("mail.retry.delay"),
  MAIL_RETRY_LINEAR_BACKOFF_FACTOR("mail.retry.linear"),
  MAIL_RETRY_EXPONENTIAL_BACKOFF_FACTOR("mail.retry.exponential"),
  MAIL_DEFAULT_FROM("mail.from"),
  MAIL_DEFAULT_FROM_NAME("mail.fromname"),
  MAIL_DEFAULT_DESTINATION("mail.default.destination"),
  MAIL_DEFAULT_CC("mail.default.cc", false),
  MAIL_DEFAULT_BCC("mail.default.bcc", false),
  MAIL_DEBOUNCE_PERIOD("mail.debounce.period", false),
  MAIL_ASSISTENZA_ENABLE("mail.assistenza.enable"),

  // misc
  NOME_AMBIENTE("nome.ambiente", ExposurePolicy.EXTERNAL),
  TESTMODE_ENABLED("testmode.enabled", true),

  //upload documenti zip
  UPLOADSESSION_DOCUMENTIZIP_MAX_SIZE("uploadsession.documentizip.max.size"),
  UPLOADSESSION_DOCUMENTIZIP_EXCEL_MAX_ROWS("uploadsession.documentizip.excel.max.rows");


  //@formatter:on

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

  private ParametriApplicativo(String codice, boolean obbligatorio) {
    this.codice = codice;
    this.obbligatorio = obbligatorio;
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
