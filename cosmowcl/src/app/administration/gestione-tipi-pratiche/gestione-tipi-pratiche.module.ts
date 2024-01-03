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
import { GestioneTipiPraticheRoutingModule } from './gestione-tipi-pratiche.routing';
import { GestioneTipiPraticheComponent } from './gestione-tipi-pratiche.component';
import { AggiungiModificaTipoPraticaComponent } from './aggiungi-modifica-tipo-pratica/aggiungi-modifica-tipo-pratica.component';
import { GestioneTipiPraticheService } from './gestione-tipi-pratiche.service';
import { ModificaStatoPraticaModalComponent } from './aggiungi-modifica-tipo-pratica/modifica-stato-pratica-modal/modifica-stato-pratica-modal.component';
import { ModificaTipoDocumentoModalComponent } from './aggiungi-modifica-tipo-pratica/modifica-tipo-documento-modal/modifica-tipo-documento-modal.component';
import { ModificaTrasformazioneModalComponent } from './aggiungi-modifica-tipo-pratica/modifica-trasformazione-modal/modifica-trasformazione-modal.component';
import { ModificaFormatoFileModalComponent } from './aggiungi-modifica-tipo-pratica/modifica-tipo-documento-modal/modifica-formato-file-modal/modifica-formato-file-modal.component';
import { FormatoFileService } from 'src/app/shared/services/formato-file.service';

@NgModule({
  declarations: [
    GestioneTipiPraticheComponent,
    AggiungiModificaTipoPraticaComponent,
    ModificaStatoPraticaModalComponent,
    ModificaTipoDocumentoModalComponent,
    ModificaTrasformazioneModalComponent,
    ModificaFormatoFileModalComponent
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneTipiPraticheRoutingModule,
    DragDropModule,
  ],
  providers: [
    GestioneTipiPraticheService,
    FormatoFileService
  ],
  entryComponents: [
    ModificaStatoPraticaModalComponent,
    ModificaTipoDocumentoModalComponent,
    ModificaTrasformazioneModalComponent,
    ModificaFormatoFileModalComponent
  ]
})
export class GestioneTipiPraticheModule { }
