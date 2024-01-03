/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { debug } from 'console';
import { forkJoin, Observable, of } from 'rxjs';
import { flatMap, map, mergeMap } from 'rxjs/operators';
import { AttivitaEseguibileMassivamente } from 'src/app/shared/models/api/cosmopratiche/attivitaEseguibileMassivamente';
import { TipiDocumentoObbligatori } from '../form-logici/gestione-documenti/models/tipi-documento-obbligatori.models';
import { AttivitaAssegnazione } from '../form-logici/pianificazione-attivita/models/attivita-assegnazione.model';
import { AttivitaUtente } from '../form-logici/pianificazione-attivita/models/attivita-utente.model';
import { OutputAssegnazione } from '../form-logici/pianificazione-attivita/models/output-assegnazione.model';
import { UtenteGruppoDTO } from '../form-logici/pianificazione-attivita/models/utente-gruppo.model';
import { Constants } from '../shared/constants/constants';
import { NomeFunzionalita } from '../shared/enums/nome-funzionalita';
import { EsecuzioneMultiplaApprovaRequest } from '../shared/models/api/cosmobusiness/esecuzioneMultiplaApprovaRequest';
import { EsecuzioneMultiplaVariabiliProcessoRequest } from '../shared/models/api/cosmobusiness/esecuzioneMultiplaVariabiliProcessoRequest';
import { RiferimentoOperazioneAsincrona } from '../shared/models/api/cosmobusiness/riferimentoOperazioneAsincrona';
import { VariabileProcesso } from '../shared/models/api/cosmobusiness/variabileProcesso';
import { EsecuzioneMultiplaFirmaRequest } from '../shared/models/api/cosmoecm/esecuzioneMultiplaFirmaRequest';
import { EsecuzioneMultiplaRifiutoFirmaRequest } from '../shared/models/api/cosmoecm/esecuzioneMultiplaRifiutoFirmaRequest';
import { VerificaTipologiaDocumentiSalvati } from '../shared/models/api/cosmoecm/verificaTipologiaDocumentiSalvati';
import { FunzionalitaFormLogico } from '../shared/models/api/cosmopratiche/funzionalitaFormLogico';
import { PraticheService } from '../shared/services/pratiche.service';
import { TipiDocumentiService } from '../shared/services/tipi-documenti/tipi-documenti.service';
import { ApiUrls } from '../shared/utilities/apiurls';
import { Utils } from '../shared/utilities/utilities';
import { DatePipe } from '@angular/common';
import { DateUtils } from '../shared/utilities/date-utils';

@Injectable()
export class EsecuzioneMultiplaService {

  private selectedTasks: AttivitaEseguibileMassivamente[] = [];

  constructor(private http: HttpClient,
              private praticheService: PraticheService,
              private tipiDocumentiService: TipiDocumentiService) {
    // NOP
  }

  postEsecuzioneMultiplaApprova(payload: EsecuzioneMultiplaApprovaRequest): Observable<RiferimentoOperazioneAsincrona> {
    return this.http.post<RiferimentoOperazioneAsincrona>(ApiUrls.ESECUZIONE_MULTIPLA_APPROVAZIONE, payload);
  }

  postEsecuzioneMultiplaVariabiliProcesso(payload: EsecuzioneMultiplaVariabiliProcessoRequest): Observable<RiferimentoOperazioneAsincrona> {
    return this.http.post<RiferimentoOperazioneAsincrona>(ApiUrls.ESECUZIONE_MULTIPLA_VARIABILI_PROCESSO, payload);
  }

  setSelectedTasks(tasks: AttivitaEseguibileMassivamente[]) {
    this.selectedTasks = tasks;
  }

  getSelectedTasks(): AttivitaEseguibileMassivamente[] {
    return this.selectedTasks;
  }

  removeSelectedTask(task: AttivitaEseguibileMassivamente) {
    const index = this.selectedTasks.findIndex(c => c.attivita?.id === task.attivita?.id);
    if (index === null || index === undefined || typeof index === 'undefined') {
      return;
    }
    this.selectedTasks.splice(index, 1);
  }

