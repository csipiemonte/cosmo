/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { ErrorService } from '../../shared/services/error.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-error-not-found',
  templateUrl: './error-not-found.component.html'
})
export class ErrorNotFoundComponent implements OnInit {

  constructor(
    private errorService: ErrorService,
    private translateService: TranslateService) { }

  ngOnInit() {
    this.translateService.get('errori.pagina_non_trovata').subscribe((result: string) => {
      this.errorService.handleFatalError(this.translateService.instant(result));
    });

  }

}
