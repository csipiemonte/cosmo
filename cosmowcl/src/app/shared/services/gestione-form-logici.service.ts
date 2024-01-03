/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FormLogiciResponse } from 'src/app/shared/models/api/cosmobusiness/formLogiciResponse';
import { FormLogico } from 'src/app/shared/models/api/cosmobusiness/formLogico';
import { IstanzeFormLogiciResponse } from 'src/app/shared/models/api/cosmobusiness/istanzeFormLogiciResponse';
import { StrutturaFormLogico } from 'src/app/shared/models/api/cosmopratiche/strutturaFormLogico';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';
import { Utils } from 'src/app/shared/utilities/utilities';
import { AggiornaFormLogicoRequest } from '../models/api/cosmobusiness/aggiornaFormLogicoRequest';
import { CreaFormLogicoRequest } from '../models/api/cosmobusiness/creaFormLogicoRequest';
import { IstanzaFunzionalitaFormLogico } from '../models/api/cosmobusiness/istanzaFunzionalitaFormLogico';

@Injectable()
export class GestioneFormLogiciService {

  constructor(private http: HttpClient) { }

  /** GET: Restituisce una lista di form logici. */
  getFormLogici(filter: string): Observable<FormLogiciResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<FormLogiciResponse>(ApiUrls.FORM_LOGICI, options);
  }

  /** GET: Restituisce un form logico. */
  getFormLogico(id: number): Observable<FormLogico> {
    Utils.require(id);

    return this.http.get<FormLogico>(ApiUrls.FORM_LOGICO.replace('{id}', id.toString()));
  }

  /** DELETE: Elimina un form logico. */
  deleteFormLogico(id: number): Observable<void> {
    Utils.require(id);

    return this.http.delete<void>(ApiUrls.FORM_LOGICO.replace('{id}', id.toString()));
  }

  /**
   * POST: Salva un FormLogico.
   *
   * @param request FormLogico da salvare.
   * @returns Observable<FormLogico> il form logico salvato.
   */
  creaFormLogico(request: CreaFormLogicoRequest): Observable<FormLogico> {
    Utils.require(request);

    return this.http.post<FormLogico>(ApiUrls.FORM_LOGICI, request);
  }

  /**
   * PUT: Aggiorna un form logico.
   *
   * @param request FormLogico da aggiornare.
   * @returns Observable<FormLogico> il form logico aggiornato.
   */

  aggiornaFormLogico(id: number, request: AggiornaFormLogicoRequest): Observable<FormLogico> {
    Utils.require(id);
    Utils.require(request);

    return this.http.put<FormLogico>(ApiUrls.FORM_LOGICO.replace('{id}', id.toString()), request);
  }

  // TODO - temporary
  getIstanze(filter: string): Observable<IstanzeFormLogiciResponse> {
    const options = filter ?
    { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<IstanzeFormLogiciResponse>(ApiUrls.ISTANZE_FUNZIONALITA_FORM_LOGICI, options);
  }

  // TODO - temporary
  getIstanza(id: number): Observable<IstanzaFunzionalitaFormLogico> {
    return this.http.get<IstanzaFunzionalitaFormLogico>(
      ApiUrls.ISTANZA_FUNZIONALITA_FORM_LOGICI.replace('{id}', Utils.require(id).toString())
    );
  }
}
