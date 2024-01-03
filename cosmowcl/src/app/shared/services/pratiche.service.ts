/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { ApiOptions, ApiUrls } from 'src/app/shared/utilities/apiurls';
import { PraticheResponse } from 'src/app/shared/models/api/cosmopratiche/praticheResponse';
import { map } from 'rxjs/operators';
import { PraticaExtResponse } from 'src/app/shared/models/practices/pratica-ext.model';
import { Pratica } from 'src/app/shared/models/api/cosmopratiche/pratica';
import { StatoPratica } from 'src/app/shared/models/api/cosmobusiness/statoPratica';
import { Attivita } from '../models/api/cosmopratiche/attivita';
import { Lock } from '../models/api/cosmobusiness/lock';
import { OperazioneAsincrona } from '../models/api/cosmobusiness/operazioneAsincrona';
import { VariabiliProcessoResponse } from '../models/api/cosmobusiness/variabiliProcessoResponse';
import { VariabileProcesso } from '../models/api/cosmobusiness/variabileProcesso';
import { Task } from '../models/api/cosmobusiness/task';
import { StrutturaFormLogico } from '../models/api/cosmopratiche/strutturaFormLogico';
import { Utils } from '../utilities/utilities';
import { AssegnaAttivitaRequest } from '../models/api/cosmobusiness/assegnaAttivitaRequest';
import { CreaCondivisionePraticaRequest } from '../models/api/cosmopratiche/creaCondivisionePraticaRequest';
import { GetElaborazionePraticaRequest } from '../models/api/cosmobusiness/getElaborazionePraticaRequest';
import { RiferimentoAttivita } from '../models/api/cosmopratiche/riferimentoAttivita';

@Injectable({
  providedIn: 'root',
})
export class PraticheService {

  constructor(private http: HttpClient) {
    // NOP
  }

  /** GET: Restituisce tutte le tipologie di pratica. */
  listStatiPraticaByTipo(tipo: string): Observable<StatoPratica[]> {
    if (!tipo) {
      throw new Error('Il tipo e\' obbligatorio');
    }
    const params = new HttpParams().append('tipo_pratica', tipo + '');

    return this.http.get<StatoPratica[]>(ApiUrls.GET_STATI_PRATICHE, { params }).pipe(
      map(lista => {
        lista.sort((o1, o2) => (o1.descrizione?.toUpperCase() ?? '').localeCompare((o2.descrizione?.toUpperCase() ?? '')));
        return lista;
      })
    );
  }

  listTaskPossibili(filter: any): Observable<RiferimentoAttivita[]> {

    const params = new HttpParams()
      .append('filter', JSON.stringify({
        filter: {},
        ...filter,
      }));
    return this.http.get<RiferimentoAttivita[]>(ApiUrls.ESECUZIONE_MULTIPLA_TASK_DISPONIBILI, { params });
  }

  listStatiPratica(): Observable<StatoPratica[]> {
    return this.http.get<StatoPratica[]>(ApiUrls.GET_STATI_PRATICHE).pipe(
      map(lista => {
        lista.sort((o1, o2) => (o1.descrizione?.toUpperCase() ?? '').localeCompare((o2.descrizione?.toUpperCase() ?? '')));
        return lista;
      })
    );
  }

  /** GET: Esegue la ricerca delle pratiche. */
  search(filter: any, exportData: boolean): Observable<PraticheResponse> {

    const params = new HttpParams()
      .append('export', exportData.toString())
      .append('filter', JSON.stringify({
        filter: {},
        ...filter,
      }));
    return this.http.get<PraticheResponse>(ApiUrls.GET_PRATICHE, { params });
  }

  /** data una pratica tramite suo id setta il flag "preferita" come status . */
  setPreferredStatus(p: Pratica, status: boolean, inBackground = false): Observable<Pratica> {
    if (!p?.id) {
      throw new Error('Invalid or empty p.id');
    }
    const options = inBackground ? {
      headers: { [ApiOptions.IN_BACKGROUND]: 'true' }
    } : undefined;

    if (status) {
      return this.http.put<Pratica>(
        ApiUrls.SET_PREFERITA_STATUS_BY_ID.replace('{idPratica}', '' + p.id), null, options);
    }
    else {
      return this.http.delete<Pratica>(
        ApiUrls.SET_PREFERITA_STATUS_BY_ID.replace('{idPratica}', '' + p.id), options);
    }
  }

  getPratica(id: number, annullata: boolean): Observable<Pratica> {
    if (!id) {
      throw new Error('Invalid or empty id');
    }
    const params = new HttpParams().set('annullata', String(annullata));
    return this.http.get<Pratica>(ApiUrls.GET_PRATICA.replace('{id}', id.toString()), { params });
  }

