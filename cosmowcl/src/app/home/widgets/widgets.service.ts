/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injectable, ViewContainerRef, Type } from '@angular/core';
import { WidgetConfig } from 'src/app/shared/models/widget-config.model';
import { ButtonsPraticheComponent } from './buttons-pratiche/buttons-pratiche.component';
import { CalendarioComponent } from './calendario/calendario.component';
import { CardsPraticheComponent } from './cards-pratiche/cards-pratiche.component';
import { NotificheComponent } from './notifiche/notifiche.component';
import { TabPanelComponent } from './tab-panel/tab-panel.component';

const COMPONENTS = new Map<string, Type<any>>();
COMPONENTS.set('CalendarioComponent', CalendarioComponent);
COMPONENTS.set('NotificheComponent', NotificheComponent);
COMPONENTS.set('ButtonsPraticheComponent', ButtonsPraticheComponent);
COMPONENTS.set('TabPanelComponent', TabPanelComponent);
COMPONENTS.set('CardsPraticheComponent', CardsPraticheComponent);

@Injectable({
  providedIn: 'root'
})
export class WidgetsService {


  private availableWidgets: WidgetConfig[] = [];
  private createListeners: any[] = [];

  private destroyListeners: any[] = [];

  private modificabili = true;

  onContainerDestroyed(fn: any) {
    this.destroyListeners.push(fn);
  }

  onContainerCreated(fn: any) {
    this.createListeners.push(fn);
  }

  registerContainer(container: ViewContainerRef, component: Type<any>) {
    this.createListeners.forEach((fn) => {
      fn(container, component);
    });
  }

  destroyContainer(container: ViewContainerRef) {
    this.destroyListeners.forEach((fn) => {
      fn(container);
    });
    this.createListeners = [];
  }

  get widgets() {
    return this.availableWidgets;
  }

  set widgets(widgets: WidgetConfig[]) {
    this.availableWidgets = widgets;
  }

  get areModificabili(){
    return this.modificabili;
  }

  set areModificabili(areModificabili: boolean){
    this.modificabili = areModificabili;
  }


  getWidget(name: string){
    return COMPONENTS.get(name);

  }

  setDescrizione(nomeWidget: string): string{

    let descrizione = 'Descrizione non definita';

    switch (nomeWidget){
      case 'CalendarioComponent':
        descrizione = 'Eventi';
        break;
      case 'NotificheComponent':
        descrizione = 'Notifiche';
        break;
      case 'ButtonsPraticheComponent':
        descrizione = 'Azioni';
        break;
      case 'TabPanelComponent':
        descrizione = 'My Work';
        break;
      case 'CardsPraticheComponent':
        descrizione = 'Cards Pratiche';
        break;
    }

    return descrizione;

  }

  get defaultWidgets(): WidgetConfig[]{
    return [
    {
      widget: CalendarioComponent,
      colsMin: 8,
      colsMax: 12,
      enabled: true,
      name: 'CalendarioComponent',
      descrizione: 'Eventi'
    },
    {
      widget: NotificheComponent,
      colsMin: 4,
      colsMax: 12,
      enabled: true,
      name: 'NotificheComponent',
      descrizione: 'Notifiche'
    },
    {
      widget: ButtonsPraticheComponent,
      colsMin: 4,
      colsMax: 12,
      enabled: true,
      name: 'ButtonsPraticheComponent',
      descrizione: 'Azioni'
    },
    {
      widget: TabPanelComponent,
      colsMin: 12,
      colsMax: 12,
      enabled: true,
      name: 'TabPanelComponent',
      descrizione: 'My Work'
    },
    {
      widget: CardsPraticheComponent,
      colsMin: 12,
      colsMax: 12,
      enabled: true,
      name: 'CardsPraticheComponent',
      descrizione: 'Cards Pratiche'
    }];
  }


  constructor() { }
}
