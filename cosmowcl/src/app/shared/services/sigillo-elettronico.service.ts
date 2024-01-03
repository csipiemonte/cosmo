/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';
import { CredenzialiSigilloElettronicoResponse } from '../models/api/cosmoecm/credenzialiSigilloElettronicoResponse';
import { CredenzialiSigilloElettronico } from '../models/api/cosmoecm/credenzialiSigilloElettronico';
import { CreaCredenzialiSigilloElettronicoRequest } from '../models/api/cosmoecm/creaCredenzialiSigilloElettronicoRequest';

@Injectable()
export class SigilloElettronicoService {

  constructor( private http: HttpClient) { }

    /** GET: Restituisce un sigillo elettronico. */
    getSigilloElettronicoId(id: number): Observable<CredenzialiSigilloElettronico> {
      Utils.require(id);

      return this.http.get<CredenzialiSigilloElettronico>(ApiUrls.SIGILLO_ELETTRONICO_ID.replace('{id}', id.toString()));
    }

    /** GET: Restituisce una lista di sigilli elettronici. */
    getSigilloElettronico(filter: string): Observable<CredenzialiSigilloElettronicoResponse> {
      const options = filter ?
        { params: new HttpParams().set('filter', filter) } : {};
      // return this.http.get<SigilloElettronico>(ApiUrls.SIGILLO_ELETTRONICO,options);
      return this.http.get<CredenzialiSigilloElettronicoResponse>(ApiUrls.SIGILLO_ELETTRONICO, options);
    }

    /** DELETE: Elimina un sigillo elettronico. */
    deleteSigilloElettronico(id: number): Observable<CredenzialiSigilloElettronico> {
      Utils.require(id);

      return this.http.delete<CredenzialiSigilloElettronico>(ApiUrls.SIGILLO_ELETTRONICO_ID.replace('{id}', id.toString()));
    }

    /**
     * POST: Salva un sigillo elettronico.
     *
     * @param request CreaCredenzialiSigilloElettronicoRequest con le credenziali da salvare.
     * @returns Observable<CredenzialiSigilloElettronico> il sigillo elettronico salvato.
     */
    creaSigilloElettronico(request: CreaCredenzialiSigilloElettronicoRequest): Observable<CredenzialiSigilloElettronico> {
      Utils.require(request);

      return this.http.post<CredenzialiSigilloElettronico>(ApiUrls.SIGILLO_ELETTRONICO, request);
    }

    /**
     * PUT: Aggiorna un sigillo elettronico.
     *
     * @param request CreaCredenzialiSigilloElettronicoRequest da aggiornare.
     * @returns Observable<CredenzialiSigilloElettronico> il sigillo elettronico aggiornato.
     */

    aggiornaSigilloElettronico(id: number, request: CreaCredenzialiSigilloElettronicoRequest): Observable<CredenzialiSigilloElettronico> {
      Utils.require(id);
      Utils.require(request);

      return this.http.put<CredenzialiSigilloElettronico>(ApiUrls.SIGILLO_ELETTRONICO_ID.replace('{id}', id.toString()), request);
    }
}
