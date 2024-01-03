/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';
import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { InfoVerificaFirma } from 'src/app/shared/models/api/cosmoecm/infoVerificaFirma';

@Component({
  selector: 'app-dettaglio-verifica-firme',
  templateUrl: './dettaglio-verifica-firme.component.html',
  styleUrls: ['./dettaglio-verifica-firme.component.scss']
})
export class DettaglioVerificaFirmeComponent implements OnInit {

  @Input() firme: InfoVerificaFirma[] | null = null;
  @Input() layout: string | null = null;

  constructor(public modal: NgbActiveModal,
              private logger: NGXLogger,
              public breakpointObserver: BreakpointObserver) { }

  ngOnInit(): void {
    this.breakpointObserver
      .observe([Breakpoints.XSmall, Breakpoints.Small, Breakpoints.Medium, Breakpoints.HandsetPortrait])
      .subscribe((state: BreakpointState) => {
        if (state.matches) {
          this.logger.debug(
            'Matched small viewport or handset in portrait mode'
          );
          this.layout = 'small';
        }
      });
    this.breakpointObserver
      .observe([Breakpoints.Large, Breakpoints.XLarge])
      .subscribe((state: BreakpointState) => {
        if (state.matches) {
          this.logger.debug(
            'Matched large viewport'
          );
          this.layout = 'large';
        }
      });
  }

  get render(): boolean {
    return !!(this.firme?.length);
  }

  get small(): boolean {
    return 'small' === this.layout;
  }



}
