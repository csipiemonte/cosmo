/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HasProfileEnteGuard } from '../shared/guards/has-profile-ente.guard';
import { AggiungiModificaCertificatoFirmaComponent } from './aggiungi-modifica-certificato-firma/aggiungi-modifica-certificato-firma.component';
import { PreferenzeUtenteComponent } from './preferenze-utente.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileEnteGuard],
    children: [
      { path: '', component: PreferenzeUtenteComponent },
      { path: 'nuovo', component: AggiungiModificaCertificatoFirmaComponent, data: { codicePagina: 'aggiungi_modifica_certificato_firma'} },
      { path: 'modifica/:id', component: AggiungiModificaCertificatoFirmaComponent, data: { codicePagina: 'aggiungi_modifica_certificato_firma'} },
      { path: 'modifica/:id/back', redirectTo: '', pathMatch: 'full' },
      { path: 'nuovo/back', redirectTo: '', pathMatch: 'full' },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PreferenzeUtenteRoutingModule { }


