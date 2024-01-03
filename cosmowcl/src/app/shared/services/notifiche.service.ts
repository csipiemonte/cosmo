/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  HttpClient,
  HttpParams,
} from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import {
  PaginaNotifiche,
} from 'src/app/shared/models/api/cosmonotifications/paginaNotifiche';

import {
  ApiOptions,
  ApiUrls,
} from '../utilities/apiurls';

@Injectable()
export class NotificheService {

    constructor(private http: HttpClient) {
        // NOP
    }

    /** GET: Restituisce tutte le notifiche. */
    getAllNotifications( limit: number = 10, offset: number = 0  ): Observable<PaginaNotifiche> {

        const params = new HttpParams()
            .append('offset', '' + offset)
            .append('limit', '' + limit);
        return this.http.get<PaginaNotifiche>(ApiUrls.NOTIFICHE , { params });
    }

    // PUT : segna una notifica come letta (e la ritorna)
    async markAsRead(idNotifica: number): Promise<Notification> {
        return this.http.put<Notification>(
            ApiUrls.NOTIFICHE + '/' + idNotifica,
            {},
            {
                headers: {
                    [ApiOptions.IN_BACKGROUND]: 'true'
                }
            }).toPromise();
    }

    // PUT : segna una notifica come letta (e la ritorna)
    async markAllAsRead(): Promise<void> {
        return this.http.put<void>(
            ApiUrls.NOTIFICHE_TUTTE,
            {},
            {
                headers: {
                    [ApiOptions.IN_BACKGROUND]: 'true'
                }
            }).toPromise();
    }
}
