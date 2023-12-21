/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.config;

import java.util.Locale;


/**
 * Classe di utility contenente tutte le costanti
 */
public class Constants {

  public static final String ORCHESTRATOR_USERNAME = "COSMO_ORCHESTRATOR";

  public static final String PRODUCT = "cosmo";

  public static final String COMPONENT_NAME = "cosmo";

  public static final String COMPONENT_DESCRIPTION = "Cosmo Orchestrator";

  /**
   * Utility classes, which are collections of static members, are not meant to be instantiated.
   * Even abstract utility classes, which can be extended, should not have public constructors.
   */
  private Constants() {
    throw new IllegalStateException("Utility class");
  }

  public static final Locale LOCAL_IT = new Locale("it", "IT");

  /**
   * Classe di utility contenente le cache predisposte
   */
  public static class CACHES {

    private CACHES() {
      throw new IllegalStateException("Utility class");
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
