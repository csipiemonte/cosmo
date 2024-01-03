/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PendingChangesGuard } from 'src/app/shared/guards/can-deactivate.guard';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { AggiungiModificaTagComponent } from './aggiungi-modifica-tag/aggiungi-modifica-tag.component';
import { GestioneTagsComponent } from './gestione-tags.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: GestioneTagsComponent },
      { path: 'nuovo', component: AggiungiModificaTagComponent, canDeactivate: [] },
      { path: 'modifica/:id', component: AggiungiModificaTagComponent, canDeactivate: [] },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneTagsRoutingModule { }
