/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AggiungiModificaUtenteComponent } from './aggiungi-modifica-utente/aggiungi-modifica-utente.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { UtenteFilter } from './utente-filter.pipe';
import { CommonModule } from '@angular/common';
import { GestioneUtentiRoutingModule } from './gestione-utenti.routing';
import { GestioneUtentiComponent } from './gestione-utenti/gestione-utenti.component';
import { ReactiveFormsModule } from '@angular/forms';
import { DragDropModule } from '@angular/cdk/drag-drop';

@NgModule({
  declarations: [
    GestioneUtentiComponent,
    AggiungiModificaUtenteComponent,
    UtenteFilter,
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneUtentiRoutingModule,
    DragDropModule,
  ]
})
export class GestioneUtentiModule { }
