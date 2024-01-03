/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.config;

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

  public static final String FRUITORE_NON_PRESENTE = "Fruitore non presente.";
  public static final String FRUITORE_NON_VALIDO = "Fruitore non valido.";
  public static final String NOTIFICA_NON_TROVATA = "Notifica %s non trovata.";
  public static final String NOTIFICA_NON_VALIDA = "Notifica non valida.";
  public static final String DESTINATARIO_OBBLIGATORIO = "Destinatario obbligatorio.";
  public static final String OFFSET_NON_VALIDO = "Offset %s non valido.";
  public static final String LIMIT_NON_VALIDO = "Limit %s non valido.";
  public static final String UTENTE_NON_VALIDO = "Utente non valido.";
  public static final String UTENTE_NON_PRESENTE = "Utente non presente.";
  public static final String UTENTE_NON_AUTENTICATO = "Utente non autenticato";

  public static final String OPERATORE_NON_SUPPORTATO = "Operatore %s non supportato.";
  public static final String FORMATO_FILTRO_NON_CORRETTO = "Formato filtro non corretto";

  public static final String HELPER_ID_NON_VALORIZZATO = "Helper ID non valorizzato";
  public static final String HELPER_ID_INESISTENTE = "Helper ID %s inesistente";
  public static final String CODICE_PAGINA_HELPER_GIA_ASSEGNATO_TROVATO = "Codice Pagina '%s' gia' assegnato ad un altro helper";
  public static final String HELPER_PAGINA_NON_TROVATO = "Helper pagina con codice '%s' non trovato";
  public static final String HELPER_TAB_NON_TROVATO = "Helper tab con codice '%s' non trovato";
  public static final String HELPER_MODALE_NON_TROVATO = "Helper modale con codice '%s' non trovato";
  public static final String HELPER_CUSTOM_FORM_NON_TROVATO ="Helper custom form con codice '%s' non trovato";

  public static final String MESSAGGI_NOTIFICHE_ENTE_NON_TROVATO = "Ente non trovato";
  public static final String MESSAGGI_NOTIFICHE_TIPO_MESSAGGIO_NON_TROVATO =
      "Tipo messaggio non trovato";
  public static final String MESSAGGI_NOTIFICHE_TIPO_PRATICA_NON_TROVATO =
      "Tipo pratica non trovato";
  public static final String MESSAGGI_NOTIFICHE_CONFIGURAZIONE_NON_TROVATA =
      "Configurazione messaggio notifica non trovata";
  public static final String MESSAGGI_NOTIFICHE_CONFIGURAZIONE_PRESENTE =
      "Configurazione messaggio notifica già presente";


}
