/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.config;

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
  public static final String COMPONENT_NAME = "cosmoauthorization";

  public static final String COMPONENT_DESCRIPTION = "Cosmo Authorization";

  public static class DISCOVERY {

    private DISCOVERY() {
      throw new IllegalStateException("Utility class");
    }

    public static final String SERVICE_NAME = COMPONENT_NAME;

    public static final String SERVICE_ROUTE = "authorization";

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

  public static final String USER_PREF_VERSION = "user.pref.version";

  public static final String ENTE_PREF_VERSION = "ente.pref.version";

  public static final String ENTE_PREF_MAX_SIZE = "ente.pref.maxsize";

  public static final String ENTE_PREF_MAX_PIX = "ente.pref.maxpix";

  public static final String FULLNAME_REGEX = "\\s+(?=\\S*+$)";

  public static final String SPLIT_REGEX = ", ";

  public static class SORT {

    private SORT() {
      throw new IllegalStateException("Utility class");
    }

    public static final String EMAIL = "email";

    public static final String ID = "id";

    public static final String FIRST_NAME = "firstName";

    public static final String LAST_NAME = "lastName";

    public static final String FULL_NAME = "fullName";

    public static final String GROUP_ID = "groupId";

    public static final String NAME = "name";

    public static final String USER_ID = "userId";

    public static final String DESCRIZIONE = "descrizione";
    
    public static final String CODE = "code";
  }

}
