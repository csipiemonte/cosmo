/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.config;

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

  public static final String PRODUCT = "cosmo";

  /**
   * Nome della componente da utilizzare per il logging
   */
  public static final String COMPONENT_NAME = "cosmocmmn";

  public static final String COMPONENT_DESCRIPTION = "Cosmo CMMN";

  public static class DISCOVERY {

    private DISCOVERY() {
      throw new IllegalStateException();


    }

    public static final String SERVICE_NAME = COMPONENT_NAME;

    public static final String SERVICE_ROUTE = "cmmn";

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

}