  postEsecuzioneMultiplaFirma(payload: EsecuzioneMultiplaFirmaRequest): Observable<RiferimentoOperazioneAsincrona> {
    return this.http.post<RiferimentoOperazioneAsincrona>(ApiUrls.ESECUZIONE_MULTIPLA_FIRMA, payload);
  }

  postEsecuzioneMultiplaRifiutoFirma(payload: EsecuzioneMultiplaRifiutoFirmaRequest): Observable<RiferimentoOperazioneAsincrona> {
    return this.http.post<RiferimentoOperazioneAsincrona>(ApiUrls.ESECUZIONE_MULTIPLA_RIFIUTO_FIRMA, payload);
  }

  public getValidation(task: string, mapped: AttivitaEseguibileMassivamente[]) {
    return of(mapped).pipe(
      map(attivitaEseguibili => attivitaEseguibili.map(attivitaEseguibile => ({
        linkPratica: attivitaEseguibile.pratica?.linkPratica?.split('/pratiche/')[1] ?? '',
        idPratica: attivitaEseguibile.pratica?.id ?? 0,
        formKey: attivitaEseguibile.attivita?.formKey,
        creationTime: attivitaEseguibile.attivita?.campiTecnici?.dtIniVal
      }))),

      map(data => data.map(single => forkJoin({
        strutturaFormLogico: this.praticheService.getForms(single.formKey ?? ''),
        variabiliProcesso: this.praticheService.getVariabiliProcesso(single.linkPratica),
        idPratica: of(single.idPratica),
        creationTime: of(single.creationTime),
      }).pipe(mergeMap(otherData => {
        const parametri = this.buildParams(otherData.strutturaFormLogico.funzionalita ?? []);
        const funzionalita = otherData.strutturaFormLogico.funzionalita ?? [];
        const variabiliProcesso = otherData.variabiliProcesso.variabili ?? [];

        if (!funzionalita || (!funzionalita.find(singleFunz => singleFunz.codice === NomeFunzionalita.GESTIONE_DOCUMENTI)
          && !funzionalita.find(singleFunz => singleFunz.codice === NomeFunzionalita.FIRMA_DOCUMENTI))) {
          return of({
            idPratica: otherData.idPratica,
            validazione: this.validation(task, variabiliProcesso, funzionalita, parametri, [], [], [], [])
          });
        }

        let requestDoc = '';
        let requestDocDaFirmare = '';

        const listaDocObbligatori: TipiDocumentoObbligatori[] = [];
        const listaDocDaFirmareObbligatori: TipiDocumentoObbligatori[] = [];

        if (funzionalita.find(singleFunz => singleFunz.codice === NomeFunzionalita.GESTIONE_DOCUMENTI)) {
          JSON.parse(parametri.get(Constants.FORM_LOGICI.TIPI_DOC_OBBLIGATORI_KEY) as string)
            ?.tipi_documento.forEach((tipoDocumentoObbligatorio: TipiDocumentoObbligatori) => {
              listaDocObbligatori.push(tipoDocumentoObbligatorio);
            });

          const tipologieObbligatorie: VerificaTipologiaDocumentiSalvati[] = [];
          listaDocObbligatori.forEach(docObb => {
            if (docObb.codicePadre){
              tipologieObbligatorie.push({ codiceTipologiaDocumento: docObb.codice, codiceTipologiaDocumentoPadre: docObb.codicePadre });
            }else{

              tipologieObbligatorie.push({ codiceTipologiaDocumento: docObb.codice });
            }
        });

          requestDoc = JSON.stringify({
            idPratica: otherData.idPratica ?? 0,
            tipologieDocumenti: tipologieObbligatorie
          });
        }

        if (funzionalita.find(singleFunz => singleFunz.codice === NomeFunzionalita.FIRMA_DOCUMENTI)) {
          JSON.parse(parametri.get(Constants.FORM_LOGICI.TIPI_DOC_DA_FIRMARE_OBBLIGATORI_KEY) as string)
            ?.tipi_documento.forEach((tipoDocumentoObbligatorio: TipiDocumentoObbligatori) => {
              listaDocDaFirmareObbligatori.push(tipoDocumentoObbligatorio);
            });

          const tipologieObbligatorie: VerificaTipologiaDocumentiSalvati[] = [];
          listaDocDaFirmareObbligatori.forEach(docObb => {
            if (docObb.codicePadre){
              tipologieObbligatorie.push({ codiceTipologiaDocumento: docObb.codice, codiceTipologiaDocumentoPadre: docObb.codicePadre });
            } else{

              tipologieObbligatorie.push({ codiceTipologiaDocumento: docObb.codice });
            }
          });

          requestDocDaFirmare = JSON.stringify({
            idPratica: otherData.idPratica ?? 0,
            tipologieDocumenti: tipologieObbligatorie,
            daFirmare: true,
            creationTime: otherData?.creationTime ? 
            new Date(otherData?.creationTime).toISOString() : null
          });
        }


        if (funzionalita.find(singleFunz => singleFunz.codice === NomeFunzionalita.GESTIONE_DOCUMENTI)
          && funzionalita.find(singleFunz => singleFunz.codice === NomeFunzionalita.FIRMA_DOCUMENTI)) {
          return forkJoin({
            tipologieDocSalvati: this.tipiDocumentiService.getTipologieDocumentiSalvati(requestDoc),
            tipologieDocDaFirmareSalvati: this.tipiDocumentiService.getTipologieDocumentiSalvati(requestDocDaFirmare)
          }).pipe(

            map(tipologia => {
              return {
                idPratica: otherData.idPratica,
                validazione: this.validation(task, variabiliProcesso, funzionalita, parametri,
                  tipologia.tipologieDocSalvati, listaDocObbligatori, tipologia.tipologieDocDaFirmareSalvati, listaDocDaFirmareObbligatori)
              };
            }));
        } else if (funzionalita.find(singleFunz => singleFunz.codice === NomeFunzionalita.GESTIONE_DOCUMENTI)
          && !funzionalita.find(singleFunz => singleFunz.codice === NomeFunzionalita.FIRMA_DOCUMENTI)) {
          return forkJoin({
            tipologieDocSalvati: this.tipiDocumentiService.getTipologieDocumentiSalvati(requestDoc)
          }).pipe(

            map(tipologia => {
              return {
                idPratica: otherData.idPratica,
                validazione: this.validation(task, variabiliProcesso, funzionalita, parametri,
                  tipologia.tipologieDocSalvati, listaDocObbligatori, [], [])
              };
            }));
        } else if (!funzionalita.find(singleFunz => singleFunz.codice === NomeFunzionalita.GESTIONE_DOCUMENTI)
          && funzionalita.find(singleFunz => singleFunz.codice === NomeFunzionalita.FIRMA_DOCUMENTI)) {
          return forkJoin({
            tipologieDocDaFirmareSalvati: this.tipiDocumentiService.getTipologieDocumentiSalvati(requestDocDaFirmare)
          }).pipe(

            map(tipologia => {
              return {
                idPratica: otherData.idPratica,
                validazione: this.validation(task, variabiliProcesso, funzionalita, parametri, [], [],
                  tipologia.tipologieDocDaFirmareSalvati, listaDocDaFirmareObbligatori)
              };
            }));
        } else{
          return of({
            idPratica: otherData.idPratica,
            validazione: true
          });
        }
      })
      ))),
      flatMap(data => forkJoin(data.map(dataMapped => dataMapped)))
    );
  }


