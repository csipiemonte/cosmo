/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreaPraticaRequest } from '../shared/models/api/cosmobusiness/creaPraticaRequest';
import { OperazioneAsincrona } from '../shared/models/api/cosmobusiness/operazioneAsincrona';
import { ApiUrls } from '../shared/utilities/apiurls';

@Injectable({
  providedIn: 'root'
})
export class CreazionePraticaService {


  constructor(private http: HttpClient) {
    // NOP
  }

  creaPratica(pratica: CreaPraticaRequest): Observable<OperazioneAsincrona>{
    return this.http.post<OperazioneAsincrona>( ApiUrls.CREA_PRATICA , pratica);
  }

}
