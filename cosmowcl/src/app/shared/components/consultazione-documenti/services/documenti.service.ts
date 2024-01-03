/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  HttpClient,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';

import { NGXLogger } from 'ngx-logger';
import {
  BehaviorSubject,
  concat,
  forkJoin,
  Observable,
  of,
  Subject,
  Subscription,
} from 'rxjs';
import {
  delay,
  finalize,
  map,
  mergeMap,
  tap,
} from 'rxjs/operators';
import {
  TipiDocumentoObbligatori,
} from 'src/app/form-logici/gestione-documenti/models/tipi-documento-obbligatori.models';
import {
  CompleteUploadSessionRequest,
} from 'src/app/shared/models/api/cosmo/completeUploadSessionRequest';
import {
  CompleteUploadSessionResponse,
} from 'src/app/shared/models/api/cosmo/completeUploadSessionResponse';
import {
  CreateUploadSessionRequest,
} from 'src/app/shared/models/api/cosmo/createUploadSessionRequest';
import {
  CreateUploadSessionResponse,
} from 'src/app/shared/models/api/cosmo/createUploadSessionResponse';
import {
  AggiornaDocumentoRequest,
} from 'src/app/shared/models/api/cosmoecm/aggiornaDocumentoRequest';
import {
  CreaDocumentiRequest,
} from 'src/app/shared/models/api/cosmoecm/creaDocumentiRequest';
import { Documenti } from 'src/app/shared/models/api/cosmoecm/documenti';
import {
  DocumentiResponse,
} from 'src/app/shared/models/api/cosmoecm/documentiResponse';
import { Documento } from 'src/app/shared/models/api/cosmoecm/documento';
import { InfoFirmaFea } from 'src/app/shared/models/api/cosmoecm/infoFirmaFea';
import {
  TipoDocumento,
} from 'src/app/shared/models/api/cosmoecm/tipoDocumento';
import {
  DocumentiDTOResponse,
  DocumentoDTO,
  TipoContenutoDocumentoEnum,
  TipoContenutoDocumentoFirmatoEnum,
} from 'src/app/shared/models/documento/documento.model';
import {
  FileUploadResult,
} from 'src/app/shared/models/files/file-upload-result';
import {
  SessionUploadResult,
} from 'src/app/shared/models/files/session-upload-result';
import {
  ApiOptions,
  ApiUrls,
} from 'src/app/shared/utilities/apiurls';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

@Injectable()
export class DocumentiService {

