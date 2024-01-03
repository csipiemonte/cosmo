/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { concat, forkJoin, Observable, of, Subject, Subscription } from 'rxjs';
import { delay, finalize, mergeMap, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { CompleteUploadDocumentiZipSessionRequest } from '../models/api/cosmo/completeUploadDocumentiZipSessionRequest';
import { CompleteUploadDocumentiZipSessionResponse } from '../models/api/cosmo/completeUploadDocumentiZipSessionResponse';
import { CompleteUploadSessionRequest } from '../models/api/cosmo/completeUploadSessionRequest';
import { CompleteUploadSessionResponse } from '../models/api/cosmo/completeUploadSessionResponse';
import { CreateUploadSessionRequest } from '../models/api/cosmo/createUploadSessionRequest';
import { CreateUploadSessionResponse } from '../models/api/cosmo/createUploadSessionResponse';
import { Esito } from '../models/api/cosmo/esito';
import { FileUploadPraticheResult } from '../models/api/cosmo/fileUploadPraticheResult';
import { CaricamentoPratica } from '../models/api/cosmopratiche/caricamentoPratica';
import { CaricamentoPraticaRequest } from '../models/api/cosmopratiche/caricamentoPraticaRequest';
import { CaricamentoPraticheResponse } from '../models/api/cosmopratiche/caricamentoPraticheResponse';
import { StatoCaricamentoPratica } from '../models/api/cosmopratiche/statoCaricamentoPratica';
import { SessionUploadResult } from '../models/files/session-upload-result';
import { ApiOptions, ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';
import { SecurityService } from './security.service';
import { InfoFile } from '../models/api/cosmo/infoFile';

@Injectable({
  providedIn: 'root'
})
export class CaricamentoPraticheService {

  constructor(private http: HttpClient, private logger: NGXLogger) {}

 
  uploadPratiche(formData: FormData): Observable<FileUploadPraticheResult> {
    return this.http.post<FileUploadPraticheResult>(ApiUrls.UPLOAD_FILE_PRATICHE, formData);
  }

  startUploading(req: CaricamentoPraticaRequest): Observable<CaricamentoPratica>{

    return this.http.post<CaricamentoPratica>(ApiUrls.PRATICHE_UPLOAD_FILE, req);
  }


  getCaricamentiPratica(filter: string): Observable<CaricamentoPraticheResponse>{

    let params = new HttpParams();
    params = params.set('filter', filter);

    return this.http.get<CaricamentoPraticheResponse>(ApiUrls.PRATICHE_UPLOAD_FILE, { params } );
  }

  getCaricamentiPraticaId(filter: string, id: string, esporta: boolean): Observable<CaricamentoPraticheResponse>{

    let params = new HttpParams();
    params = params.set('filter', filter);
    params = params.set('export', esporta.toString());

    return this.http.get<CaricamentoPraticheResponse>(ApiUrls.PRATICHE_UPLOAD_FILE_ID.replace('{id}', id), { params } );
  }

  getCaricamentiInBozza(filter: string): Observable<CaricamentoPraticheResponse>{

    let params = new HttpParams();
    params = params.set('filter', filter);

    return this.http.get<CaricamentoPraticheResponse>(ApiUrls.PRATICHE_UPLOAD_CARICAMENTO_IN_BOZZA, { params } );
  }

  getFilePratiche(path: string): Observable<InfoFile[]>{
    let params = new HttpParams();
    params = params.set('path', path);

    return this.http.get<InfoFile[]>(ApiUrls.FILE_PRATICHE, { params });
  }

  getStatiCaricamento(): Observable<StatoCaricamentoPratica[]>{
    return this.http.get<StatoCaricamentoPratica[]>(ApiUrls.PRATICHE_UPLOAD_FILE_STATI);
  }

  createUploadSession(fileName: string, mimeType: string, size: number): Observable<CreateUploadSessionResponse> {
    const req: CreateUploadSessionRequest = {
      fileName,
      size,
      mimeType,
    };
    return this.http.post<CreateUploadSessionResponse>(ApiUrls.UPLOAD_SESSION_FILE_ZIP, req, {
      headers: {[ApiOptions.IN_BACKGROUND]: 'true'}
    });
  }

  uploadSessionPart(sessionUUID: string, partNumber: number, chunk: Blob): Observable<void> {
    const formData = new FormData();
    formData.append('payload', chunk, 'part' + partNumber);
    return this.http.post<void>(ApiUrls.UPLOAD_SESSION_FILE_ZIP_ID.replace('{sessionUUID}', sessionUUID) + '/parts/'
      + partNumber, formData, {
      headers: {[ApiOptions.IN_BACKGROUND]: 'true'}
    });
  }

  completeUploadSession(sessionUUID: string, folderName: string): Observable<CompleteUploadDocumentiZipSessionResponse> {
    const req: CompleteUploadDocumentiZipSessionRequest = {
      hashes: '',
      folderName
    };
    return this.http.post<CompleteUploadDocumentiZipSessionResponse>(
        ApiUrls.UPLOAD_SESSION_FILE_ZIP_ID.replace('{sessionUUID}', sessionUUID) + '/complete', req, {
          headers: {[ApiOptions.IN_BACKGROUND]: 'true'}
        });
  }

  completeUploading(id: string, request: CaricamentoPraticaRequest): Observable<CaricamentoPratica>{
    return this.http.put<CaricamentoPratica>(ApiUrls.PRATICHE_UPLOAD_FILE_ID.replace('{id}', id), request);
  }

  eliminaCaricamentoPratica(id: string): Observable<void>{
    return this.http.delete<void>(ApiUrls.PRATICHE_UPLOAD_FILE_ID.replace('{id}', id));
  }

  uploadDocumentiZipWithUploadSession(
    file: File,
    folderName: string,
    opts?: {
      onProgress?: (num: number, desc: string) => void,
      cancellationSignal?: Subject<any>,
    }
  ): Observable<string> {

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
          completion: this.completeUploadSession(results.session.sessionUUID, folderName),
        });
      }),
      tap(s => this.logger.debug('completed upload session ' + s.session.sessionUUID)),
      mergeMap(results => {
        // build response
        return of(results.completion.esito ?? '');
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

}
