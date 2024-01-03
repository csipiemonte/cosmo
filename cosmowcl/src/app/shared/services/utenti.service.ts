/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiUrls } from '../utilities/apiurls';
import { Utente } from '../models/api/cosmoauthorization/utente';
import { map } from 'rxjs/operators';
import { UtenteResponse } from '../models/api/cosmoauthorization/utenteResponse';
import { UtentiResponse } from '../models/api/cosmoauthorization/utentiResponse';
import { UtenteCampiTecnici } from '../models/api/cosmoauthorization/utenteCampiTecnici';

@Injectable()
export class UtentiService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  /** GET: Restituisce un utente. */
  getUtente(id: number): Observable<UtenteResponse> {
    return this.http.get<UtenteResponse>(ApiUrls.GET_DELETE_UTENTE.replace('{id}', id.toString()));
  }

  getUtenteByCodiceFiscale(cf: string, idEnte?: number): Observable<Utente | null> {
    const filter = JSON.stringify({
      filter: {
        codiceFiscale: {
          eq: cf
        },
        idEnte: idEnte ? {
          eq: idEnte
        } : undefined
      },
      page: 0,
      size: 2
    });
    const options = {
        params: new HttpParams().set('filter', filter)
    };
    return this.http.get<UtentiResponse>(ApiUrls.UTENTI, options).pipe(
      map(response => {
        const len = response?.utenti?.length ?? 0;
        if (len === 1) {
          return (response.utenti ?? [])[0];
        } else if (len > 1) {
          throw new Error('Troppi utenti corrispondenti al codice fiscale indicato');
        } else {
          return null;
        }
      })
    );
  }

  /** GET: Restituisce una lista di utenti. */
  getUtenti(filter: string): Observable<UtentiResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<UtentiResponse>(ApiUrls.UTENTI, options);
  }

  /** GET: Restituisce una lista di utenti. */
  getUtentiEnte(filter: string): Observable<UtentiResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<UtentiResponse>(ApiUrls.GET_UTENTI_ENTE, options);
  }


  /** DELETE: Elimina un utente. */
  deleteUtente(id: number): Observable<UtenteResponse> {
    return this.http.delete<UtenteResponse>(ApiUrls.GET_DELETE_UTENTE.replace('{id}', id.toString()));
  }
  /**
   * POST: Salva un utente. Inserisce anche l'inizio di validita' e di fine validita' dell'utente
   *
   * @param utente utente da salvare.
   * @returns Observable<Utente> il utente salvato.
   */

  salvaUtenteCampiTecnici(utente: UtenteCampiTecnici): Observable<UtenteResponse> {
    return this.http.post<UtenteResponse>(ApiUrls.UTENTI, utente);
  }

  /**
   * PUT: Aggiorna un utente. Inserisce anche l'inizio di validita' e di fine validita' dell'utente
   *
   * @param utente utente da aggiornare.
   * @returns Observable<Utente> l'utente aggiornato.
   */

  aggiornaUtenteCampiTecnici(utente: UtenteCampiTecnici): Observable<UtenteResponse> {
    return this.http.put<UtenteResponse>(ApiUrls.UTENTI, utente);
  }

  /** GET: Restituisce un utente con i valori di validita. */
  getUtenteValidita(idUtente: number, idEnte: number): Observable<UtenteCampiTecnici> {
    return this.http.get<UtenteCampiTecnici>(ApiUrls.GET_UTENTE_ENTE_VALIDITA.replace
      ('{idUtente}', idUtente.toString()).replace('{idEnte}', idEnte.toString()));
  }
}
