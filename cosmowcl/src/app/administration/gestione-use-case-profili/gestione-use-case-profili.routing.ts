/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AggiungiModificaUseCaseProfiliComponent } from './aggiungi-modifica-use-case-profili/aggiungi-modifica-use-case-profili.component';
import { GestioneUseCaseProfiliComponent } from './gestione-use-case-profili.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [],
    children: [
      { path: '', component: GestioneUseCaseProfiliComponent },
      { path: 'nuovo', component: AggiungiModificaUseCaseProfiliComponent, canDeactivate: [] },
      { path: 'modifica/:id', component: AggiungiModificaUseCaseProfiliComponent, canDeactivate: [] },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneUseCaseProfiliRoutingModule { }
