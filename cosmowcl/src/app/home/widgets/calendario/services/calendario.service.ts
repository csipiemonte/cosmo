/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { HttpParams, HttpClient } from '@angular/common/http';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';
import { Observable } from 'rxjs';
import { PaginaTask } from 'src/app/shared/models/api/cosmobusiness/paginaTask';
import { Evento } from 'src/app/shared/models/api/cosmobusiness/evento';
export enum TipoAssegnazioneEvento {
  UTENTE_CORRENTE = 'UTENTE_CORRENTE',
  GRUPPO = 'GRUPPO'
}

const TASK_SPAN = {
  start: -7,

};

@Injectable({
  providedIn: 'root'
})
export class CalendarioService {

  constructor(
    private http: HttpClient
  ) { }

  /*
   * Metodo che recupera tutti gli eventi assegnati a un utente o a un gruppo nel range di un mese
   */
  recuperaEventiAssegnati(cfUtente: string, mese: number, anno: number, size: number, start: number): Observable<PaginaTask> {
    const date = new Date(((mese + 1) + ' 01 ' + anno ));
    const dueBefore = new Date(date);
    dueBefore.setDate(date.getDate() + 38);

    const dueAfter = new Date(date);
    dueAfter.setDate(date.getDate() - 7);

    let params: HttpParams = new HttpParams();

    params = params.set('involvedUser', cfUtente);
    params = params.set('dueAfter', dueAfter.toISOString().replace('Z', '0').replace('.', '+'));
    params = params.set('sort', 'dueDate');

    params = params.set('dueBefore', dueBefore.toISOString().replace('Z', '0').replace('.', '+'));

    params = params.set('size', size.toString());
    params = params.set('start', start.toString());


    return this.http.get<PaginaTask>(ApiUrls.GET_EVENTI, {params});
  }

  /*
   * Metodo che recupera i prossimi 'size' eventi assegnati a un utente o a un gruppo
   * size = recuperato da configurazione su db
   */
  recuperaProssimiEventiAssegnati(cfUtente: string, mese: number, anno: number, size: number, start: number): Observable<PaginaTask> {
    const date = new Date(((mese + 1) + ' 01 ' + anno ));

    // const dueAfter = new Date(date);
    // dueAfter.setDate(date.getDate());

    const dueAfter = new Date();
    dueAfter.setDate(dueAfter.getDate() - 1);
    let params: HttpParams = new HttpParams();

    params = params.set('involvedUser', cfUtente);
    params = params.set('dueAfter', dueAfter.toISOString().replace('Z', '0').replace('.', '+'));
    params = params.set('sort', 'dueDate');

    params = params.set('size', size.toString());
    params = params.set('start', start.toString());


    return this.http.get<PaginaTask>(ApiUrls.GET_EVENTI, {params});
  }

  /*
   * metodo per inserire un nuovo evento sul database
   */
  inserisciNuovoEvento( evento: Evento  ): Observable<Evento>  {
    return this.http.post<Evento>( ApiUrls.CREATE_NEW_EVENTO, evento);
  }

  modificaEvento(idEvento: string, evento: Evento  ): Observable<Evento>  {
    return this.http.put<Evento>( ApiUrls.UPDATE_DELETE_NEW_EVENTO.replace('{idEvento}', idEvento), evento);
  }

  eliminaEvento(idEvento: string ): Observable<Evento>  {
    return this.http.delete<Evento>( ApiUrls.UPDATE_DELETE_NEW_EVENTO.replace('{idEvento}', idEvento));
  }


  // valorizzazione dei request param comuni
  private getCommonHttpParams(cfUtente: string, date: Date): HttpParams {
    const dueAfter = new Date(date);
    dueAfter.setDate(date.getDate() - 7);

    let params: HttpParams = new HttpParams();

    params = params.set('involvedUser', cfUtente);
    params = params.set('dueAfter', dueAfter.toISOString().replace('Z', '0').replace('.', '+'));
    params = params.set('sort', 'dueDate');

    return params;
  }
}
