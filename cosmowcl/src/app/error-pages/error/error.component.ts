/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { ErrorService } from '../../shared/services/error.service';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { Utils } from 'src/app/shared/utilities/utilities';
import { Constants } from 'src/app/shared/constants/constants';
import { Message } from 'src/app/shared/models/message.model';


@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {

  error: Message | null = null;
  emailAssistenza!: string;

  subject!: string;
  body!: string;

  constructor(
    private errorService: ErrorService,
    private translateService: TranslateService,
    private router: Router) { }

  ngOnInit() {
    this.emailAssistenza = Constants.EMAIL_ASSISTENZA;

    this.error = this.errorService.getError();

    if (!this.error) {
      this.router.navigate([Constants.ROUTE_PAGINA_LOGIN]);
      return;
    }

    const ray = this.error?.ray || Utils.randomString(12, 'Aa#');
    this.error.ray = ray;

    let subjectMsg = this.translateService.instant('mail.oggetto_segnalazione_errore');
    subjectMsg = Utils.parseAndReplacePlaceholders(subjectMsg, [ray || '<none>']);
    this.subject = encodeURIComponent(subjectMsg);

    const body = '';
    this.body = encodeURIComponent(body);
  }
}
