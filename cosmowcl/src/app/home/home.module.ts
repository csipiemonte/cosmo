/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';

import { HomeComponent } from './home.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { LayoutModule } from '@angular/cdk/layout';
import { NotificheComponent } from './widgets/notifiche/notifiche.component';
import { CalendarioComponent } from './widgets/calendario/calendario.component';
import { WidgetsContainerDirective, WidgetsContainerComponent } from './widgets/widgets-container/widgets-container.component';
import { ButtonsPraticheComponent } from './widgets/buttons-pratiche/buttons-pratiche.component';
import { TabPanelComponent } from './widgets/tab-panel/tab-panel.component';
import { EventoModalComponent } from './widgets/calendario/evento-modal/evento-modal.component';
import { PraticheComponent } from './widgets/pratiche/pratiche.component';
import { NuovoEventoModalComponent } from './widgets/calendario/nuovo-evento-modal/nuovo-evento-modal.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CardsPraticheComponent } from './widgets/cards-pratiche/cards-pratiche.component';



@NgModule({
  declarations: [
    NotificheComponent,
    CalendarioComponent,
    HomeComponent,
    WidgetsContainerDirective,
    WidgetsContainerComponent,
    ButtonsPraticheComponent,
    TabPanelComponent,
    EventoModalComponent,
    PraticheComponent,
    NuovoEventoModalComponent,
    CardsPraticheComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    DragDropModule,
    LayoutModule,
    ReactiveFormsModule
  ],
  exports: [
    HomeComponent
  ],
  entryComponents: [
    CalendarioComponent,
    NotificheComponent,
    ButtonsPraticheComponent,
    TabPanelComponent
  ]
})
export class HomeModule { }
