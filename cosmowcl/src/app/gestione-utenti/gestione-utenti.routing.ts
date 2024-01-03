/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AggiungiModificaUtenteComponent } from './aggiungi-modifica-utente/aggiungi-modifica-utente.component';
import { GestioneUtentiComponent } from './gestione-utenti/gestione-utenti.component';
import { PendingChangesGuard } from '../shared/guards/can-deactivate.guard';
import { HasProfileEnteGuard } from '../shared/guards/has-profile-ente.guard';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileEnteGuard],
    children: [
      { path: '', component: GestioneUtentiComponent },
      { path: 'nuovo', component: AggiungiModificaUtenteComponent, canDeactivate: [PendingChangesGuard], data: { codicePagina: 'aggiungi_modifica_utente'} },
      { path: 'modifica/:id', component: AggiungiModificaUtenteComponent, canDeactivate: [PendingChangesGuard], data: { codicePagina: 'aggiungi_modifica_utente'} },
      { path: 'modifica/:id/back', redirectTo: '', pathMatch: 'full' },
      { path: 'nuovo/back', redirectTo: '', pathMatch: 'full' },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneUtentiRoutingModule { }


