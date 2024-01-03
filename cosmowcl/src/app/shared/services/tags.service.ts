/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AggiornaTagRequest } from '../models/api/cosmoauthorization/aggiornaTagRequest';
import { CreaTagRequest } from '../models/api/cosmoauthorization/creaTagRequest';
import { TagResponse } from '../models/api/cosmoauthorization/tagResponse';
import { TagsResponse } from '../models/api/cosmoauthorization/tagsResponse';
import { TipoTag } from '../models/api/cosmoauthorization/tipoTag';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable({
  providedIn: 'root'
})
export class TagsService {

  constructor(
    private http: HttpClient
  ) { }

  getTipiTag(): Observable<TipoTag[]> {
    return this.http.get<TipoTag[]>(ApiUrls.GET_TIPI_TAGS);
  }

  /** GET: Restituisce una lista tags */
  getTags(filter: string): Observable<TagsResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<TagsResponse>(ApiUrls.TAGS, options);
  }

  get(id: string): Observable<TagResponse> {
    Utils.require(id);
    return this.http.get<TagResponse>(ApiUrls.TAG_ID.replace('{id}', id.toString()));
  }

  deleteTag(id: number): Observable<void> {
    Utils.require(id);
    return this.http.delete<void>(ApiUrls.TAG_ID.replace('{id}', id.toString()));
  }

  createTag(payload: CreaTagRequest): Observable<TagResponse> {
    Utils.require(payload, 'payload');
    return this.http.post<TagResponse>(ApiUrls.TAGS, payload);
  }

  updateTag(id: number, payload: AggiornaTagRequest): Observable<TagResponse> {
    Utils.require(id);
    return this.http.put<TagResponse>(ApiUrls.TAG_ID.replace('{id}', id.toString()), payload);
  }
}



