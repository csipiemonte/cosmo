/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import { RouterModule, Routes } from '@angular/router';
import { GestioneTemplateFeaComponent } from './gestione-template-fea.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { NgModule } from '@angular/core';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { AggiungiModificaTemplateFeaComponent } from './aggiungi-modifica-template-fea/aggiungi-modifica-template-fea.component';
import { PendingChangesGuard } from 'src/app/shared/guards/can-deactivate.guard';
import { TemplateFeaService } from 'src/app/shared/services/template-fea.service';

const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: GestioneTemplateFeaComponent },
      { path: 'nuovo', component: AggiungiModificaTemplateFeaComponent, canDeactivate: [PendingChangesGuard] },
      { path: 'modifica/:id', component: AggiungiModificaTemplateFeaComponent, canDeactivate: [PendingChangesGuard] },
    ]
  },
];

@NgModule({
  declarations: [
    GestioneTemplateFeaComponent, AggiungiModificaTemplateFeaComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
  ],
  providers: [
    TemplateFeaService
  ],
  exports: [
    RouterModule
  ]
})
export class GestioneTemplateFeaModule { }
