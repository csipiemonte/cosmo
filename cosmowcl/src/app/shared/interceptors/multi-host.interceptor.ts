/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable()
export class MultiHostInterceptor implements HttpInterceptor {

    constructor(
        private logger: NGXLogger) {}
    intercept(
        request: HttpRequest<any>,
        next: HttpHandler
      ): Observable<HttpEvent<any>> {
        this.logger.debug(request);

        //TODO: FIX per  doppia esposizione

        if ('env-01' === environment.environmentName) {
            let newUrl = '';
            this.logger.debug(' Window location hostname is ', window.location.hostname);
            if (!environment.beServer.includes(window.location.hostname) && request.url.startsWith('http')) {
                newUrl = request.url.replace(environment.beServer, environment.intracomHostname);
                this.logger.debug(' Redirecting http request to ', newUrl);
            } else if (!environment.beServer.includes(window.location.hostname) && request.url.startsWith('ws')) {
                newUrl = request.url.replace(environment.wsServer, environment.alternativeWsServer);
                this.logger.debug(' Redirecting websocket request to ', newUrl);
            }

            if (newUrl.length > 0) {
                const httpRequest = new HttpRequest(request.method as any, newUrl);
                request = Object.assign(request, httpRequest);
            }
        } else if ('env-02' === environment.environmentName) {
          let newUrl = '';
          this.logger.debug(' Window location hostname is ', window.location.hostname);
          if (!environment.beServer.includes(window.location.hostname) && request.url.startsWith('http')) {
              newUrl = request.url.replace(environment.beServer, environment.intranetHostname);
              this.logger.debug(' Redirecting http request to ', newUrl);
          } else if (!environment.beServer.includes(window.location.hostname) && request.url.startsWith('ws')) {
              newUrl = request.url.replace(environment.wsServer, environment.alternativeWsServer);
              this.logger.debug(' Redirecting websocket request to ', newUrl);
          }

          if (newUrl.length > 0) {
              const httpRequest = new HttpRequest(request.method as any, newUrl);
              request = Object.assign(request, httpRequest);
          }
      }

        return next.handle(request);
      }
}
