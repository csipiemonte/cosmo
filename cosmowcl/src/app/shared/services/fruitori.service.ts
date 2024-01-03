/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  HttpClient,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import {
  AggiornaEndpointFruitoreRequest,
} from '../models/api/cosmoauthorization/aggiornaEndpointFruitoreRequest';
import {
  AggiornaFruitoreRequest,
} from '../models/api/cosmoauthorization/aggiornaFruitoreRequest';
import {
  AggiornaSchemaAutenticazioneFruitoreRequest,
} from '../models/api/cosmoauthorization/aggiornaSchemaAutenticazioneFruitoreRequest';
import {
  AutorizzazioniFruitoreResponse,
} from '../models/api/cosmoauthorization/autorizzazioniFruitoreResponse';
import {
  CreaEndpointFruitoreRequest,
} from '../models/api/cosmoauthorization/creaEndpointFruitoreRequest';
import {
  CreaFruitoreRequest,
} from '../models/api/cosmoauthorization/creaFruitoreRequest';
import {
  CreaSchemaAutenticazioneFruitoreRequest,
} from '../models/api/cosmoauthorization/creaSchemaAutenticazioneFruitoreRequest';
import {
  EndpointFruitore,
} from '../models/api/cosmoauthorization/endpointFruitore';
import { Fruitore } from '../models/api/cosmoauthorization/fruitore';
import {
  FruitoriResponse,
} from '../models/api/cosmoauthorization/fruitoriResponse';
import {
  OperazioneFruitore,
} from '../models/api/cosmoauthorization/operazioneFruitore';
import {
  SchemaAutenticazioneFruitore,
} from '../models/api/cosmoauthorization/schemaAutenticazioneFruitore';
import { ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable()
export class FruitoriService {

  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  /** GET: Restituisce un fruitore. */
  getFruitore(id: number): Observable<Fruitore> {
    Utils.require(id);

    return this.http.get<Fruitore>(ApiUrls.GET_FRUITORE.replace('{id}', id.toString()));
  }

  /** GET: Restituisce una lista di fruitori. */
  getFruitori(filter: string): Observable<FruitoriResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<FruitoriResponse>(ApiUrls.FRUITORI, options);
  }

  /** GET: Restituisce una lista di fruitori. */
  getAutorizzazioniFruitori(filter: string): Observable<AutorizzazioniFruitoreResponse> {
    const options = filter ?
      { params: new HttpParams().set('filter', filter) } : {};
    return this.http.get<AutorizzazioniFruitoreResponse>(ApiUrls.AUTORIZZAZIONI_FRUITORE, options);
  }

  /** DELETE: Elimina un fruitore. */
  deleteFruitore(id: number): Observable<void> {
    Utils.require(id);

    return this.http.delete<void>(ApiUrls.GET_FRUITORE.replace('{id}', id.toString()));
  }

  /**
   * POST: Salva un Fruitore.
   *
   * @param fruitore Fruitore da salvare.
   * @returns Observable<Fruitore> il fruitore salvato.
   */
  creaFruitore(request: CreaFruitoreRequest): Observable<Fruitore> {
    Utils.require(request);

    return this.http.post<Fruitore>(ApiUrls.FRUITORI, request);
  }

  /**
   * PUT: Aggiorna un fruitore.
   *
   * @param fruitore Fruitore da aggiornare.
   * @returns Observable<Fruitore> l'fruitore aggiornato.
   */

  aggiornaFruitore(id: number, request: AggiornaFruitoreRequest): Observable<Fruitore> {
    Utils.require(id);
    Utils.require(request);

    return this.http.put<Fruitore>(ApiUrls.GET_FRUITORE.replace('{id}', id.toString()), request);
  }

  creaSchemaAutenticazioneFruitore(idFruitore: number, request: CreaSchemaAutenticazioneFruitoreRequest):
    Observable<SchemaAutenticazioneFruitore>
  {
    Utils.require(idFruitore, 'idFruitore');

    return this.http.post<SchemaAutenticazioneFruitore>(
      ApiUrls.SCHEMI_AUTENTICAZIONE_FRUITORE
        .replace('{idFruitore}', idFruitore.toString()),
      request
    );
  }

  aggiornaSchemaAutenticazioneFruitore(idFruitore: number, idSchema: number, request: AggiornaSchemaAutenticazioneFruitoreRequest):
    Observable<SchemaAutenticazioneFruitore>
  {
    Utils.require(idFruitore, 'idFruitore');
    Utils.require(idSchema, 'idSchema');
    Utils.require(request);

    return this.http.put<SchemaAutenticazioneFruitore>(
      ApiUrls.SCHEMA_AUTENTICAZIONE_FRUITORE
        .replace('{idFruitore}', idFruitore.toString())
        .replace('{idSchema}', idSchema.toString()),
      request
    );
  }

  eliminaSchemaAutenticazioneFruitore(idFruitore: number, idSchema: number): Observable<void> {
    Utils.require(idFruitore, 'idFruitore');
    Utils.require(idSchema, 'idSchema');

    return this.http.delete<void>(
      ApiUrls.SCHEMA_AUTENTICAZIONE_FRUITORE
        .replace('{idFruitore}', idFruitore.toString())
        .replace('{idSchema}', idSchema.toString())
    );
  }

  testSchemaAutenticazioneFruitore(idSchema: number): Observable<object> {
    Utils.require(idSchema, 'idSchema');

    return this.http.post<object>(
      ApiUrls.TEST_SCHEMA_AUTENTICAZIONE_FRUITORE
        .replace('{idSchema}', idSchema.toString()), {}
    );
  }

  creaEndpointFruitore(idFruitore: number, request: CreaEndpointFruitoreRequest): Observable<EndpointFruitore> {
    Utils.require(idFruitore, 'idFruitore');

    return this.http.post<EndpointFruitore>(
      ApiUrls.ENDPOINTS_FRUITORE
        .replace('{idFruitore}', idFruitore.toString()),
      request
    );
  }

  aggiornaEndpointFruitore(idFruitore: number, idEndpoint: number, request: AggiornaEndpointFruitoreRequest): Observable<EndpointFruitore> {
    Utils.require(idFruitore, 'idFruitore');
    Utils.require(idEndpoint, 'idEndpoint');
    Utils.require(request);

    return this.http.put<EndpointFruitore>(
      ApiUrls.ENDPOINT_FRUITORE
        .replace('{idFruitore}', idFruitore.toString())
        .replace('{idEndpoint}', idEndpoint.toString()),
      request
    );
  }

  eliminaEndpointFruitore(idFruitore: number, idEndpoint: number): Observable<void> {
    Utils.require(idFruitore, 'idFruitore');
    Utils.require(idEndpoint, 'idEndpoint');

    return this.http.delete<void>(
      ApiUrls.ENDPOINT_FRUITORE
        .replace('{idFruitore}', idFruitore.toString())
        .replace('{idEndpoint}', idEndpoint.toString())
    );
  }

  getOperazioniFruitore(): Observable<OperazioneFruitore[]> {
    return this.http.get<OperazioneFruitore[]>(ApiUrls.OPERAZIONI_FRUITORE);
  }
}
