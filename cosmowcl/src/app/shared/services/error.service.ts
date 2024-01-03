/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { NGXLogger } from 'ngx-logger';
import { Message, MessageType } from '../models/message.model';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  private errorContextSubject = new Subject<Message>();
  public errorContext$: Observable<Message> = this.errorContextSubject.asObservable();

  private cachedError: any | null = null;

  constructor(private logger: NGXLogger, private router: Router) {
    this.logger.trace('building service');
  }

  handleFatalError(error: any): void {
    this.setError(this.parseError(error));
    this.router.navigateByUrl('/error');
  }

  private setError(error: any): void {
    this.cachedError = error;
  }

  getError(): any {
    return this.cachedError;
  }

  emit( message: Message ) {
    this.logger.debug('emitting message', message);
    this.errorContextSubject.next( message );
  }

  error(error: Error | HttpErrorResponse | string, dismissAfter: number | null = null) {
    const message: Message = this.parseMessagePayload( error, MessageType.ERROR, dismissAfter );
    this.logger.debug('emitting error message', message);
    this.errorContextSubject.next( message );
  }

  warning(error: Error | HttpErrorResponse | string, dismissAfter: number | null = null) {
    const message: Message = this.parseMessagePayload( error, MessageType.WARNING, dismissAfter );
    this.logger.debug('emitting error message', message);
    this.errorContextSubject.next( message );
  }

  info(error: Error | HttpErrorResponse | string, dismissAfter: number | null = null) {
    const message: Message = this.parseMessagePayload( error, MessageType.INFO, dismissAfter );
    this.logger.debug('emitting info message', message);
    this.errorContextSubject.next( message );
  }

  success(error: Error | HttpErrorResponse | string, dismissAfter: number | null = null) {
    const message: Message = this.parseMessagePayload( error, MessageType.SUCCESS, dismissAfter );
    this.logger.debug('emitting success message', message);
    this.errorContextSubject.next( message );
  }

  public parseError(raw: HttpErrorResponse | Error | string): Message {
    return this.parseMessagePayload(raw, MessageType.ERROR, null);
  }

  private parseMessagePayload(
    raw: HttpErrorResponse | Error | string,
    messageType: MessageType, dismissAfter: number | null = null): Message {

    this.logger.trace('parsing message payload', raw);

    if (raw instanceof HttpErrorResponse) {
      this.logger.trace('recognized message payload as HttpErrorResponse');
      return new Message(
        messageType,
        raw.error?.title || raw.message || raw.statusText,
        raw.statusText.toUpperCase(),
        raw.status,
        dismissAfter
      );
    } else if (raw instanceof Error) {
      this.logger.trace('recognized message payload as Error');
      return new Message( messageType, raw.message );
    } else if ((raw as any).status && (raw as any).status >= 400 && (raw as any).error && (raw as any).error.message) {
      this.logger.trace('recognized message payload as mocked error');
      return new Message(
        messageType,
        (raw as any).error.message,
        (raw as any).error.message.toUpperCase(),
        (raw as any).status,
        dismissAfter
      );
    } else {
      this.logger.trace('recognized message payload as raw, treating as string');
      return new Message( messageType, raw, undefined, undefined, dismissAfter );
    }
  }

}
