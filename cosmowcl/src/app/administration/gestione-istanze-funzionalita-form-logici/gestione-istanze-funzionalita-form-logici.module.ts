/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'src/app/shared/shared.module';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { GestioneIstanzeFunzionalitaFormLogiciService } from './gestione-istanze-funzionalita-form-logici.service';
import { GestioneIstanzeFunzionalitaFormLogiciComponent } from './gestione-istanze-funzionalita-form-logici.component';
import { GestioneIstanzeFunzionalitaFormLogiciRoutingModule } from './gestione-istanze-funzionalita-form-logici.routing';
import { AggiungiModificaIstanzaComponent } from './aggiungi-modifica-istanza/aggiungi-modifica-istanza.component';

@NgModule({
  declarations: [
    GestioneIstanzeFunzionalitaFormLogiciComponent,
    AggiungiModificaIstanzaComponent,
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneIstanzeFunzionalitaFormLogiciRoutingModule
  ],
  providers: [
    GestioneIstanzeFunzionalitaFormLogiciService,
  ]
})
export class GestioneIstanzeFunzionalitaFormLogiciModule { }
