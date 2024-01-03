/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CertificatoFirma } from 'src/app/shared/models/api/cosmoauthorization/certificatoFirma';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';

@Injectable()
export class CertificatiService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  /** GET: Restituisce un certificato. */
  getCertificato(id: number): Observable<CertificatoFirma> {
    return this.http.get<CertificatoFirma>(ApiUrls.CERTIFICATI_ID.replace('{id}', id.toString()));

  }

  /** GET: Restituisce una lista di certificati. */
  getCertificati(): Observable<CertificatoFirma[]> {
    return this.http.get<CertificatoFirma[]>(ApiUrls.CERTIFICATI);
  }

  /** DELETE: Elimina un certificato. */
  deleteCertificato(id: number): Observable<CertificatoFirma> {
    return this.http.delete<CertificatoFirma>(ApiUrls.CERTIFICATI_ID.replace('{id}', id.toString()));
  }

  /**
   * POST: Salva un certificato.
   *
   * @param body CertificatoFirma da salvare.
   * @returns Observable<CertificatoFirma> il certificato salvato.
   */

  salvaCertificato(body: CertificatoFirma): Observable<CertificatoFirma> {
    return this.http.post<CertificatoFirma>(ApiUrls.CERTIFICATI, body);
  }

  /**
   * PUT: Aggiorna un certificato.
   *
   * @param body Certificato da aggiornare.
   * @returns Observable<CertificatoFirma> il certificato aggiornato.
   */

  aggiornaCertificato(id: number, body: CertificatoFirma): Observable<CertificatoFirma> {
    return this.http.put<CertificatoFirma>(ApiUrls.CERTIFICATI_ID.replace('{id}', id.toString()), body);
  }

  salvaCertificatoUltimoUtilizzato(id: number): Observable<CertificatoFirma> {
    return this.http.put<CertificatoFirma>(ApiUrls.CERTIFICATI_ULTIMO_USATO.replace('{id}', id.toString()), {});
  }
}
