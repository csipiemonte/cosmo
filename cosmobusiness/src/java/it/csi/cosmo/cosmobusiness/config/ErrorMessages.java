/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.config;

/**
 * Interfaccia contente tutti i messaggi di errore nell'applicativo.
 */
public interface ErrorMessages {

  /**
   * LOGGER
   */
  String CODICE_RUOLO_NON_DEFINITO = "Codice ruolo non definito";

  String RUOLO_DESIDERATO_NON_CONSENTITO = "Ruolo desiderato non consentito";

  String ERRORE_NEL_REPERIMENTO_DEI_RUOLI_DISPONIBILI = "Errore nel reperimento dei ruoli disponibili";

  String MISSING_PRINCIPAL = "Missing principal";

  String ERRORE_NEL_REPERIMENTO_DEGLI_UC = "Errore nel reperimento degli UC";

  String IMPOSSIBILE_TROVARE_VOCE_TITOLARIO_CON_CLASSIFICAZIONE = "Impossibile trovare la voce di titolario con classificazione estesa: %s";

  String ERRORE_GENERICO = "Errore generico";

  String PARAMETRO_CONFIGURATO_CON_TROPPE_VARIANTI = "Parametro configurato su file di properties con troppe varianti: %s";

  String ERRORE_VERIFICA_FILE_PROPERTIES = "Errore nella verifica del file di properties per l'ambiente %s";

  String AMBIENTE_CORRENTE_NON_CORRETTO = "Ambiente corrente non configurato o non corretto";

  String ERRORI_NELLA_CONFIGURAZIONE_DEI_PROFILI = "Sono presenti degli errori bloccanti nella configurazione dei profili";

  String RUOLO_NON_RICONOSCIUTO = "Unrecognized role ";

  String PARAMETRO_NON_VALORIZZATO = "Parametro '%s' non valorizzato";

  String PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE = "Parametro '%s' non valorizzato correttamente";


  /**
   * ENTE
   */

  String E_ENTE_NON_TROVATO = "Ente con id '%s' non trovato";

  String E_ENTE_NON_ELIMINATO = "Errore nell'eliminazione dell'ente con id '%s'";

  String E_ENTE_NON_VALORIZZATO = "Ente da creare non valorizzato";

  String E_ENTE_GIA_ESISTENTE = "Ente con il nome '%s' gia' esistente";

  String E_NOME_ENTE_NON_VALORIZZATO = "Nome dell'ente da creare non valorizzato";

  String E_DATA_INIZIO_VALIDITA_ENTE_NON_VALORIZZATA = "Data dell'inizio validita' dell'ente da creare non valorizzato";

  String E_ENTE_CORRENTE_NON_TROVATO = "Ente corrente non trovato";
  /**
   * PREFERENZE ENTE
   */

  String PE_PREFERENZE_ENTE_NON_VALORIZZATO = "Preferenze ente da creare non valorizzato";

  String PE_VERSIONE_PREFERENZE_ENTE_NON_VALORIZZATO = "Versione delle preferenze dell'ente da creare non valorizzata";

  String PE_VALORE_PREFERENZE_ENTE_NON_VALORIZZATO = "Valore delle preferenze dell'ente da creare non valorizzata";

  /**
   * UTENTE
   */
  String U_UTENTE_NON_VALORIZZATO = "Utente da creare non valorizzato";

  String U_NOME_UTENTE_NON_VALORIZZATO = "Nome dell'utente da creare non valorizzato";

  String U_COGNOME_UTENTE_NON_VALORIZZATO = "Cognome dell'utente da creare non valorizzato";

  String U_CODICE_FISCALE_UTENTE_NON_VALORIZZATO = "Codice fiscale dell'utente da creare non valorizzato";

  String U_UTENTE_NON_TROVATO = "Utente con id '%s' non trovato";

  String U_UTENTE_CON_CODICE_FISCALE_NON_TROVATO = "Utente con codice fiscale '%s' non trovato";

  String U_UTENTE_CORRENTE_NON_TROVATO = "Utente corrente non trovato";

  String U_DATA_INIZIO_VALIDITA_UTENTE_NON_VALORIZZATA = "Data dell'inizio validita' dell'ente da creare non valorizzato";

  String U_UTENTE_GIA_ESISTENTE = "Utente con il codice fiscale '%s' gia' esistente";

  /**
   * PREFERENZE UTENTE
   */

  String PU_PREFERENZE_UTENTE_NON_VALORIZZATO = "Preferenze utente da creare non valorizzato";

  String PU_VERSIONE_PREFERENZE_UTENTE_NON_VALORIZZATO = "Versione delle preferenze dell'utente da creare non valorizzata";

  String PU_VALORE_PREFERENZE_UTENTE_NON_VALORIZZATO = "Valore delle preferenze dell'utente da creare non valorizzata";

  /**
   * PROFILI
   */

