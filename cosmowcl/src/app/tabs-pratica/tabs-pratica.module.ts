/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { CommonModule } from '@angular/common';
import { TabsPraticaComponent } from './tabs-pratica.component';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CustomFormService } from '../shared/services/customForm.service';
import { TabSingoloComponent } from './tab-singolo/tab-singolo.component';

@NgModule({
  declarations: [
    TabsPraticaComponent,
    TabSingoloComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    ReactiveFormsModule,
  ],
  exports: [
    TabsPraticaComponent,

  ],
  providers: [
    CustomFormService
  ]
})
export class TabsPraticaModule { }
