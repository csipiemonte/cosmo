/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { configurationRoutes } from './configurator.routing';
import { SharedModule } from '../shared/shared.module';
import { ConfiguratorComponent } from './configurator.component';



@NgModule({
  declarations: [
    ConfiguratorComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(configurationRoutes)
  ],
  exports: [
    RouterModule,
  ]
})
export class ConfiguratorModule { }
