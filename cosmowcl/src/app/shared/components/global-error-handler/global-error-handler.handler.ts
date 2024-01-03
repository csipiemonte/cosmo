/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ErrorHandler, Injectable} from '@angular/core';
import { ErrorService } from '../../services/error.service';
import { NGXLogger } from 'ngx-logger';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

  constructor(
    private logger: NGXLogger,
    private errorService: ErrorService
  ) { }

  handleError(error: any) {
    try {
      this.logger.error( 'Uncaught error intercepted in global handler', error);
    } catch (e) {
      this.logger.warn('cannot render error details in global handler', e);
    }

    this.errorService.error(error);
  }
}
