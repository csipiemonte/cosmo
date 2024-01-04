/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.config;

/**
 * Interfaccia contente tutti i messaggi di errore nell'applicativo.
 */
public abstract class ErrorMessages {

  private ErrorMessages() {
    // private constructor
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

  public static final String PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE =
      "Paramentro non definito tra le configurazioni";

  public static final String E_ENTE_CORRENTE_NON_TROVATO = "Ente corrente non trovato";

  public static final String FIRMA_ERRORE_RICHIESTA_OTP = "Errore occorso in fase di richiesta OTP";

  public static final String FIRMA_ERRORE = "Errore occorso in fase di firma";

  public static final String FIRMA_PUSHOTP_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA =
      "pushOtp - DoSign e' stato avviato in modalita' di test. Nessuna operazione effettuata";

  public static final String
  FIRMA_STARTTRANSACTION_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA =
  "startTransaction - DoSign e' stato avviato in modalita' di test. Nessuna operazione effettuata";

  public static final String
  FIRMA_FIRMASINGOLODOCUMENTO_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA =
  "firmaSingoloDocumento - DoSign e' stato avviato in modalita' di test. Nessuna operazione effettuata";

  public static final String
  FIRMA_ENDTRANSACTION_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA =
  "endTransaction - DoSign e' stato avviato in modalita' di test. Nessuna operazione effettuata";

  public static final String FIRMA_NESSUNA_CONFIGURAZIONE_PER_LA_CHIAVE =
      "Nessuna configurazione trovata per la chiave: %s";

  public static final String FIRMA_ERRORE_CONFIGURAZIONE_DATA = "Configurazione della data errata";

  public static final String FIRMA_ERRORE_DURANTE_FIRMA =
      "Errore durante la firma del documento.\nIdentificativo transazione: %s.\nTentativo di %s del documento %s";

  public static final String FIRMA_ERRORE_DURANTE_FIRMA_INVALID_SIGNER =
      "Errore durante la firma del documento, verificare le credenziali di firma.";

  public static final String FIRMA_ERRORE_DURANTE_FIRMA_INVALID_PIN =
      "Errore durante la firma del documento, verificare le credenziali di firma.";

  public static final String FIRMA_ERRORE_DURANTE_FIRMA_INVALID_OTP =
      "Errore durante la firma del documento, OTP scaduto o non valido.";

  public static final String FIRMA_ERRORE_DURANTE_FIRMA_PIN_LOCKED =
      "Errore durante la firma del documento, verificare le credenziali di firma.";

  public static final String FIRMA_NESSUN_ERRORE_RESTITUITO =
      "Nessun errore restituito dal servizio remoto";
  //
  public static final String FIRMA_ERRORE_DURANTE_PARSING_MESSAGGIO_ERRORE =
      "Errore durante il parsing del messaggio di errore restituito dal sistema remoto";

  public static final String FIRMA_MESSAGGIO_ORIGINALE_ERRORE = "Messaggio originale: %s";

  public static final String FIRMA_ERRORE_IN_FASE_DI_APERTURA_SESSIONE =
      "Errore in fase di apertura sessione per firma con id transazione: %s";

  /**
   * FIRMA (FIRMA_)
   */

  public static final String TIPO_FIRMA_PER_FORMATO_FILE_E_PROFILO_FEQ_NON_TROVATA =
      "Tipo firma per formato file '%s' e profilo feq '%' non trovata";

  public static final String FIRMA_TIPO_FIRMA_NON_TROVATA =
      "Tipo firma del documento firmato non trovata";

  public static final String FIRMA_FORMATO_DOCUMENTO_FIRMATO_NON_TROVATO =
      "Formato del documento firmato non trovato";

  public static final String REPERIMENTO_DOCUMENTO_INDEX =
      "Impossibile reperire il documento su index";

  public static final String FORMATO_DOCUMENTI_NON_COMPATIBILE_FORMATO_FEQ_SELEZIONATO = null;

  /**
   * SIGILLO ELETTRONICO
   */

  public static final String SIGILLO_ERRORE_COMUNICAZIONE_DOSIGN = "Errore occorso in fase di comunicazione con Dosign";

  public static final String SIGILLO_APPONI_SIGILLO_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA =
      "apponiSigilloSingoloDocumento - DoSign e' stato avviato in modalita' di test. Nessuna operazione effettuata";


}
