/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Directive, Input, OnInit, Type, ViewContainerRef } from '@angular/core';
import { FormLogiciConfig } from 'src/app/shared/models/form-logici/form-logici-config.model';
import { TabsService } from '../services/tabs.service';

@Directive({
  selector: '[appTabs]'
})
export class TabsDirective implements OnInit {

  @Input() formConfig?: FormLogiciConfig;

  constructor(
    private viewContainerRef: ViewContainerRef,
    private tabsService: TabsService) { }

  ngOnInit(): void {
    if (this.formConfig) {
      this.tabsService.registerContainer(this.viewContainerRef, this.formConfig);
    }
  }
}
