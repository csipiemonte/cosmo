/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiUrls } from '../utilities/apiurls';
import { FirmaFeaRequest } from '../models/api/cosmoecm/firmaFeaRequest';
import { RiferimentoOperazioneAsincrona } from '../models/api/cosmoecm/riferimentoOperazioneAsincrona';

@Injectable()
export class FeaService {

  constructor(private http: HttpClient) { }

  richiediOtp(): Observable<void> {
    return this.http.post<void>(ApiUrls.RICHIEDI_OTP_FEA, {});
  }

  firma(request: FirmaFeaRequest): Observable<RiferimentoOperazioneAsincrona> {
    return this.http.post<RiferimentoOperazioneAsincrona>(ApiUrls.FIRMA_FEA, request);
  }

}
