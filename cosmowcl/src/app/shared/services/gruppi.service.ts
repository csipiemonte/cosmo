/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AggiornaGruppoRequest } from '../models/api/cosmoauthorization/aggiornaGruppoRequest';
import { CreaGruppoRequest } from '../models/api/cosmoauthorization/creaGruppoRequest';
import { GruppiResponse } from '../models/api/cosmoauthorization/gruppiResponse';
import { Gruppo } from '../models/api/cosmoauthorization/gruppo';
import { GruppoResponse } from '../models/api/cosmoauthorization/gruppoResponse';
import { RiferimentoGruppo } from '../models/api/cosmoauthorization/riferimentoGruppo';
import { ApiUrls } from '../utilities/apiurls';

@Injectable()
export class GruppiService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  /** GET: Restituisce un gruppo. */
  getGruppo(id: number): Observable<GruppoResponse> {
    return this.http.get<GruppoResponse>(ApiUrls.GET_GRUPPO.replace('{id}', id.toString()));
  }

  /** GET: Restituisce una lista di gruppi. */
  getGruppi(filter: string): Observable<GruppiResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<GruppiResponse>(ApiUrls.GRUPPI, options);
  }

    /** GET: Restituisce una lista di gruppi. */
    getGruppiUtenteCorrente(): Observable<RiferimentoGruppo[]> {
      return this.http.get<RiferimentoGruppo[]>(ApiUrls.GRUPPI_UTENTE_CORRENTE);
    }

  /**
   * POST: Salva un gruppo.
   *
   * @param gruppo gruppo da salvare.
   * @returns Observable<Gruppo> il gruppo salvato.
   */

  creaGruppo(gruppo: CreaGruppoRequest): Observable<GruppoResponse> {
    return this.http.post<GruppoResponse>(ApiUrls.GRUPPI, gruppo);
  }

  /**
   * PUT: Aggiorna un gruppo.
   *
   * @param gruppo gruppo da aggiornare.
   * @returns Observable<Gruppo> il gruppo aggiornato.
   */

  aggiornaGruppo(id: number, gruppo: AggiornaGruppoRequest): Observable<GruppoResponse> {
    return this.http.put<GruppoResponse>(ApiUrls.GET_GRUPPO.replace('{id}', id.toString()), gruppo);
  }

  /** DELETE: Elimina un utente. */
  deleteGruppo(id: number): Observable<void> {
    return this.http.delete<void>(ApiUrls.GET_GRUPPO.replace('{id}', id.toString()));
  }
}
