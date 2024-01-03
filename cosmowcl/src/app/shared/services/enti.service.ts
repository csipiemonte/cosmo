/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AggiornaEnteRequest } from '../models/api/cosmoauthorization/aggiornaEnteRequest';
import { CreaEnteRequest } from '../models/api/cosmoauthorization/creaEnteRequest';
import { EnteResponse } from '../models/api/cosmoauthorization/enteResponse';
import { EntiResponse } from '../models/api/cosmoauthorization/entiResponse';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable()
export class EntiService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  /** GET: Restituisce un ente. */
  getEnte(id: number): Observable<EnteResponse> {
    Utils.require(id);

    return this.http.get<EnteResponse>(ApiUrls.GET_DELETE_ENTE.replace('{id}', id.toString()));
  }

  /** GET: Restituisce una lista di enti. */
  getEnti(filter: string): Observable<EntiResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<EntiResponse>(ApiUrls.ENTI, options);
  }

  /** DELETE: Elimina un ente. */
  deleteEnte(id: number): Observable<EnteResponse> {
    Utils.require(id);

    return this.http.delete<EnteResponse>(ApiUrls.GET_DELETE_ENTE.replace('{id}', id.toString()));
  }

  /**
   * POST: Salva un Ente.
   *
   * @param ente Ente da salvare.
   * @returns Observable<Ente> il ente salvato.
   */
  creaEnte(request: CreaEnteRequest): Observable<EnteResponse> {
    Utils.require(request);

    return this.http.post<EnteResponse>(ApiUrls.ENTI, request);
  }

  /**
   * PUT: Aggiorna un ente.
   *
   * @param ente Ente da aggiornare.
   * @returns Observable<Ente> l'ente aggiornato.
   */

  aggiornaEnte(id: number, request: AggiornaEnteRequest): Observable<EnteResponse> {
    Utils.require(id);
    Utils.require(request);

    return this.http.put<EnteResponse>(ApiUrls.GET_DELETE_ENTE.replace('{id}', id.toString()), request);
  }
}
