/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'src/app/shared/shared.module';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { AggiungiModificaGruppoComponent } from './aggiungi-modifica-gruppo/aggiungi-modifica-gruppo.component';
import { GestioneGruppiRoutingModule } from './gestione-gruppi.routing';
import { GruppoFilter } from './gruppi-filter.pipe';
import { GestioneGruppiComponent } from './gestione-gruppi/gestione-gruppi.component';
import { GestioneGruppoUtenteTagComponent } from './tags/gestione-gruppo-utente-tag/gestione-gruppo-utente-tag.component';

@NgModule({
  declarations: [
    GestioneGruppiComponent,
    AggiungiModificaGruppoComponent,
    GruppoFilter,
    GestioneGruppoUtenteTagComponent
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneGruppiRoutingModule,
    DragDropModule,
  ]
})
export class GestioneGruppiModule { }
