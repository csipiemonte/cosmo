/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { ErrorService } from './error.service';
import { TranslateService } from '@ngx-translate/core';
import { ApiUrls } from '../utilities/apiurls';
import { map } from 'rxjs/operators';
import { NGXLogger } from 'ngx-logger';
import { UserInfo } from '../models/api/cosmo/userInfo';
import { Utente } from '../models/api/cosmoauthorization/utente';
import { UtenteResponse } from '../models/api/cosmoauthorization/utenteResponse';
import { Utils } from '../utilities/utilities';


@Injectable({ providedIn: 'root' })
export class LoginService {

  accessoDirettoSubject = new BehaviorSubject<boolean>(false);

  constructor(
    private logger: NGXLogger,
    private http: HttpClient) {
    // NOP
    this.logger.debug('creo login service');
  }

  getProfilazioneUtenteCorrente(): Observable<Utente> {
    return this.http.get<UtenteResponse>(ApiUrls.GET_UTENTE_CORRENTE).pipe(
      // tslint:disable-next-line: no-non-null-assertion
      map(response => response.utente!));
  }

  selezionaEnteProfilo(idEnte: number, idProfilo: number): Observable<UserInfo> {
    return this.http.post<UserInfo>(ApiUrls.SESSIONE, {
      idEnte: Utils.require(idEnte, 'idEnte'),
      idProfilo: Utils.require(idProfilo, 'idProfilo'),
    });
  }

  selezionaProfilo(idProfilo: number): Observable<UserInfo> {
    return this.http.post<UserInfo>(ApiUrls.SESSIONE, {
      idEnte: null,
      idProfilo: Utils.require(idProfilo, 'idProfilo'),
    });
  }

  isAccessoDiretto(): Observable<boolean> {
    return this.accessoDirettoSubject.asObservable();
  }

  setAccessoDiretto(accessoDiretto: boolean) {
    this.accessoDirettoSubject.next(accessoDiretto);
  }

}
