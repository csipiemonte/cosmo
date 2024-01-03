/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';

@Injectable()
export class ProcessiService {

  constructor(
    private http: HttpClient) {
  }

  deployProcesso(formData: FormData): Observable<any> {
    return this.http.post<any>(ApiUrls.PROCESSI, formData);
  }

}
