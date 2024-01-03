/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Injector, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { TabBadge } from '../models/tab-status.models';

@Component({
  selector: 'app-consultazione-documenti-form-logici',
  templateUrl: './consultazione-documenti.component.html',
  styleUrls: ['./consultazione-documenti.component.scss']
})
export class ConsultazioneDocumentiFormLogiciComponent extends FunzionalitaParentComponent implements OnInit {

  totDocCaricati?: number;

  constructor(
    public injector: Injector,
    private translateService: TranslateService
  ) {
    super(injector);
  }

  public getBadges(): TabBadge[] | null {
    if (this.totDocCaricati && (+this.totDocCaricati > 0) ) {
      return [
        { class: 'info',
          text: this.totDocCaricati.toString(),
          tooltip: this.translateService.instant('common.caricati') }
      ];
    }
    return null;
  }

  getTotDocumentiCaricati(totaleDocumenti: number) {
    return this.totDocCaricati = totaleDocumenti;
  }


}
