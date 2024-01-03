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
import { GestioneFormLogiciComponent } from './gestione-form-logici.component';
import { GestioneFormLogiciRoutingModule } from './gestione-form-logici.routing';
import { AggiungiModificaFormLogicoComponent } from './aggiungi-modifica-form-logico/aggiungi-modifica-form-logico.component';
import { GestioneIstanzeFunzionalitaFormLogiciService } from '../gestione-istanze-funzionalita-form-logici/gestione-istanze-funzionalita-form-logici.service';
import { CustomFormService } from 'src/app/shared/services/customForm.service';

@NgModule({
  declarations: [
    GestioneFormLogiciComponent,
    AggiungiModificaFormLogicoComponent,
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneFormLogiciRoutingModule,
    DragDropModule,
  ],
  providers: [
    GestioneIstanzeFunzionalitaFormLogiciService,
    CustomFormService,
  ],
  entryComponents: []
})
export class GestioneFormLogiciModule { }
