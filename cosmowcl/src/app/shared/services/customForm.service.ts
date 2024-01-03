/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  HttpClient,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';

import {
  forkJoin,
  Observable,
} from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { CustomForm } from '../models/api/cosmopratiche/customForm';
import {
  CustomFormResponse,
} from '../models/api/cosmopratiche/customFormResponse';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable()
export class CustomFormService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  /** GET: Restituisce una lista di custom form */
  getCustomForms(filter: string): Observable<CustomFormResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<CustomFormResponse>(ApiUrls.CUSTOM_FORMS, options);
  }

  get(codice: string): Observable<CustomForm> {
    Utils.require(codice);
    return this.http.get<CustomForm>(ApiUrls.CUSTOM_FORM.replace('{codice}', codice.toString()));
  }

  getFromCodiceTipoPratica(codiceTipoPratica: string): Observable<CustomForm> {
    Utils.require(codiceTipoPratica);
    return this.http.get<CustomForm>(ApiUrls.CUSTOM_FORM_FROM_CODICE_TIPO_PRATICA
      .replace('{codiceTipoPratica}', codiceTipoPratica.toString()));
  }

  create(request: CustomForm): Observable<CustomForm> {
    Utils.require(request);
    return this.http.post<CustomForm>(ApiUrls.CUSTOM_FORMS, request);
  }

  delete(codice: string): Observable<any> {
    Utils.require(codice);
    return this.http.delete<any>(ApiUrls.CUSTOM_FORM.replace('{codice}', codice.toString()));
  }

  update(codice: string, payload: CustomForm): Observable<CustomForm> {
    Utils.require(codice);
    return this.http.put<CustomForm>(ApiUrls.CUSTOM_FORM.replace('{codice}', codice.toString()), payload );
  }

  importa(codice: string, baseUrl: string): Observable<CustomForm> {
    Utils.require(codice);
    // import directly from modeler
    const modelerUrl = baseUrl + '/' + codice;
    return forkJoin({
      existingForm: this.get(codice),
      newDefinition: this.http.get<object>(modelerUrl, {
        withCredentials: false,
        headers: {
          'X-Cosmo-Skip-Credentials': '1'
        }
      }),
    }) .pipe(
      mergeMap(data => {
        const payload: CustomForm = {
          codice,
          customForm: JSON.stringify(data.newDefinition),
          descrizione: data.existingForm.descrizione,
        };
        return this.http.put<CustomForm>(ApiUrls.CUSTOM_FORM.replace('{codice}', codice.toString()), payload);
      })
    );
  }
}
