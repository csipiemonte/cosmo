/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable, ViewContainerRef, Type } from '@angular/core';
import { FormLogiciConfig } from 'src/app/shared/models/form-logici/form-logici-config.model';

@Injectable()
export class TabsService {

  private availableForms: FormLogiciConfig[] = [];

  private createListeners: any[] = [];

  private destroyListeners: any[] = [];

  onContainerDestroyed(fn: any) {
    this.destroyListeners.push(fn);
  }

  onContainerCreated(fn: any) {
    this.createListeners.push(fn);
  }

  registerContainer(container: ViewContainerRef, config: FormLogiciConfig) {
    this.createListeners.forEach((fn) => {
      fn(container, config);
    });
  }

  destroyContainer(container: ViewContainerRef) {
    this.destroyListeners.forEach((fn) => {
      fn(container);
    });
    this.createListeners = [];
  }

  get formLogici() {
    return this.availableForms;
  }

  set formLogici(formLogici: FormLogiciConfig[]) {
    this.availableForms = formLogici;
  }

  constructor() { }
}
