/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { GestioneParametriDiSistemaRoutingModule } from './gestione-parametri-di-sistema.routing';
import { GestioneParametriDiSistemaComponent } from './gestione-parametri-di-sistema.component';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'src/app/shared/shared.module';
import { AggiungiModificaParametroDiSistemaComponent } from './aggiungi-modifica-parametro-di-sistema/aggiungi-modifica-parametro-di-sistema.component';


@NgModule({
  declarations: [
    GestioneParametriDiSistemaComponent,
    AggiungiModificaParametroDiSistemaComponent,
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneParametriDiSistemaRoutingModule
  ],
  providers: [
  ],
  entryComponents: []
})
export class GestioneParametriDiSistemaModule { }
