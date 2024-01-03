/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AggiungiModificaSigilloElettronicoComponent } from './aggiungi-modifica-sigillo-elettronico/aggiungi-modifica-sigillo-elettronico.component';
import { GestioneSigilliElettroniciComponent } from './gestione-sigilli-elettronici.component';
import { SigilloElettronicoService } from 'src/app/shared/services/sigillo-elettronico.service';
import { ReactiveFormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';
import { PendingChangesGuard } from 'src/app/shared/guards/can-deactivate.guard';
import { HasProfileGuard } from 'src/app/shared/guards/has-profile.guard';
import { SharedModule } from 'src/app/shared/shared.module';

const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileGuard],
    children: [
      { path: '', component: GestioneSigilliElettroniciComponent },
      { path: 'nuovo', component: AggiungiModificaSigilloElettronicoComponent, canDeactivate: [PendingChangesGuard] },
      { path: 'modifica/:id', component: AggiungiModificaSigilloElettronicoComponent, canDeactivate: [PendingChangesGuard] },
    ]
  },
];

@NgModule({
  declarations: [
    GestioneSigilliElettroniciComponent, AggiungiModificaSigilloElettronicoComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),
  ],
  providers: [
    SigilloElettronicoService
  ],
  exports: [
    RouterModule
  ]
})
export class GestioneSigilliElettroniciModule { }
