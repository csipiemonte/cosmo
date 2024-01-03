/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { Observable, Subject, Subscription, timer } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { OperazioneAsincrona } from '../models/api/cosmobusiness/operazioneAsincrona';
import { AsyncTaskStatus, OperazioneAsincronaWrapper } from '../models/async';
import { ApiOptions, ApiUrls } from '../utilities/apiurls';
import { Utils } from '../utilities/utilities';

@Injectable()
export class AsyncTaskService {

  constructor(
    private logger: NGXLogger,
    protected http: HttpClient,
  ) {
    // NOP
  }

  get<T>(uuid: string, etag?: string): Observable<HttpResponse<OperazioneAsincronaWrapper<T>>> {
    const headers: {[key: string]: string} = {};
    if (etag) {
      headers['If-None-Match'] = etag;
    }
    return this.http.get<OperazioneAsincronaWrapper<T>>(
      ApiUrls.OPERAZIONI_ASINCRONE_SINGLE.replace('{uuid}', uuid),
      {
        observe: 'response',
        headers
    });
  }

  delete(uuid: string, inBackground = false): Observable<HttpResponse<any>> {
    const options = inBackground ? {
      headers: {[ApiOptions.IN_BACKGROUND]: 'true'}
    } : {};

    return this.http.delete<any>(ApiUrls.OPERAZIONI_ASINCRONE_SINGLE.replace('{uuid}', uuid), {
      ...options,
      observe: 'response'
    });
  }

  watcher<T>(uuid: string): AsnycTaskWatcher<T> {
    Utils.require(uuid, 'uuid');

    return new AsyncTaskWatcherImpl(
      this.logger,
      this,
      uuid
    );
  }
}

export interface AsnycTaskWatcher<T> {
  started: Subject<OperazioneAsincronaWrapper<T>>;
  updated: Subject<OperazioneAsincronaWrapper<T>>;
  completed: Subject<OperazioneAsincronaWrapper<T>>;
  failed: Subject<OperazioneAsincronaWrapper<T>>;
  finalized: Subject<OperazioneAsincronaWrapper<T>>;
  fetchError: Subject<Error | HttpErrorResponse>;
  result: Subject<OperazioneAsincronaWrapper<T>>;
  start: () => void;
}

class AsyncTaskWatcherImpl<T> implements AsnycTaskWatcher<T> {

  isStarted = false;
  task: OperazioneAsincronaWrapper<T> | null = null;
  fetching = false;
  errorFetching = null;
  refreshTimerSubscription: Subscription | null = null;
  isFinished = false;
  steps: OperazioneAsincrona[] | null = null;
  lastETag: string | null = null;

  private refreshTimer: Observable<number> | null = null;

  private delayIfChanged = 2000;
  private delayIfNotChanged = 4000;
  private hasChangedCounter = 2;
  private hasChangedCounterMax = 6;
  private hasChangedCounterIncrement = 2;
  private tickInterval = 100;
  private delayCounter = 0;

  started: Subject<OperazioneAsincronaWrapper<T>>;
  updated: Subject<OperazioneAsincronaWrapper<T>>;
  completed: Subject<OperazioneAsincronaWrapper<T>>;
  failed: Subject<OperazioneAsincronaWrapper<T>>;
  finalized: Subject<OperazioneAsincronaWrapper<T>>;
  result: Subject<OperazioneAsincronaWrapper<T>>;
  fetchError: Subject<Error | HttpErrorResponse>;

  constructor(
    private logger: NGXLogger,
    private taskService: AsyncTaskService,
    private id: string,
  ) {
    this.started = new Subject<OperazioneAsincronaWrapper<T>>();
    this.updated = new Subject<OperazioneAsincronaWrapper<T>>();
    this.completed = new Subject<OperazioneAsincronaWrapper<T>>();
    this.failed = new Subject<OperazioneAsincronaWrapper<T>>();
    this.finalized = new Subject<OperazioneAsincronaWrapper<T>>();
    this.result = new Subject<OperazioneAsincronaWrapper<T>>();
    this.fetchError = new Subject<Error | HttpErrorResponse>();
  }

  public start(): void {

    this.refreshTimer = timer(500, this.tickInterval);
    this.refreshTimerSubscription = this.refreshTimer.subscribe(() => {
      this.delayCounter += this.tickInterval;
      if (this.delayCounter >= (this.hasChangedCounter > 0 ? this.delayIfChanged : this.delayIfNotChanged)) {
        this.delayCounter = 0;
        this.refresh();
      }
    });
  }

  private refresh() {

    if (this.fetching || this.isFinished || !this.id) {
      return;
    }
    this.fetching = true;

    this.taskService.get(this.id, this.lastETag ?? undefined).subscribe(response => {
      let hasChanged = false;

      const task = response.body;

      if (task) {
        this.lastETag = response.headers.get('etag');

        if (( !this.task || (this.task.versione !== task.versione) )) {
          this.logger.debug('task report has changed from ' + (this.task ? this.task.versione : 'NONE') + ' to ' + task.versione);
          hasChanged = true;
          this.hasChangedCounter += this.hasChangedCounterIncrement;
          if (this.hasChangedCounter > this.hasChangedCounterMax) {
            this.hasChangedCounter = this.hasChangedCounterMax;
          }
          this.task = task as any;
          this.steps = task.steps ?? [];

          if (!this.isStarted) {
            this.isStarted = true;
            this.started.next(Utils.require(this.task));
            this.started.complete();
          }

          this.updated.next(Utils.require(this.task));

          if (task.stato === AsyncTaskStatus.COMPLETED) {
            this.handleFinished(true);
          } else if (task.stato === AsyncTaskStatus.FAILED) {
            this.handleFinished(false);
          }
        }
      }

      if (!hasChanged) {
        this.handleUnchanged();
      }

      this.fetching = false;
    }, err => {
      if (err instanceof HttpErrorResponse && err.status === 304) {
        this.handleUnchanged();
      } else {
        this.errorFetching = err;
        this.fetchError.next(err);
      }
      this.fetching = false;
    });
  }

  private handleUnchanged(): void {
    this.logger.debug('task was unchanged');
    if (this.hasChangedCounter > 0) {
      this.hasChangedCounter --;
    }
  }

  private handleFinished(success: boolean) {
    if (this.isFinished || !this.id) {
      return;
    }

    this.isFinished = true;

    if (this.refreshTimerSubscription) {
      this.refreshTimerSubscription.unsubscribe();
    }

    this.refreshTimer = null;
    const taskDef = Utils.require(this.task);

    if (success) {
      this.completed.next(taskDef);
      this.result.next(taskDef);
    } else {
      this.failed.next(taskDef);
      this.result.error(taskDef);
    }

    this.taskService.delete(this.id, true)
    .pipe(
      finalize(() => {
        this.finalized.next(Utils.require(this.task));

        this.updated.complete();
        this.fetchError.complete();
        this.completed.complete();
        this.failed.complete();
        this.result.complete();
        this.finalized.complete();
      })
    )
    .subscribe(() => {
      this.logger.info('long task deleted');
    }, err => {
      this.logger.error('error deleting long task', err);
    });
  }

}