  private buildParams(funzionalita: FunzionalitaFormLogico[]): Map<string, string> {
    const parametriFunzionalita = new Map<string, string>();

    funzionalita.forEach(singleFunzionalita => {
      if (singleFunzionalita && singleFunzionalita.parametri) {
        singleFunzionalita.parametri.forEach(parametro => {
          parametriFunzionalita.set(parametro.chiave ?? '', parametro.valore ?? '');
        });
      }
    });
    return parametriFunzionalita;
  }

  private validation(task: string, variabiliProcesso: VariabileProcesso[], funzionalita: FunzionalitaFormLogico[],
                     parametri: Map<string, string>, tipologieDoc: VerificaTipologiaDocumentiSalvati[],
                     listaDocObbligatori: TipiDocumentoObbligatori[], tipologieDocDaFirmare: VerificaTipologiaDocumentiSalvati[],
                     listaDocDaFirmareObbligatori: TipiDocumentoObbligatori[]): boolean {

    let isValid = true;

    for (const singleFunz of funzionalita) {
      switch (singleFunz.codice) {
        case NomeFunzionalita.CREAZIONE_PRATICA: {
          const found = variabiliProcesso.find(variabile => 'pratiche_figlie_create_da_form' === variabile.name);
          const praticaFigliaCreataDaForm = found ? Number(found) : 0;
          isValid = praticaFigliaCreataDaForm > 0;
          break;
        }
        case NomeFunzionalita.GESTIONE_DOCUMENTI: {
          isValid = this.gestioneDocumenti(tipologieDoc, listaDocObbligatori);
          break;
        }
        case NomeFunzionalita.PIANIFICAZIONE_ATTIVITA: {
          isValid = this.pianificazioneAttivita(variabiliProcesso, parametri);
          break;
        }
        case NomeFunzionalita.SCELTA: {
          isValid = this.scelta(variabiliProcesso, parametri);
          break;
        }
        case NomeFunzionalita.APPROVAZIONE: {
          isValid = this.approvazione(task, variabiliProcesso, parametri);
          break;
        }
        case NomeFunzionalita.FIRMA_DOCUMENTI: {
          isValid = this.firmaDocumenti(task, variabiliProcesso, parametri, tipologieDocDaFirmare, listaDocDaFirmareObbligatori);
          break;
        }
      }

      if (!isValid) {
        break;
      }
    }
    return isValid;
  }


