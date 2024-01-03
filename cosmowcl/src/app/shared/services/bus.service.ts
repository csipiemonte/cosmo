/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { PraticheService } from 'src/app/shared/services/pratiche.service';

@Injectable({
  providedIn: 'root'
})
export class BusService {
  private praticheSubject = new BehaviorSubject<boolean>(false);
  private notificheSubject = new BehaviorSubject<boolean>(false);
  private loggingOutSubject = new Subject<void>();

  constructor(private logger: NGXLogger,
              private praticheService: PraticheService) {
    this.logger.debug('Creazione servizio busService');
  }

  getCercaPratiche(): Observable<boolean> {
    return this.praticheSubject.asObservable();
  }

  setCercaPratiche(cercaPratiche: boolean) {
    this.praticheSubject.next(cercaPratiche);
  }

  getCercaNotifiche(): Observable<boolean> {
    return this.notificheSubject.asObservable();
  }

  setCercaNotifiche(cercaNotifiche: boolean) {
    this.notificheSubject.next(cercaNotifiche);
  }

  getLoggingOut(): Observable<void> {
    return this.loggingOutSubject.asObservable();
  }

  emitLoggingOut(): void {
    this.loggingOutSubject.next();
  }

  emitLoggedOut(): void {
    // not implemented
  }
}
