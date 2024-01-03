/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { SecurityService } from '../services/security.service';
import { NGXLogger } from 'ngx-logger';

@Directive({
  selector: '[appHasUseCase]'
})
export class HasUseCaseDirective {

  @Input() any?: boolean;

  private useCaseArr: string[] = [];
  constructor(
    private logger: NGXLogger,
    private securityService: SecurityService,
    private templateRef: TemplateRef<any>,
    private viewContainerRef: ViewContainerRef) {
  }

  @Input()
  set appHasUseCase(useCases: string | string[]) {
    this.useCaseArr = typeof useCases === 'string' ? [useCases] : useCases;
    this.securityService.principal$.subscribe(() => this.updateView(this.useCaseArr));
  }

  private updateView(useCases: string[]) {
    this.viewContainerRef.clear();
    if (useCases) {
      const useCasesArr: string[] = typeof useCases === 'string' ? [useCases] : useCases;
      this.securityService.hasUseCases(useCasesArr, this.any ? this.any : false).subscribe(result => {
        if (result) {
          this.viewContainerRef.createEmbeddedView(this.templateRef);
        } else {
          this.logger.debug('removed container');
        }
      });
    }
  }

}
