/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { ApiUrls } from '../utilities/apiurls';
import { ParametriResponse } from '../models/funzionalita/parametri-response.model';
import { map, share, tap } from 'rxjs/operators';
import { Utils } from '../utilities/utilities';
import { NGXLogger } from 'ngx-logger';


@Injectable()
export class ConfigurazioniService {

  private getObservable?: Observable<ParametriResponse>;
  private parametriCache?: {value: ParametriResponse, time: Date};

  constructor(private http: HttpClient, private logger: NGXLogger) {
    // NOP
  }

  /**
   * GET: Restituisce la configurazione per il parametro passato in input
   *
   *
   */
  getConfigurazioneByChiave(chiave: string): Observable<string | undefined> {
    return this.getConfigurazione().pipe(
      map(confResp => {
        const ret = confResp.parametri.find(param =>
          param.chiave === chiave
        )?.valore;

        if (!Utils.isDefined(ret)) {
          this.logger.warn('required parameter was not found in cache', chiave);
        }

        return ret;
      })
    );
  }

  private getConfigurazione(): Observable<ParametriResponse> {
    if (this.getObservable) {
      return this.getObservable;
    }

    // expires in: 30 min
    const expireDate = new Date(new Date().getTime() + 1000 * 60 * 30);
    if (this.parametriCache?.value && this.parametriCache?.time && this.parametriCache.time < expireDate) {
      return of(this.parametriCache.value);
    }

    this.getObservable = this.http.get<ParametriResponse>(ApiUrls.GET_CONFIGURAZIONE_PARAMETRO).pipe(
      tap(value => {
        this.parametriCache = {value, time: new Date()};
        this.getObservable = undefined;
      }),
      share(),
    );

    return this.getObservable;
  }

}
