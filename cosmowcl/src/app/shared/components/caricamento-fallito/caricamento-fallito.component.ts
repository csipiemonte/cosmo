/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Constants } from '../../constants/constants';
import { Utils } from '../../utilities/utilities';


@Component({
  selector: 'app-caricamento-fallito',
  templateUrl: './caricamento-fallito.component.html',
  styleUrls: ['./caricamento-fallito.component.scss']
})
export class CaricamentoFallitoComponent implements OnInit {

  @Input() error: any | undefined;
  @Input() message: string | undefined;
  @Input() messageKey: string | undefined;
  @Input() canRetry: boolean | undefined;

  @Output() retry = new EventEmitter<void>();

  linkAssistenza = 'mailto:' + Constants.EMAIL_ASSISTENZA;

  constructor(private translateService: TranslateService) { }

  get showRetry(): boolean {
    return this.canRetry ?? true;
  }

  ngOnInit(): void {
  }

  get errorMessage(): string | undefined {
    if (this.messageKey) {
      return this.translateService.instant(this.messageKey);
    } else if (this.message) {
      return this.message;
    } else if (this.error) {
      return Utils.toMessage(this.error);
    } else {
      return undefined;
    }
  }

  doRetry(): void {
    this.retry.next();
  }
}
