/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { TemplateService } from 'src/app/shared/services/template.service';
import { SharedModule } from 'src/app/shared/shared.module';
import { AggiungiModificaTemplateComponent } from './aggiungi-modifica-template/aggiungi-modifica-template.component';
import { GestioneTemplateReportComponent } from './gestione-template-report.component';
import { GestioneTemplateReportRoutingModule } from './gestione-template-report.routing';

@NgModule({
  declarations: [
    GestioneTemplateReportComponent,
    AggiungiModificaTemplateComponent
  ],
  imports: [
    RouterModule,
    ReactiveFormsModule,
    CommonModule,
    SharedModule,
    GestioneTemplateReportRoutingModule,
  ],
  providers: [
    TemplateService
  ],
  entryComponents: [
  ]
})
export class GestioneTemplateReportModule { }
