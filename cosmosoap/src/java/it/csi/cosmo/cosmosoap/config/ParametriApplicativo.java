/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.config;

/**
 * Enumerazione con i parametri dell'applicativo da configurare
 */
public enum ParametriApplicativo {

  //@formatter:off
  // internal microservice security
  INTERNAL_JWT_SECRET("security.internal.jwt.secret", ExposurePolicy.SENSIBLE),

  // discovery configuration
  DISCOVERY_SERVER_LOCATION(ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY),
  DISCOVERY_CLIENT_ENABLE("discovery.client.enable"),
  DISCOVERY_CLIENT_USERNAME("discovery.client.username"),
  DISCOVERY_CLIENT_PASSWORD("discovery.client.password", ExposurePolicy.SENSIBLE),
  DISCOVERY_CLIENT_LOCATION("discovery.client.location"),
  DISCOVERY_CLIENT_HEARTBEAT_INTERVAL("discovery.client.heartbeat.interval"),

  // filters
  AUTHENTICATION_USER_MOCK_ENABLE("authentication.user.mock.enable"),
  AUTHENTICATION_CLIENT_MOCK_ENABLE("authentication.client.mock.enable"),

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

  // index
  ECM_USERNAME ( "integration.soap.ecmengine.username", ExposurePolicy.SENSIBLE ),
  ECM_PASSWORD ( "integration.soap.ecmengine.password", ExposurePolicy.SENSIBLE ),
  ECM_FRUITORE ( "integration.soap.ecmengine.fruitore" ),
  ECM_NOME_FISICO ( "integration.soap.ecmengine.nomeFisico" ),
  ECM_REPOSITORY ( "integration.soap.ecmengine.repository" ),
  ECM_ROOT_NODE_NAME ( "integration.soap.ecmengine.rootNodeName" ),
  ECM_URL ( "integration.soap.ecmengine.endpoint" ),
  ECM_STREAMING_URL ( "integration.soap.ecmengine.streamingEndpoint" ),
  ECM_TIMEOUT ( "integration.soap.ecmengine.timeout" ),

  // misc
  NOME_AMBIENTE("nome.ambiente", ExposurePolicy.EXTERNAL),
  TESTMODE_ENABLED("testmode.enabled", true),

  MAX_DOCUMENT_SIZE_FOR_EMAIL ( "mail.attachments.maxsize" ),

  // firma digitale
  REMOTESIGN_DOSIGN_URL("remotesign.dosign.providerUrl"),
  SIGNATUREVALIDATION_DOSIGN_URL("signaturevalidation.dosign.providerUrl"),
  REMOTESIGN_DOSIGN_CA_CODE("remotesign.dosign.ca.code"),
  REMOTESIGN_DOSIGN_TSA("remotesign.dosign.tsa", ExposurePolicy.SENSIBLE),
  REMOTESIGN_DOSIGN_MODE("remotesign.dosign.mode"),
  REMOTESIGN_DOSIGN_ENCODING("remotesign.dosign.encoding"),
  REMOTESIGN_DOSIGN_OTP_SENDING_TYPE("remotesign.dosign.otp.sending.type", ExposurePolicy.SENSIBLE),
  REMOTESIGN_DOSIGN_SIGNED_CONTENT_FILE_EXTENSION("remotesign.dosign.signed.content.file.extension"),
  REMOTESIGN_DOSIGN_TESTMODE_ENABLED("remotesign.dosign.testmode.enabled"),
  REMOTESIGN_UANATACA_URL("remotesign.uanataca.provideUrl"),
  //processing parallelo firma digitale
  NUM_MAX_FIRME_PARALLELE ("max.elaborazioni.firma", ExposurePolicy.EXTERNAL),


  //sigillo elettronico
  DOSIGN_URL("dosign.url"),

  // stardas
  STARDAS_URL("stardas.url"),
  STARDAS_CODICE_FRUITORE("stardas.codice.fruitore"),

  //stilo
  STILO_COSMOBEPROXY_ENABLED("stilo.cosmobeproxy.enabled"),
  STILO_COD_APPLICAZIONE("stilo.codiceapplicazione"),
  STILO_ISTANZA_APPLICAZIONE("stilo.istanzaapplicazione"),
  STILO_USERNAME("stilo.username"),
  STILO_PASSWORD("stilo.password"),
  STILO_ADDUD_URL("stilo.addud.url"),
  STILO_UPDUD_URL("stilo.updud.url"),

  // integrazione ACTA
  ACTA_MOCK_ENTE_ENABLE("acta.mock.ente.enable"),
  ACTA_MOCK_UTENTE_ENABLE("acta.mock.utente.enable"),
  ACTA_MOCK_UTENTE_CODICE_FISCALE("acta.mock.utente.codicefiscale"),
  ACTA_MOCK_ENTE_REPOSITORY_NAME("acta.mock.ente.repositoryname"),
  ACTA_MOCK_ENTE_APP_KEY("acta.mock.ente.appkey"),
  ACTA_USE_API_MANAGER("acta.apimanager.enable"),
  ACTA_API_MANAGER_CONSUMER_KEY("acta.apimanager.consumerkey"),
  ACTA_API_MANAGER_CONSUMER_SECRET("acta.apimanager.consumersecret"),
  ACTA_API_MANAGER_TOKEN_ENDPOINT("acta.apimanager.endpoints.token"),
  ACTA_ROOT_ENDPOINT("acta.endpoints.root"),
  ACTA_REPOSITORY_NAME("acta.repositoryname"),
  ACTA_APP_KEY("acta.appkey"),

  //file share
  FILE_SHARE_PATH("fileshare.path"),
  ;
  //@formatter:on

  public static final String DISCOVERY_SERVER_LOCATION_KEY = "discovery.server.location";
  public static final String STILO_COSMOBEPROXY = "stilo.cosmobeproxy";

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

