/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OperazioneAsincrona } from '../models/api/cosmobusiness/operazioneAsincrona';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable({
  providedIn: 'root'
})
export class AttivazioneSistemaEsternoService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  getPayload(idPratica: number, idAttivita: number) {
    // tslint:disable-next-line:max-line-length
    return this.http.get(ApiUrls.GET_PAYLOAD_ATTIVAZIONE_SISTEMA_ESTERNO
        .replace('{idPratica}', idPratica.toString()).replace('{idAttivita}', idAttivita.toString()));
  }

  setAttivazione(idPratica: number, idAttivita: number): Observable<OperazioneAsincrona>{
    return this.http.post<OperazioneAsincrona>(ApiUrls.POST_ATTIVAZIONE_SISTEMA_ESTERNO
      .replace('{idPratica}', idPratica.toString())
        .replace('{idAttivita}', idAttivita.toString()), {});
  }

}
