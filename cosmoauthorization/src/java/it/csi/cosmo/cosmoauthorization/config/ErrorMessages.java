/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.config;

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

  public static final String PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE =
      "Parametro non definito tra le configurazioni";
  /**
   * ENTE
   */

  public static final String E_ENTE_CODICE_NON_TROVATO = "Ente con codice fiscale o ipa '%s' non trovato";

  public static final String E_ENTE_NON_TROVATO = "Ente con id '%s' non trovato";

  public static final String E_ENTE_NON_ELIMINATO =
      "Errore nell'eliminazione dell'ente con id '%s'";

  public static final String E_ENTE_NON_VALORIZZATO = "Ente da creare non valorizzato";

  public static final String E_ENTE_NOME_GIA_ESISTENTE = "Ente con il nome '%s' gia' esistente";

  public static final String E_ENTE_CF_GIA_ESISTENTE =
      "Ente con il codice fiscale '%s' gia' esistente";

  public static final String E_ENTE_IPA_GIA_ESISTENTE =
      "Ente con il codice IPA '%s' gia' esistente";

  public static final String E_NOME_ENTE_NON_VALORIZZATO =
      "Nome dell'ente da creare non valorizzato";

  public static final String E_DATA_INIZIO_VALIDITA_ENTE_NON_VALORIZZATA =
      "Data dell'inizio validita' dell'ente da creare non valorizzato";

  public static final String E_ENTE_CORRENTE_NON_TROVATO = "Ente corrente non trovato";
  /**
   * PREFERENZE ENTE
   */

  public static final String PE_PREFERENZE_ENTE_NON_TROVATO =
      "Preferenze per l'ente con id '%s' non trovate";

  public static final String PE_PREFERENZE_VERSIONE_ENTE_NON_TROVATE =
      "Preferenze con versione '%s' non trovate";

  public static final String PE_PREFERENZE_ENTE_NON_VALORIZZATO =
      "Preferenze ente da creare non valorizzato";

  public static final String PE_VERSIONE_PREFERENZE_ENTE_NON_VALORIZZATO =
      "Versione delle preferenze dell'ente da creare non valorizzata";

  public static final String PE_VALORE_PREFERENZE_ENTE_NON_VALORIZZATO =
      "Valore delle preferenze dell'ente da creare non valorizzata";

  public static final String PE_PREFERENZE_GRANDEZZA_LOGO_SUPERATA =
      "Errore durante il caricamento del logo: grandezza massima di '%s' kb superata";

  public static final String PE_PREFERENZE_GRANDEZZA_ICONA_FEA_SUPERATA =
      "Errore durante il caricamento dell'icona Fea: grandezza massima di '%s' kb superata";

  public static final String PE_PREFERENZE_GRANDEZZA_PIXELS_LOGO_SUPERATA =
      "Errore durante il caricamento del logo: grandezza di '%s' pixels superata";

  public static final String PE_PREFERENZE_GRANDEZZA_PIXELS_ICONA_FEA_SUPERATA =
      "Errore durante il caricamento dell'icona Fea: grandezza di '%s' pixels superata";

  public static final String PE_PREFERENZE_VERSIONE_ENTE_GIA_ESISTENTI =
      "Preferenze con versione '%s' gia' esistenti";

  public static final String PE_PREFERENZE_CONVERSIONE_LOGO_NON_RIUSCITA =
      "Conversione logo non riuscita";

  public static final String PE_PREFERENZE_CONVERSIONE_ICONA_FEA_NON_RIUSCITA =
      "Conversione icona Fea non riuscita";


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

  public static final String U_UTENTE_CON_CODICE_FISCALE_NON_TROVATO =
      "Utente con codice fiscale '%s' non trovato";

  public static final String U_UTENTE_CORRENTE_NON_TROVATO = "Utente corrente non trovato";

  public static final String U_DATA_INIZIO_VALIDITA_UTENTE_NON_VALORIZZATA =
      "Data dell'inizio validita' dell'ente da creare non valorizzato";

  public static final String U_UTENTE_GIA_ESISTENTE =
      "Utente con il codice fiscale '%s' gia' esistente";

  /**
   * PREFERENZE UTENTE
   */

  public static final String PU_PREFERENZE_UTENTE_NON_VALORIZZATO =
      "Preferenze utente da creare non valorizzato";

  public static final String PU_VERSIONE_PREFERENZE_UTENTE_NON_VALORIZZATO =
      "Versione delle preferenze dell'utente da creare non valorizzata";

  public static final String PU_VALORE_PREFERENZE_UTENTE_NON_VALORIZZATO =
      "Valore delle preferenze dell'utente da creare non valorizzata";

  public static final String PU_PREFERENZE_VERSIONE_UTENTE_NON_TROVATE =
      "Preferenze con versione '%s' non trovate";

  public static final String PU_PREFERENZE_VERSIONE_UTENTE_GIA_ESISTENTI =
      "Preferenze con versione '%s' gia' esistenti";
  /**
   * PROFILI
   */

  public static final String P_PROFILO_NON_TROVATO = "Profilo con id '%s' non trovato";

  public static final String P_PROFILO_NON_VALORIZZATO = "Profilo da creare non valorizzato";

  public static final String P_CODICE_PROFILO_NON_VALORIZZATO =
      "Codice del profilo da creare non valorizzato";

  public static final String P_DATA_INIZIO_VALIDITA_PROFILO_NON_VALORIZZATA =
      "Data dell'inizio validita' del profilo da creare non valorizzato";

  public static final String P_PROFILO_GIA_ESISTENTE = "Profilo con codice '%s' gia' esistente";
  /**
   * GRUPPI
   */

  public static final String G_GRUPPO_NON_TROVATO = "Gruppo con id '%d' non trovato";
  public static final String G_GRUPPO_CODICE_NON_TROVATO = "Gruppo con codice '%s' non trovato";

  public static final String G_GRUPPO_NON_VALORIZZATO = "Gruppo non valorizzato";

  public static final String G_GRUPPO_NESSUN_ENTE_ASSOCIATO =
      "Nessun ente associato al gruppo da creare";

  public static final String G_GRUPPO_GIA_ESISTENTE = "Gruppo con flId '%s' gia' esistente";

  /**
   * USE-CASE
   */


  public static final String UC_USECASE_NON_VALORIZZATO = "Use-case non valorizzato";

  public static final String UC_CODICE_USECASE_NON_VALORIZZATO =
      "Codice dello use-case non valorizzato";

  public static final String UC_DESCRIZIONE_USECASE_NON_VALORIZZATO =
      "Descrizione dello use-case non valorizzato";

  public static final String UC_CODICE_CATEGORIA_USECASE_NON_VALORIZZATO =
      "Codice categoria dello use-case non valorizzato";

  public static final String UC_DATA_INIZIO_VALIDITA_USECASE_NON_VALORIZZATA =
      "Data dell'inizio validita' dello usecase da creare non valorizzato";

  public static final String UC_CODICE_CATEGORIA_USECASE_NON_TROVATO =
      "Codice categoria dello use-case non trovato";

  public static final String UC_USECASE_NON_TROVATO = "Usecase con codice '%s' non trovato";


  /**
   * FILTRI NELLA RICERCA
   */
  public static final String SIZE_PAGINA_LIMIT_VALORIZZATI =
      "Valorizzazione errata: e' valorizzata sia la size della pagina che il numero massimo di elementi";
  public static final String PAGE_PAGINA_OFFSET_VALORIZZATI =
      "Valorizzazione errata: e' valorizzata sia il numero della pagina che l'offset degli elementi";

  /**
   * CONFIGURAZIONE
   */
  public static final String C_CONFIGURAZIONE_NON_TROVATA = "Configurazione non trovata";

  public static final String C_CONFIGURAZIONE_DECODED_NON_TROVATA = "Configurazione '%s' non trovata";

  /**
   * FRUITORE
   */
  public static final String F_FRUITORE_NON_TROVATO = "Fruitore con id '%s' non trovato";

  public static final String F_FRUITORE_NON_TROVATO_API_MANAGER_ID =
      "Fruitore con api manager id '%s' non trovato";

  public static final String F_FRUITORE_NON_VALORIZZATO = "Fruitore non valorizzato";

  public static final String F_FRUITORE_GIA_ESISTENTE =
      "Fruitore con api manager id '%s' gia' esistente";

  public static final String F_AUTORIZZAZIONE_NON_TROVATA = "Autorizzazione '%s' non trovata";

  /**
   * CERTIFICATO FIRMA
   */
  public static final String CF_CERTIFICATO_FIRMA_NON_TROVATO =
      "Certificato di firma con id %s non trovato";
  public static final String CF_SCADENZA_CERTIFICATO_NON_VALIDA =
      "La scadenza del certificato non e' valida";
  public static final String CF_ENTE_CERTIFICATORE_NON_VALIDO =
      "L'ente certificatore con codice %s non e' valido";
  public static final String CF_SCELTA_TEMPORALE_NON_VALIDA =
      "La scelta temporale con codice %s non e' valida";
  public static final String CF_PROFILO_FEQ_NON_VALIDO =
      "Il profilo FEQ con codice %s non e' valido";
  public static final String CF_TIPO_CREDENZIALI_FIRMA_NON_VALIDO =
      "Il tipo di credenziali firma con codice %s non e' valido";
  public static final String CF_TIPO_OTP_NON_VALIDO = "Il tipo di otp con codice %s non e' valido";
  public static final String CF_UTENTE_CERTIFICATO_FIRMA_NON_CORRISPONDENTE =
      "Utente del certificato di firma non corrispondente";

  /**
   * APPLICAZIONE ESTERNA
   */
  public static final String AE_APPLICAZIONE_ESTERNA_NON_TROVATA =
      "Applicazione esterna con id %s non trovata";

  public static final String AE_APPLICAZIONE_NON_CORRETTA = "Applicazione non corretta";

  public static final String AE_FUNZIONALITA_NON_CORRETTA =
      "Funzionalita' non corretta";

  public static final String AE_FUNZIONALITA_NON_TROVATA =
      "Funzionalita' con id %s non trovata";

  public static final String AE_ENTE_ASSOCIATO_NON_VALIDO = "Ente associato non valido";

  public static final String AE_NESSUNA_APPLICAZIONE_DA_ASSOCIARE =
      "Nessuna applicazione da associare";

  public static final String AE_NESSUNA_APPLICAZIONE_ASSOCIATA_ENTE =
      "Nessuna applicazione associata all'ente da collegare alla funzionalita'";

  public static final String AE_NESSUNA_FUNZIONALITA_DA_ASSOCIARE =
      "Nessuna funzionalita' da associare";

  public static final String CONFIGURAZIONE_ENTE_ATTIVA =
      "Configurazione ente con chiave %s per l'ente corrente gia' presente";

  /**
   * TAGS
   */
  public static final String T_TAG_NON_TROVATO = "Tag con id '%s' non trovato";

  public static final String T_TAG_CODE_GIA_ESISTENTE = "Tag con il codice '%s' gia' esistente";

  public static final String D_TIPO_TAG_NON_TROVATO = "Tipo Tag con codice '%s' non trovato";

}
