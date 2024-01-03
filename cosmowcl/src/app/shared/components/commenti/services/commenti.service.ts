/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { Observable } from 'rxjs';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';
import { Commento, PaginaCommenti } from '../models/commento.model';

@Injectable({
  providedIn: 'root'
})
export class CommentiService {

  constructor(
    private logger: NGXLogger,
    private http: HttpClient
  ) { }

  // Metodo che recupera tutti i commenti associati a una pratica
  recuperaCommentiPratica(idProcesso: string | number): Observable<PaginaCommenti> {
    return this.http.get<PaginaCommenti>(ApiUrls.GET_COMMENTI.replace('{idProcesso}', '' + idProcesso));
  }

  postCommento(c: Commento, idProcesso: string | number) {
    return this.http.post(ApiUrls.POST_COMMENTI.replace('{idProcesso}', '' + idProcesso), c).toPromise();
  }

  deleteCommento(c: Commento, idProcesso: string | number): Observable<any> {
    return this.http.delete(
      ApiUrls.DELETE_COMMENTI.replace('{idProcesso}', '' + idProcesso).replace('{idCommento}', '' + c.id)
    );
  }

}
