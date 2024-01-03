/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreaTemplateRequest } from '../models/api/cosmoecm/creaTemplateRequest';
import { TemplateReport } from '../models/api/cosmoecm/templateReport';
import { TemplateReportResponse } from '../models/api/cosmoecm/templateReportResponse';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable()
export class TemplateService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  /** GET: Restituisce una lista di template */
  getTemplates(filter: string): Observable<TemplateReportResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<TemplateReportResponse>(ApiUrls.GET_REPORT_TEMPLATES, options);
  }

  create(request: CreaTemplateRequest): Observable<TemplateReport> {
    Utils.require(request);
    return this.http.post<TemplateReport>(ApiUrls.POST_REPORT_TEMPLATES, request);
  }

  delete(id: number): Observable<any> {
    Utils.require(id);
    return this.http.delete<any>(ApiUrls.DELETE_REPORT_TEMPLATE.replace('{id}', id.toString()));
  }

  update(id: number, payload: CreaTemplateRequest): Observable<TemplateReport> {
    Utils.require(id);
    return this.http.put<TemplateReport>(ApiUrls.UPDATE_REPORT_TEMPLATE.replace('{id}', id.toString()), payload );
  }

  get(id: number): Observable<TemplateReport> {
    Utils.require(id);
    return this.http.get<TemplateReport>(ApiUrls.GET_REPORT_TEMPLATE.replace('{id}', id.toString()));
  }
}
