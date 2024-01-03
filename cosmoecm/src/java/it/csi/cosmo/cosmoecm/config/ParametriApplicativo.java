/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.config;

/**
 * Enumerazione con i parametri dell'applicativo da configurare
 */
public enum ParametriApplicativo {

  //@formatter:off
  // internal microservice security
  INTERNAL_JWT_SECRET("security.internal.jwt.secret", ExposurePolicy.SENSIBLE),

  // batch
  BATCH_FS_TO_INDEX_ENABLE("batch.fs2index.enable"),
  BATCH_SMISTAMENTO_STARDAS_ENABLE("batch.smistamento.stardas.enable"),
  BATCH_FS_TO_INDEX_MAX_DOCUMENTI_PER_ESECUZIONE("batch.fs2index.maxdoc"),
  BATCH_SMISTAMENTO_STARDAS_MAXNUMRETRY("batch.smistamento.stardas.maxnumretry"),
  BATCH_SIGILLO_ELETTRONICO_ENABLE("batch.sigillo.elettronico.enable"),

  // discovery configuration
  DISCOVERY_SERVER_LOCATION(ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY),

  DISCOVERY_CLIENT_ENABLE("discovery.client.enable"),
  DISCOVERY_CLIENT_USERNAME("discovery.client.username"),
  DISCOVERY_CLIENT_PASSWORD("discovery.client.password", ExposurePolicy.SENSIBLE),
  DISCOVERY_CLIENT_LOCATION("discovery.client.location"),
  DISCOVERY_CLIENT_HEARTBEAT_INTERVAL("discovery.client.heartbeat.interval"),

  // infinispan
  INFINISPAN_JNDI_ENABLE("infinispan.jndi.enable"),

  // filters
  AUTHENTICATION_USER_MOCK_ENABLE("authentication.user.mock.enable"),
  AUTHENTICATION_CLIENT_MOCK_ENABLE("authentication.client.mock.enable"),

  // file share
  FILE_SHARE_PATH("fileshare.path"),

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
  MAX_PAGE_SIZE("max.page.size", ExposurePolicy.EXTERNAL),
  EXPORT_ROW_MAX_SIZE("export.row.max.size", ExposurePolicy.EXTERNAL),

  TESTMODE_ENABLED("testmode.enabled", true),

  MAX_DOCUMENT_SIZE_FOR_EMBEDDED_UPLOAD ( "fruitore.documenti.maxembeddedsize" ),

  //  MAX_DOCUMENT_SIZE_FOR_LINK_DOWNLOAD_UPLOAD("fruitore.documenti.maxlinksize"),
  MAX_DOCUMENT_SIZE_FOR_EMAIL ( "mail.attachments.maxsize" ),

  //report template
  TEMPLATE_MAX_SIZE("template.max.size", ExposurePolicy.EXTERNAL),

  // stardas
  STARDAS_CODICE_FRUITORE("stardas.codice.fruitore"),

  DURATA_OTP_FEA_DEFAULT("durata.otp.fea.default"),

  ;
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

