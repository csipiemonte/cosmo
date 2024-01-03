/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';
import { AcquisizioneLockRequest } from '../models/api/cosmobusiness/acquisizioneLockRequest';
import { Lock } from '../models/api/cosmobusiness/lock';
import { RilascioLockRequest } from '../models/api/cosmobusiness/rilascioLockRequest';
import { ApiUrls } from '../utilities/apiurls';
import { SecurityService } from './security.service';

@Injectable()
export class LockService {

  constructor(
    private securityService: SecurityService,
    private http: HttpClient
  ) {
    // NOP
  }

  public ttl(lock: Lock): number | null {
    if (!lock?.dataScadenza) {
      return null;
    }
    const now = new Date();
    const scadenza = new Date(lock.dataScadenza);
    const ttl = (scadenza.getTime() - now.getTime());
    if (ttl < 0) {
      return 0;
    } else {
      return ttl;
    }
  }

  public acquireOrGet(resourceCode: string): Observable<{
    existing: Lock | null,
    acquired: Lock | null,
  }> {
    return this.getLock(resourceCode).pipe(
      mergeMap(existing => {
        if (!existing) {
          return this.acquireLock(resourceCode).pipe(map(acquired => {
            return {
              existing: null,
              acquired
            };
          }));
        } else {
          return this.owned(existing).pipe(map(owned => {
            if (owned) {
              return {
                existing: null,
                acquired: existing
              };
            } else {
              return {
                existing,
                acquired: null
              };
            }
          }));
        }
      })
    );
  }

  public owned(lock: Lock): Observable<boolean> {
    return this.securityService.getSessionKey().pipe(
      map(sessionKey => lock.codiceOwner === sessionKey)
    );
  }

  public getLock(resourceCode: string): Observable<Lock | null> {
    if (!resourceCode?.length) {
      throw new Error('ResourceCode is required');
    }

    return this.http.get<Lock>(ApiUrls.LOCK, {params: {
      codiceRisorsa: resourceCode
    }}).pipe(
      map(lock => {
        if (lock?.codiceOwner?.length) {
          return lock;
        } else {
          return null;
        }
      })
    );
  }

  public renewLock(lock: Lock, duration = 5 * 60 * 1000): Observable<Lock | null> {
    return this.acquireLock(lock.codiceRisorsa, duration);
  }

  public acquireLock(resourceCode: string, duration = 5 * 60 * 1000): Observable<Lock | null> {
    if (!resourceCode?.length) {
      throw new Error('ResourceCode is required');
    }
    if (!duration || duration < 1000) {
      throw new Error('Duration is required and should be at least 1000 ms');
    }

    const payload: AcquisizioneLockRequest = {
      codiceOwner: '',
      codiceRisorsa: resourceCode,
      durata: duration,
    };

    return this.securityService.getSessionKey().pipe(
      mergeMap(sessionKey => this.http.post<Lock>(ApiUrls.LOCK, {
        ...payload,
        codiceOwner: sessionKey
      }))
    );
  }

  public releaseLock(resourceCode: string, codiceOwner?: string): Observable<any> {
    if (!resourceCode?.length) {
      throw new Error('ResourceCode is required');
    }

    const payload: RilascioLockRequest = {
      codiceOwner: codiceOwner ?? '',
      codiceRisorsa: resourceCode,
    };

    // use navigator.sendBeacon(ApiUrls.LOCK)

    return this.securityService.getSessionKey().pipe(
      mergeMap(sessionKey => this.http.request<any>('delete', ApiUrls.LOCK, {
        body: {
          ...payload,
          codiceOwner: (codiceOwner ?? sessionKey)
        }
      }))
    );
  }

  public releaseLockViaBeacon(resourceCode: string, codiceOwner: string): void {
    if (!resourceCode?.length) {
      throw new Error('ResourceCode is required');
    }
    if (!codiceOwner?.length) {
      throw new Error('CodiceOwner is required');
    }

    const payload: RilascioLockRequest = {
      codiceOwner: codiceOwner ?? '',
      codiceRisorsa: resourceCode,
    };

    navigator.sendBeacon(ApiUrls.LOCK_RELEASE,
      new Blob([JSON.stringify(payload)], {type : 'application/json; charset=UTF-8'})
    );
  }
}
