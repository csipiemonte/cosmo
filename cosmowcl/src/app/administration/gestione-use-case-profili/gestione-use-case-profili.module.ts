/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AggiungiModificaUseCaseProfiliComponent } from './aggiungi-modifica-use-case-profili/aggiungi-modifica-use-case-profili.component';
import { GestioneUseCaseProfiliComponent } from './gestione-use-case-profili.component';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'src/app/shared/shared.module';
import { GestioneUseCaseProfiliRoutingModule } from './gestione-use-case-profili.routing';
import { DragDropModule } from '@angular/cdk/drag-drop';



@NgModule({
  declarations: [
    GestioneUseCaseProfiliComponent,
    AggiungiModificaUseCaseProfiliComponent
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneUseCaseProfiliRoutingModule,
    DragDropModule,
  ],
  providers: [
  ],
  entryComponents: [
  ]
})
export class GestioneUseCaseProfiliModule { }
