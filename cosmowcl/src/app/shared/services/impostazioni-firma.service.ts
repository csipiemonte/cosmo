/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ImpostazioniFirma } from '../models/api/cosmoauthorization/impostazioniFirma';
import { ApiUrls } from '../utilities/apiurls';

@Injectable()
export class ImpostazioniFirmaService {

  constructor(
    private http: HttpClient) {
  }

  /** GET: Restituisce un le impostazioni di firma. */
  getImpostazioniFirma(): Observable<ImpostazioniFirma> {
    return this.http.get<ImpostazioniFirma>(ApiUrls.IMPOSTAZIONI_FIRMA);
  }
}
