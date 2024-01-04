/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.config;

import java.util.Locale;


/**
 * Classe di utility contenente tutte le costanti
 */
public class Constants {

  /**
   * Utility classes, which are collections of static members, are not meant to be instantiated. Even abstract utility classes, which can be extended, should
   * not have public constructors.
   */
  private Constants () {
    throw new IllegalStateException();
  }

  public static final Locale LOCAL_IT = new Locale ( "it", "IT" );

  public static final String PRODUCT = "cosmo";

  /**
   * Nome della componente da utilizzare per il logging
   */
  public static final String COMPONENT_NAME = "cosmosoap";

  public static final String COMPONENT_DESCRIPTION = "Cosmo SOAP";

  public static class DISCOVERY {

    private DISCOVERY() {
      throw new IllegalStateException();
    }

    public static final String SERVICE_NAME = COMPONENT_NAME;

    public static final String SERVICE_ROUTE = "soap";

  }
  /**
   * Classe di utility contenente le cache predisposte
   */
  public static class CACHES {

    private CACHES () {
      throw new IllegalStateException();
    }

    /**
     * Cache di default
     */
    public static final String DEFAULT = "defaultCache";

    /**
     * Cache per i parametri di configurazione
     */
    public static final String CONFIGURATION = "configurationCache";

  }

  public static class EVENTS {

    private EVENTS() {
      throw new IllegalStateException();
    }

    public static final String PRATICA_INIZIO_FIRMA = "pratica.inizio_firma";
    public static final String PRATICA_FINE_FIRMA = "pratica.fine_firma";
    public static final String DOCUMENTI_INIZIO_FIRMA = "documenti.inizio_firma";
    public static final String DOCUMENTI_FIRMATI = "documenti.firmati";
    public static final String PRATICA_ERRORI_FIRMA = "pratica.errori_firma";
    public static final String FIRMA_ERRORI_PRELIMINARI = "firma.errori_preliminari";
    public static final String FIRMA_ERRORE_DOCUMENTO = "firma.errore_documento";
    public static final String DOCUMENTI_FINE_FIRMA = "documenti.fine_firma";
  }

  public static final String ESITO_SMISTAMENTO_OK = "000";
  public static final String REGEX_CODICE_ESITO_WARNING = "([0][0][2-9]|[0][1-9][0-9])";

  public static class TYPEDOSIGN {

    private TYPEDOSIGN() {
      throw new IllegalStateException();
    }

    public static final String PDF = "PDF";

    public static final String XML = "XMLENVELOPED";

  }


}
