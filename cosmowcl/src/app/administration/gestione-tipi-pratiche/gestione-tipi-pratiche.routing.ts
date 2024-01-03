/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PendingChangesGuard } from 'src/app/shared/guards/can-deactivate.guard';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { AggiungiModificaTipoPraticaComponent } from './aggiungi-modifica-tipo-pratica/aggiungi-modifica-tipo-pratica.component';
import { GestioneTipiPraticheComponent } from './gestione-tipi-pratiche.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: GestioneTipiPraticheComponent },
      { path: 'nuovo', component: AggiungiModificaTipoPraticaComponent, canDeactivate: [PendingChangesGuard] },
      { path: 'modifica/:codice', component: AggiungiModificaTipoPraticaComponent, canDeactivate: [PendingChangesGuard] },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneTipiPraticheRoutingModule { }
