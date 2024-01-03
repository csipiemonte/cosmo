/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { EsecuzioneMultiplaComponent } from './esecuzione-multipla.component';
import { EsecuzioneMultiplaService } from './esecuzione-multipla.service';
import { esecuzioneMultiplaRoute } from './esecuzione-multipla.route';
import { ApprovazioneMultiplaComponent } from './approvazione/approvazione-multipla.component';
import { TabsPraticaModule } from '../tabs-pratica/tabs-pratica.module';
import { FirmaMultiplaComponent } from './firma/firma-multipla.component';
import { NgIdleModule } from '@ng-idle/core';
import { FormsComponent } from './forms/forms.component';
import { CompilaFormComponent } from './forms/compila-form/compila-form.component';
import { SimpleFormModule } from '../simple-form/simple-form.module';

@NgModule({
  declarations: [
    EsecuzioneMultiplaComponent,
    ApprovazioneMultiplaComponent,
    FirmaMultiplaComponent,
    FormsComponent,
    CompilaFormComponent,
  ],
  imports: [
    SharedModule,
    CommonModule,
    ReactiveFormsModule,
    RouterModule.forChild(esecuzioneMultiplaRoute),
    TabsPraticaModule,
    NgIdleModule.forRoot(),
    SimpleFormModule
  ],
  providers: [
    EsecuzioneMultiplaService,
  ]
})
export class EsecuzioneMultiplaModule { }
