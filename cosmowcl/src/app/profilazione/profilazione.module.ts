/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { SharedModule } from '../shared/shared.module';
import { UtentiService } from '../shared/services/utenti.service';
import { EntiService } from '../shared/services/enti.service';
import { ProfiliService } from '../shared/services/profili.service';
import { GruppiService } from '../shared/services/gruppi.service';
import { UseCaseService } from '../shared/services/usecase.service';
import { FruitoriService } from '../shared/services/fruitori.service';


@NgModule({
  declarations: [LoginComponent],
  imports: [
    CommonModule,
    SharedModule
  ],
  exports: [
    LoginComponent
  ],
  providers: [
    UtentiService,
    EntiService,
    ProfiliService,
    GruppiService,
    UseCaseService,
    FruitoriService,
  ],
})
export class ProfilazioneModule { }
