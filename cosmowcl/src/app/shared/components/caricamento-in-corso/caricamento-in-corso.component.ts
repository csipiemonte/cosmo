/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CaricamentoInCorsoIconPicker } from './caricamento-in-corso.icons';

@Component({
  selector: 'app-caricamento-in-corso',
  templateUrl: './caricamento-in-corso.component.html',
  styleUrls: ['./caricamento-in-corso.component.scss']
})
export class CaricamentoInCorsoComponent implements OnInit {

  icon: string;

  @Input() top: string | number | undefined;
  @Input() bottom: string | number | undefined;
  @Input() message: string | undefined;
  @Input() messageKey: string | undefined;

  constructor(private translateService: TranslateService) {
    this.icon = CaricamentoInCorsoIconPicker.get();
  }

  ngOnInit(): void {
  }

  get topMargin(): string {
    if (!!this.top) {
      if (typeof this.top === 'string') {
        return this.top;
      } else {
        return this.top + ' em';
      }
    }
    return '2em';
  }

  get bottomMargin(): string {
    if (!!this.bottom) {
      if (typeof this.bottom === 'string') {
        return this.bottom;
      } else {
        return this.bottom + ' em';
      }
    }
    return '1em';
  }

  get loadingMessage(): string | undefined {
    if (this.messageKey) {
      return this.translateService.instant(this.messageKey);
    } else if (this.message) {
      return this.message;
    } else {
      return undefined;
    }
  }


}
