/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.config;

/**
 * Interfaccia contente tutti i messaggi di errore nell'applicativo.
 */
public abstract class ErrorMessages {

  private ErrorMessages() {
    // PRIVATE
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

  public static final String NON_VALIDO = "non valido";
  public static final String NON_TROVATO = "non trovato";
  public static final String PRATICA_NON_TROVATA = "Codice pratica %s " + NON_TROVATO;
  public static final String ATTIVITA_NON_TROVATA = "Codice attivita %s " + NON_TROVATO;
  public static final String TIPO_PRATICA_NON_TROVATA = "Tipo pratica %s " + NON_TROVATO;
  public static final String STATO_CARICAMENTO_PRATICA_NON_TROVAT0 =
      "Stato caricamento pratica %s " + NON_TROVATO;
  public static final String TIPO_PRATICA_NON_CREABILE_DA_SERVIZIO = "Tipo pratica %s non creabile da servizio";
  public static final String TIPO_PRATICA_NON_CREABILE_DA_INTERFACCIA = "Tipo pratica %s non creabile da interfaccia";
  public static final String PRATICA_NON_VALIDA = "Pratica " + NON_VALIDO;
  public static final String PRATICA_UTENTE_NON_TROVATA = "Legame pratica-utente " + NON_TROVATO;
  public static final String OPERATORE_NON_SUPPORTATO = "Operatore %s non supportato";
  public static final String CODICE_IPA_ENTE_NON_TROVATA = "Codice ipa ente %s " + NON_TROVATO;

  public static final String FORMATO_FILTRO_NON_CORRETTO = "Formato filtro non corretto";

  public static final String OFFSET_NON_VALIDO = "Offset %s " + NON_VALIDO;

  public static final String LIMIT_NON_VALIDO = "Limit %s " + NON_VALIDO;

  public static final String UTENTE_NON_VALIDO = "Utente " + NON_VALIDO;
  public static final String UTENTE_NON_TROVATO = "Utente %s " + NON_TROVATO;
  public static final String ID_FRUITORE_OBBLIGATORIO = "Id fruitore obbligatorio o non valido";

  public static final String ID_PRATICA_NON_DEVE_ESSERE_VALORIZZATO =
      "L'id della pratica non deve essere valorizzato";

  public static final String ID_PRATICA_DEVE_ESSERE_VALORIZZATO =
      "L'id della pratica deve essere valorizzato";

  public static final String ID_PRATICA_DEVE_ESSERE_NUMERICO =
      "L'id della pratica deve essere un numero";

  public static final String PRATICA_CON_ID_NON_TROVATA = "Pratica con id '%d' non trovata";

  public static final String PRATICA_CON_ID_EXT_NON_TROVATA = "Pratica con idExt %s non trovata";

  public static final String TIPO_PRATICA_DEVE_ESSERE_VALORIZZATO =
      "Il tipo della pratica non deve essere valorizzato";

  public static final String TIPO_RELAZIONE_PRATICA_CON_ID_NON_TROVATA =
      "La tipologia di relazione tra pratiche '%s' non e' stata trovata";

  /**
   * FILTRI NELLA RICERCA
   */
  public static final String SIZE_PAGINA_LIMIT_VALORIZZATI =
      "Valorizzazione errata: e' valorizzata sia la size della pagina che il numero massimo di elementi";
  public static final String PAGE_PAGINA_OFFSET_VALORIZZATI =
      "Valorizzazione errata: e' valorizzata sia il numero della pagina che l'offset degli elementi";

  /**
   * FILTRI RICERCA PRATICHE
   */
  public static final String DIMENSIONE_MASSIMA_CAMPO_OGGETTO_SUPERATA = "Superata dimensione massima campo oggetto";

  public static final String VALORE_OGGETTO_NON_ALFANUMETICO = "Il valore del campo oggetto deve essere di tipo alfanumerico!";

  public static final String VALORE_OGGETTO_CON_CARATTERI_NON_VALIDI =
      "Il valore del campo oggetto presenta caratteri non validi!";

  public static final String DATA_ULTIMA_MODIFICA_DA_NON_FORMATTATA = "Data ultima modifica da non formattata correttamente";

  public static final String DATA_ULTIMA_MODIFICA_A_NON_FORMATTATA = "Data ultima modifica a non formattata correttamente";

  public static final String DATA_APERTURA_PRATICA_DA_NON_FORMATTATA = "Data apertura pratica da non formattata correttamente";

  public static final String DATA_APERTURA_PRATICA_A_NON_FORMATTATA = "Data apertura pratica a non formattata correttamente";

  public static final String DATA_ULTIMO_CAMBIO_DI_STATO_DA_NON_FORMATTATA = "Data ultimo cambio di stato da non formattata correttamente";

  public static final String DATA_ULTIMO_CAMBIO_DI_STASTO_A_NON_FORMATTATA = "Data ultimo cambio di stato a non formattata correttamente";

  public static final String DATA_APERTURA_PRATICA_DA_MINORE_DATA_APERTURA_PRATICA_A = "Data apertura pratica da deve essere minore di data apertura pratica a";

  public static final String DATA_ULTIMA_MODIFICA_DA_MINORE_DATA_ULTIMA_MODIFICA_A = "Data ultima modifica da deve essere minore di data ultima modifica a";

  public static final String DATA_ULTIMO_CAMBIO_STATO_DA_MINORE_DATA_ULTIMO_CAMBIO_STATO_A = "Data ultimcambio di stato da deve essere minore di data ultimo cambio di stato a";
  public static final String NOME_VARIABILE_PROCESSO_OBBLIGATORIA =
      "Nome variabile processo obbligatoria";
  public static final String FILTRO_VARIABILE_PROCESSO_OBBLIGATORIA =
      "Filtro (singolo o range da o range a) variabile processo obbligatorio";


  public static final String VARIABILE_PROCESSO_DATA_RANGE_DA_MINORE_DATA_RANGE_A =
      "Data range da deve essere minore di data range a per variabile processo";

  public static final String PROCESS_INSTANCE_ID_NON_VALIDO =
      "ID dell'istanza del processo non valido";

  public static final String TIPO_PRATICA_NON_VALORIZZATA =
      "Tipologia della pratica non valorizzata";

  public static final String PROCESSO_NON_TROVATO = "Nessun processo con ID '%s' trovato";

  public static final String CONFIGURAZIONE_METADATI_PER_TIPO_PRATICA_NON_TROVATA =
      "Configurazione dei metadati valida per il tipo di pratica '%s' non trovata";

  public static final String PROBLEMI_NELLA_LETTURA_DEI_METADATI =
      "Problemi nella lettura dei metadati";

  /**
   * ACCESSO TRAMITE LINK DIRETTO
   */
  public static final String ACCESSO_PRATICA_NON_CONSENTITO = "Accesso alla pratica con id %s non consentito";

  public static final String ACCESSO_ATTIVITA_NON_CONSENTITO = "Accesso all'attivita con id %s non consentito";

  public static final String PARAMETRO_NON_VALORIZZATO = "Parametro '%s' non valorizzato";

  /**
   * CUSTOM FORM
   */
  public static final String CODICE_CUSTOM_FORM_NON_VALORIZZATO_CORRETTAMENTE =
      "Codice del custom form non valorizzato correttamente";

  public static final String CUSTOM_FORM_CON_CODICE_NON_TROVATO =
      "Custom form con codice  '%s' non trovato";

  public static final String CODICE_CUSTOM_FORM_GIA_ASSEGNATO_TROVATO =
      "Codice '%s' gia' assegnato ad un altro custom form";

  public static final String CODICE_CUSTOM_FORM_CODICE_TIPO_PRATICA_NON_VALORIZZATO_CORRETTAMENTE =
      "Codice del tipo pratica associato al custom form non valorizzato correttamente";

  public static final String CUSTOM_FORM_CON_CODICE_TIPO_PRATICA_NON_TROVATO =
      "Custom form associato al tipo pratica con codice  '%s' non trovato";

  public static final String TROPPI_CUSTOM_FORM_CON_CODICE_TIPO_PRATICA_ASSOCIATI =
      "Piu' custom form associati al tipo pratica con codice  '%s' non trovato";

  public static final String VARIABILEFILTRO_NON_VALIDA = "Variabile filtro " + NON_VALIDO;
  public static final String VARIABILEFILTRO_NON_TROVATA = "Id variabile filtro %s " + NON_TROVATO;
  public static final String TIPO_FILTRO_NON_TROVATO = "Tipo filtro %s " + NON_TROVATO;
  public static final String TIPO_FORMATO_NON_TROVATO = "Tipo formato %s " + NON_TROVATO;
  public static final String LABEL_VARIABILEFILTRO_DUPLICATA =
      "Label %s gia' presente per la tipologia di pratica/ente";
  public static final String NOME_VARIABILEFILTRO_DUPLICATA =
      "Nome %s gia' presente per la tipologia di pratica/ente";

  public static final String ENTE_CERITFICATORE_NON_TROVATO = "Ente certificatore non trovato";
  public static final String TIPO_CREDENZIALI_FIRMA_NON_TROVATO =
      "Tipo credenziali firma non trovato";
  public static final String PROFILO_FEQ_NON_TROVATO = "Profilo Feq non trovato";
  public static final String TIPO_OTP_NON_TROVATO = "Tipo otp non trovato";
  public static final String SCELTA_MARCA_TEMPORALE_NON_TROVATA =
      "Scelta marca temporale non trovata";

}
