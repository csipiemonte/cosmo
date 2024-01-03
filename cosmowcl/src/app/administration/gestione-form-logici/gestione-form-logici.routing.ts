/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PendingChangesGuard } from 'src/app/shared/guards/can-deactivate.guard';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { AggiungiModificaFormLogicoComponent } from './aggiungi-modifica-form-logico/aggiungi-modifica-form-logico.component';
import { GestioneFormLogiciComponent } from './gestione-form-logici.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: GestioneFormLogiciComponent },
      { path: 'nuovo', component: AggiungiModificaFormLogicoComponent, canDeactivate: [PendingChangesGuard] },
      { path: 'modifica/:id', component: AggiungiModificaFormLogicoComponent, canDeactivate: [PendingChangesGuard] },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneFormLogiciRoutingModule { }
