/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { environment } from '../../../environments/environment';

const SLASH = '/';

const urlContext = environment.beServer.replace(/\/+$/, '') +
  SLASH +
  environment.beServerContextRoot;

const wsContext = environment.wsServer.replace(/\/+$/, '') +
  SLASH +
  environment.beServerContextRoot;

const templatesContext = environment.beServer.replace(/\/+$/, '') + SLASH + 'templates';

/**
 * Rest services path constants.
 */
const apiContext = urlContext + SLASH + environment.api;
const apiAuthorizationContext = urlContext + SLASH + environment.apiAuthorization;
const apiCmmnContext = urlContext + SLASH + environment.apiCmmn;
const apiPraticheContext = urlContext + SLASH + environment.apiPratiche;
const apiEcmContext = urlContext + SLASH + environment.apiEcm;
const apiWsContext = wsContext + SLASH + environment.wsContext;
const apiBusinessContext = urlContext + SLASH + environment.apiBusiness;

export const ApiOptions = {
  IN_BACKGROUND: 'X-Cosmo-Background'
};

export const ApiUrls = {
  /**
   * General api url
   */
  APIURL: apiContext,

  /** ROOTS
   *
   */
  COSMO_BUSINESS: apiBusinessContext,

  /**
   * COMMONS
   */
  SESSIONE: apiContext + 'sessione',
  UPLOAD_FILE: apiContext + 'file/upload',
  UPLOAD_FILE_PRATICHE: apiContext + 'file/pratiche/upload',
  FILE_PRATICHE: apiContext + 'file/pratiche',
  UPLOAD_SESSION: apiContext + 'file/upload-session',
  UPLOAD_SESSION_ID: apiContext + 'file/upload-session/{sessionUUID}',
  UPLOAD_SESSION_FILE_ZIP: apiContext + 'file/documentizip/upload-session',
  UPLOAD_SESSION_FILE_ZIP_ID: apiContext + 'file/documentizip/upload-session/{sessionUUID}',
  IMPOSTAZIONI_FIRMA: apiAuthorizationContext + 'impostazioni-firma',


  TEMPLATE: environment.beServer.replace(/\/+$/, '') + SLASH + 'templates/{file}',

  /**
   * api UTENTI
   */
  UTENTI: apiAuthorizationContext + 'utenti',
  GET_DELETE_UTENTE: apiAuthorizationContext + 'utenti/{id}',
  GET_UTENTE_CORRENTE: apiAuthorizationContext + 'utenti/utente-corrente',
  GET_UTENTI_ENTE: apiAuthorizationContext + 'utenti/ente',
  GET_UTENTE_ENTE_VALIDITA: apiAuthorizationContext + 'utenti/{idUtente}/ente/{idEnte}/validita',

  /**
   * ENTI
   */
  ENTI: apiAuthorizationContext + 'enti',
  GET_DELETE_ENTE: apiAuthorizationContext + 'enti/{id}',

  /**
   * USE-CASE
   */
  USE_CASE: apiAuthorizationContext + 'use-case',
  USE_CASE_CATEGORIE: apiAuthorizationContext + 'use-case/categorie',

  /**
   * PROFILI
   */
  PROFILI: apiAuthorizationContext + 'profili',
  GET_PROFILO: apiAuthorizationContext + 'profili/{id}',

  /**
   * GRUPPI
   */
  GRUPPI: apiAuthorizationContext + 'gruppi',
  GET_GRUPPO: apiAuthorizationContext + 'gruppi/{id}',
  GRUPPI_UTENTE_CORRENTE: apiAuthorizationContext + 'gruppi/utente-corrente',

  /**
   * PREFERENZE UTENTE
   */
  PREFERENZE_UTENTE: apiAuthorizationContext + 'preferenze-utente',

  /**
   * CONFIGURAZIONE
   */
  GET_CONFIGURAZIONE_PARAMETRO: apiAuthorizationContext + 'configurazione/parametri',

  /**
   * PREFERENZE ENTE
   */
  PREFERENZE_ENTE: apiAuthorizationContext + 'settings/organization',

  /**
   * AMMINISTRAZIONE
   */
  DISCOVERY_SERVICES: apiContext + 'discovery/services',

  /**
   * WEBSOCKET
   */
  WEBSOCKET_ROOT: apiWsContext + '',

  /**
   * NOTIFICHE
   */
  NOTIFICHE: apiContext + 'proxy/notifications/utenti/notifiche',
  NOTIFICHE_TUTTE: apiContext + 'proxy/notifications/utenti/notifiche/tutte',

  /**
   * PRATICHE
   */
  TIPI_PRATICHE_PER_ENTE: apiPraticheContext + 'tipi-pratiche-per-ente',
  TIPI_PRATICHE: apiPraticheContext + 'tipi-pratiche',
  TIPO_PRATICA: apiPraticheContext + 'tipi-pratiche/{codice}',
  GET_STATI_PRATICHE: apiPraticheContext + 'pratiche/stato',
  GET_PRATICHE: apiPraticheContext + 'pratiche',
  SET_PREFERITA_STATUS_BY_ID: apiPraticheContext + 'utenti/pratiche/preferite/{idPratica}',
  GET_PRATICA: apiPraticheContext + 'pratiche/{id}',
  GET_FORMS: apiPraticheContext + 'forms/{nome}',
  GET_FORMS_ATTIVITA: apiPraticheContext + 'forms/attivita/{id}',
  GET_VISIBILITA_BY_ID: apiPraticheContext + 'pratiche/visibilita',
  GET_VISIBILITA_BY_TASK: apiPraticheContext + 'pratiche/visibilita/task',
  GET_TASK_SUBTASKS : apiPraticheContext + 'pratiche/task/{idTask}/subtasks',
  GET_STORICO_PRATICA: apiPraticheContext + 'pratiche/{id}/storico',
  GET_FORM: apiPraticheContext + 'forms/{nomeForm}',
  GET_ATTIVITA_BY_TASK_ID: apiPraticheContext + 'pratiche-attivita/{idTask}',
  GET_ATTIVITA_BY_ID_PRATICA: apiPraticheContext + 'pratiche/{idPratica}/attivita',
  PRATICA_CONDIVISIONI: apiPraticheContext + 'pratiche/{idPratica}/condivisioni',
  PRATICA_CONDIVISIONE: apiPraticheContext + 'pratiche/{idPratica}/condivisioni/{idCondivisione}',
  FORM_DEFINITION_BY_FORM_KEY: apiPraticheContext + 'forms/definitions/{formKey}',
  FORM_DEFINITION_BY_TASK_ID: apiPraticheContext + 'pratiche/task/{idTask}/form',
  PRATICA_DIAGRAMMA: apiPraticheContext + 'pratiche/{idPratica}/diagramma',
  PRATICA_ELABORAZIONE: apiBusinessContext + 'pratiche/{idPratica}/elaborazione',
  PRATICA_ELABORAZIONE_CONTESTO: apiBusinessContext + 'pratiche/{idPratica}/elaborazione/contesto',
  TIPI_RELAZIONE_PRATICA_PRATICA: apiPraticheContext + 'pratiche/tipi-relazione-pratica-pratica',
  RELAZIONI_PRATICA: apiPraticheContext + 'pratiche/{idPratica}/in-relazione',
  DA_RELAZIONARE_PRATICA: apiPraticheContext + 'pratiche/{idPratica}/da-relazionare',
  CREA_AGGIORNA_RELAZIONE_PRATICA: apiPraticheContext + 'pratiche/{idPraticaDa}/{codiceTipoRelazione}',

  ESPORTA_TIPO_PRATICA_OPZIONI_TENANT: apiPraticheContext + 'import-export/opzioni-esportazione-tenant',
  ESPORTA_TIPO_PRATICA: apiPraticheContext + 'import-export/esporta',
  IMPORTA_TIPO_PRATICA: apiPraticheContext + 'import-export/importa',
  UPDATE_PRATICA: apiPraticheContext + 'pratiche/{idPratica}',
  PRATICHE_UPLOAD_FILE: apiPraticheContext + 'pratiche/file',
  PRATICHE_UPLOAD_FILE_ID: apiPraticheContext + 'pratiche/file/{id}',
  PRATICHE_UPLOAD_CARICAMENTO_IN_BOZZA: apiPraticheContext + 'pratiche/file/caricamento-in-bozza',
  PRATICHE_UPLOAD_FILE_STATI: apiPraticheContext + 'pratiche/file/stati-caricamento',
  VARIABILI_DI_FILTRO: apiPraticheContext + 'variabili-filtro',
  VARIABILE_DI_FILTRO: apiPraticheContext + 'variabili-filtro/{idVariabile}',
  VARIABILE_DI_FILTRO_TIPO_PRATICA: apiPraticheContext + 'variabili-filtro/tipo-pratica/{codice}',
  FORMATI_VARIABILI_DI_FILTRO: apiPraticheContext + 'variabili-filtro/formati',
  TIPI_FILTRO_VARIABILI_DI_FILTRO: apiPraticheContext + 'variabili-filtro/tipiFiltro',

  /**
   * EVENTI
   */
  CREATE_NEW_EVENTO: apiBusinessContext + 'eventi',
  UPDATE_DELETE_NEW_EVENTO: apiBusinessContext + 'eventi/{idEvento}',
  GET_EVENTI : apiBusinessContext + 'task',

  /**
   * COMMENTI
   */
  GET_TASK_COMMENTI : apiBusinessContext + 'task/{idTask}/commenti',
  POST_TASK_COMMENTI : apiBusinessContext + 'task/{idTask}/commenti',
  POST_TASK_SUBTASK : apiBusinessContext + 'task',
  GET_COMMENTI: apiBusinessContext + 'pratiche/{idProcesso}/commenti',
  POST_COMMENTI: apiBusinessContext + 'pratiche/{idProcesso}/commenti',
  DELETE_COMMENTI: apiBusinessContext + 'pratiche/{idProcesso}/commenti/{idCommento}',

  /**
   * CMMN
   */
  CREA_PRATICA :  apiBusinessContext + 'pratiche',
  GET_TASK: apiBusinessContext + 'task/{idTask}',
  POST_TASK: apiBusinessContext + 'task/{idTask}',
  RUNTIME_INSTANCES: apiBusinessContext + 'pratiche/{idProcesso}/',
  PROC_INSTANCE_VARIABLES: apiBusinessContext + 'pratiche/variabili/{processInstanceId}',
  HISTORIC_PROCESS_INSTACES: apiBusinessContext + 'pratiche/history-process/{processInstanceId}/variabili',
  POST_PRATICA_SALVA_TASK: apiBusinessContext + 'pratiche/{idPratica}/attivita/{idAttivita}/salva',
  POST_PRATICA_CONFERMA_TASK: apiBusinessContext + 'pratiche/{idPratica}/attivita/{idAttivita}/conferma',
  POST_PRATICA_ASSEGNA_TASK: apiBusinessContext + 'pratiche/{idPratica}/attivita/{idAttivita}/assegna',
  POST_PRATICA_ASSEGNA_TASK_ME: apiBusinessContext + 'pratiche/{idPratica}/attivita/{idAttivita}/assegnami',
  POST_PRATICA_ASSEGNA: apiBusinessContext + 'pratiche/{idPratica}/assegna',
  POST_ATTIVAZIONE_SISTEMA_ESTERNO: apiBusinessContext + 'pratiche/{idPratica}/attivita/{idAttivita}/attivazione-sistema-esterno/invia',
  GET_PAYLOAD_ATTIVAZIONE_SISTEMA_ESTERNO: apiBusinessContext + 'pratiche/{idPratica}/attivita/{idAttivita}/attivazione-sistema-esterno/payload',

  /**
   * DOCUMENTI
   */
  DOCUMENTI: apiEcmContext + 'documenti',
  DOWNLOAD_CONTENUTO_DOCUMENTO: apiEcmContext + 'documenti/{idDocumento}/contenuti/{idContenuto}',
  GET_PUT_DELETE_DOCUMENTO: apiEcmContext + 'documenti/{id}',
  DELETE_FILE: apiEcmContext + 'file/{fileUUID}',
  GET_TIPI_DOCUMENTI: apiEcmContext + 'tipi-documenti',
  DOCUMENTI_DA_FIRMARE: apiEcmContext + 'documenti-da-firmare',
  GET_TIPI_DOCUMENTO: apiEcmContext + 'tipi-documento',
  GET_TIPI_DOCUMENTI_ALL: apiEcmContext + 'tipi-documenti/all',
  RICERCA_TIPI_DOCUMENTO: apiEcmContext + 'tipi-documento/ricerca',
  GET_TIPI_DOCUMENTI_SALVATI: apiEcmContext + 'documenti/tipologie-documenti-salvati',
  GET_REPORT_TEMPLATES: apiEcmContext + 'report-template',
  POST_REPORT_TEMPLATES: apiEcmContext + 'report-template',
  DELETE_REPORT_TEMPLATE: apiEcmContext + 'report-template/{id}',
  GET_REPORT_TEMPLATE: apiEcmContext + 'report-template/{id}',
  UPDATE_REPORT_TEMPLATE: apiEcmContext + 'report-template/{id}',
  REPORT_ASYNC: apiEcmContext + 'report/async',
  REPORT_ANTEPRIMA: apiEcmContext + 'report/anteprima',
  DUPLICA_DOCUMENTI: apiEcmContext + 'documenti/{idPraticaDa}/duplica-documenti/{idPraticaA}',
  GET_CONTENUTO_DOCUMENTO_INDEX: apiEcmContext + 'documenti/{idDocumento}/contenuti/{idContenuto}/content',
  GET_INFO_FEA: apiEcmContext + 'documenti/{idDocumento}/info-fea',

  /**
   * api CERTIFICATI
   */
  CERTIFICATI: apiAuthorizationContext + 'certificati',
  CERTIFICATI_ID: apiAuthorizationContext + 'certificati/{id}',
  CERTIFICATI_ULTIMO_USATO: apiAuthorizationContext + 'certificati/salva-ultimo-usato/{id}',

  /**
   * FIRMA DIGITALE
   */
  RICHIEDI_OTP: apiEcmContext + 'richiesta-otp',
  FIRMA: apiEcmContext + 'firma',

  /**
   * LOCK
   */
  LOCK: apiBusinessContext + 'lock',
  LOCK_RELEASE: apiBusinessContext + 'lock/release',

  /**
   * OPERAZIONI ASINCRONE
   */
  OPERAZIONI_ASINCRONE_SINGLE: apiBusinessContext + 'operazioni-asincrone/{uuid}',

  /**
   * APPLICAZIONI ESTERNE
   */
  APP_ESTERNE: apiAuthorizationContext + 'applicazione-esterna',
  APP_ESTERNE_ALL: apiAuthorizationContext + 'applicazione-esterna/all',
  APP_ESTERNA: apiAuthorizationContext + 'applicazione-esterna/{idApp}',
  FUNZIONALITA: apiAuthorizationContext + 'applicazione-esterna/{idApp}/funzionalita',
  SINGOLA_FUNZIONALITA: apiAuthorizationContext + 'applicazione-esterna/{idApp}/funzionalita/{idFunzionalita}',
  ASSOCIAZIONE_APP_UTENTE: apiAuthorizationContext + 'applicazione-esterna/associazione-utente',
  ASSOCIAZIONE_FUNZIONALITA: apiAuthorizationContext + 'applicazione-esterna/{idApp}/associazione-funzionalita',
  ASSOCIAZIONE_APP_ENTE: apiAuthorizationContext + 'applicazione-esterna/associazione-ente',
  APP_PER_ASSOCIAZIONE_ENTE: apiAuthorizationContext + 'applicazione-esterna/{idApp}/associazione-ente',

  /**
   * PROCESSI
   */
  PROCESSI: apiCmmnContext + 'process/repository/deployments',
  // PROCESSI: apiCmmnContext + 'process/cosmo/processi/deploy',

   /**
    * FRUITORI
    */
  FRUITORI: apiAuthorizationContext + 'fruitori',
  GET_FRUITORE: apiAuthorizationContext + 'fruitori/{id}',
  AUTORIZZAZIONI_FRUITORE: apiAuthorizationContext + 'autorizzazioni-fruitore',
  SCHEMI_AUTENTICAZIONE_FRUITORE: apiAuthorizationContext + 'fruitori/{idFruitore}/schemi-autenticazione',
  SCHEMA_AUTENTICAZIONE_FRUITORE: apiAuthorizationContext + 'fruitori/{idFruitore}/schemi-autenticazione/{idSchema}',
  TEST_SCHEMA_AUTENTICAZIONE_FRUITORE: apiBusinessContext + 'schemi-autenticazione/{idSchema}/test',
  ENDPOINTS_FRUITORE: apiAuthorizationContext + 'fruitori/{idFruitore}/endpoints',
  ENDPOINT_FRUITORE: apiAuthorizationContext + 'fruitori/{idFruitore}/endpoints/{idEndpoint}',
  OPERAZIONI_FRUITORE: apiAuthorizationContext + 'operazioni-fruitore',

  /**
   * FORM LOGICI
   */
  FORM_LOGICI: apiBusinessContext + 'form-logici',
  FORM_LOGICO: apiBusinessContext + 'form-logici/{id}',
  ISTANZE_FUNZIONALITA_FORM_LOGICI: apiBusinessContext + 'form-logici/istanze-funzionalita',
  ISTANZA_FUNZIONALITA_FORM_LOGICI: apiBusinessContext + 'form-logici/istanze-funzionalita/{id}',
  TIPOLOGIA_ISTANZA_FUNZIONALITA_FORM_LOGICI: apiBusinessContext + 'form-logici/tipologie-istanze-funzionalita',
  // tslint:disable-next-line:max-line-length
  PARAMETRI_TIPOLOGIA_ISTANZA_FUNZIONALITA_FORM_LOGICI: apiBusinessContext + 'form-logici/tipologie-istanze-funzionalita/{codice}/parametri',

  /**
   * CUSTOM FORMS
   */

  CUSTOM_FORMS: apiPraticheContext + 'custom-forms',
  CUSTOM_FORM: apiPraticheContext + 'custom-forms/{codice}',
  CUSTOM_FORM_FROM_CODICE_TIPO_PRATICA: apiPraticheContext + 'custom-forms/tipo-pratica/{codiceTipoPratica}',

  /**
   *  HELPERS
   */

  HELPERS: apiContext + 'proxy/notifications/helper',
  HELPER: apiContext + 'proxy/notifications/helper/{id}',
  GET_CODICI_PAGINA: apiContext + 'proxy/notifications/helper/codicePagina',
  GET_CODICI_TAB: apiContext + 'proxy/notifications/helper/codicePagina/{codice}/codiceTab',
  GET_CODICI_MODALE: apiContext + 'proxy/notifications/helper/codice-modale',
  GET_DECODIFICA: apiContext + 'proxy/notifications/helper/decodifica',
  ESPORTA_HELPER: apiContext + 'proxy/notifications/helper/export/{id}',
  IMPORTA_HELPER: apiContext + 'proxy/notifications/helper/import',

  ESECUZIONE_MULTIPLA_TASK: apiPraticheContext + 'esecuzione-multipla/task',
  ESECUZIONE_MULTIPLA_TASK_DISPONIBILI: apiPraticheContext + 'esecuzione-multipla/task-disponibili',
  ESECUZIONE_MULTIPLA_APPROVAZIONE: apiBusinessContext + 'esecuzione-multipla/approva',
  ESECUZIONE_MULTIPLA_VARIABILI_PROCESSO: apiBusinessContext + 'esecuzione-multipla/variabili-processo',
  ESECUZIONE_MULTIPLA_FIRMA: apiEcmContext + 'esecuzione-multipla/firma',
  ESECUZIONE_MULTIPLA_RIFIUTO_FIRMA: apiEcmContext + 'esecuzione-multipla/rifiutoFirma',

  /**
   * TEMPORARY - ACTA
   */
  RICERCA_ACTA: apiEcmContext + 'acta/documenti',
  IMPORTAZIONE_DOCUMENTI_ACTA: apiEcmContext + 'acta/documenti/importa',
  IDENTITA_UTENTE_ACTA: apiEcmContext + 'acta/identita-utente',

  /**
   * SEGNALIBRO
   */
  TIPI_SEGNALIBRO: apiAuthorizationContext + 'segnalibri/tipi-segnalibri',

  /**
   * CONFIGURAZIONE ENTE
   */
  CONFIGURAZIONE_ENTE: apiAuthorizationContext + 'configurazioni-ente',
  CONFIGURAZIONE_ENTE_CON_CHIAVE: apiAuthorizationContext + 'configurazioni-ente/{chiave}',

  /**
   * TAGS
   */
   TAGS: apiAuthorizationContext + 'tags',
   GET_TIPI_TAGS: apiAuthorizationContext + '/tipi-tags',
   TAG_ID: apiAuthorizationContext + 'tags/{id}',

  /**
   * TABS-DETTAGLIO
   */
   TABS_DETTAGLIO: apiPraticheContext + 'tabs-dettaglio',
   TABS_DETTAGLIO_FROM_CODICE_TIPO_PRATICA: apiPraticheContext + 'tabs-dettaglio/{codiceTipoPratica}',

  /**
   * PARAMETRI-DI-SISTEMA
   */

   PARAMETRI_DI_SISTEMA: apiAuthorizationContext + 'parametri-di-sistema',
   PARAMETRO_DI_SISTEMA: apiAuthorizationContext + 'parametri-di-sistema/{chiave}',

  /**
   * FORMATO-FILE
   */
   FORMATI_FILE: apiEcmContext + 'formati-file',
   FORMATI_FILE_GROUPED: apiEcmContext + 'formati-file/grouped',

  /**
   * DEADLETTER_JOBS
   */
   DEADLETTER_JOBS: apiBusinessContext + 'deadletter-jobs',
   DEADLETTER_JOB_POST: apiBusinessContext + 'deadletter-jobs/{jobId}',

  /**
   * TEMPLATE-FEA
   */
  TEMPLATE_FEA: apiEcmContext + 'template-fea',
  TEMPLATE_FEA_ID: apiEcmContext + 'template-fea/{id}',

  /**
   * FIRMA-FEA
   */
  RICHIEDI_OTP_FEA: apiEcmContext + 'fea/richiedi-otp',
  FIRMA_FEA: apiEcmContext + 'fea/firma',

  /**
   * SIGILLO-ELETTRONICO
   */
  SIGILLO_ELETTRONICO: apiEcmContext + 'sigillo-elettronico',
  SIGILLO_ELETTRONICO_ID: apiEcmContext + 'sigillo-elettronico/{id}',


  /**
   * CONFIGURAZIONI-MESSAGGI-NOTIFICHE
   */
  CONFIGURAZIONI_MESSAGGI_NOTIFICHE: apiContext + 'proxy/notifications/configurazioni-messaggi-notifiche',
  CONFIGURAZIONI_MESSAGGI_NOTIFICHE_ID: apiContext + 'proxy/notifications/configurazioni-messaggi-notifiche/{id}',

  /**
   * TIPI-NOTIFICHE
   */
  TIPI_NOTIFICHE: apiContext + 'proxy/notifications/tipi-notifiche'
};
