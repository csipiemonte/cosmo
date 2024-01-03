/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigurazioneEnte } from '../models/api/cosmoauthorization/configurazioneEnte';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable()
export class ConfigurazioneEnteService {

  constructor(private http: HttpClient) {
    // NOP
  }

  getConfigurazioneEnte(chiave: string, idEnte?: number): Observable<ConfigurazioneEnte> {
    Utils.require(chiave);

    const options = idEnte ?  { params: new HttpParams().set('idEnte', idEnte.toString()) } : {};
    return this.http.get<ConfigurazioneEnte>(ApiUrls.CONFIGURAZIONE_ENTE_CON_CHIAVE.replace('{chiave}', chiave), options);
  }

  getConfigurazioniEnte(idEnte?: number): Observable<ConfigurazioneEnte[]> {
    const options = idEnte ?  { params: new HttpParams().set('idEnte', idEnte.toString()) } : {};
    return this.http.get<ConfigurazioneEnte[]>(ApiUrls.CONFIGURAZIONE_ENTE, options);
  }

  deleteConfigurazioneEnte(chiave: string, idEnte?: number): Observable<void> {
    Utils.require(chiave);
    const options = idEnte ?  { params: new HttpParams().set('idEnte', idEnte.toString()) } : {};
    return this.http.delete<void>(ApiUrls.CONFIGURAZIONE_ENTE_CON_CHIAVE.replace('{chiave}', chiave), options);
  }

  creaConfigurazioneEnte(request: ConfigurazioneEnte, idEnte?: number): Observable<ConfigurazioneEnte> {
    Utils.require(request);
    const options = idEnte ?  { params: new HttpParams().set('idEnte', idEnte.toString()) } : {};
    return this.http.post<ConfigurazioneEnte>(ApiUrls.CONFIGURAZIONE_ENTE, request, options);
  }

  aggiornaConfigurazioneEnte(chiave: string, request: ConfigurazioneEnte, idEnte?: number): Observable<ConfigurazioneEnte> {
    Utils.require(chiave);
    Utils.require(request);
    const options = idEnte ?  { params: new HttpParams().set('idEnte', idEnte.toString()) } : {};

    return this.http.put<ConfigurazioneEnte>(ApiUrls.CONFIGURAZIONE_ENTE_CON_CHIAVE.replace('{chiave}', chiave), request, options);
  }
}
