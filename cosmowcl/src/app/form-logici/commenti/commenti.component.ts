/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, EventEmitter, Injector, OnInit, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FunzionalitaParentComponent } from '../funzionalita-parent.component';
import { TabBadge } from '../models/tab-status.models';

@Component({
  selector: 'app-commenti-form-logici',
  templateUrl: './commenti.component.html',
  styleUrls: ['./commenti.component.scss']
})
export class CommentiFormLogiciComponent extends FunzionalitaParentComponent implements OnInit {

  constructor(
    public injector: Injector,
    private translateService: TranslateService
  ) {
    super(injector);
  }

  numCommenti?: number;

  public getBadges(): TabBadge[] | null {
    if (this.numCommenti && (+this.numCommenti > 0) ) {
      return [
        { class: 'info',
          text: this.numCommenti.toString(),
          tooltip: this.translateService.instant('common.inseriti') }
      ];
    }
    return null;
  }

  getNumeroCommenti(numeroCommenti: number) {
    this.numCommenti = numeroCommenti;
  }

}
