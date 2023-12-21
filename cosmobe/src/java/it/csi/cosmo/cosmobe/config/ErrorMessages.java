/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.config;

/**
 * Interfaccia contente tutti i messaggi di errore nell'applicativo.
 */
public abstract class ErrorMessages {

  private ErrorMessages() {
    // private
  }

  /**
   * LOGGER
   */
  public static final String CODICE_RUOLO_NON_DEFINITO = "Codice ruolo non definito";

  public static final String RUOLO_DESIDERATO_NON_CONSENTITO = "Ruolo desiderato non consentito";

  public static final String ERRORE_NEL_REPERIMENTO_DEI_RUOLI_DISPONIBILI =
      "Errore nel reperimento dei ruoli disponibili";

  public static final String MISSING_PRINCIPAL = "Missing principal";

  public static final String ERRORE_NEL_REPERIMENTO_DEGLI_UC = "Errore nel reperimento degli UC";

  public static final String IMPOSSIBILE_TROVARE_VOCE_TITOLARIO_CON_CLASSIFICAZIONE =
      "Impossibile trovare la voce di titolario con classificazione estesa: %s";

  public static final String ERRORE_GENERICO = "Errore generico";

  public static final String PARAMETRO_CONFIGURATO_CON_TROPPE_VARIANTI =
      "Parametro configurato su file di properties con troppe varianti: %s";

  public static final String ERRORE_VERIFICA_FILE_PROPERTIES =
      "Errore nella verifica del file di properties per l'ambiente %s";

  public static final String AMBIENTE_CORRENTE_NON_CORRETTO =
      "Ambiente corrente non configurato o non corretto";

  public static final String ERRORI_NELLA_CONFIGURAZIONE_DEI_PROFILI =
      "Sono presenti degli errori bloccanti nella configurazione dei profili";

  public static final String RUOLO_NON_RICONOSCIUTO = "Unrecognized role ";


  public static final String PARAMETRO_NON_VALORIZZATO = "Parametro '%s' non valorizzato";

  public static final String PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE =
      "Parametro '%s' non valorizzato correttamente";

  /**
   * ENTE
   */

  public static final String E_ENTE_NON_TROVATO = "Ente con id '%s' non trovato";

  public static final String E_ENTE_NON_ELIMINATO =
      "Errore nell'eliminazione dell'ente con id '%s'";

  public static final String E_ENTE_NON_VALORIZZATO = "Ente da creare non valorizzato";

  public static final String E_NOME_ENTE_NON_VALORIZZATO =
      "Nome dell'ente da creare non valorizzato";

  public static final String E_DATA_INIZIO_VALIDITA_ENTE_NON_VALORIZZATA =
      "Data dell'inizio validita' dell'ente da creare non valorizzato";

  /**
   * UTENTE
   */
  public static final String U_UTENTE_NON_VALORIZZATO = "Utente da creare non valorizzato";

  public static final String U_NOME_UTENTE_NON_VALORIZZATO =
      "Nome dell'utente da creare non valorizzato";

  public static final String U_COGNOME_UTENTE_NON_VALORIZZATO =
      "Cognome dell'utente da creare non valorizzato";

  public static final String U_CODICE_FISCALE_UTENTE_NON_VALORIZZATO =
      "Codice fiscale dell'utente da creare non valorizzato";

  public static final String U_UTENTE_NON_TROVATO = "Utente con id '%s' non trovato";

  public static final String U_UTENTE_CORRENTE_NON_TROVATO = "Utente corrente non trovato";

  public static final String U_DATA_INIZIO_VALIDITA_UTENTE_NON_VALORIZZATA =
      "Data dell'inizio validita' dell'ente da creare non valorizzato";

  /**
   * PROFILI
   */

  public static final String P_PROFILO_NON_TROVATO = "Profilo con id '%s' non trovato";

  public static final String P_PROFILO_NON_VALORIZZATO = "Profilo da creare non valorizzato";

  public static final String P_CODICE_PROFILO_NON_VALORIZZATO =
      "Codice del profilo da creare non valorizzato";

  public static final String P_DATA_INIZIO_VALIDITA_PROFILO_NON_VALORIZZATA =
      "Data dell'inizio validita' del profilo da creare non valorizzato";

}
