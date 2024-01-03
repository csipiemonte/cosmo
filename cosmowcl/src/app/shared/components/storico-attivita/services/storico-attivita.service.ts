/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StoricoPratica } from 'src/app/shared/models/api/cosmopratiche/storicoPratica';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';

@Injectable({
  providedIn: 'root'
})
export class StoricoAttivitaService {

  constructor(
    private http: HttpClient
  ) { }

  // Metodo che recupera lo storico delle attività associate ad una pratica
  recuperaStoricoAttivitaPratica(idPratica: number): Observable<StoricoPratica>{

    return this.http.get<StoricoPratica>(ApiUrls.GET_STORICO_PRATICA.replace('{id}', '' + idPratica));
  }

}
