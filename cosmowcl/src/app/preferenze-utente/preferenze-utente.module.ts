/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'src/app/shared/shared.module';
import { CommonModule } from '@angular/common';
import { PreferenzeUtenteComponent } from './preferenze-utente.component';
import { PreferenzeUtenteRoutingModule } from './preferenze-utente.routing';
import { ImpostazioniFirmaService } from '../shared/services/impostazioni-firma.service';
import { ReactiveFormsModule } from '@angular/forms';
import { AggiungiCertificatoModalComponent } from '../shared/components/certificati-firma/aggiungi-certificato-modal/aggiungi-certificato-modal.component';
import { AggiungiModificaCertificatoFirmaComponent } from './aggiungi-modifica-certificato-firma/aggiungi-modifica-certificato-firma.component';

@NgModule({
  declarations: [
    PreferenzeUtenteComponent,
    AggiungiModificaCertificatoFirmaComponent
  ],
  imports: [
    RouterModule,
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    PreferenzeUtenteRoutingModule
  ],
  providers: [
    ImpostazioniFirmaService
  ],
})
export class PreferenzeUtenteModule { }
