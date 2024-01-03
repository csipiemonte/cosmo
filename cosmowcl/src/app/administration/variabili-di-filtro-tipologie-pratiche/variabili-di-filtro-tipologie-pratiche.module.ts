/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VariabiliDiFiltroTipologiePraticheComponent } from './variabili-di-filtro-tipologie-pratiche.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { VariabiliDiFiltroService } from 'src/app/shared/services/variabili-di-filtro.service';
import {AggiungiModificaVariabileFiltroComponent} from './aggiungi-modifica-variabile-filtro/aggiungi-modifica-variabile-filtro.component';
import { PendingChangesGuard } from 'src/app/shared/guards/can-deactivate.guard';
import { NgxJsonViewerModule } from 'ngx-json-viewer';

const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: VariabiliDiFiltroTipologiePraticheComponent },
      { path: 'nuovo', component: AggiungiModificaVariabileFiltroComponent, canDeactivate: [PendingChangesGuard] },
      { path: 'modifica/:id', component: AggiungiModificaVariabileFiltroComponent, canDeactivate: [PendingChangesGuard] },
    ]
  },
];

@NgModule({
  declarations: [VariabiliDiFiltroTipologiePraticheComponent, AggiungiModificaVariabileFiltroComponent],
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    NgxJsonViewerModule,
    RouterModule.forChild(routes),
  ],
  providers: [
    VariabiliDiFiltroService
  ],
  exports: [
    RouterModule
  ]
})
export class VariabiliDiFiltroTipologiePraticheModule { }
