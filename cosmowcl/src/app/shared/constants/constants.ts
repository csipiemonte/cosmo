/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
export class Constants {
  public static STORAGE_SCHEMA_VERSION = '0.0.1';

  public static ROUTE_PAGINA_HOME = '/home';
  public static ROUTE_PAGINA_ERRORE = '/error';
  public static ROUTE_PAGINA_LOGIN = '/login';
  public static ROUTE_PAGINA_HOME_ADMIN = '/amministrazione';
  public static ROUTE_PAGINA_ERROR_UNATHORIZED = '/error-unauthorized';
  public static ROUTE_PAGINA_UTENTE_NON_VALIDO = '/error-expired';
  public static ROUTE_PAGINA_ERRORE_NO_PROFILAZIONE = '/nessuna-profilazione';

  public static X_COSMO_IDENTITA_ATTIVA = 'X-Cosmo-Identita-Attiva';

  public static X_COSMO_SESSION_MANAGEMENT = 'X-Cosmo-Session-Management';

  public static EMAIL_ASSISTENZA = 'assistenza.cosmo@csi.it';

  public static STORICO_ATTIVITA = {USER_TASK: 'userTask'};


  public static PARAMETRI = {
    PREFERENZE_ENTE: {
      VERSIONE: 'ente.pref.version',
      LOGO_MAX_SIZE: 'ente.pref.maxsize',
      LOGO_MAX_NUM_OF_PIXELS: 'ente.pref.maxpix',
      FEA_MAX_SIZE: 'ente.pref.fea.maxsize',
      FEA_MAX_NUM_OF_PIXELS: 'ente.pref.fea.maxpix'
    },
    MAX_PAGE_SIZE: 'max.page.size',
    HOMEPAGE: {
      PRATICHE : {
        MAX_SIZE_PREFERITE: 'home.max.preferite',
        MAX_SIZE_DA_LAVORARE: 'home.max.dalavorare',
        MAX_SIZE_IN_EVIDENZA: 'home.max.inevidenza',
        MAX_SIZE_DEFAULT: '5'
      }
    },
    AMMINISTRAZIONE : {
      FILE_PROCESSO_MAX_SIZE: 'file.processo.max.size'
    },
    TEMPLATE_REPORT: {
      TEMPLATE_MAX_SIZE: 'template.max.size'
    },
    HELPER: {
      HTML_MAX_SIZE: 'helper.html.max.size'
    },
    EXPORT_ROW_SIZE: 'export.row.max.size',
    DOCUMENTI_PDF_ANTEPRIMA_MAX_SIZE: 'documenti.pdf.anteprima.maxsize',
    EXCEL_PRATICHE_MAX_SIZE: 'upload.pratiche.excel.maxsize',
    ZIP_MAX_SIZE: 'uploadsession.documentizip.max.size'
  };

  public static OK_MODAL = 'ok';

  public static APPLICATION_EVENTS = {
    DOCUMENTI_CARICATI: 'ecm.batch.documentiElaborati',
    NOTIFICA: 'notifica',
    NOTIFICA_ULTIME_LAVORATE: 'notifica.ultime.lavorate',
    DOCUMENTI_SMISTATI: 'ecm.batch.documentiSmistati',
    DOCUMENTI_FIRMATI: 'documenti.firmati',
    DOCUMENTI_INIZIO_FIRMA: 'documenti.inizio_firma',
    PRATICA_INIZIO_FIRMA: 'pratica.inizio_firma',
    PRATICA_FINE_FIRMA: 'pratica.fine_firma',
    PRATICA_ERRORI_FIRMA: 'pratica.errori_firma',
    FIRMA_ERRORI_PRELIMINARI: 'firma.errori_preliminari',
    DOCUMENTI_FINE_FIRMA: 'documenti.fine_firma',
    FIRMA_ERRORE_DOCUMENTO: 'firma.errore_documento',
    NOTIFICA_BATCH_CARICAMENTOPRATICHE: 'notifica.batch.caricamentopratiche',
    NOTIFICA_BATCH_CARICAMENTOPRATICHE_PRATICA: 'notifica.batch.caricamentopratiche.pratica',
    DOCUMENTI_SIGILLATI: 'ecm.batch.documentiSigillati'
  };

