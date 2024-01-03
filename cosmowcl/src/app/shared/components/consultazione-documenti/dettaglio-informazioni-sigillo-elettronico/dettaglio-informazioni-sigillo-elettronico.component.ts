/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';
import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { ModaleParentComponent } from 'src/app/modali/modale-parent-component';
import { InfoVerificaFirma } from 'src/app/shared/models/api/cosmoecm/infoVerificaFirma';
import { HelperService } from 'src/app/shared/services/helper.service';

@Component({
  selector: 'app-dettaglio-informazioni-sigillo-elettronico',
  templateUrl: './dettaglio-informazioni-sigillo-elettronico.component.html',
  styleUrls: ['./dettaglio-informazioni-sigillo-elettronico.component.scss']
})
export class DettaglioInformazioniSigilloElettronicoComponent extends ModaleParentComponent implements OnInit {

  firme: InfoVerificaFirma[] | null = null;
  @Input() layout: string | null = null;
  codicePagina!: string;
  codiceTab!: string;
  codiceModale!: string;

  constructor(public modal: NgbActiveModal,
              private logger: NGXLogger,
              public breakpointObserver: BreakpointObserver,
              helperService: HelperService) {
                super(helperService);
              }

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
    this.setModalName(this.codiceModale);
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  get render(): boolean {
    return !!(this.firme?.length);
  }

  get small(): boolean {
    return 'small' === this.layout;
  }
}
