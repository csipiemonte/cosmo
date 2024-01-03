/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DragDropModule } from '@angular/cdk/drag-drop';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'src/app/shared/shared.module';

import {
  AggiungiModificaFruitoreComponent,
} from './aggiungi-modifica-fruitore/aggiungi-modifica-fruitore.component';
import { GestioneFruitoriComponent } from './gestione-fruitori.component';
import { GestioneFruitoriRoutingModule } from './gestione-fruitori.routing';
import {
  ModificaEndpointFruitoreComponent,
} from './modifica-endpoint/modifica-endpoint-fruitore.component';
import {
  ModificaSchemiAutorizzazioneFruitoreComponent,
} from './modifica-schemi-auth/modifica-schemi-auth-fruitore.component';

@NgModule({
  declarations: [
    GestioneFruitoriComponent,
    AggiungiModificaFruitoreComponent,
    ModificaSchemiAutorizzazioneFruitoreComponent,
    ModificaEndpointFruitoreComponent,
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneFruitoriRoutingModule,
    DragDropModule,
  ],
  entryComponents: [
    ModificaSchemiAutorizzazioneFruitoreComponent,
    ModificaEndpointFruitoreComponent,
  ]
})
export class GestioneFruitoriModule { }