  public static DATA_REGEX_VALIDATION = /^[0-9]{2}\/[0-9]{2}\/[0-9]{4}$/u;
  public static OGGETTO_REGEX_VALIDATION = /^[^\p{C}]*$/u;
  public static OGGETTO_REGEX_MASKING = /[\p{C}]/u;

  public static PATTERNS = {
    DATA_REGEX_VALIDATION: /^[0-9]{2}\/[0-9]{2}\/[0-9]{4}$/u,
    OGGETTO_REGEX_VALIDATION: /^[^\p{C}]*$/u,
    OGGETTO_REGEX_MASKING: /[\p{C}]/u,
    CODICE_FISCALE_UTENTE: '^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$',
    CODICE_FISCALE_ENTE: '^[0-9]{1,30}$',
    EMAIL: /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/,
    TELEFONO: /^[0-9]{1}[0-9\s\-]{0,30}[0-9]$/,
    URL_WITH_PROTOCOL: /^https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)$/,
    URL_ABSOLUTE_OR_RELATIVE: /^\/[-a-zA-Z0-9()@:%_\+.~#?&//=\{\}]*|https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=\{\}]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=\{\}]*)$/,
    CODICE: /^[a-zA-Z0-9\-_]+$/,
    PROFILO_UTENTE_DEFAULT: /^(?!profilo\.utente\.default$).*$/
  };

  public static PRATICHE = {
    PREFERITE: 'preferite',
    IN_LAVORAZIONE: 'in lavorazione',
    IN_EVIDENZA: 'in evidenza'
  };

  public static IMPOSTAZIONI_FIRMA = {
    TIPI_OTP: {
      SMS: 'SMS',
      SMART_TOKEN: 'SMART-TOKEN'
    },
    ENTE_CERTIFICATORE: {
      ARUBA: 'Aruba',
      INFOCERT: 'InfoCert'
    }
  };

  public static CODICE_ESITO_OK = '000';

  public static FORM_LOGICI = {
    ATTIVITA_KEY: 'ATT_ASSEGNABILI',
    OUTPUT_KEY: 'O_ASSEGNAZIONE',
    TIPI_DOC_OBBLIGATORI_KEY: 'TIPI_DOC_OBBLIGATORI',
    TIPI_DOC_DA_FIRMARE_OBBLIGATORI_KEY: 'TIPI_DOC_DA_FIRMARE',
    TIPI_DOC_NON_RIMOVIBILI_KEY: 'TIPI_DOC_NON_RIMOVIBILI',
    TIPI_DOC_CARICABILI_KEY: 'TIPI_DOC_CARICABILI',
    APPOSIZIONE_FIRMA_KEY: 'APPOSIZIONE_FIRMA',
    ABILITA_RICERCA_ACTA_KEY: 'ABILITA_RICERCA_ACTA',
    CONTROLLI_OBBLIGATORIETA : {
      ATTIVITA_ASSEGNABILI: 'ATT_ASSEGNABILI'
    },
    SYS_EXT_CALL : {
      TITOLO_KEY: 'TITOLO_BUTTON',
      LINK_KEY: 'LINK_BUTTON',
      PAYLOAD_KEY: 'PAYLOAD_BUTTON',
      METODO_KEY: 'METODO_BUTTON'
    },
    TIPOLOGIE_STATI_PRATICA_DA_ASSOCIARE: 'TIPOLOGIE_STATI_PRATICA_DA_ASSOCIARE',
    NOME_GRUPPO: 'nomeGruppo',
    NOME_TAG: 'nomeTag',
    VARIABILE_PROCESSO: 'variabileProcesso',
    CONTROLLO_VALIDITA_FIRMA_KEY: 'CONTROLLO_VALIDITA_FIRMA',
    VERIFICA_DATA_DOC_OBBLIGATORI: 'VERIFICA_DATA_DOC_OBBLIGATORI'
  };

  public static SCELTA_FIRMA_FEA = 'SCELTA_FIRMA_FEA';

  public static CODICE_PROFILO_CONFIGURAZIONE = 'CONFIG';

}
