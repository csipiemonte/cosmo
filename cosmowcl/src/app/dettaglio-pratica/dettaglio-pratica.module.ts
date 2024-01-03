/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { DettaglioPraticaComponent } from './dettaglio-pratica.component';
import { ReactiveFormsModule } from '@angular/forms';
import { TabsPraticaModule } from '../tabs-pratica/tabs-pratica.module';
import { TipiDocumentiService } from '../shared/services/tipi-documenti/tipi-documenti.service';

export const dettaglioPraticaRoutes: Routes = [
  { path: '', component: DettaglioPraticaComponent }
];

@NgModule({
  declarations: [
    DettaglioPraticaComponent,
  ],
  imports: [
    SharedModule,
    CommonModule,
    ReactiveFormsModule,
    TabsPraticaModule,
    RouterModule.forChild(dettaglioPraticaRoutes),
  ],
  providers: [
    TipiDocumentiService
  ]
})
export class DettaglioPraticaModule { }