  private gestioneDocumenti(tipologie: VerificaTipologiaDocumentiSalvati[], listaDocObbligatori: TipiDocumentoObbligatori[]): boolean {

    let numeroObbligatorio = 0;

    listaDocObbligatori.forEach(docObblig => {
      const tipoDoc = tipologie.find(res => res.codiceTipologiaDocumento === docObblig.codice);

      if (tipoDoc) {
        if (!tipoDoc.presente) {
          if (docObblig.obbligatorio === 'true') {
            numeroObbligatorio++;
          }
        }
      } else {
        if (docObblig.obbligatorio === 'true') {
          numeroObbligatorio++;
        }
      }
    });
    return listaDocObbligatori.length === 0 || (listaDocObbligatori.length > 0 && numeroObbligatorio === 0);
  }

  private firmaDocumenti(task: string, variabiliProcesso: VariabileProcesso[], parametri: Map<string, string>,
                         tipologie: VerificaTipologiaDocumentiSalvati[], listaDocObbligatori: TipiDocumentoObbligatori[]): boolean {

    if (task === NomeFunzionalita.FIRMA_DOCUMENTI){
      return true;
    }

    const docPresenti = this.gestioneDocumenti(tipologie, listaDocObbligatori);

    const raw = parametri?.get('O_FIRMA') ?? undefined;
    if (!raw) {
      return false;
    }
    let parsed;
    try {
      parsed = JSON.parse(raw);
    } catch (err) {
      return false;
    }

    const variabileRisultato = parsed.variabileRisultatoFirma;
    if (!variabileRisultato?.trim().length) {
      return false;
    }

    const variabileNote = parsed.variabileNoteFirma;
    if (!variabileNote?.trim().length) {
      return false;
    }

    let approvazione = false;
    let note = '';

    variabiliProcesso.forEach(variabile => {
      if (Utils.isNotBlank(variabile?.value) && variabileRisultato.trim() === variabile.name) {
        approvazione = (variabile.value as any) === 'true';
      }
      if (Utils.isNotBlank(variabile?.value) && variabileNote.trim() === variabile.name) {
        note = JSON.stringify(variabile.value);
      }
    });

    return (approvazione && !docPresenti) || (!approvazione && note.trim().length > 0);
  }


  private pianificazioneAttivita(variabiliProcesso: VariabileProcesso[], parametri: Map<string, string>): boolean {

    const outputAssegnazione = JSON.parse(parametri.get('O_ASSEGNAZIONE') || '{}') as OutputAssegnazione;

    const pianificazioni: { utenteGruppoList: UtenteGruppoDTO[], attivita: AttivitaAssegnazione }[] = [];
    outputAssegnazione?.attivitaAssegnazione.forEach(attivita => {
      const utenteGruppoList = variabiliProcesso.find(variabile => variabile.name === attivita.ruolo);
      if (utenteGruppoList && (utenteGruppoList.value as UtenteGruppoDTO[]).length > 0) {
        pianificazioni.push({
          utenteGruppoList: utenteGruppoList.value as UtenteGruppoDTO[],
          attivita
        });
      }
    });

    let attivitaUtente: AttivitaUtente[];
    const attivitaWip: AttivitaUtente[] = [];
    if (parametri && parametri.get(Constants.FORM_LOGICI.ATTIVITA_KEY)) {
      attivitaUtente = JSON.parse(parametri.get(Constants.FORM_LOGICI.ATTIVITA_KEY) || '{}');
      if (pianificazioni.length === 0 && attivitaUtente.length > 0) {
        attivitaUtente.forEach(singolaAttivita => {
          attivitaWip.push(singolaAttivita);
        });
      }

      attivitaUtente.forEach(singolaAttivita => {
        const findAttivitaPianificata = pianificazioni.find(pian => pian.attivita.nomeAttivita === singolaAttivita.nome);
        if (!findAttivitaPianificata) {
          attivitaWip.push(singolaAttivita);
        }
      });
    }

    const attivitaObbligatorie: string[] = [];

    attivitaWip.forEach(attivita => {
      if (String(attivita.obbligatorio) === 'true') {
        attivitaObbligatorie.push(attivita.nome);
      }
    });

    return pianificazioni.length > 0 && attivitaObbligatorie.length === 0;
  }

  private scelta(variabiliProcesso: VariabileProcesso[], parametri: Map<string, string>): boolean {

    let isValid = false;

    const variabileRisultato = (parametri?.get('O_SCELTA') as string).trim();
    if (!Utils.isNotBlank(variabileRisultato)) {
      return isValid;
    }

    const rawScelte = parametri?.get('SCELTE') ?? undefined;
    if (!rawScelte) {
      return isValid;
    }
    let scelte: { testo?: string; chiaveTesto?: string; icona?: string; classe?: string; valore?: string; }[];

    try {
      scelte = JSON.parse(rawScelte);
    } catch (err) {
      return isValid;
    }

    variabiliProcesso.forEach(variabile => {
      if (Utils.isNotBlank(variabile?.value) && variabileRisultato === variabile.name) {
        const sceltaSelezionata = scelte.find(c => c.valore === (variabile.value as any as string));
        isValid = !!sceltaSelezionata;
      }
    });
    return isValid;
  }

  private approvazione(task: string, variabiliProcesso: VariabileProcesso[], parametri: Map<string, string>): boolean {
    if (task === NomeFunzionalita.APPROVAZIONE){
      return true;
    }
    let approvazione = false;
    let note = '';

    const raw = parametri?.get('O_APPROVAZIONE') ?? undefined;
    if (!raw) {
      return false;
    }
    let parsed;
    try {
      parsed = JSON.parse(raw);
    } catch (err) {
      return false;
    }

    const variabileRisultato = parsed.variabileRisultatoApprovazione;
    if (!variabileRisultato?.trim().length) {
      return false;
    }

    const variabileNote = parsed.variabileNoteApprovazione;
    if (!variabileNote?.trim().length) {
      return false;
    }

    variabiliProcesso.forEach(variabile => {
      if (Utils.isNotBlank(variabile?.value) && variabileRisultato.trim() === variabile.name) {
        approvazione = (variabile.value as any) === 'true';
      }
      if (Utils.isNotBlank(variabile?.value) && variabileNote.trim() === variabile.name) {
        note = JSON.stringify(variabile.value);
      }
    });

    return approvazione || (!approvazione && note.trim().length > 0);
  }

}
