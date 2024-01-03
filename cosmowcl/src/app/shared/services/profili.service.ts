/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProfiliResponse } from '../models/api/cosmoauthorization/profiliResponse';
import { Profilo } from '../models/api/cosmoauthorization/profilo';
import { ProfiloResponse } from '../models/api/cosmoauthorization/profiloResponse';
import { UseCase } from '../models/api/cosmoauthorization/useCase';
import { ApiUrls } from '../utilities/apiurls';

@Injectable()
export class ProfiliService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  /** GET: Restituisce un profilo. */
  getProfilo(id: number): Observable<ProfiloResponse> {
    return this.http.get<ProfiloResponse>(ApiUrls.GET_PROFILO.replace('{id}', id.toString()));
  }

  /** GET: Restituisce una lista di profili. */
  getProfili(filter: string): Observable<ProfiliResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<ProfiliResponse>(ApiUrls.PROFILI, options);

  }

  /**
   * POST: Salva un profilo.
   *
   * @param profilo profilo da salvare.
   * @returns Observable<Profilo> il profilo salvato.
   */

  salvaProfilo(profilo: Profilo): Observable<ProfiloResponse> {
    return this.http.post<ProfiloResponse>(ApiUrls.PROFILI, profilo);
  }

  /**
   * PUT: Aggiorna un profilo.
   *
   * @param profilo profilo da aggiornare.
   * @returns Observable<Profilo> il profilo aggiornato.
   */

  aggiornaProfilo(id: number, profilo: Profilo): Observable<ProfiloResponse> {
    return this.http.put<ProfiloResponse>(ApiUrls.GET_PROFILO.replace('{id}', id.toString()), profilo);
  }

  /** GET: Restituisce un profilo. */
  deleteProfilo(id: number): Observable<ProfiloResponse> {
    return this.http.delete<ProfiloResponse>(ApiUrls.GET_PROFILO.replace('{id}', id.toString()));
  }

}
