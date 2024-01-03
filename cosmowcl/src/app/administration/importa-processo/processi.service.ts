/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { FormLogico } from 'src/app/shared/models/api/cosmobusiness/formLogico';
import { ApiUrls } from 'src/app/shared/utilities/apiurls';

@Injectable()
export class ProcessiService {

  constructor(
    private http: HttpClient) {
  }

  deployProcesso(formData: FormData): Observable<any> {
    return this.http.post<any>(ApiUrls.PROCESSI, formData);
  }

  importProcesso(tenantId: string, preview: boolean, conflictResolutionInput: any[], sourceContent: string): Observable<ImportResponse> {
    return this.http.post<ImportResponse>(ApiUrls.IMPORTA_TIPO_PRATICA, {
      tenantId,
      preview,
      conflictResolutionInput,
      sourceContent,
    });
  }
}

export interface ImportResponse {
  tipoPratica?: string;
  formLogici?: FormLogico[];
  preview?: boolean;
  processDeployment?: any;
  message?: string;
  exitReason?: string;
  done?: boolean;
  conflicts?: PendingConflict[];
  messages?: ImportMessage[];
}

export interface PendingConflict {
  existingValue: string;
  importValue: string;
  message: string;
  path: string;
  fullKey: string;
  fieldName: string;
}

export interface FieldConflictResolutionInput {
  fullKey: string;
  acceptedValue: string;
  action: string;
}

export interface ImportMessage {
  livello: string;
  messaggio: string;
}
