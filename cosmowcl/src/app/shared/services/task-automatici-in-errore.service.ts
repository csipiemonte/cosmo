/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DeadLetterJobAction } from '../models/api/cosmobusiness/deadLetterJobAction';
import { DeadLetterJobsResponse } from '../models/api/cosmobusiness/deadLetterJobsResponse';
import { ApiUrls } from '../utilities/apiurls';

@Injectable()
export class TaskAutomaticiInErroreService {

  constructor( private http: HttpClient) { }

   /** GET: Restituisce una lista di deadletter-jobs. */
   getQueue(): Observable<DeadLetterJobsResponse> {
    return this.http.get<DeadLetterJobsResponse>(ApiUrls.DEADLETTER_JOBS);
  }

  /** POST: Invia richiesta di esecuzione di un deadletter-job */
  postJobs(jobId: string, action: DeadLetterJobAction): Observable<void>{
    return this.http.post<void>(ApiUrls.DEADLETTER_JOB_POST.replace('{jobId}',jobId),action);
  }
}
