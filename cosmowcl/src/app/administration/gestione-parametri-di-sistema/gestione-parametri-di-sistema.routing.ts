/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { AggiungiModificaParametroDiSistemaComponent } from './aggiungi-modifica-parametro-di-sistema/aggiungi-modifica-parametro-di-sistema.component';
import { GestioneParametriDiSistemaComponent } from './gestione-parametri-di-sistema.component';

const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: GestioneParametriDiSistemaComponent },
      { path: 'nuovo', component: AggiungiModificaParametroDiSistemaComponent, canDeactivate: [] },
      { path: 'modifica/:chiave', component: AggiungiModificaParametroDiSistemaComponent, canDeactivate: [] },
    ]
  },
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneParametriDiSistemaRoutingModule { }
