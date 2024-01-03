/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AggiornaParametroDiSistemaRequest } from '../models/api/cosmoauthorization/aggiornaParametroDiSistemaRequest';
import { ParametroDiSistema } from '../models/api/cosmoauthorization/parametroDiSistema';
import { ParametroDiSistemaResponse } from '../models/api/cosmoauthorization/parametroDiSistemaResponse';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable({
  providedIn: 'root'
})
export class GestioneParametriDiSistemaService {

  constructor(
    private http: HttpClient
  ) { }

  /** GET: Restituisce una lista di parametri di sistema. */
  getParametriDiSistema(filter: string): Observable<ParametroDiSistemaResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<ParametroDiSistemaResponse>(ApiUrls.PARAMETRI_DI_SISTEMA, options);
  }

  /** DELETE: Elimina un paraemtro di sistema. */
  deleteParametroDiSistema(chiave: string): Observable<void> {
    Utils.require(chiave);
    return this.http.delete<void>(ApiUrls.PARAMETRO_DI_SISTEMA.replace('{chiave}', chiave));
  }

  /** GET: Restituisce un parametro di sistema. */
  getParametroDiSistema(chiave: string): Observable<ParametroDiSistema> {
    Utils.require(chiave);
    return this.http.get<ParametroDiSistema>(ApiUrls.PARAMETRO_DI_SISTEMA.replace('{chiave}', chiave));
  }

  /**
   * POST: Salva un Parametro di Sistema.
   *
   * @param parametroDiSistema parametro di sistema da salvare.
   * @returns Observable<ParametroDiSistema> il parametro di sistema salvato.
   */
  create(request: ParametroDiSistema): Observable<ParametroDiSistema> {
    Utils.require(request);
    return this.http.post<ParametroDiSistema>(ApiUrls.PARAMETRI_DI_SISTEMA, request);
  }

  /**
   * PUT: Aggiorna un Parametro di Sistema.
   *
   * @param parametroDiSistema Parametro di Sistema da aggiornare.
   * @returns Observable<ParametroDiSistema> il Parametro di Sistema aggiornato.
   */
  update(chiave: string, request: AggiornaParametroDiSistemaRequest): Observable<ParametroDiSistema> {
    Utils.require(chiave);
    Utils.require(request);
    return this.http.put<ParametroDiSistema>(ApiUrls.PARAMETRO_DI_SISTEMA.replace('{chiave}', chiave), request );
  }

}
