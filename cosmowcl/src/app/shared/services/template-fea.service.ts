/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TemplateFeaResponse } from '../models/api/cosmoecm/templateFeaResponse';
import { ApiUrls } from '../utilities/apiurls';
import { TemplateFea } from '../models/api/cosmoecm/templateFea';
import { Utils } from '../utilities/utilities';
import { CreaTemplateFeaRequest } from '../models/api/cosmoecm/creaTemplateFeaRequest';

@Injectable({
  providedIn: 'root',
})
export class TemplateFeaService {

  constructor( private http: HttpClient) { }

  search(filter: string, tutti ?: boolean): Observable<TemplateFeaResponse> {
    let params = new HttpParams();
    if (filter){
      params = params.set('filter', filter);
    }
    if (tutti){
      params = params.set('tutti', tutti.toString());
    }
    return this.http.get<TemplateFeaResponse>(ApiUrls.TEMPLATE_FEA, {params});
  }

  get(id: string): Observable<TemplateFea> {
    Utils.require(id);
    return this.http.get<TemplateFea>(ApiUrls.TEMPLATE_FEA_ID.replace('{id}', id.toString()));
  }

  delete(id: number): Observable<void> {
    Utils.require(id);
    return this.http.delete<void>(ApiUrls.TEMPLATE_FEA_ID.replace('{id}', id.toString()));
  }

  create(payload: CreaTemplateFeaRequest): Observable<TemplateFea> {
    Utils.require(payload, 'payload');
    return this.http.post<TemplateFea>(ApiUrls.TEMPLATE_FEA, payload);
  }

  update(id: number, payload: CreaTemplateFeaRequest): Observable<TemplateFea> {
    Utils.require(id);
    return this.http.put<TemplateFea>(ApiUrls.TEMPLATE_FEA_ID.replace('{id}', id.toString()), payload);
  }

}
