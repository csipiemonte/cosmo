/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, HostListener } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { NGXLogger } from 'ngx-logger';
import { SecurityService } from 'src/app/shared/services/security.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { AppEsterneService } from 'src/app/shared/services/app-esterne.service';
import { environment } from 'src/environments/environment';
import { EnvironmentRelatedFunction } from 'src/app/shared/utilities/enviroment-enum';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-menu',
  animations: [
    trigger('openClose', [
      state('open', style({
        left: '0px',
      })),
      state('closed', style({
        left: '-80px'
      })),
      transition('open => closed', [
        animate('0.2s ease-out')
      ]),
      transition('closed => open', [
        animate('0.2s ease-in')
      ]),
    ]),
  ],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  private scrollYThreshold = 200;
  sticky = false;
  menuOpen = false;

  principal: UserInfoWrapper | null = null;


  @HostListener('window:scroll', ['$event']) onScroll(event: any) {
    if (!!this.logger) {
      this.logger.trace('sticky = ' + this.sticky);
    }
    if (event.path && event.path.length > 1) {
      this.sticky = event.path[1].scrollY > this.scrollYThreshold;
    }
  }

  constructor(
    private logger: NGXLogger,
    private securityService: SecurityService,
    private modalService: ModalService,
    private appEsterneService: AppEsterneService
  ) { }

  ngOnInit(): void {
    if (!this.menuOpen) {
      this.appEsterneService.setShowMenu(true);
    }
    this.securityService.principal$.subscribe(newPrincipal => {
      this.principal = newPrincipal;
    });

  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;

    if (!this.menuOpen) {
      this.appEsterneService.setShowMenu(true);
    }
  }

  logout() {
    this.modalService.logout();
  }

  openIntranet() {

    const intranet = Utils.performEnvironmentRelatedAction(EnvironmentRelatedFunction.INTRANET) as string;

    window.open(intranet, '_blank');
  }

}
