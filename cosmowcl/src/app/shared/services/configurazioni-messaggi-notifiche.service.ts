/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigurazioniMessaggiNotificheResponse } from '../models/api/cosmonotifications/configurazioniMessaggiNotificheResponse';
import { ApiUrls } from '../utilities/apiurls';
import { ConfigurazioneMessaggioNotifica } from '../models/api/cosmonotifications/configurazioneMessaggioNotifica';
import { ConfigurazioneMessaggioNotificaRequest } from '../models/api/cosmonotifications/configurazioneMessaggioNotificaRequest';
import { Observable } from 'rxjs';
import { Utils } from '../utilities/utilities';

@Injectable({
  providedIn: 'root'
})
export class ConfigurazioniMessaggiNotificheService {

  constructor(private http: HttpClient) { }

  /** GET: Restituisce una lista di form logici. */
  getConfigurazioniMessaggiNotifiche(filter: string): Observable<ConfigurazioniMessaggiNotificheResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<ConfigurazioniMessaggiNotificheResponse>(ApiUrls.CONFIGURAZIONI_MESSAGGI_NOTIFICHE, options);
  }

  /** GET: Restituisce una configurazione messaggio notifica. */
  getConfigurazioneMessaggioNotifica(id: number): Observable<ConfigurazioneMessaggioNotifica> {
    Utils.require(id);

    return this.http.get<ConfigurazioneMessaggioNotifica>(ApiUrls.CONFIGURAZIONI_MESSAGGI_NOTIFICHE_ID.replace('{id}', id.toString()));
  }

  /** DELETE: Elimina una configurazione messaggio notifica. */
  deleteConfigurazioneMessaggioNotifica(id: number): Observable<ConfigurazioneMessaggioNotifica> {
    Utils.require(id);

    return this.http.delete<ConfigurazioneMessaggioNotifica>(ApiUrls.CONFIGURAZIONI_MESSAGGI_NOTIFICHE_ID.replace('{id}', id.toString()));
  }

  /**
   * POST: Salva una configurazione messaggio notifica.
   *
   * @param request ConfigurazioneMessaggioNotifica da salvare.
   * @returns Observable<ConfigurazioneMessaggioNotifica> il form logico salvato.
   */
  creaConfigurazioneMessaggioNotifica(request: ConfigurazioneMessaggioNotificaRequest): Observable<ConfigurazioneMessaggioNotifica> {
    Utils.require(request);

    return this.http.post<ConfigurazioneMessaggioNotifica>(ApiUrls.CONFIGURAZIONI_MESSAGGI_NOTIFICHE, request);
  }

  /**
   * PUT: Aggiorna una configurazione messaggio notifica.
   *
   * @param request ConfigurazioneMessaggioNotifica da aggiornare.
   * @returns Observable<ConfigurazioneMessaggioNotifica> il form logico aggiornato.
   */

  aggiornaConfigurazioneMessaggioNotifica(id: number, request: ConfigurazioneMessaggioNotificaRequest):
   Observable<ConfigurazioneMessaggioNotifica> {
    Utils.require(id);
    Utils.require(request);

    return this.http.put<ConfigurazioneMessaggioNotifica>(ApiUrls.CONFIGURAZIONI_MESSAGGI_NOTIFICHE_ID.replace('{id}', id.toString()),
     request);
  }
}
