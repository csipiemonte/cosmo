/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from 'src/app/shared/shared.module';
import { DeployProcessoComponent } from './deploy-processo.component';
import { NgxJsonViewerModule } from 'ngx-json-viewer';
import { ProcessiService } from './processi.service';
import { DiscoveryService } from '../services/discovery.service';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    component: DeployProcessoComponent
  },
];


@NgModule({
    declarations: [
      DeployProcessoComponent
    ],
    imports: [
      RouterModule.forChild(routes),
      CommonModule,
      SharedModule,
      NgxJsonViewerModule],
    providers: [
      ProcessiService,
      DiscoveryService
    ],
    exports: [
      RouterModule
    ]
  })
  export class DeployProcessoModule { }
