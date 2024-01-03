/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DiscoveryComponent } from './discovery/discovery.component';
import { RouterModule } from '@angular/router';
import { administationRoutes } from './administration.routing';
import { HomeComponent } from './home/home.component';
import { DiscoveryService } from './services/discovery.service';
import { SharedModule } from '../shared/shared.module';
import { JsltSandboxComponent } from './jslt-sandbox/jslt-sandbox.component';
import { NgxJsonViewerModule } from 'ngx-json-viewer';
import { ImportaProcessoComponent } from './importa-processo/importa-processo.component';
import { GestioneSigilliElettroniciComponent } from './gestione-sigilli-elettronici/gestione-sigilli-elettronici.component';

@NgModule({
  declarations: [
    HomeComponent,
    DiscoveryComponent,
    ImportaProcessoComponent,
    JsltSandboxComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    NgxJsonViewerModule,
    RouterModule.forChild(administationRoutes)],
  providers: [
    DiscoveryService
  ],
  exports: [
    RouterModule,
  ],
})
export class AdministrationModule { }
