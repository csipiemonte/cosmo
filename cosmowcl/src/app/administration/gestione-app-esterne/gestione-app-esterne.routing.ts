/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PendingChangesGuard } from 'src/app/shared/guards/can-deactivate.guard';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { AggiungiModificaAppEsterneComponent } from './aggiungi-modifica-app-esterne/aggiungi-modifica-app-esterne.component';
import { GestioneAppEsterneComponent } from './gestione-app-esterne.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: GestioneAppEsterneComponent },
      { path: 'nuovo', component: AggiungiModificaAppEsterneComponent, canDeactivate: [PendingChangesGuard] },
      { path: 'nuovo/back', redirectTo: '', pathMatch: 'full', canDeactivate: [PendingChangesGuard] },
      { path: 'modifica/:id', component: AggiungiModificaAppEsterneComponent, canDeactivate: [PendingChangesGuard] },
      { path: 'modifica/:id/back', redirectTo: '', pathMatch: 'full' , canDeactivate: [PendingChangesGuard]},
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneAppEsterneRoutingModule { }


