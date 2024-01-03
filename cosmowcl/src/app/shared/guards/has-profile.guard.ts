/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Utils } from '../utilities/utilities';
import { SecurityService } from '../services/security.service';
import { Constants } from '../constants/constants';
import { NGXLogger } from 'ngx-logger';
import { RedirectService } from '../services/redirect.service';
import { map } from 'rxjs/operators';

@Injectable()
export class HasProfileGuard implements CanActivate {
  constructor(
    private logger: NGXLogger,
    private authService: SecurityService,
    private redirectService: RedirectService,
    private router: Router) { }

  public canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

    return this.authService.getCurrentUser().pipe(
      map(user => {
        let result: boolean;

        if (user.anonimo) {
          this.logger.info('utente anonimo. redirigo a pagina di login');
          this.redirectLogin(route);
          result = false;
        } else if (!Utils.hasValidProfile(user)) {
          this.logger.info('ruolo non selezionato. redirigo a pagina di login');
          this.redirectLogin(route);
          result = false;
        } else {
          result = true;
        }
        return result;
      })
    );
  }

  private redirectLogin(route: ActivatedRouteSnapshot): void {
    this.redirectService.saveOriginalRequestedUrl(route);
    this.router.navigate([Constants.ROUTE_PAGINA_LOGIN]);
  }
}
