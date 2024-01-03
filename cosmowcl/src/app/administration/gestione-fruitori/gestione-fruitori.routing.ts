/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PendingChangesGuard } from 'src/app/shared/guards/can-deactivate.guard';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { AggiungiModificaFruitoreComponent } from './aggiungi-modifica-fruitore/aggiungi-modifica-fruitore.component';
import { GestioneFruitoriComponent } from './gestione-fruitori.component';
import { ModificaEndpointFruitoreComponent } from './modifica-endpoint/modifica-endpoint-fruitore.component';
import { ModificaSchemiAutorizzazioneFruitoreComponent } from './modifica-schemi-auth/modifica-schemi-auth-fruitore.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: GestioneFruitoriComponent },
      { path: 'nuovo', component: AggiungiModificaFruitoreComponent, canDeactivate: [PendingChangesGuard] },
      { path: 'modifica/:id', component: AggiungiModificaFruitoreComponent, canDeactivate: [PendingChangesGuard] },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneFruitoriRoutingModule { }


