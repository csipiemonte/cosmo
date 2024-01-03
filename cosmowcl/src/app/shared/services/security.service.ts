/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, ReplaySubject, of } from 'rxjs';
import { Utils } from '../utilities/utilities';
import { ApiUrls } from '../utilities/apiurls';
import { Constants } from '../constants/constants';
import { Profilazione } from '../models/user/profilazione';
import { NGXLogger } from 'ngx-logger';
import { LogoutResponse } from '../models/api/cosmo/logoutResponse';
import { finalize, map, share } from 'rxjs/operators';
import {v4 as uuidv4} from 'uuid';
import { UserInfo } from '../models/api/cosmo/userInfo';
import { UserInfoWrapper } from '../models/user/user-info';

@Injectable({providedIn: 'root'})
export class SecurityService {

  private principal!: UserInfoWrapper;

  private principalSubject = new ReplaySubject<UserInfoWrapper>(1);
  public principal$: Observable<UserInfoWrapper> = this.principalSubject.asObservable();

  private pendingFetch: Observable<UserInfoWrapper> | null = null;

  private localSessionKey = uuidv4();

  constructor(
    private logger: NGXLogger,
    private http: HttpClient) {
    // NOP
    this.logger.info('creo security service');
  }

  public getSessionKey(): Observable<string> {
    return this.getCurrentUser().pipe(
      map(user => {
        return (user?.codiceFiscale ?? 'GUEST') + '/' + this.localSessionKey;
      })
    );
  }

  public getCurrentUser(): Observable<UserInfoWrapper> {
    if (this.principal != null) {
      this.logger.info('cache hit for principal');
      return of(this.principal);
    }
    this.logger.info('cache miss for principal, fetching from BE');
    return this.fetchCurrentUser();
  }

  public fetchCurrentUser(force = false): Observable<UserInfoWrapper> {
    if (this.pendingFetch) {
      if (!force) {
        return this.pendingFetch;
      }
    }

    this.pendingFetch = this.fetchUserFromBackend().pipe(
      share(),
      map(principal => {
        this.logger.debug('fetched principal', principal);
        this.principal = principal;
        this.principalSubject.next(this.principal);
        return principal;
      }),
      finalize(() => this.pendingFetch = null)
    );

    return this.pendingFetch;
  }

  private fetchUserFromBackend(): Observable<UserInfoWrapper> {
    const service = this;
    const url = ApiUrls.SESSIONE;
    const headers: { [key: string]: any } = {};
    this.logger.trace('Fetching user from BE');
    headers[Constants.X_COSMO_SESSION_MANAGEMENT] = 'true';
    this.logger.trace('Session management header set');
    return this.http.get<UserInfo>(url, { headers }).pipe(
      map(raw => {
        const response = service.postProcessServerResponse(raw);
        service.principal = response;
        this.logger.info('loaded principal from server', this.principal);
        service.principalSubject.next(this.principal);
        return service.principal;
      })
    );
  }

  private postProcessServerResponse(response: UserInfo): UserInfoWrapper {
    const profilazione: Profilazione = {
      hasValidRole: Utils.hasValidProfile(response),
      profili: {},
      useCase: {}
    };

    if (response.profilo?.codice) {
      profilazione.profili[response.profilo.codice] = true;

      response.profilo?.useCases?.forEach(useCase => {
        if (useCase.codice) {
          profilazione.useCase[useCase.codice] = true;
        }
      });
    }

    return {
      ...response,
      anonimo: response.anonimo ?? true,
      nome: Utils.require(response.nome, 'nome'),
      cognome: Utils.require(response.cognome, 'cognome'),
      codiceFiscale: Utils.require(response.codiceFiscale, 'codiceFiscale'),
      profilazione
    };
  }

  /**
   * effettua l'invalidazione della sessione applicativa, sia sul
   * client che sul server, richiamando un apposito servizio di backend
   */
  localLogout(ambienteLogout: string): Observable<LogoutResponse> {
    let params: HttpParams = new HttpParams();
    params = params.set('ambienteLogout', ambienteLogout);
    return this.http.delete<LogoutResponse>(ApiUrls.SESSIONE, { params });
  }

  sessioneScaduta(): Observable<UserInfo> {
    // in caso di sessione scaduta devo ricaricare l'utente e tornare al login
    return this.fetchCurrentUser();
  }

  hasUseCases(providedUseCases: string[], anyEnough: boolean): Observable<boolean> {
    return this.getCurrentUser().pipe(
      map((userInfo) => {
      if (Utils.hasValidProfile(userInfo)) {
        return anyEnough ? providedUseCases.some((useCaseCode: string) => {
          return (userInfo?.profilo?.useCases?.findIndex((userUseCase) => {
            return userUseCase?.codice?.trim() === useCaseCode;
          }) ?? -1) >= 0;
        }) : providedUseCases.every((useCaseCode: string) => {
          return (userInfo?.profilo?.useCases?.findIndex((userUseCase) => {
            return userUseCase?.codice?.trim() === useCaseCode;
          }) ?? -1) >= 0;
        });
      } else {
        return false;
      }
    }));
  }
}

