/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CategorieUseCaseResponse } from '../models/api/cosmoauthorization/categorieUseCaseResponse';
import { UseCaseResponse } from '../models/api/cosmoauthorization/useCaseResponse';
import { ApiUrls } from '../utilities/apiurls';

@Injectable()
export class UseCaseService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  /** GET: Restituisce uno UseCase. */
  getUseCases(filter: string): Observable<UseCaseResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<UseCaseResponse>(ApiUrls.USE_CASE, options);

  }

  /** GET: Restituisce una lista di categorie di UseCases. */
  getCategorieUseCase(codice: string): Observable<CategorieUseCaseResponse> {
    const options = codice ?
      { params: new HttpParams().set('string', codice) } : {};
    return this.http.get<CategorieUseCaseResponse>(ApiUrls.USE_CASE_CATEGORIE, options);

  }
}
