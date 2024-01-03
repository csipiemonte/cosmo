/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TipoPratica } from 'src/app/shared/models/api/cosmopratiche/tipoPratica';
import { Observable } from 'rxjs';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';
import { Utils } from 'src/app/shared/utilities/utilities';
import { CreaTipoPraticaRequest } from 'src/app/shared/models/api/cosmopratiche/creaTipoPraticaRequest';
import { AggiornaTipoPraticaRequest } from 'src/app/shared/models/api/cosmopratiche/aggiornaTipoPraticaRequest';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { TipiPraticheResponse } from 'src/app/shared/models/api/cosmopratiche/tipiPraticheResponse';

@Injectable({
  providedIn: 'root',
})
export class GestioneTipiPraticheService {

  constructor(private http: HttpClient) {
    // NOP
  }

/** GET: Restituisce tutte le tipologie pratiche  */
  listTipiPraticaByEnte(idEnte?: number, creazionePratica?: boolean, withEnte?: boolean): Observable<TipoPratica[]> {
    let params: HttpParams = new HttpParams();
    if (idEnte){
      params = params.set('idEnte', idEnte.toString());
    }
    if (typeof creazionePratica  !== 'undefined' ){
      params = params.set('creazionePratica', creazionePratica.toString());
    }
    if (withEnte){
      params = params.set('conEnte', withEnte.toString());
    }
    return this.http.get<TipoPratica[]>(ApiUrls.TIPI_PRATICHE_PER_ENTE, { params });
  }



  /** GET: Restituisce una lista pratiche with filters */
  getTipiPratica(filter: string): Observable<TipiPraticheResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<TipiPraticheResponse>(ApiUrls.TIPI_PRATICHE, options);
  }


  get(codice: string): Observable<TipoPratica> {
    Utils.require(codice, 'codice');

    return this.http.get<TipoPratica>(
      ApiUrls.TIPO_PRATICA.replace('{codice}', codice)
    );
  }

  create(payload: CreaTipoPraticaRequest): Observable<TipoPratica> {
    Utils.require(payload, 'payload');

    return this.http.post<TipoPratica>(
      ApiUrls.TIPI_PRATICHE, payload
    );
  }

  update(codice: string, payload: AggiornaTipoPraticaRequest): Observable<TipoPratica> {
    Utils.require(codice, 'codice');
    Utils.require(payload, 'payload');

    return this.http.put<TipoPratica>(
      ApiUrls.TIPO_PRATICA.replace('{codice}', codice),
      payload
    );
  }

  delete(codice: string): Observable<void> {
    Utils.require(codice, 'codice');

    return this.http.delete<void>(
      ApiUrls.TIPO_PRATICA.replace('{codice}', codice)
    );
  }

  getOpzioniTenantPerEsportazione(codice: string): Observable<Ente[]> {
    Utils.require(codice, 'codice');

    return this.http.get<Ente[]>(
      ApiUrls.ESPORTA_TIPO_PRATICA_OPZIONI_TENANT.replace('{codice}', codice),
      {
        params: {
          codiceTipoPratica: codice
        }
      }
    );
  }

  esporta(codiceTipoPratica: string, tenantId: string): Observable<any> {
    Utils.require(codiceTipoPratica, 'codiceTipoPratica');
    Utils.require(tenantId, 'tenantId');

    return this.http.post<any>(
      ApiUrls.ESPORTA_TIPO_PRATICA,
      {
        codiceTipoPratica,
        tenantId
      }
    );
  }

}
