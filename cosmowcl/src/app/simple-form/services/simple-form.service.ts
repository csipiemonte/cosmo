/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PraticheService } from 'src/app/shared/services/pratiche.service';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';
import { SimpleForm } from '../models/simple-form.model';

@Injectable({
  providedIn: 'root'
})
export class SimpleFormService {

  constructor(private http: HttpClient, private praticheService: PraticheService) { }

  storedSimpleForm: SimpleForm | null = null;

  getSimpleFormFromFormKey(formKey: string): Observable<SimpleForm> {

    return this.http.get<SimpleForm>(
      ApiUrls.FORM_DEFINITION_BY_FORM_KEY.replace('{formKey}', formKey));
  }

  getSimpleFormFromTaskId(idTask: string): Observable<SimpleForm> {

    return this.http.get<SimpleForm>(
      ApiUrls.FORM_DEFINITION_BY_TASK_ID.replace('{idTask}', idTask));
  }

}
