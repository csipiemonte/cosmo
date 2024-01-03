/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TipiSegnalibro } from '../models/api/cosmoauthorization/tipiSegnalibro';
import { ApiUrls } from '../utilities/apiurls';

@Injectable({
  providedIn: 'root'
})
export class SegnalibroService {

  constructor(private http: HttpClient){}

  getTipiSegnalibro(): Observable<TipiSegnalibro>{
    return this.http.get<TipiSegnalibro>(ApiUrls.TIPI_SEGNALIBRO);
  }
}
