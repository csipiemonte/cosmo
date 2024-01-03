/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.config;

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
  public static final String COMPONENT_NAME = "cosmopratiche";

  public static final String COMPONENT_DESCRIPTION = "Cosmo Pratiche";

  public static class EVENTS {

    private EVENTS() {
      throw new IllegalStateException();
    }


    public static final String CARICAMENTOPRATICHE_BATCH = "notifica.batch.caricamentopratiche";
    public static final String CARICAMENTOPRATICHE_BATCH_PRATICA =
        "notifica.batch.caricamentopratiche.pratica";



  }


  public static class DISCOVERY {

    private DISCOVERY() {
      throw new IllegalStateException();
    }

    public static final String SERVICE_NAME = COMPONENT_NAME;

    public static final String SERVICE_ROUTE = "pratiche";

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

  public static final String RESEARCH_SIZE = "Research size";

  public static final int NUM_RESEARCH_SIZE = 100;

  public static final String FULLNAME_REGEX = "\\s+(?=\\S*+$)";
  public static final String SPLIT_REGEX = ", ";



  public static class SORT {

    private SORT() {
      throw new IllegalStateException();
    }

    public static final String EMAIL = "email";

    public static final String ID = "id";

    public static final String FIRST_NAME = "firstName";

    public static final String LAST_NAME = "lastName";

    public static final String FULL_NAME = "fullName";

    public static final String GROUP_ID = "groupId";

    public static final String NAME = "name";

    public static final String TYPE = "type";

    public static final String USER_ID = "userId";

    public static final String DATA_CREAZIONE_PRATICA = "dataCreazionePratica";

    public static final String DATA_AGGIORNAMENTO_PRATICA = "dataAggiornamentoPratica";

    public static final String DATA_CAMBIO_STATO = "dataCambioStato";

    public static final String OGGETTO = "oggetto";

    public static final String TIPOLOGIA = "tipologia";

    public static final String STATO = "stato";
  }

  public static class PROFILO {

    private PROFILO() {
      throw new IllegalStateException();
    }

    public static final String AMMINISTRATORE = "ADMIN";

    public static final String OPERATORE = "OPER";
  }

  public static class PRATICHE {

    private PRATICHE() {
      throw new IllegalStateException();
    }

    public static final int MAX_LENGTH_OGGETTO = 255;

    public static final String OGGETTO_ALPHANUMERIC_REGEX = "^[a-zA-Z0-9 ]*$";

    public static final String OGGETTO_PRINTABLE_CHARACTERS_REGEX = "^[^\\p{C}]*$";

    public static final String FORMATO_DATE_REGEX = "^\\d{4}\\-\\d{2}\\-\\d{2}$";

    public static final String PREFERITA = "preferita";

    public static final String CONDIVISA = "condivisa";

    public static final int MAX_PAGE_SIZE = 50;

  }

  public static final String CONDIVISIONE_PRATICA_UTENTE =
      "'%s' ha condiviso con te la pratica '%s'";
  public static final String CONDIVISIONE_PRATICA_GRUPPO =
      "'%s' ha condiviso con il gruppo '%s' la pratica '%s'";
  public static final String RIMOZIONE_CONDIVISIONE_PRATICA =
      "'%s' ha rimosso la condivisione della pratica '%s'";


  public static class POSTGRESQL_FUNCTIONS {

    private POSTGRESQL_FUNCTIONS() {
      throw new IllegalStateException();
    }

    public static final String GET_JSON_VALUE_BOOLEAN = "get_json_value_boolean";
    public static final String GET_JSON_VALUE_DATE = "get_json_value_date";
    public static final String GET_JSON_VALUE_NUMERIC = "get_json_value_numeric";
    public static final String GET_JSON_VALUE_STRING = "get_json_value_string";

    public static final String GET_JSON_ARRAY_BOOLEAN = "get_json_array_boolean";
    public static final String GET_JSON_ARRAY_DATE = "get_json_array_date";
    public static final String GET_JSON_ARRAY_NUMERIC = "get_json_array_numeric";
    public static final String GET_JSON_ARRAY_STRING = "get_json_array_string";

  }
}




