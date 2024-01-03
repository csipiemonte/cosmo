/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PendingChangesGuard } from 'src/app/shared/guards/can-deactivate.guard';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { AggiungiModificaTemplateComponent } from './aggiungi-modifica-template/aggiungi-modifica-template.component';
import { GestioneTemplateReportComponent } from './gestione-template-report.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: GestioneTemplateReportComponent },
      { path: 'nuovo', component: AggiungiModificaTemplateComponent, canDeactivate: [PendingChangesGuard] },
      { path: 'modifica/:id', component: AggiungiModificaTemplateComponent, canDeactivate: [PendingChangesGuard] },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneTemplateReportRoutingModule { }
