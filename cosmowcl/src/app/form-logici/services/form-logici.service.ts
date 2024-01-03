/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { FormLogiciData } from 'src/app/shared/models/form-logici/form-logici-data.model';

@Injectable()
export class FormLogiciService {

  formLogicoData = new BehaviorSubject<FormLogiciData>({});
  currentformLogicoData = this.formLogicoData.asObservable();

  constructor() { }

  getFormLogicoData(): Observable< FormLogiciData> {
    return this.formLogicoData.asObservable();
  }

  setFormLogicoData(formLogicoData: FormLogiciData) {
    this.formLogicoData.next(formLogicoData);
  }

}
