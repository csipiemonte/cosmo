/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';
import { Pratica } from '../models/api/cosmopratiche/pratica';
import { PraticaInRelazione } from '../models/api/cosmopratiche/praticaInRelazione';
import { PraticheResponse } from '../models/api/cosmopratiche/praticheResponse';
import { TipoRelazionePraticaPratica } from '../models/api/cosmopratiche/tipoRelazionePraticaPratica';

@Injectable({
  providedIn: 'root',
})
export class RelazioniPraticheService {

  constructor(private http: HttpClient) {
    // NOP
  }

  /** GET: Restituisce tutte le tipologie di associazioni possibili tra le pratiche. */
  listTipiRelazioniPratica(): Observable<TipoRelazionePraticaPratica[]> {
    return this.http.get<TipoRelazionePraticaPratica[]>(ApiUrls.TIPI_RELAZIONE_PRATICA_PRATICA);
  }

  /** GET: Restituisce la lista delle pratiche con cui e' in relazione quella di cui si passa l'id. */
  listRelazioniDellaPratica(idPratica: number): Observable<PraticaInRelazione[]> {
    return this.http.get<PraticaInRelazione[]>(ApiUrls.RELAZIONI_PRATICA.replace('{idPratica}', '' + idPratica));
  }

  /** GET: Esegue la ricerca delle pratiche che possono essere relazionate alla pratica. */
  search(idPratica: number, filter: any): Observable<PraticheResponse> {

    const params = new HttpParams()
      .append('filter', JSON.stringify({
        filter: {},
        ...filter,
      }));
    return this.http.get<PraticheResponse>(ApiUrls.DA_RELAZIONARE_PRATICA.replace('{idPratica}', '' + idPratica), { params });
  }

  creaAggiornaRelazioni(idPraticaDa: number, praticheDaAssociare: number[], tipoRelazione: string ): Observable<Pratica> {

    return this.http.put<Pratica>(ApiUrls.CREA_AGGIORNA_RELAZIONE_PRATICA
      .replace('{idPraticaDa}', '' + idPraticaDa).replace('{codiceTipoRelazione}', tipoRelazione), praticheDaAssociare);
  }

}