  // recupero delle attivita' legate a una pratica
  getElencoAttivitaPratica(idPratica: number | null): Observable<Attivita[]> {
    if (!idPratica) {
      throw new Error('id pratica non valorizzato');
    }
    return this.http.get<Attivita[]>(ApiUrls.GET_ATTIVITA_BY_ID_PRATICA.replace('{idPratica}', '' + idPratica));
  }

  getTask(idTask: string): Observable<Task> {
    if (!idTask) {
      throw new Error('Invalid or empty idTask');
    }
    return this.http.get<Task>(ApiUrls.GET_TASK.replace('{idTask}', idTask));
  }

  getAttivitaByTaskId(idTask: string): Observable<Attivita> {
    if (!idTask) {
      throw new Error('Invalid or empty idTask');
    }
    return this.http.get<Attivita>(ApiUrls.GET_ATTIVITA_BY_TASK_ID.replace('{idTask}', idTask));
  }

  getVisibilitaById(idPratica: number): Observable<Pratica> {
    const params = new HttpParams().append('idPratica', '' + idPratica);
    return this.http.get<Pratica>(ApiUrls.GET_VISIBILITA_BY_ID, { params });
  }

  getVisibilitaByTask(idTask: string): Observable<Pratica> {
    let params: HttpParams = new HttpParams();
    params = params.set('idTask', idTask);
    return this.http.get<Pratica>(ApiUrls.GET_VISIBILITA_BY_TASK, { params });
  }

  getInstance(idProcesso: number | string): Observable<PraticaExtResponse> {
    if (!idProcesso) {
      throw new Error('Invalid or empty idProcesso');
    }
    return this.http.get<PraticaExtResponse>(ApiUrls.RUNTIME_INSTANCES.replace('{idProcesso}', '' + idProcesso));
  }

  getForms(nome: string): Observable<StrutturaFormLogico> {
    if (!nome) {
      throw new Error('Invalid or empty nome');
    }
    return this.http.get<StrutturaFormLogico>(ApiUrls.GET_FORMS.replace('{nome}', nome));
  }

  getFormsAttivita(id: number | string): Observable<StrutturaFormLogico> {
    if (!id) {
      throw new Error('Invalid or empty nome');
    }
    return this.http.get<StrutturaFormLogico>(ApiUrls.GET_FORMS_ATTIVITA.replace('{id}', id + ''));
  }

  annullaPratica(id: number): Observable<HttpResponse<any>> {
    if (!id) {
      throw new Error('Invalid or empty id');
    }
    return this.http.delete(ApiUrls.RUNTIME_INSTANCES.replace('{idProcesso}', '' + id), { observe: 'response' });
  }

  getVariabiliProcesso(processInstanceId: string): Observable<VariabiliProcessoResponse> {
    if (!processInstanceId) {
      throw new Error('Invalid or empty processInstanceId');
    }
    return this.http.get<VariabiliProcessoResponse>(
      ApiUrls.PROC_INSTANCE_VARIABLES.replace('{processInstanceId}', processInstanceId));
  }

  getHistoryVariabiliProcesso(processInstanceId: string, includeProcessVariables: boolean): Observable<VariabiliProcessoResponse> {
    if (!processInstanceId) {
      throw new Error('Invalid or empty processInstanceId');
    }
    let params: HttpParams = new HttpParams();
    params = params.set('includeProcessVariables', includeProcessVariables.toString());
    return this.http.get<VariabiliProcessoResponse>(
      ApiUrls.HISTORIC_PROCESS_INSTACES.replace('{processInstanceId}', processInstanceId), { params });
  }

  salvaLavorazioneTask(
    idPratica: number,
    idAttivita: number,
    variabiliProcesso: VariabileProcesso[],
    lock?: Lock
  ): Observable<OperazioneAsincrona> {
    if (!idPratica) {
      throw new Error('Invalid or empty idPratica');
    }
    if (!idAttivita) {
      throw new Error('Invalid or empty idAttivita');
    }
    const payload: any = {
      variables: variabiliProcesso
    };
    return this.http.post<OperazioneAsincrona>
      (ApiUrls.POST_PRATICA_SALVA_TASK
        .replace('{idPratica}', '' + idPratica)
        .replace('{idAttivita}', '' + idAttivita),
        payload,
        {
          headers: this.resourceLockHeader(lock)
        });
  }

  confermaLavorazioneTask(
    idPratica: number,
    idAttivita: number,
    variabiliProcesso: VariabileProcesso[],
    lock?: Lock
  ): Observable<OperazioneAsincrona> {
    if (!idPratica) {
      throw new Error('Invalid or empty idPratica');
    }
    if (!idAttivita) {
      throw new Error('Invalid or empty idAttivita');
    }
    const payload: any = {
      variables: variabiliProcesso
    };
    return this.http.post<OperazioneAsincrona>
      (ApiUrls.POST_PRATICA_CONFERMA_TASK
        .replace('{idPratica}', '' + idPratica)
        .replace('{idAttivita}', '' + idAttivita),
        payload,
        {
          headers: this.resourceLockHeader(lock)
        });
  }

