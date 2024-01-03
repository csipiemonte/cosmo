/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AggiungiModificaCustomFormComponent } from './aggiungi-modifica-custom-form/aggiungi-modifica-custom-form.component';
import { GestioneCustomFormComponent } from './gestione-custom-form.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [],
    children: [
      { path: '', component: GestioneCustomFormComponent },
      { path: 'nuovo', component: AggiungiModificaCustomFormComponent, canDeactivate: [] },
      { path: 'modifica/:id', component: AggiungiModificaCustomFormComponent, canDeactivate: [] },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneCustomFormRoutingModule { }
