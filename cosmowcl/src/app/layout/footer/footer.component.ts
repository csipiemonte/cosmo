/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnDestroy, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { environment } from 'src/environments/environment';
import { PreferenzeEnteService } from 'src/app/shared/services/preferenze-ente.service';
import { Subscription } from 'rxjs';
import { ValorePreferenzeEnte } from 'src/app/shared/models/preferenze/valore-preferenze-ente.model';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { SecurityService } from 'src/app/shared/services/security.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit, OnDestroy {

  constructor(
    private preferenzeEnteService: PreferenzeEnteService,
    private securityService: SecurityService,
    private sanitizer: DomSanitizer) { }

  principal?: UserInfoWrapper;

  deployContextPath = environment.deployContextPath;

  valorePreferenzeEnte: ValorePreferenzeEnte = { header: '', logo: '', logoFooter: '', logoFooterCentrale: '', logoFooterDestra: ''};

  private preferenzeEnteSubscription: Subscription | null = null;

  ngOnInit(): void {
    this.securityService.principal$.subscribe(newPrincipal => {
      this.principal = newPrincipal;
    });

    this.preferenzeEnteSubscription = this.preferenzeEnteService.subscribePreferenze.subscribe(preferenze => {
      if (preferenze && preferenze.valore) {
        this.valorePreferenzeEnte = JSON.parse(preferenze.valore);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.preferenzeEnteSubscription) {
      this.preferenzeEnteSubscription.unsubscribe();
    }
  }

  getLogoToview() {
    return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + this.valorePreferenzeEnte.logoFooter);
  }

  getLogoCentraleToview() {
    return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + this.valorePreferenzeEnte.logoFooterCentrale);
  }

  getLogoDestraToview() {
    return this.sanitizer.bypassSecurityTrustResourceUrl('data:image/jpg;base64,' + this.valorePreferenzeEnte.logoFooterDestra);
  }

}