  putVariabiliProcesso(
    processInstanceId: string, variabiliProcesso: VariabileProcesso[], lock?: Lock
  ): Observable<VariabiliProcessoResponse> {
    if (!processInstanceId) {
      throw new Error('Invalid or empty processInstanceId');
    }
    return this.http.put<VariabiliProcessoResponse>
      (ApiUrls.PROC_INSTANCE_VARIABLES.replace('{processInstanceId}', processInstanceId), variabiliProcesso, {
        headers: this.resourceLockHeader(lock)
      });
  }

  getChecksumModifica(taskIds: (string | number)[]): Observable<string> {
    if (!taskIds?.length) {
      throw new Error('No task ids');
    }
    const calls: { [key: string]: Observable<Attivita> } = {};
    taskIds.forEach(id => calls['' + id] = this.getAttivitaByTaskId('' + id));

    return forkJoin(calls).pipe(map(attivitaMap => {

      return (Object.values(attivitaMap) as Attivita[])
        .sort((a1, a2) => (a1.id ?? 0) - (a2.id ?? 0))
        .map(a => a.id + '_' + (a.campiTecnici?.dtModifica ?? a.campiTecnici?.dtIniVal))
        .join(',');
    }));
  }

  private resourceLockHeader(lock?: Lock): { [key: string]: any } {
    const headers: { [key: string]: any } = {};
    if (lock?.codiceOwner) {
      headers['X-Resource-Lock'] = lock.codiceOwner;
    }
    return headers;
  }

  assegnaPratica(idPratica: number, idUtente?: number, idGruppo?: number, keepAssignement?: boolean) {
    Utils.require(idPratica, 'idPratica');
    Utils.require(idUtente ?? idGruppo, 'idUtente o idGruppo');

    const request: AssegnaAttivitaRequest = {
      esclusivo: true,
      mantieniAssegnazione: keepAssignement ?? false,
      assegnazioni: [
        idGruppo ? { idGruppo, tipologia: 'candidate' } :
          { idUtente, tipologia: 'assignee' }
      ]
    };

    return this.http.post(
      ApiUrls.POST_PRATICA_ASSEGNA
        .replace('{idPratica}', '' + idPratica),
      request
    );
  }

  condividiPratica(idPratica: number, request: CreaCondivisionePraticaRequest) {
    Utils.require(request, 'request');

    return this.http.post(
      ApiUrls.PRATICA_CONDIVISIONI
        .replace('{idPratica}', '' + idPratica),
      request
    );
  }

  eliminaCondivisionePratica(idPratica: number, idCondivisione: number) {
    Utils.require(idPratica, 'idPratica');
    Utils.require(idCondivisione, 'idCondivisione');

    return this.http.delete(
      ApiUrls.PRATICA_CONDIVISIONE
        .replace('{idPratica}', '' + idPratica)
        .replace('{idCondivisione}', '' + idCondivisione));
  }

  postElaborazionePratica(idPratica: number, specifica: string): Observable<object> {
    Utils.require(idPratica, 'idPratica');
    Utils.require(specifica, 'specifica');

    const payload: GetElaborazionePraticaRequest = {
      mappatura: specifica
    };

    return this.http.post<object>(
      ApiUrls.PRATICA_ELABORAZIONE
        .replace('{idPratica}', '' + idPratica), payload);
  }

  getContestoElaborazionePratica(idPratica: number): Observable<object> {
    Utils.require(idPratica, 'idPratica');

    return this.http.get<object>(
      ApiUrls.PRATICA_ELABORAZIONE_CONTESTO
        .replace('{idPratica}', '' + idPratica));
  }

  aggiornaPratica(idPratica: number, pratica: Pratica): Observable<Pratica> {
    Utils.require(idPratica, 'idPratica');

    return this.http.put<Pratica>(ApiUrls.UPDATE_PRATICA.replace('{idPratica}', idPratica.toString()), pratica);
  }


  getDiagram(idPratica: number): Observable<any> {

    const HTTPOptions: any = {
      headers: new HttpHeaders({
        'Cache-Control': 'no-cache, no-store, must-revalidate, post-check=0, pre-check=0',
        'Pragma': 'no-cache',
      }),
      responseType: 'blob',
    };

    return this.http.get<any>(ApiUrls.PRATICA_DIAGRAMMA.replace('{idPratica}', '' + idPratica), HTTPOptions);
  }


}
