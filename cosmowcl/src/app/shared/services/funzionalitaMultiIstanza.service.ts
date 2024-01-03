/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  HttpClient,
} from '@angular/common/http';
import { Injectable } from '@angular/core';

import {
  BehaviorSubject,
  Observable,
} from 'rxjs';
import { FormLogiciDataFromComponent } from '../models/form-logici/form-logici-data.model';

@Injectable()
export class FunzionalitaMultiIstanzaService {

  private MISubject = new BehaviorSubject<Map<string, FormLogiciDataFromComponent>>(new Map<string, FormLogiciDataFromComponent>());
  currentMI = this.MISubject.asObservable();
  constructor(
    private http: HttpClient
  ) {
    // NOP
  }

  getMultiIstanzaData(): Observable<any> {
    return this.MISubject.asObservable();
  }

  setMultiIstanzaData(funzionalitaPayload: Map<string, FormLogiciDataFromComponent>) {
    this.MISubject.next(funzionalitaPayload);
  }
}
