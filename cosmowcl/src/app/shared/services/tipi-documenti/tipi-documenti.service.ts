/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TipoDocumento } from '../../models/api/cosmoecm/tipoDocumento';
import { VerificaTipologiaDocumentiSalvati } from '../../models/api/cosmoecm/verificaTipologiaDocumentiSalvati';
import { ApiUrls } from '../../utilities/apiurls';

@Injectable()
export class TipiDocumentiService {

  constructor(private httpClient: HttpClient) { }

  /**
   * GET: ottiene i tipi di documenti in base alla pratica
   * @param idPratica e' la pratica da cui si vogliono ottenere i tipi di documento
   * @param codiceTipoDocPadre se presente contiene il codice tipo documento del
   * documento padre: usato per filtrare i tipi documento ammissibili per gli allegat
   */
  getTipiDocumenti(idPratica: number, codiceTipoDocPadre: string | null): Observable<TipoDocumento[]> {
    const params = { idPratica: idPratica ? idPratica.toString() : '',
    codiceTipoDocPadre : codiceTipoDocPadre ?? ''};
    return this.httpClient.get<TipoDocumento[]>(ApiUrls.GET_TIPI_DOCUMENTI, { params });
  }

  /**
   * GET: ottiene i tipi di documenti in base al tipo di pratica
   * @param tipoPratica e' il tipo di pratica da cui si vogliono ottenere i tipi di documento
   */
   getTipiDocumentiAll(tipoPratica: string): Observable<TipoDocumento[]> {
    const params = { tipoPratica };
    return this.httpClient.get<TipoDocumento[]>(ApiUrls.GET_TIPI_DOCUMENTI_ALL, { params });
  }

  getTipologieDocumentiSalvati(tipologieDocumenti: string): Observable<VerificaTipologiaDocumentiSalvati[]>{
    return this.httpClient.get<VerificaTipologiaDocumentiSalvati[]>(ApiUrls.GET_TIPI_DOCUMENTI_SALVATI, { params: { tipologieDocumenti } });

  }
}
