/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.config;

import java.util.Locale;


/**
 * Classe di utility contenente tutte le costanti
 */
public class Constants {

  /**
   * Utility classes, which are collections of static members, are not meant to be instantiated.
   * Even abstract utility classes, which can be extended, should not have public constructors.
   */



  private Constants() {
    throw new IllegalStateException();
  }

  public static final Locale LOCAL_IT = new Locale("it", "IT");



  /**
   * Nome della componente da utilizzare per il logging
   */
  public static final String COMPONENT_NAME = "cosmonotifications";



  public static final String PRODUCT = "cosmo";

  public static final String COMPONENT_DESCRIPTION = "Cosmo Notifications";

  public static class DISCOVERY {

    private DISCOVERY() {
      throw new IllegalStateException();
    }

    public static final String SERVICE_NAME = COMPONENT_NAME;

    public static final String SERVICE_ROUTE = "notifications";

  }

  /**
   * Classe di utility contenente le cache predisposte
   */
  public static class CACHES {

    private CACHES() {
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

  public static final String RESOURCE_METHOD_INVOKER_PROPERTY =
      "org.jboss.resteasy.core.ResourceMethodInvoker";

  public static final String ALL_NOTIFICATIONS = "all";

  public static final String COSMO_NOTIFICATIONS = "cosmo";

  public static final String EMAIL_NOTIFICATIONS = "email";

  public static final String SPLIT_REGEX = ", ";

  public static class SORT {

    private SORT() {
      throw new IllegalStateException();
    }

    public static final String CODICE_PAGINA = "codicePagina";

    public static final String DESCRIZIONE = "descrizione";

    public static final String CODICE_MODALE = "codiceModale";

  }

  public static final String MESSAGGIO = "%s pratica '%s'";

  public static final String MESSAGGIO_PARENT = "%s pratica '%s' %s '%s'";
}
