/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HelperService } from 'src/app/shared/services/helper.service';
import { SharedModule } from 'src/app/shared/shared.module';
import { AggiungiModificaHelperComponent } from './aggiungi-modifica-helper/aggiungi-modifica-helper.component';
import { GestioneHelperComponent } from './gestione-helper.component';
import { GestioneHelperRoutingModule } from './gestione-helper.routing';

@NgModule({
  declarations: [
    GestioneHelperComponent,
    AggiungiModificaHelperComponent
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneHelperRoutingModule
  ],
  providers: [
    HelperService
  ],
  entryComponents: [
  ]
})
export class GestioneHelperModule { }
