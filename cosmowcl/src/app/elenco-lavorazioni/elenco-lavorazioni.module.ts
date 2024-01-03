/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { ElencoLavorazioniComponent } from './elenco-lavorazioni.component';

export const elencoLavorazioniRoute: Routes = [
  { path: '', component: ElencoLavorazioniComponent }
];

@NgModule({
  declarations: [
    ElencoLavorazioniComponent,
  ],
  imports: [
    SharedModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(elencoLavorazioniRoute),
  ]
})
export class ElencoLavorazioniModule { }
