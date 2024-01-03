/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.config;

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
  public static final String COMPONENT_NAME = "cosmoecm";

  public static final String COMPONENT_DESCRIPTION = "Cosmo ECM";

  public static class DISCOVERY {

    private DISCOVERY() {
      throw new IllegalStateException();
    }

    public static final String SERVICE_NAME = COMPONENT_NAME;

    public static final String SERVICE_ROUTE = "ecm";

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

  public static final String MAX_PAGE_SIZE = "max.page.size";


  public static final String APPLICATION_TEXT = "application/text";

  public static final String SLASH = "/";

  public static class EVENTS {

    private EVENTS() {
      throw new IllegalStateException();
    }

    public static final String DOCUMENTI_ELABORATI = "ecm.batch.documentiElaborati";
    public static final String DOCUMENTI_SMISTATI = "ecm.batch.documentiSmistati";
    public static final String STATO_SMISTAMENTO = "notifica.stato.smistamento";
    public static final String STATO_DOCUMENTI = "notifica.stato.documenti";
    public static final String DOCUMENTI_SIGILLATI = "ecm.batch.documentiSigillati";

  }

  // smistamento stardas
  public static final int SOLO_PRINCIPALI = 1;
  public static final int SOLO_ALLEGATI = 2;
  public static final int PRINCIPALI_E_ALLEGATI = 3;
  public static final String ESITO_SMISTAMENTO_OK = "000";
  public static final String ESITO_SMISTAMENTO_OK_PARZIALE = "001";
  public static final String ESITO_RICHIESTA_SMISTAMENTO_TMP = "TMP";
  public static final String REGEX_CODICE_ESITO_WARNING = "([0][0][2-9]|[0][1-9][0-9])";

  public static final String SPLIT_REGEX = ", ";

  public static class SORT {

    private SORT() {
      throw new IllegalStateException();
    }

    public static final String ULTIMA_MODIFICA = "ultimaModifica";

    public static final String TITOLO_NOME_FILE = "titoloNomeFile";

  }

  public static final String STATO_SMISTAMENTO =
      "E' cambiato in '%s' lo stato dello smistamento del documento '%s' della";

  public static final String STATO_DOCUMENTO_SUCCESS =
      "E' stato elaborato il documento '%s' relativo alla ";

  public static final String STATO_DOCUMENTO_ERROR =
      "Mancata elaborazione del documento '%s' della ";

  // apposizione sigillo elettronico
  public static final String ESITO_RICHIESTA_APPOSIZIONE_SIGILLO_TMP = "TMP";
  public static final String ESITO_SIGILLO_OK = "000";
  public static final String ERR_CREDENZIALI_SIGILLO = "900";

  public static final String STATO_SIGILLO =
      "E' cambiato in '%s' lo stato della sigillazione del documento '%s' della";
}
