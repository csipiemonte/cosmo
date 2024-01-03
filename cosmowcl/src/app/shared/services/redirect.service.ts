/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { Router, ActivatedRouteSnapshot } from '@angular/router';
import { Utils } from '../utilities/utilities';


@Injectable()
export class RedirectService {

  private originalRequestedUrl: string | null = null;

  constructor(
    private logger: NGXLogger,
    private router: Router
  ) {
    // NOP
    this.logger.debug('creo RedirectService');
  }

  saveOriginalRequestedUrl(route: ActivatedRouteSnapshot): void {
    let originalRequestedUrl = route.url.join('/');
    if (route.queryParams) {
      let first = true;
      for (const entry of Object.entries(route.queryParams)) {
        originalRequestedUrl += (first ? '?' : '&') + encodeURIComponent(entry[0]) + '=' + encodeURIComponent(entry[1]);
        first = false;
      }
    }

    this.originalRequestedUrl = originalRequestedUrl;
    this.logger.debug('saved originally requested URL', this.originalRequestedUrl);
  }

  getOriginalRequestedUrl(): string | null {
    return this.originalRequestedUrl;
  }

  clearOriginalRequestedUrl(): void {
    this.originalRequestedUrl = null;
  }

  public checkForQueryParam(): void {
    const originalPath = Utils.getParameterByName('originalPath');

    if (originalPath != null) {
      this.logger.info('got an originalPath query param, redirecting on local router', originalPath);
      this.router.navigateByUrl(originalPath);
    }
  }

}
