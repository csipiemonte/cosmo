/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApiUrls } from '../utilities/apiurls';
import { Observable } from 'rxjs';
import { TabsDettaglio } from '../models/api/cosmopratiche/tabsDettaglio';
import { Utils } from '../utilities/utilities';

@Injectable({
  providedIn: 'root'
})
export class TabsDettaglioService {

  constructor(
    private http: HttpClient
  ) { }

  getTabsDettaglio(): Observable<TabsDettaglio[]> {
    return this.http.get<TabsDettaglio[]>(ApiUrls.TABS_DETTAGLIO);
  }

  getTabsDettaglioCodiceTipoPratica(codiceTipoPratica: string): Observable<TabsDettaglio[]> {
    Utils.require(codiceTipoPratica);
    return this.http.get<TabsDettaglio[]>(ApiUrls.TABS_DETTAGLIO_FROM_CODICE_TIPO_PRATICA
      .replace('{codiceTipoPratica}', codiceTipoPratica.toString()));
  }

}
