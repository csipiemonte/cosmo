/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.config;

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

  public static final String PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE =
      "Paramentro non definito tra le configurazioni";

  public static final String PATH_NON_TROVATO = "path non trovato";

  public static final String FILENAME_NON_TROVATO = "fileName non trovato";

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

  public static final String E_ENTE_CORRENTE_NON_TROVATO = "Ente corrente non trovato";

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

  /**
   * DOCUMENTI
   */
  public static final String D_NESSUN_DOCUMENTO_FORNITO = "Nessun documento fornito";

  public static final String D_DOCUMENTO_ESISTENTE = "Documento con id '%d' gia' esistente";

  public static final String D_DOCUMENTO_NON_TROVATO = "Documento con id '%d' non trovato";

  public static final String D_DOCUMENTO_PADRE_NON_TROVATO = "Documento padre con id '%d' non trovato";

  public static final String D_DOCUMENTO_FRUITORE_ESISTENTE =
      "Documento con id '%s' gia' esistente";

  public static final String D_DOCUMENTO_FRUITORE_NON_TROVATO = "Documento con id '%s' non trovato";

  public static final String D_CODICE_TIPO_DOCUMENTO =
      "Tipologia di documento non indicata o errata";

  public static final String D_DOCUMENTO_NON_ASSOCIATO =
      "Il documento non e' associato ad alcuna pratica";

  public static final String D_PRATICA_SENZA_NODO =
      "La pratica associata al documento non ha alcun nodo su Index";

  public static final String D_OGGETTO_DOCUMENTO_NON_ERRATO =
      "Oggetto del documento inviato errato";

  public static final String D_DOCUMENTO_INVIATO_ERRATO = "Documento inviato errato";

  public static final String D_NOME_FILE_ERRATO = "Nome del file inviato errato";

  public static final String D_ERRORE_REPERIMENTO_CONTENUTO_DOCUMENTO =
      "Errore nel reperimento del contenuto del file inviato";

  public static final String D_TIPO_DOCUMENTO_NON_ESISTENTE =
      "Tipo documento con codice '%s' non esistente";

  public static final String D_TIPO_DOCUMENTO_NON_TROVATO =
      "Tipo documento con codice '%s' non trovato";

  public static final String D_TIPO_FIRMA_NON_ESISTENTE =
      "Tipo firma con codice '%s' non esistente";

  public static final String D_DOCUMENTO_PRINCIPALE_NON_TROVATO =
      "Il documento principale con id '%s' non esiste";

  public static final String D_TIPO_DOC_PADRE_NO_ALLEGATI =
      "Non e' possibile inserire allegati al documento padre '%s";

  public static final String D_TIPO_DOC_NON_AMMISSIBILE =
      "Il tipo documento '%s' non e' compatibile con il tipo del documento padre con id '%s'";

  public static final String D_DOCUMENTO_NON_COMPETENZA_FRUITORE =
      "Documento non di competenza del fruitore";

  public static final String D_NESSUN_CONTENUTO_DISPONIBILE_RECUPERO_FRUITORE =
      "Nessun contenuto disponibile per il documento da recuperare";

  public static final String D_NESSUN_DOCUMENTO_TASK_DISPONIBILE =
      "Nessun documento disponibile per il task con id '%s'";

  public static final String D_TIPO_DOC_PROTO_MODIF =
      "Tipologia documento protocollabile: il formato del file non e' tra quelli ammessi all' importazione";

  public static final String DOCUMENTO_VUOTO = "Il documento inserito e' vuoto";

  public static final String D_PADRE_NON_PROTOCOLLABILE =
      "Errore in fase di importazione allegato protocollabile: il padre risulta non protocollabile";

  public static final String D_RELAZIONE_TIPO_DOC_NON_TROVATA =
      "Relazione tra il tipo documento padre '%d' e il tipo documento allegato '%d' non trovata";

  public static final String D_LETTURA_MIME_TYPE_ERRATA =
      "Errore in fase di rilevamento mime type per il documento con id '%d'";

  public static final String D_TIPO_DOCUMENTO_TIPO_PRATICA_NON_ESISTENTE =
      "Tipo documento con codice '%s' non presente per la tipologia di pratica: '%s'";

  public static final String D_TIPO_DOCUMENTO_ALLEGATO_TIPO_PRATICA =
      "Tipo documento con codice '%s' non puo' essere inserito come documento principale essendo allegato del tipo documento padre: '%s'";

  public static final String D_DOWNLOAD_DIRETTO_NON_DISPONIBILE_CONTENUTI_TEMPORANEI =
      "Download diretto non disponibile per i contenuti temporanei";

  public static final String D_CONTENUTO_FISICO_NON_LOCALIZZABILE =
      "Contenuto fisico non localizzabile";

  public static final String D_ERRORE_CREAZIONE_URI_DA_URL =
      "Errore nella creazione dell'URI dell'url: %s";

  /**
   * FILTRI NELLA RICERCA
   */
  public static final String SIZE_PAGINA_LIMIT_VALORIZZATI =
      "Valorizzazione errata: e' valorizzata sia la size della pagina che il numero massimo di elementi";
  public static final String PAGE_PAGINA_OFFSET_VALORIZZATI =
      "Valorizzazione errata: e' valorizzata sia il numero della pagina che l'offset degli elementi";

  /**
   * INDEX
   */
  public static final String I_NESSUN_CONTENUTO_DA_SCARICARE = "Nessun contenuto da scaricare";

  public static final String I_NESSUNA_CARTELLA =
      "Nessuna cartella con UUID '%s' trovata su index. Impossibile spostare il documento";

  public static final String I_ERRORE_CREAZIONE_CARTELLA = "Errore nella creazione della cartella";

  /**
   * PRATICHE
   */

  public static final String P_PRATICA_NON_TROVATA = "Pratica con id '%s' non trovata";

  public static final String P_ID_PRATICA_NON_VALORIZZATO = "Id pratica non valorizzato";

  public static final String D_TIPO_DOCUMENTO_NON_VALORIZZATO = "Tipo documento non valorizzato";

  public static final String D_TIPO_PRATICA_NON_TROVATA = "Tipo pratica %s " + "non trovata";

  /**
   * RICERCA FILE SYSTEM
   */
  public static final String FS_UUID_FILE_NON_TROVATO = "File con uuid '%s' non presente a sistema";

  public static final String FS_FILE_ELIMINATO_CORRETTAMENTE =
      "File con uuid '%s' eliminato correttamente";

  /**
   * CRYPT
   */
  public static final String CRYPT_ERRORE_DURANTE_DECRIPTAZIONE = "Errore durante l'operazione di decrypt";

  public static final String CRYPT_ERRORE_DURANTE_CRIPTAZIONE = "Errore durante l'operazione di encrypt";

  public static final String CRYPT_ERRORE_DURANTE_INIZIALIZZAZIONE_CIPHTER_EN = "Errore durante l'inizializzazione del cipher di encryption";

  public static final String CRYPT_ERRORE_DURANTE_INIZIALIZZAZIONE_CIPHTER_DEC = "Errore durante l'inizializzazione del cipher di decryption";

  public static final String CRYPT_PASSPHRASE_NON_TROVATA_NELLE_PROPERTIES_ESTERNE = "passphrase non trovata nel file di properties esterno";

  public static final String CRYPT_IVPARAMETER_NON_TROVATO_NELLE_PROPERTIES_ESTERNE = "ivparameter non trovato nel file di properties esterno";

  /**
   * FIRMA (FIRMA_)
   */

  public static final String FIRMA_ERRORE_RICHIESTA_OTP = "Errore occorso in fase di richiesta OTP";

  public static final String FIRMA_ERRORE = "Errore occorso in fase di firma";

  public static final String FIRMA_STARTTRANSACTION_DOSIGN_IN_MODALITA_TEST_NESSUNA_OPERAZIONE_EFFETTUATA =
      "startTransaction - DoSign e' stato avviato in modalita' di test. Nessuna operazione effettuata";

  public static final String STATO_SMISTAMENTO_NON_PRESENTE_O_NON_ATTIVO =
      "Lo stato %s e' presente e attivo su db?\"";

  public static final String FIRMA_ERRORE_CERTIFICATO_SCADUTO =
      "La scadenza del certificato non e' valida";

  public static final String REPERIMENTO_DOCUMENTO_INDEX =
      "Impossibile reperire il documento su index";

  public static final String TIPO_FIRMA_PER_FORMATO_FILE_E_PROFILO_FEQ_NON_TROVATA =
      "Tipo firma per formato file '%s' e profilo feq '%' non trovata";

  public static final String FORMATO_DOCUMENTI_NON_COMPATIBILE_FORMATO_FEQ_SELEZIONATO =
      "Il formato di uno o piu' documenti non e' compatibile con il profilo FEQ selezionato";

  public static final String FIRMA_ENTE_CERTIFICATOR_NON_PRESENTE_O_NON_ATTIVO =
      "Errore durante la firma del documento, verificare l'ente certificatore";

  public static final String FIRMA_FORMATO_DOCUMENTO_FIRMATO_NON_TROVATO =
      "Formato del documento firmato non trovato";

  public static final String FIRMA_TIPO_FIRMA_NON_TROVATA =
      "Tipo firma del documento firmato non trovata";

  public static final String D_TEMPLATE_REPORT_NON_TROVATO =
      "Template report con id %s non trovato";

  public static final String DIMENSIONE_DOCUMENTO_SUPERIORE_AL_MASSIMO_CONFIGURATO =
      "Dimensione documento superiore al massimo previsto per questa tipologia di documento '%s'";

  public static final String D_DA_FIRMARE_INFO_NON_CORRETTE = "Errore nelle informazioni inviate";

  public static final String FIRMA_ERRORE_ENTE_CERTIFICATORE_NON_PRESENTE = "Ente certificatore non riconosciuto";

  /**
   * STILO
   */

  public static final String STILO_GIA_INVIATO =
      "Il documento con id '%d' e idUd '%d' è stato già inviato a Stilo";

  public static final String STILO_GIA_IN_FASE_DI_INVIO =
      "Il documento con id '%d' e idUd '%d' è già in fase di invio a Stilo";

  public static final String STILO_STATO_NON_TROVATO = "Stato invio Stilo '%s' non trovato";

  /**
   * FEA
   */
  public static final String FEA_TEMPLATE_NON_TROVATO = "Template fea con id '%s' non trovato";
  public static final String FEA_DOCUMENTO_NON_TROVATO =
      "Nessun contenuto firmato con firma fea per il documento %s";
  public static final String FEA_UTENTE_NON_TROVATO = "Utente non trovato";
  public static final String FEA_FIRMATARIO_NON_TROVATO =
      "Nessun firmatario trovato con il codice fiscale %s";

  public static final String FEA_CODICE_OTP_NON_TROVATO = "Codice OTP '%s' non trovato o scaduto";

  public static final String FEA_UTENTE_NON_AUTENTICATO_PRESSO_ENTE = "Utente non autenticato presso un ente";

  /**
   * SIGILLO ELETTRONICO
   */

  public static final String SIGILLO_ELETTRONICO_NON_TROVATO = "Sigillo elettronico con id '%s' non trovato";

  public static final String SIGILLO_ELETTRONICO_CON_ALIAS_ESISTENTE =
      "Sigillo elettronico con alias '%s' gia' esistente";

  public static final String SIGILLO_ELETTRONICO_ERRORE = "Errore occorso in fase di apposizione sigillo elettronico";

  public static final String ALIAS_SIGILLO_NON_TROVATO = "L'alias per la configurazione del sigillo elettronico non e' presente";

  public static final String STATO_SIGILLO_NON_PRESENTE_O_NON_ATTIVO =
      "Lo stato %s e' presente e attivo su db?\"";

  public static final String DOCUMENTO_NON_PRESENTE_ID_SIGILLO_INSERIMENTO_ESITO_SIGILLO =
      "Nessun documento con id sigillo '%s' presente per l'inserimento esito apposizione sigillo";

  /**
   * FILESYSTEM VERSO INDEX
   */

  public static final String FS2IDX_IMPOSSIBILE_REPERIRE_RECORD_CORRISPONDENTE_SU_INDEX =
      "Impossibile reperire il record corrispondente al contenuto su index per il documento %s";

  public static final String FS2IDX_IMPOSSIBILE_OTTENERE_CONTENUTO = "Impossibile ottenere il contenuto";

  /**
   * STARDAS
   */

  public static final String STARDAS_NESSUN_DOCUMENTO_IN_SMISTAMENTO_DA_AGGIORNARE =
      "Nessun documento in smistamento da aggiornare";

  public static final String STARDAS_NESSUN_SMISTAMENTO_PRESENTE = "Nessun smistamento presente";

  /**
   * STREAMDATA VERSO INDEX
   */

  public static final String SD2IDX_IMPOSSIBILE_RECUPERARE_CONTENUTO_SU_INDEX =
      "Impossibile reperire il record corrispondente al contenuto su index per il documento %d";

  /**
   * FRUITORI
   */

  public static final String F_FRUITORE_NON_AUTENTICATO_NON_RICONOSCIUTO =
      "Fruitore non autenticato o non riconosciuto";

  public static final String F_FRUITORE_IPA_ENTE_NON_CORRISPONDE_AGLI_ENTI_ASSOCIATI =
      "Il codice IPA ente non coincide con nessuno degli enti associati al fruitore";

  public static final String F_FRUITORE_NESSUN_CONTENUTO_FORNITO =
      "Nessun contenuto fornito.Valorizzare alternativamente'uploadUUID'o'contenuto'";

  public static final String F_FRUITORE_CONTENUTO_FORNITO_NON_UNIVOCO =
      "Contenuto fornito non univoco. Valorizzare solo uno fra 'uploadUUID' o 'contenuto'";

  public static final String F_FRUITORE_CONTENUTO_BINARIO_NON_PRESENTE =
      "Contenuto binario non presente. Valorizzare il campo 'contenuto'.'contenutoFisico'";

  public static final String F_FRUITORE_CONTENUTO_BINARIO_ECCESSIVAMENTE_GRANDE =
      "Contenuto binario eccessivamente grande. Utilizzare la modalita' "
          + "di upload preventivo e fornire l'UploadUUID invece del contenuto fisico per i file di dimensione superiore a %d bytes";

  public static final String F_FRUITORE_ERRORE_DECODIFICA_CONTENUTO_BASE64 =
      "Errore nella decodifica del contenuto in Base64";

  /**
   * REPORT
   */

  public static final String R_FORMATO_NON_VALIDO = "Formato %s non valido";


}
