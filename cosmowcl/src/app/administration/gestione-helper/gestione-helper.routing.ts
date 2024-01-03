/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AggiungiModificaHelperComponent } from './aggiungi-modifica-helper/aggiungi-modifica-helper.component';
import { GestioneHelperComponent } from './gestione-helper.component';

const routes: Routes = [
  {
    path: '',
    canActivate: [],
    children: [
      { path: '', component: GestioneHelperComponent },
      { path: 'nuovo', component: AggiungiModificaHelperComponent, canDeactivate: [] },
      { path: 'modifica/:id', component: AggiungiModificaHelperComponent, canDeactivate: [] },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneHelperRoutingModule { }
