/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FormatoVariabileDiFiltro } from '../models/api/cosmopratiche/formatoVariabileDiFiltro';
import { TipoFiltro } from '../models/api/cosmopratiche/tipoFiltro';
import { VariabileDiFiltro } from '../models/api/cosmopratiche/variabileDiFiltro';
import { VariabiliDiFiltroResponse } from '../models/api/cosmopratiche/variabiliDiFiltroResponse';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable({
    providedIn: 'root',
})
export class VariabiliDiFiltroService {

  constructor( private http: HttpClient) { }


   getVariabiliDiFiltro(filter: string): Observable<VariabiliDiFiltroResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<VariabiliDiFiltroResponse>(ApiUrls.VARIABILI_DI_FILTRO, options);
  }



  getVariabileDiFiltro(idVariabile: number): Observable<VariabileDiFiltro> {
    Utils.require(idVariabile);
    return this.http.get<VariabileDiFiltro>(ApiUrls.VARIABILE_DI_FILTRO.replace('{idVariabile}', idVariabile.toString()));
  }

  getVariabileDiFiltroTipoPratica(codice: string): Observable<VariabileDiFiltro[]> {
    Utils.require(codice);
    return this.http.get<VariabileDiFiltro[]>(ApiUrls.VARIABILE_DI_FILTRO_TIPO_PRATICA.replace('{codice}', codice));
  }


  delete(idVariabile: number): Observable<void> {
    Utils.require(idVariabile);
    return this.http.delete<void>(ApiUrls.VARIABILE_DI_FILTRO.replace('{idVariabile}', idVariabile.toString()));
  }

  update(idVariabile: number, payload: VariabileDiFiltro): Observable<VariabileDiFiltro>{
    Utils.require(idVariabile);
    Utils.require(payload);
    return this.http.put<VariabileDiFiltro>(ApiUrls.VARIABILE_DI_FILTRO.replace('{idVariabile}', idVariabile.toString()), payload);
  }

  create(payload: VariabileDiFiltro): Observable<VariabileDiFiltro>{
    Utils.require(payload);
    return this.http.post<VariabileDiFiltro>(ApiUrls.VARIABILI_DI_FILTRO, payload);
  }

  getFormatiVariabili(): Observable<FormatoVariabileDiFiltro[]> {
    return this.http.get<FormatoVariabileDiFiltro[]>(ApiUrls.FORMATI_VARIABILI_DI_FILTRO);
  }

  getTipiFiltriVariabili(): Observable<TipoFiltro[]> {
    return this.http.get<FormatoVariabileDiFiltro[]>(ApiUrls.TIPI_FILTRO_VARIABILI_DI_FILTRO);
  }

}
