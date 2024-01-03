/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { GestioneAppEsterneComponent } from './gestione-app-esterne.component';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'src/app/shared/shared.module';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { GestioneAppEsterneRoutingModule } from './gestione-app-esterne.routing';
import { AggiungiModificaAppEsterneComponent } from './aggiungi-modifica-app-esterne/aggiungi-modifica-app-esterne.component';

@NgModule({
  declarations: [
    GestioneAppEsterneComponent,
    AggiungiModificaAppEsterneComponent,
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneAppEsterneRoutingModule
  ]
})
export class GestioneAppEsterneModule { }
