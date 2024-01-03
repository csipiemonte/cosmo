/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';

import { IdentitaUtente } from '../shared/models/api/cosmoecm/identitaUtente';
import {
  ImportaDocumentiActaRequest,
} from '../shared/models/api/cosmoecm/importaDocumentiActaRequest';
import {
  RiferimentoOperazioneAsincrona,
} from '../shared/models/api/cosmoecm/riferimentoOperazioneAsincrona';
import { FiltroRicercaActaOutput } from './filtri-ricerca-acta.component';

@Injectable()
export class RicercaActaService {
  constructor(private http: HttpClient) {}

  getIdentitaUtente(): Observable<Array<IdentitaUtente>>{
    return this.http.get<Array<IdentitaUtente>>(ApiUrls.IDENTITA_UTENTE_ACTA);
  }

  eseguiRicerca(identita: string, filtri: FiltroRicercaActaOutput, page: number, size: number, expand?: string):
    Observable<RiferimentoOperazioneAsincrona> {
    return this.http.get<RiferimentoOperazioneAsincrona>(ApiUrls.RICERCA_ACTA, {
      params: {
        identita,
        filter: JSON.stringify({
          page,
          size,
          filter: filtri,
          expand
        })
      }
    });
  }

  importa(identita: string, request: ImportaDocumentiActaRequest): Observable<RiferimentoOperazioneAsincrona> {
    return this.http.post<RiferimentoOperazioneAsincrona>(ApiUrls.IMPORTAZIONE_DOCUMENTI_ACTA, {
      ...request
    }, {
      params: {
        identita
      }
    });
  }

}
