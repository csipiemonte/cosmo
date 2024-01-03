/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { ConfigurazioniMessaggiNotificheComponent } from './configurazioni-messaggi-notifiche.component';
import { PendingChangesGuard } from 'src/app/shared/guards/can-deactivate.guard';
import { AggiungiModificaConfigurazioneMessaggioNotificaComponent } from './aggiungi-modifica-configurazione-messaggio-notifica/aggiungi-modifica-configurazione-messaggio-notifica.component';

const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: ConfigurazioniMessaggiNotificheComponent },
      { path: 'nuovo', component: AggiungiModificaConfigurazioneMessaggioNotificaComponent, canDeactivate: [PendingChangesGuard] },
      { path: 'modifica/:id', component: AggiungiModificaConfigurazioneMessaggioNotificaComponent, canDeactivate: [PendingChangesGuard] },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ConfigurazioniMessaggiNotificheRoutingModule { }
