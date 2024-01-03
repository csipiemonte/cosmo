/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CodicePagina } from '../models/api/cosmonotifications/codicePagina';
import { CodiceTab } from '../models/api/cosmonotifications/codiceTab';
import { Helper } from '../models/api/cosmonotifications/helper';
import { HelperResponse } from '../models/api/cosmonotifications/helperResponse';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';
import { DecodificaHelper } from '../models/api/cosmonotifications/decodificaHelper';
import { HelperImportRequest } from '../models/api/cosmonotifications/helperImportRequest';
import { HelperImportResult } from '../models/api/cosmonotifications/helperImportResult';
import { CodiceModale } from '../models/api/cosmonotifications/codiceModale';
import { ActivatedRoute } from '@angular/router';

@Injectable()
export class HelperService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  /** GET: Restituisce una lista di helper */
  getHelpers(filter: string): Observable<HelperResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<HelperResponse>(ApiUrls.HELPERS, options);
  }

  get(id: number): Observable<Helper> {
    Utils.require(id);
    return this.http.get<Helper>(ApiUrls.HELPER.replace('{id}', id.toString()));
  }

  getCodiciPagina(): Observable<CodicePagina[]> {
    return this.http.get<CodicePagina[]>(ApiUrls.GET_CODICI_PAGINA);
  }

  getCodiciTab(codice: string): Observable<CodiceTab[]> {
    return this.http.get<CodiceTab[]>(ApiUrls.GET_CODICI_TAB.replace('{codice}', encodeURIComponent(codice)));
  }

  getCodiciModale(filter: string): Observable<CodiceModale[]> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<CodiceModale[]>(ApiUrls.GET_CODICI_MODALE, options);
  }

  update(id: number, payload: Helper): Observable<Helper> {
    Utils.require(id);
    return this.http.put<Helper>(ApiUrls.HELPER.replace('{id}', id.toString()), payload );
  }

  create(request: Helper): Observable<Helper> {
    Utils.require(request);
    return this.http.post<Helper>(ApiUrls.HELPERS, request);
  }

  delete(id: number): Observable<any> {
    Utils.require(id);
    return this.http.delete<any>(ApiUrls.HELPER.replace('{id}', id.toString()));
  }

  getDecodifica(pagina: string, tab: string, form: string): Observable<DecodificaHelper> {
    Utils.require(pagina);
    const params: any = {pagina, tab, form};
    return this.http.get<DecodificaHelper>(ApiUrls.GET_DECODIFICA, {params});
  }

  esporta(id: number): Observable<any> {
    Utils.require(id, 'id');
    return this.http.get<any>(ApiUrls.ESPORTA_HELPER.replace('{id}', id.toString()));
  }

  importa(req: HelperImportRequest): Observable<HelperImportResult> {
    Utils.require(req);
    return this.http.post<HelperImportResult>(ApiUrls.IMPORTA_HELPER, req);
  }

  public searchHelperRef(route: ActivatedRoute) {

    /* ad oggi ci sono 3 livelli di profondita' delle route
           *  1) pagina root
           *  2) children dichiarati nel routing direttamente
           *  3) children dichiarati tramite loadChildren con passaggio su modulo dedicato
           *  In base a questi livelli ricerco il route corretto su cui instradare la ricerca dell' helper
           *  aggiornamento: con l'inserimento della logica legata agli helper sui modali, si effettua un controllo
           *  preliminare sulla route, in quanto alcuni modali risiedono all'interno di template che non hanno un
           *  percorso specifico
           */
    if (!route.firstChild) {
      return route;
    }
    if (route.firstChild?.firstChild?.firstChild) {
     return route.firstChild?.firstChild?.firstChild;
   } else if (route.firstChild?.firstChild) {
     return route.firstChild?.firstChild;
   } else {
     return route.firstChild;
   }
 }

}
