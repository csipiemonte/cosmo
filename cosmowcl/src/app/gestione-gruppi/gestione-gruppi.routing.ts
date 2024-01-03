/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PendingChangesGuard } from '../shared/guards/can-deactivate.guard';
import { HasProfileEnteGuard } from '../shared/guards/has-profile-ente.guard';
import { AggiungiModificaGruppoComponent } from './aggiungi-modifica-gruppo/aggiungi-modifica-gruppo.component';
import { GestioneGruppiComponent } from './gestione-gruppi/gestione-gruppi.component';
import { GestioneGruppoUtenteTagComponent } from './tags/gestione-gruppo-utente-tag/gestione-gruppo-utente-tag.component';


const routes: Routes = [
  {
    path: '',
    canActivate: [HasProfileEnteGuard],
    children: [
      { path: '', component: GestioneGruppiComponent },
      { path: 'nuovo', component: AggiungiModificaGruppoComponent, canDeactivate: [PendingChangesGuard], data: { codicePagina: 'aggiungi_modifica_gruppo'} },
      { path: 'modifica/:id', component: AggiungiModificaGruppoComponent, canDeactivate: [PendingChangesGuard], data: { codicePagina: 'aggiungi_modifica_gruppo'} },
      { path: 'modifica/:id/back', redirectTo: '', pathMatch: 'full' },
      { path: 'nuovo/back', redirectTo: '', pathMatch: 'full' },
      // tslint:disable-next-line:max-line-length
      { path: 'modifica/:idGruppo/gestioneTags/:idUtente', component: GestioneGruppoUtenteTagComponent, data: { codicePagina: 'gestione_gruppo_utente_tag'} },
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GestioneGruppiRoutingModule { }


