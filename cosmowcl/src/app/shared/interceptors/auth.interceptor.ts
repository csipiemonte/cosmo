/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { NGXLogger } from 'ngx-logger';
import {
  Observable,
  of,
} from 'rxjs';
import {
  map,
  mergeMap,
  tap,
} from 'rxjs/operators';
import { environment } from 'src/environments/environment';

import { Constants } from '../constants/constants';
import { SecurityService } from '../services/security.service';
import { EnvironmentRelatedFunction } from '../utilities/enviroment-enum';
import { Utils } from '../utilities/utilities';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private logger: NGXLogger,
    private securityService: SecurityService,
    private router: Router
  ) { }

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    this.logger.debug('http request to ' + request.url);
    const interceptor = this;
    this.logger.debug('Request before multi host check', request);
    request = Object.assign(
      request,
      Utils.performEnvironmentRelatedAction(EnvironmentRelatedFunction.REQUEST_INTERCEPTION, request) as HttpRequest<any>);
    this.logger.debug('Request after multi host check', request);
    const smCredHeader = request.headers.get('X-Cosmo-Skip-Credentials');
    if (!smCredHeader) {
      request = request.clone({
        withCredentials: true,
        headers: request.headers.set(
          'Authorization', btoa(environment.basicAuthUsername + ':' + environment.basicAuthPassword)
        )
      });
    }

    return next.handle(request).pipe(
      mergeMap(event => {
        this.logger.trace('Event', event);
        if (event instanceof HttpResponse) {
          this.logger.trace('Event is a response');
          const smHeader = request.headers.get('X-Cosmo-Session-Management');
          if (smHeader) {
            this.logger.debug('skipped identity check on session management API call');
            return of(event);
          }
          const remoteHeader = event.headers.get(Constants.X_COSMO_IDENTITA_ATTIVA);
          if (remoteHeader) {
            this.logger.trace('remote header X_COSMO_IDENTITA_ATTIVA presente');
            return this.securityService.getCurrentUser().pipe(
              mergeMap(currentUser => {
                if (currentUser.hashIdentita !== remoteHeader) {
                  this.logger.warn('identity mismatch: local = ' + currentUser.hashIdentita + ', remote = ' + remoteHeader);
                  return this.securityService.fetchCurrentUser().pipe(
                    map(newUser => {
                      this.logger.info('new user is', newUser);
                    })
                  );
                }
                return of(currentUser);
              })
            ).pipe(
              mergeMap(() => of(event))
            );
          }
        }
        return of(event);
      }),
      tap(
        (event: HttpEvent<any>) => { },
        (err: any) => {
          if (err instanceof HttpErrorResponse) {
            this.logger.debug('got status ', err.status);

            if (err.status === 401) {
              this.logger.warn('got 401 from server', err);

              const remoteHeader = err.headers.get(Constants.X_COSMO_IDENTITA_ATTIVA);
              if (remoteHeader && remoteHeader.length) {
                this.router.navigate([Constants.ROUTE_PAGINA_ERROR_UNATHORIZED]);
              } else {
                // sessione scaduta
                this.logger.warn('sessione scaduta. eseguo il logout locale');
                // in caso di sessione scaduta devo ricaricare l'utente e tornare al login
                interceptor.securityService.sessioneScaduta().subscribe(userInfo => {
                  interceptor.router.navigate([Constants.ROUTE_PAGINA_LOGIN]);
                });
              }
            }
          }
        }
      )
    );
  }

}
