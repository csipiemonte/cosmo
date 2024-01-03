/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ApplicazioneEsterna } from '../models/api/cosmoauthorization/applicazioneEsterna';
import { ApplicazioneEsternaConValidita } from '../models/api/cosmoauthorization/applicazioneEsternaConValidita';
import { FunzionalitaApplicazioneEsternaConValidita } from '../models/api/cosmoauthorization/funzionalitaApplicazioneEsternaConValidita';
import { ApiUrls } from '../utilities/apiurls';

@Injectable({ providedIn: 'root' })
export class AppEsterneService {

  constructor(
    private http: HttpClient) { }

  private showMenuSubject = new BehaviorSubject<boolean>(true);
  private reloadMenuSubject = new BehaviorSubject<boolean>(false);

  getShowMenu(): Observable<boolean> {
    return this.showMenuSubject.asObservable();
  }

  setShowMenu(showMenu: boolean) {
    this.showMenuSubject.next(showMenu);
  }

  getReloadMenu(): Observable<boolean> {
    return this.reloadMenuSubject.asObservable();
  }

  setReloadMenu(reloadMenu: boolean) {
    this.reloadMenuSubject.next(reloadMenu);
  }

  getAppEsterna(id: number): Observable<ApplicazioneEsterna> {
    return this.http.get<ApplicazioneEsterna>(ApiUrls.APP_ESTERNA.replace('{idApp}', id.toString()));
  }

  getAppEsternaDaAssociare(id: number): Observable<ApplicazioneEsternaConValidita> {
    return this.http.get<ApplicazioneEsternaConValidita>(ApiUrls.APP_PER_ASSOCIAZIONE_ENTE.replace('{idApp}', id.toString()));
  }

  getAppEsterne(configurate: boolean): Observable<ApplicazioneEsterna[]> {
    let params: HttpParams = new HttpParams();
    params = params.set('configurata', configurate.toString());

    return this.http.get<ApplicazioneEsterna[]>(ApiUrls.APP_ESTERNE, { params });
  }

  getAppAssociateEnte(enteAssociato: boolean): Observable<ApplicazioneEsternaConValidita[]> {
    let params: HttpParams = new HttpParams();
    params = params.set('enteAssociato', enteAssociato.toString());
    return this.http.get<ApplicazioneEsternaConValidita[]>(ApiUrls.ASSOCIAZIONE_APP_ENTE, { params });
  }

  getAllApp(): Observable<ApplicazioneEsternaConValidita[]> {
    return this.http.get<ApplicazioneEsternaConValidita[]>(ApiUrls.APP_ESTERNE_ALL);
  }

  getFunzionalita(idApp: number): Observable<FunzionalitaApplicazioneEsternaConValidita[]> {
    return this.http.get<FunzionalitaApplicazioneEsternaConValidita[]>(ApiUrls.FUNZIONALITA.replace('{idApp}', idApp.toString()));
  }

  getSingolaFunzionalita(idApp: number, idFunzionalita: number): Observable<FunzionalitaApplicazioneEsternaConValidita> {
    return this.http.get<FunzionalitaApplicazioneEsternaConValidita>(ApiUrls.SINGOLA_FUNZIONALITA.replace('{idApp}', idApp.toString())
      .replace('{idFunzionalita}', idFunzionalita.toString()));
  }

  associaDissociaAppUtente(appEsterne: ApplicazioneEsterna[]): Observable<ApplicazioneEsterna[]> {
    return this.http.put<ApplicazioneEsterna[]>(ApiUrls.ASSOCIAZIONE_APP_UTENTE, appEsterne);
  }

  associaDissociaAppEnte(id: number, appEsterne: ApplicazioneEsternaConValidita): Observable<ApplicazioneEsternaConValidita> {
    return this.http.put<ApplicazioneEsternaConValidita>(ApiUrls.APP_PER_ASSOCIAZIONE_ENTE.replace('{idApp}', id.toString()), appEsterne);
  }

  eliminaApplicazione(id: number): Observable<void> {
    return this.http.delete<void>(ApiUrls.APP_ESTERNA.replace('{idApp}', id.toString()));
  }

  eliminaApplicazioneAssociata(id: number): Observable<void> {
    return this.http.delete<void>(ApiUrls.APP_PER_ASSOCIAZIONE_ENTE.replace('{idApp}', id.toString()));
  }

  eliminaFunzionalita(idApp: number, idFunzionalita: number): Observable<void> {
    return this.http.delete<void>(ApiUrls.SINGOLA_FUNZIONALITA.replace('{idApp}', idApp.toString())
      .replace('{idFunzionalita}', idFunzionalita.toString()));
  }

  aggiornaFunzionalita(idApp: number, idFunzionalita: number, funzionalita: FunzionalitaApplicazioneEsternaConValidita):
    Observable<FunzionalitaApplicazioneEsternaConValidita> {
    return this.http.put<FunzionalitaApplicazioneEsternaConValidita>(ApiUrls.SINGOLA_FUNZIONALITA.replace('{idApp}', idApp.toString())
      .replace('{idFunzionalita}', idFunzionalita.toString()), funzionalita);
  }

  salvaFunzionalita(idApp: number, funzionalita: FunzionalitaApplicazioneEsternaConValidita):
    Observable<FunzionalitaApplicazioneEsternaConValidita> {
    return this.http.post<FunzionalitaApplicazioneEsternaConValidita>(ApiUrls.FUNZIONALITA.replace('{idApp}', idApp.toString()),
      funzionalita);
  }

  aggiornaApplicazione(idApp: number, applicazione: ApplicazioneEsterna):
    Observable<ApplicazioneEsterna> {
    return this.http.put<ApplicazioneEsterna>(ApiUrls.APP_ESTERNA.replace('{idApp}', idApp.toString()), applicazione);
  }

  salvaApplicazione(applicazione: ApplicazioneEsterna):
    Observable<ApplicazioneEsterna> {
    return this.http.post<ApplicazioneEsterna>(ApiUrls.APP_ESTERNE, applicazione);
  }
}
