/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { SharedModule } from '../shared/shared.module';
import { FiltriRicercaActaComponent } from './filtri-ricerca-acta.component';
import { RicercaActaComponent } from './ricerca-acta.component';
import { RicercaActaService } from './ricerca-acta.service';

@NgModule({
  declarations: [
    RicercaActaComponent,
    FiltriRicercaActaComponent,
  ],
  imports: [
    SharedModule,
    CommonModule,
    ReactiveFormsModule,
  ],
  exports: [
    FiltriRicercaActaComponent,
  ],
  entryComponents: [
    RicercaActaComponent,
  ],
  providers: [
    RicercaActaService
  ]
})
export class RicercaActaModule { }
