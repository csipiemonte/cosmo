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
import { NgxJsonViewerModule } from 'ngx-json-viewer';
import { GestioneEntiComponent } from './gestione-enti.component';
import { AggiungiModificaEnteComponent } from './aggiungi-modifica-ente/aggiungi-modifica-ente.component';
import { GestioneEntiRoutingModule } from './gestione-enti.routing';
import { ConfigurazioneEnteComponent } from './aggiungi-modifica-ente/configurazione-ente/configurazione-ente.component';
import { AggiungiModificaConfigurazioneEnteModalComponent } from './aggiungi-modifica-ente/configurazione-ente/aggiungi-modifica-configurazione-ente-modal/aggiungi-modifica-configurazione-ente-modal.component';
import { ConfigurazioneEnteService } from 'src/app/shared/services/configurazione-ente.service';

@NgModule({
  declarations: [
    GestioneEntiComponent,
    AggiungiModificaEnteComponent,
    ConfigurazioneEnteComponent,
    AggiungiModificaConfigurazioneEnteModalComponent,
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    NgxJsonViewerModule,
    GestioneEntiRoutingModule,
    DragDropModule,
  ],
  providers: [
    ConfigurazioneEnteService
  ]
})
export class GestioneEntiModule { }
