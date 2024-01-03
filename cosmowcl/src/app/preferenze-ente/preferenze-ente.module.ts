/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'src/app/shared/shared.module';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { PreferenzeEnteComponent } from './preferenze-ente.component';
import { ImpostazioniFirmaService } from '../shared/services/impostazioni-firma.service';
import { AssociazioneEnteComponent } from './applicazioni-esterne/associazione-ente/associazione-ente.component';
import { SelezioneApplicazioneComponent } from './applicazioni-esterne/associazione-ente/selezione-applicazione/selezione-applicazione.component';
import { AssociaDissociaEnteComponent } from './applicazioni-esterne/associazione-ente/associa-dissocia-ente/associa-dissocia-ente.component';
import { FunzionalitaComponent } from './applicazioni-esterne/funzionalita/funzionalita.component';
import { GestisciFunzionalitaComponent } from './applicazioni-esterne/gestisci-funzionalita/gestisci-funzionalita.component';
import { GestisciFunzionalitaNonPrincipaleComponent } from './applicazioni-esterne/funzionalita/gestisci-funzionalita-non-principale/gestisci-funzionalita-non-principale.component';
import { DragDropModule } from '@angular/cdk/drag-drop';

@NgModule({
  declarations: [
    PreferenzeEnteComponent,
    AssociazioneEnteComponent,
    SelezioneApplicazioneComponent,
    AssociaDissociaEnteComponent,
    FunzionalitaComponent,
    GestisciFunzionalitaComponent,
    GestisciFunzionalitaNonPrincipaleComponent
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    DragDropModule
  ],
  providers: [
    ImpostazioniFirmaService,
  ]
})
export class PreferenzeEnteModule { }