  cercaDocumentiSubject = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient, private logger: NGXLogger) {}

  // TODO sostituire con BusService
  getCercaDocumenti(): Observable<boolean> {
    return this.cercaDocumentiSubject.asObservable();
  }

  // TODO sostituire con BusService
  setCercaDocumenti(cercaDocumenti: boolean) {
    this.cercaDocumentiSubject.next(cercaDocumenti);
  }

  getDocumenti(soloFirmabili: boolean, filter: string, filterDocDaFirmare: string, exportData: boolean): Observable<DocumentiDTOResponse>{
    if (soloFirmabili){
      return this.getDocumentiDaFirmare(filter, filterDocDaFirmare, exportData);
    } else{
      return this.getAllDocumenti(filter, exportData);
    }
  }

  /** GET: Restituisce una lista di documenti. */
  getAllDocumenti(filter: string, exportData: boolean): Observable<DocumentiDTOResponse> {

    const params = new HttpParams()
      .append('export', exportData.toString())
      .append('filter', filter ? filter : '');

    return this.http.get<DocumentiResponse>(ApiUrls.DOCUMENTI, { params }).pipe(
      map(resp => {
        return {
          pageInfo: resp.pageInfo,
          documenti: resp.documenti?.map(d => this.mapDocumento(d))
        };
      })
    );
  }

  mapDocumento(d: Documento): DocumentoDTO {
    const o: DocumentoDTO = {
      ...d,
      contenutoTemporaneo: d.contenuti?.find(c => c.tipo?.codice === TipoContenutoDocumentoEnum.TEMPORANEO),
      contenutoOriginale: d.contenuti?.find(c => c.tipo?.codice === TipoContenutoDocumentoEnum.ORIGINALE),
      contenutoSbustato: d.contenuti?.find(c => c.tipo?.codice === TipoContenutoDocumentoEnum.SBUSTATO),
      contenutoFirmato: d.contenuti?.find(c => c.tipo?.codice === TipoContenutoDocumentoEnum.FIRMATO && !c.dtCancellazione),
      ultimoContenutoFirmaDigitale: d.contenuti?.find(c =>
        c.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FIRMA_DIGITALE &&
        !d.contenuti?.filter(elem => elem.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FIRMA_DIGITALE)
        .find(elem => elem.dtInserimento && c.dtInserimento &&
        new Date(elem.dtInserimento).getTime() > new Date(c.dtInserimento).getTime())),
      ultimoContenutoFEA: d.contenuti?.find(c => c.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FEA &&
        !d.contenuti?.filter(elem => elem.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FEA)
        .find(elem => elem.dtInserimento && c.dtInserimento &&
        new Date(elem.dtInserimento).getTime() > new Date(c.dtInserimento).getTime())),
      ultimoContenutoSigilloElettronico: d.contenuti?.find(c =>
        c.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.SIGILLO_ELETTRONICO &&
        !d.contenuti?.filter(elem => elem.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.SIGILLO_ELETTRONICO)
        .find(elem => elem.dtInserimento && c.dtInserimento &&
        new Date(elem.dtInserimento).getTime() > new Date(c.dtInserimento).getTime()))
    };
    o.contenutoEffettivo = o.contenutoFirmato ? o.contenutoFirmato : o.contenutoOriginale || o.contenutoTemporaneo;

    if (o.contenutoSbustato?.anteprime?.length) {
      o.anteprimaEffettiva = o.contenutoSbustato.anteprime[0];
    } else if (o.contenutoOriginale?.anteprime?.length) {
      o.anteprimaEffettiva = o.contenutoOriginale.anteprime[0];
    } else if (o.contenutoFirmato?.anteprime?.length) {
      o.anteprimaEffettiva = o.contenutoFirmato.anteprime[0];
    } else if (o.contenutoTemporaneo?.anteprime?.length) {
      o.anteprimaEffettiva = o.contenutoTemporaneo.anteprime[0];
    }

    return o;
  }

  /** GET: Restituisce una lista di documenti da firmare. */
  getDocumentiDaFirmare(filter: string, filterDocDaFirmare: string, exportData: boolean): Observable<DocumentiDTOResponse> {
    const params: any = {export: exportData, filter, filterDocDaFirmare};

    return this.http.get<DocumentiResponse>(ApiUrls.DOCUMENTI_DA_FIRMARE, {params}).pipe(
      map( resp => {
        return {
          pageInfo: resp.pageInfo,
          documenti: resp.documenti?.map(d => this.mapDocumento(d))
        };
      })
    );
  }

  /** GET: Scarica il documento salvato */
  downloadContenutoDocumento(idDocumento: number, idContenuto: number): Observable<string> {
    return of(ApiUrls.DOWNLOAD_CONTENUTO_DOCUMENTO
      .replace('{idDocumento}', idDocumento.toString())
      .replace('{idContenuto}', idContenuto.toString()));
  }

  /** GET: Preview del documento senza firma o con firma ma sbustato */
  previewContenutoDocumento(idDocumento: number, idContenuto: number): Observable<string> {
    return of(
      (ApiUrls.DOWNLOAD_CONTENUTO_DOCUMENTO + '?preview=true')
        .replace('{idDocumento}', idDocumento.toString())
        .replace('{idContenuto}', idContenuto.toString()));
  }

  /** POST: Inserisce un documento sul database */
  salvaDocumento(idPratica: number, documento: CreaDocumentiRequest): Observable<Documenti> {
    const options = idPratica ? { params: new HttpParams().set('idPratica', idPratica.toString()) } : {};
    return this.http.post<Documenti>(ApiUrls.DOCUMENTI, documento, options);
  }

    /** PUT: Aggiorna logicamente i campi del documento passanti nella request. */
  aggiornaDocumento(id: number, documento: AggiornaDocumentoRequest): Observable<Documento> {
    return this.http.put<Documento>(ApiUrls.GET_PUT_DELETE_DOCUMENTO.replace('{id}', id.toString()), documento);
  }

  /** DELETE: Cancella logicamente un documento. */
  cancellaDocumento(id: number): Observable<void> {
    return this.http.delete<void>(ApiUrls.GET_PUT_DELETE_DOCUMENTO.replace('{id}', id.toString()));
  }

  uploadFile(formData: FormData): Observable<FileUploadResult> {
    return this.http.post<FileUploadResult>(ApiUrls.UPLOAD_FILE, formData);
  }

  createUploadSession(fileName: string, mimeType: string, size: number): Observable<CreateUploadSessionResponse> {
    const req: CreateUploadSessionRequest = {
      fileName,
      size,
      mimeType,
    };
    return this.http.post<CreateUploadSessionResponse>(ApiUrls.UPLOAD_SESSION, req, {
      headers: {[ApiOptions.IN_BACKGROUND]: 'true'}
    });
  }

  uploadSessionPart(sessionUUID: string, partNumber: number, chunk: Blob): Observable<void> {
    const formData = new FormData();
    formData.append('payload', chunk, 'part' + partNumber);
    return this.http.post<void>(ApiUrls.UPLOAD_SESSION_ID.replace('{sessionUUID}', sessionUUID) + '/parts/' + partNumber, formData, {
      headers: {[ApiOptions.IN_BACKGROUND]: 'true'}
    });
  }

  completeUploadSession(sessionUUID: string): Observable<CompleteUploadSessionResponse> {
    const req: CompleteUploadSessionRequest = {
      hashes: '',
    };
    return this.http.post<CompleteUploadSessionResponse>(
        ApiUrls.UPLOAD_SESSION_ID.replace('{sessionUUID}', sessionUUID) + '/complete', req, {
          headers: {[ApiOptions.IN_BACKGROUND]: 'true'}
        });
  }

  deleteFile(fileUUID: string): Observable<void> {
    return this.http.delete<void>(ApiUrls.DELETE_FILE.replace('{fileUUID}', fileUUID.toString()));
  }

  controlDocDaFirmareObbligatori(documenti: Array<Documento>, listaDocDaFirmareObbligatori: TipiDocumentoObbligatori[],
                                 cfUser: string, taskCreationTime: string) {
    const tipiDocumentoMancanti: TipiDocumentoObbligatori[] = [];
    let tipoDocMancante = false;
    listaDocDaFirmareObbligatori?.forEach((tipoDocumento: TipiDocumentoObbligatori)  => {
      const filtroByType = documenti.filter(docs => docs.tipo?.codice === tipoDocumento.codice);
      if (!filtroByType || filtroByType.length === 0){
        tipoDocMancante = true;
      }
      filtroByType.forEach(documento => {
        const contenutiFirmati = documento.contenuti?.filter(contenuto => contenuto.tipo?.codice === TipoContenutoDocumentoEnum.FIRMATO &&
          contenuto.infoVerificaFirme?.find(info => info.codiceFiscaleFirmatario === cfUser
            && info.dataApposizione && taskCreationTime &&
            // tslint:disable-next-line:max-line-length
            new Date(info.dataApposizione).getTime() >= new Date(taskCreationTime).getTime()));
        if ((!contenutiFirmati || contenutiFirmati.length === 0)){
          tipoDocMancante = true;
        }
      });
      if (tipoDocMancante) {
        tipiDocumentoMancanti.push(tipoDocumento);
      }
      tipoDocMancante = false;
    });

    return tipiDocumentoMancanti;
  }

  getTipiDocumento(codiciTipiDocumento: Array<string>): Observable<TipoDocumento[]> {
    const params = { codici: codiciTipiDocumento };
    return this.http.get<TipoDocumento[]>(ApiUrls.GET_TIPI_DOCUMENTO, {params});
  }

  ricercaTipiDocumento(codici: Array<string>, add: boolean, codicePadre: string | null,
                       codiceTipoPratica: string | null): Observable<TipoDocumento[]> {
    let params = {};

    params = {...params, codici , addFormatoFile : add.toString()};
    if (codicePadre){
      params = {
        ...params,
        codicePadre
      };
    }
    if (codiceTipoPratica){
      params = {
        ...params,
        codiceTipoPratica
      };
    }

    return this.http.get<TipoDocumento[]>(ApiUrls.RICERCA_TIPI_DOCUMENTO, {params});
  }

  uploadFileWithUploadSession(
    file: File,
    opts?: {
      onProgress?: (num: number, desc: string) => void,
      cancellationSignal?: Subject<any>,
    }
  ): Observable<SessionUploadResult> {

    // chunks da 2 MB kB
    const chunkSize = 2 * 1024 * 1024;

    const progressCb = (num: number, desc: string) => {
      if (opts?.onProgress) {
        this.logger.debug(desc);
        opts.onProgress(num, desc);
      }
    };

    const totSize = file.size;

    const statusHolder: {
      cancel: boolean,
      subscriptions: Subscription[]
    } = {
      cancel: false,
      subscriptions: [],
    };

    // create upload session
    const mainObs = of(null).pipe(
      tap(() => {
        progressCb(0, 'preparazione del caricamento ...');
        if (opts?.cancellationSignal) {
          statusHolder.subscriptions.push(opts?.cancellationSignal.subscribe(() => {
            statusHolder.cancel = true;
          }));
        }
      }),
      mergeMap(() => this.createUploadSession(file.name, file.type, file.size)),
      tap(s => this.logger.debug('created upload session ' + s.sessionUUID)),
      mergeMap(session => {
        // upload parts
        let partNumber = 0;
        const obs: Observable<any>[] = [];
        for (let offset = 0; offset < file.size; offset += chunkSize ){
          const partObs = of([partNumber, offset, chunkSize]).pipe(
            tap(n => this.logger.debug('uploading chunk ' + n[0])),
            mergeMap(n => {
              const chunk = file.slice( n[1], n[1] + n[2] );
              return forkJoin({
                info: of(n),
                upload: of(null).pipe(
                  mergeMap(() => {
                    if (statusHolder.cancel) {
                      throw new Error('canceled');
                    }
                    return this.uploadSessionPart(session.sessionUUID, n[0], chunk).pipe(
                      Utils.retryWithDelay(1000, 3)
                    );
                  })
                )
              });
            }),
            tap(x => {
              let percentage = 100.0 * x.info[1] / totSize;
              if (percentage < 0) {
                percentage = 0;
              }
              if (percentage > 100) {
                percentage = 100;
              }
              this.logger.debug('progress: ' + percentage);
              progressCb(percentage, 'caricamento in corso ...');
            }),
            delay(environment.httpMockDelay),
          );
          obs.push(partObs);
          partNumber ++;
        }
        return forkJoin({
          session: of(session),
          parts: concat(...obs)
        });
      }),
      tap(() => progressCb(100, 'finalizzazione del caricamento ...')),
      tap(s => this.logger.debug('completing upload session ' + s.session.sessionUUID)),
      mergeMap(results => {
        // call complete on the session
        if (statusHolder.cancel) {
          throw new Error('canceled');
        }
        return forkJoin({
          session: of(results.session),
          parts: of(results.parts),
          completion: this.completeUploadSession(results.session.sessionUUID),
        });
      }),
      tap(s => this.logger.debug('completed upload session ' + s.session.sessionUUID + ' to content ' + s.completion.uploadUUID)),
      mergeMap(results => {
        // build response
        const r: SessionUploadResult = {
          sessionUUID: results.session.sessionUUID,
          uploadUUID: results.completion.uploadUUID ?? '',
        };
        return of(r);
      }),
    );

    return mainObs.pipe(
      finalize(() => {
        for (const sub of statusHolder.subscriptions) {
          sub.unsubscribe();
        }
      }),
    );
  }

  /** GET: Preview del documento senza firma o con firma ma sbustato */
  getContenutoIndex(idDocumento: number, idContenuto: number) {
    return this.http.get(ApiUrls.GET_CONTENUTO_DOCUMENTO_INDEX
      .replace('{idDocumento}', idDocumento.toString())
      .replace('{idContenuto}', idContenuto.toString()), { responseType: 'blob' });
  }

  getInfoFirmaFea(idDocumento: number): Observable<InfoFirmaFea[]>{
    return this.http.get<InfoFirmaFea[]>(ApiUrls.GET_INFO_FEA.replace('{idDocumento}', idDocumento.toString()));
  }

}
