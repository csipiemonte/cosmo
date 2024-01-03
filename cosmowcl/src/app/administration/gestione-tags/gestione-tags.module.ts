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
import { GestioneTagsComponent } from './gestione-tags.component';
import { GestioneTagsRoutingModule } from './gestione-tags.routing';
import { AggiungiModificaTagComponent } from './aggiungi-modifica-tag/aggiungi-modifica-tag.component';
import { ModificaTipoTagModalComponent } from './aggiungi-modifica-tag/modifica-tipo-tag-modal/modifica-tipo-tag-modal.component';

@NgModule({
  declarations: [
    GestioneTagsComponent,
    AggiungiModificaTagComponent,
    ModificaTipoTagModalComponent
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneTagsRoutingModule,
    DragDropModule,
  ],
  providers: [
  ],
  entryComponents: []
})
export class GestioneTagsModule { }
