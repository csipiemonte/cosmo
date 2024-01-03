/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AggiornaIstanzaFunzionalitaFormLogicoRequest } from 'src/app/shared/models/api/cosmobusiness/aggiornaIstanzaFunzionalitaFormLogicoRequest';
import { CreaIstanzaFunzionalitaFormLogicoRequest } from 'src/app/shared/models/api/cosmobusiness/creaIstanzaFunzionalitaFormLogicoRequest';
import { IstanzaFunzionalitaFormLogico } from 'src/app/shared/models/api/cosmobusiness/istanzaFunzionalitaFormLogico';
import { IstanzeFormLogiciResponse } from 'src/app/shared/models/api/cosmobusiness/istanzeFormLogiciResponse';
import { TipologiaFunzionalitaFormLogico } from 'src/app/shared/models/api/cosmobusiness/tipologiaFunzionalitaFormLogico';
import { TipologiaParametroFormLogico } from 'src/app/shared/models/api/cosmobusiness/tipologiaParametroFormLogico';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';
import { Utils } from 'src/app/shared/utilities/utilities';

@Injectable()
export class GestioneIstanzeFunzionalitaFormLogiciService {

  constructor(private http: HttpClient) { }

  // /** GET: Restituisce una lista di istanze delle funzionalita' dei form logici. */
  getIstanze(filter: string): Observable<IstanzeFormLogiciResponse> {
    const options = filter ?
    { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<IstanzeFormLogiciResponse>(ApiUrls.ISTANZE_FUNZIONALITA_FORM_LOGICI, options);
  }

  /** GET: Restituisce un'istanza della funzionalita' del form logico. */
  getIstanza(id: number): Observable<IstanzaFunzionalitaFormLogico> {
    Utils.require(id);

    return this.http.get<IstanzaFunzionalitaFormLogico>(ApiUrls.ISTANZA_FUNZIONALITA_FORM_LOGICI.replace('{id}', id.toString()));
  }

  /** DELETE: Elimina un'istanza della funzionalita' del  form logico. */
  deleteIstanza(id: number): Observable<void> {
    Utils.require(id);

    return this.http.delete<void>(ApiUrls.ISTANZA_FUNZIONALITA_FORM_LOGICI.replace('{id}', id.toString()));
  }

  /**
   * POST: Salva un'istanza.
   *
   * @param request FunzionalitaFormLogico da salvare.
   * @returns Observable<FunzionalitaFormLogico> l'istanza salvata.
   */
  creaIstanza(request: CreaIstanzaFunzionalitaFormLogicoRequest): Observable<IstanzaFunzionalitaFormLogico> {
    Utils.require(request);

    return this.http.post<IstanzaFunzionalitaFormLogico>(ApiUrls.ISTANZE_FUNZIONALITA_FORM_LOGICI, request);
  }

  /**
   * PUT: Aggiorna un'istanza.
   *
   * @param request FunzionalitaFormLogico da aggiornare.
   * @returns Observable<FunzionalitaFormLogico> l'istanza aggiornata.
   */

  aggiornaIstanza(id: number, request: AggiornaIstanzaFunzionalitaFormLogicoRequest): Observable<IstanzaFunzionalitaFormLogico> {
    Utils.require(id);
    Utils.require(request);

    return this.http.put<IstanzaFunzionalitaFormLogico>(ApiUrls.ISTANZA_FUNZIONALITA_FORM_LOGICI.replace('{id}', id.toString()), request);
  }

  getTipologieIstanzeFunzionalita(): Observable<TipologiaFunzionalitaFormLogico[]>{
    return this.http.get<TipologiaFunzionalitaFormLogico[]>(ApiUrls.TIPOLOGIA_ISTANZA_FUNZIONALITA_FORM_LOGICI);
  }

  getParametriPerTipologia(codice: string): Observable<TipologiaParametroFormLogico[]>{
    Utils.require(codice);

    return this.http.get<TipologiaParametroFormLogico[]>(
      ApiUrls.PARAMETRI_TIPOLOGIA_ISTANZA_FUNZIONALITA_FORM_LOGICI.replace('{codice}', encodeURIComponent(codice)));
  }
}
