/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OperazioneAsincrona } from '../models/api/cosmobusiness/operazioneAsincrona';
import { RichiediGenerazioneReportRequest } from '../models/api/cosmoecm/richiediGenerazioneReportRequest';
import { ApiUrls } from '../utilities/apiurls';

@Injectable()
export class ReportService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  richiediGenerazioneReportAsincrona(request: RichiediGenerazioneReportRequest): Observable<OperazioneAsincrona> {
    return this.http.post<OperazioneAsincrona>(ApiUrls.REPORT_ASYNC, request);
  }

  richiediGenerazioneReportAnteprima(request: RichiediGenerazioneReportRequest): Observable<HttpResponse<Blob>> {
    return this.http.post(ApiUrls.REPORT_ANTEPRIMA, request, {
      observe: 'response',
      responseType: 'blob'
    });
  }

}
