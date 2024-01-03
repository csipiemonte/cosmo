/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FirmaRequest } from '../models/api/cosmoecm/firmaRequest';
import { RichiestaOTPRequest } from '../models/api/cosmoecm/richiestaOTPRequest';
import { ApiUrls } from '../utilities/apiurls';

@Injectable({
  providedIn: 'root'
})
export class FirmaDigitaleService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  richiediOTP(body: RichiestaOTPRequest) {
    return this.http.post(ApiUrls.RICHIEDI_OTP, body);
  }

  /**
   * POST: Firma i documenti passati nel body
   *
   * @param body CertificatoFirma documenti da firmare.
   */
  firmaDocumenti(body: FirmaRequest): Observable<void>{
    return this.http.post<void>(ApiUrls.FIRMA, body);
  }

}
