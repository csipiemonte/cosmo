/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConfigurazioniMessaggiNotificheRoutingModule } from './configurazioni-messaggi-notifiche-routing.module';
import { ConfigurazioniMessaggiNotificheComponent } from './configurazioni-messaggi-notifiche.component';
import { AggiungiModificaConfigurazioneMessaggioNotificaComponent } from './aggiungi-modifica-configurazione-messaggio-notifica/aggiungi-modifica-configurazione-messaggio-notifica.component';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'src/app/shared/shared.module';
import { ConfigurazioniMessaggiNotificheService } from 'src/app/shared/services/configurazioni-messaggi-notifiche.service';
import { DragDropModule } from '@angular/cdk/drag-drop';


@NgModule({
  declarations: [ConfigurazioniMessaggiNotificheComponent, AggiungiModificaConfigurazioneMessaggioNotificaComponent],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    ReactiveFormsModule,
    ConfigurazioniMessaggiNotificheRoutingModule,
    DragDropModule
  ],
  providers: [
    ConfigurazioniMessaggiNotificheService
  ]
})
export class ConfigurazioniMessaggiNotificheModule { }