  String P_PROFILO_NON_TROVATO = "Profilo con id '%s' non trovato";

  String P_PROFILO_NON_VALORIZZATO = "Profilo da creare non valorizzato";

  String P_CODICE_PROFILO_NON_VALORIZZATO = "Codice del profilo da creare non valorizzato";

  String P_DATA_INIZIO_VALIDITA_PROFILO_NON_VALORIZZATA = "Data dell'inizio validita' del profilo da creare non valorizzato";

  String P_PROFILO_GIA_ESISTENTE = "Profilo con codice '%s' gia' esistente";
  /**
   * GRUPPI
   */

  String G_GRUPPO_NON_TROVATO = "Gruppo con id '%s' non trovato";

  String G_GRUPPO_NON_VALORIZZATO = "Gruppo non valorizzato";

  String G_GRUPPO_NESSUN_ENTE_ASSOCIATO = "Nessun ente associato al gruppo da creare";

  String G_GRUPPO_GIA_ESISTENTE = "Gruppo con flId '%s' gia' esistente";

  /**
   * USE-CASE
   */

  String UC_USECASE_NON_VALORIZZATO = "Use-case non valorizzato";

  String UC_CODICE_USECASE_NON_VALORIZZATO = "Codice dello use-case non valorizzato";

  String UC_DESCRIZIONE_USECASE_NON_VALORIZZATO = "Descrizione dello use-case non valorizzato";

  String UC_CODICE_CATEGORIA_USECASE_NON_VALORIZZATO = "Codice categoria dello use-case non valorizzato";

  String UC_DATA_INIZIO_VALIDITA_USECASE_NON_VALORIZZATA = "Data dell'inizio validita' dello usecase da creare non valorizzato";

  String UC_CODICE_CATEGORIA_USECASE_NON_TROVATO = "Codice categoria dello use-case non trovato";

  String UC_USECASE_NON_TROVATO = "Usecase con codice '%s' non trovato";

  /**
   * FILTRI NELLA RICERCA
   */
  String SIZE_PAGINA_LIMIT_VALORIZZATI = "Valorizzazione errata: e' valorizzata sia la size della pagina che il numero massimo di elementi";
  String PAGE_PAGINA_OFFSET_VALORIZZATI = "Valorizzazione errata: e' valorizzata sia il numero della pagina che l'offset degli elementi";

  String CASE_NON_TROVATO = "Case con id pratica %s non trovato";

  String FRUITORE_NON_VALORIZZATO = "Fruitore non valorizzato";
  String FRUITORE_CON_API_MANAGER_ID_NON_TROVATO = "Fruitore con api manager id '%s' non valorizzato";
  String FRUITORE_COLLEGATO_A_PRATICA_NON_VALORIZZATO =
      "Fruitore collegato a pratica con id '%s' non valorizzato";

  String MESSAGGIO_NOTIFICA_CREAZIONE = "E' stato creato il task '%s' relativo alla pratica '%s'";
  String MESSAGGIO_NOTIFICA_RIASSEGNAZIONE = "Ti e' stato assegnato il task '%s' relativo alla pratica '%s'";
  String MESSAGGIO_NOTIFICA_ANNULLAMENTO =
      "E' stato annullato il task '%s' relativo alla pratica '%s'";

  /**
   * FORM LOGICI
   */
  String ISTANZA_FORM_LOGICO_NON_TROVATA = "Istanza con id %d non trovata";

  String CODICE_FUNZIONALITA_FORM_LOGICO_NON_TROVATO = "Funzionalita' con codice %s non trovato";

  String ISTANZA_FORM_LOGICO_NON_CORRETTA = "Istanza del form logico non corretta";

  String ISTANZA_FORM_LOGICO_NON_TROVATO = "Istanza del form logico con id '%s' non trovato";

  String FRUITORE_NON_TROVATO = "Fruitore '%s' non trovato";

  String ENDPOINT_CUSTOM_PER_FRUITORE_E_CON_CODICE_NON_TROVATO =
      "Endpoint custom per il fruitore con codice '%s' con codice descrittivo '%s' non trovato";

  String URL_FRUITORE_NON_DEFINITA = "Url del fruitore con codice '%s' non definita";

  String VALORE_VARIABILE_NON_TROVATO = "Valore da sostituire a '%s' non trovato";

  String V_VARIABILE_NON_PRESENTE =
      "Variabile con name '%s' e idPratica '%s' non presente. Si salva";
  String V_VARIABILE_PRESENTE =
      "Variabile con name '%s' e idPratica '%s' non presente. Si aggiorna";

  /**
   * ATTIVITA
   */
  String APPROVAZIONE_ATTIVITA_UTENTE_DOCUMENTO =
      "Approvazione già presente per l'attivita' %d e utente %d";

  String PRATICA_NON_TROVATA = "Codice pratica %s non trovato";

}
