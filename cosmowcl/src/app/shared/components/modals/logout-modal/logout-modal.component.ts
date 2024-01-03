/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { SpinnerVisibilityService } from 'ng-http-loader';
import { of } from 'rxjs';
import { delay, mergeMap } from 'rxjs/operators';
import { BusService } from 'src/app/shared/services/bus.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { EnvironmentRelatedFunction } from 'src/app/shared/utilities/enviroment-enum';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-logout-modal',
  template: `
 <div class="modal-header">
    <h5 class="modal-title" id="modal-title">Logout</h5>
    <button type="button" class="close" aria-label="Close button" aria-describedby="modal-title" (click)="activeModal.dismiss('Cross click')">
      <span aria-hidden="true">&times;</span>
    </button>
  </div>
  <div class="modal-body" >
      <p>Si vuole effettuare il logout?</p>
  </div>
  <div class="modal-footer">
    <button ngbAutoFocus class="btn btn-primary btn-sm" type="button" (click)="logout(false)">Esci da COSMO</button>
    <button class="btn btn-primary btn-sm" type="button" (click)="logout(true)">Esci da tutti gli applicativi</button>
  </div>
`
})

export class LogoutModalComponent {

  constructor(
    public activeModal: NgbActiveModal,
    private securityService: SecurityService,
    private busService: BusService,
    private spinner: SpinnerVisibilityService,
  ) { }

  logout(all: boolean) {
    this.spinner.show();

    this.busService.emitLoggingOut();

    // delay per consentire il cleanup ai componenti
    of(null).pipe(
      delay(1000),
      mergeMap(() => this.securityService.localLogout(
        Utils.performEnvironmentRelatedAction(EnvironmentRelatedFunction.BE_SERVER_CHECK) as string))
    ).subscribe(
      res => {
        console.log('LOGOUT RES \n' + JSON.stringify(res));
        sessionStorage.clear();
        this.busService.emitLoggedOut();
        this.spinner.hide();

        if (all) {
          window.location.href = res.url;
        } else {

          const shibbolethSSOLogoutURL = Utils.performEnvironmentRelatedAction(EnvironmentRelatedFunction.LOGOUT);
          window.location.href = shibbolethSSOLogoutURL as string;
        }
      }, () => {
        this.spinner.hide();
      }
    );
  }

}

