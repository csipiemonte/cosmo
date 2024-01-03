/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TipoNotifica } from '../models/api/cosmonotifications/tipoNotifica';
import { ApiUrls } from '../utilities/apiurls';

@Injectable({
  providedIn: 'root'
})
export class TipiNotificheService {

  constructor(private http: HttpClient) { }

  getTipiNotifiche(): Observable<TipoNotifica[]>{
    return this.http.get<TipoNotifica[]>(ApiUrls.TIPI_NOTIFICHE);
  }
}
