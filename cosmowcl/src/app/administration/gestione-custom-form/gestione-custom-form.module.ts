/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'src/app/shared/shared.module';
import { CustomFormService } from '../../shared/services/customForm.service';
import { AggiungiModificaCustomFormComponent } from './aggiungi-modifica-custom-form/aggiungi-modifica-custom-form.component';
import { GestioneCustomFormComponent } from './gestione-custom-form.component';
import { GestioneCustomFormRoutingModule } from './gestione-custom-form.routing';

@NgModule({
  declarations: [
    GestioneCustomFormComponent,
    AggiungiModificaCustomFormComponent
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneCustomFormRoutingModule
  ],
  providers: [
    CustomFormService
  ],
  entryComponents: [
  ]
})
export class GestioneCustomFormModule { }
