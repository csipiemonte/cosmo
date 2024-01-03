/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FormatoFile } from '../models/api/cosmoecm/formatoFile';
import { FormatoFileResponse } from '../models/api/cosmoecm/formatoFileResponse';
import { RaggruppamentoFormatiFileResponse } from '../models/api/cosmoecm/raggruppamentoFormatiFileResponse';
import { ApiUrls } from '../utilities/apiurls';

@Injectable()
export class FormatoFileService {

  constructor(private httpClient: HttpClient) { }

  /** GET: Restituisce una lista di custom form */
  getFormatiFile(filter: string): Observable<FormatoFileResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.httpClient.get<FormatoFileResponse>(ApiUrls.FORMATI_FILE, options);
  }

  getGroupedFormatiFile(filter: string): Observable<RaggruppamentoFormatiFileResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.httpClient.get<RaggruppamentoFormatiFileResponse>(ApiUrls.FORMATI_FILE_GROUPED, options);
  }
}
