/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './profilazione/login/login.component';
import { HasProfileGuard } from './shared/guards/has-profile.guard';
import { errorRoutes } from './error-pages/error-pages.routing';
import { PreferenzeEnteComponent } from './preferenze-ente/preferenze-ente.component';
import { CreazionePraticaComponent } from './creazione-pratica/creazione-pratica.component';
import { AssociazioneEnteComponent } from './preferenze-ente/applicazioni-esterne/associazione-ente/associazione-ente.component';
import { AssociaDissociaEnteComponent } from './preferenze-ente/applicazioni-esterne/associazione-ente/associa-dissocia-ente/associa-dissocia-ente.component';
import { HasProfileEnteGuard } from './shared/guards/has-profile-ente.guard';
import { FunzionalitaComponent } from './preferenze-ente/applicazioni-esterne/funzionalita/funzionalita.component';
import { GestisciFunzionalitaNonPrincipaleComponent } from './preferenze-ente/applicazioni-esterne/funzionalita/gestisci-funzionalita-non-principale/gestisci-funzionalita-non-principale.component';
import { InfoUtenteComponent } from './info-utente/info-utente.component';
import { CaricamentoPraticheComponent } from './caricamento-pratiche/caricamento-pratiche.component';
import { PendingChangesGuard } from './shared/guards/can-deactivate.guard';

const routes: Routes = [
  ...errorRoutes,
  { path: 'login', component: LoginComponent, data: { codicePagina: 'login'} },
  {
    path: 'home',
    canActivate: [HasProfileEnteGuard],
    component: HomeComponent,
    data: { descrizione: 'common.ritorna_alla_home', codicePagina: 'home'}
  },
  {
    path: 'pratica/:id',
    canActivate: [HasProfileEnteGuard],
    data: { isTab: true, descrizione: 'common.pratica', codicePagina: 'dettaglio_pratica' },
    loadChildren: () => import('./dettaglio-pratica/dettaglio-pratica.module').then(m => m.DettaglioPraticaModule)
  },
  {
    path: 'tasks/:id',
    canActivate: [HasProfileEnteGuard],
    data: {isTab: true, descrizione: 'common.ritorno_ai_task', codicePagina: 'lavorazione_pratica'},
    loadChildren: () => import('./form-logici/form-logici.module').then(m => m.FormLogiciModule)
  },
  {
    path: 'elenco-lavorazioni',
    canActivate: [HasProfileEnteGuard],
    data: { descrizione: 'common.ritorna_elenco_lavorazioni', root: true, codicePagina: 'elenco_lavorazioni' },
    loadChildren: () => import('./elenco-lavorazioni/elenco-lavorazioni.module').then(m => m.ElencoLavorazioniModule)
  },
  {
    path: 'ricerca-acta',
    canActivate: [HasProfileEnteGuard],
    data: { descrizione: 'common.ritorna_alla_home', root: true, codicePagina: 'ricerca_acta' },
    loadChildren: () => import('./ricerca-acta/ricerca-acta.module').then(m => m.RicercaActaModule)
  },
  {
    path: 'gestisci-utenti',
    canActivate: [HasProfileEnteGuard],
    data: { codicePagina: 'gestione_utenti'},
    loadChildren: () => import('./gestione-utenti/gestione-utenti.module').then(m => m.GestioneUtentiModule)
  },
  {
    path: 'gestisci-gruppi',
    canActivate: [HasProfileEnteGuard],
    data: { codicePagina: 'gestione_gruppi'},
    loadChildren: () => import('./gestione-gruppi/gestione-gruppi.module').then(m => m.GestioneGruppiModule)
  },
  {
    path: 'preferenze-ente',
    canActivate: [HasProfileEnteGuard],
    data: { codicePagina: 'preferenze_ente'},
    children: [
      { path: '', component: PreferenzeEnteComponent },
      { path: 'applicazioni-esterne', component: AssociazioneEnteComponent, data: { codicePagina: 'applicazioni_esterne'} },
      { path: 'applicazioni-esterne/back', redirectTo: '', pathMatch: 'full' },
      { path: 'applicazioni-esterne/associa-dissocia/:idApp', component: AssociaDissociaEnteComponent, data: { codicePagina: 'associa_app_esterne'} },
      { path: 'applicazioni-esterne/associa-dissocia/:idApp/back', redirectTo: 'applicazioni-esterne', pathMatch: 'full' },
      // tslint:disable-next-line:max-line-length
      { path: 'applicazioni-esterne/:idApp/funzionalita', component: FunzionalitaComponent, data: { codicePagina: 'app_esterne_funzionalita'} },
      {
        path: 'applicazioni-esterne/:idApp/funzionalita/back', redirectTo: 'applicazioni-esterne/associa-dissocia/:idApp',
        pathMatch: 'full'
      },
      { path: 'applicazioni-esterne/:idApp/funzionalita/nuova', component: GestisciFunzionalitaNonPrincipaleComponent, data: { codicePagina: 'aggiungi_modifica_funzionalita_app_esterne'}  },
      {
        path: 'applicazioni-esterne/:idApp/funzionalita/nuova/back', redirectTo: 'applicazioni-esterne/:idApp/funzionalita',
        pathMatch: 'full'
      },
      { path: 'applicazioni-esterne/:idApp/funzionalita/:idFunz', component: GestisciFunzionalitaNonPrincipaleComponent,
        data: { codicePagina: 'aggiungi_modifica_funzionalita_app_esterne'}  },
      {
        path: 'applicazioni-esterne/:idApp/funzionalita/:idFunz/back', redirectTo: 'applicazioni-esterne/:idApp/funzionalita',
        pathMatch: 'full'
      },
    ]
  },
  {
    path: 'preferenze-utente',
    canActivate: [HasProfileGuard],
    data: { codicePagina: 'preferenze_utente'},
    loadChildren: () => import('./preferenze-utente/preferenze-utente.module').then(m => m.PreferenzeUtenteModule)
  },
  {
    path: 'informazioni-utente',
    canActivate: [HasProfileGuard],
    data: { codicePagina: 'informazioni_utente'},
    component: InfoUtenteComponent
  },
  {
    path: 'amministrazione',
    canActivate: [HasProfileGuard],
    loadChildren: () => import('./administration/administration.module').then(mod => mod.AdministrationModule),
  },
  {
    path: 'crea-pratica',
    canActivate: [HasProfileEnteGuard],
    data: { codicePagina: 'creazione_pratica' },
    component: CreazionePraticaComponent
  },
  {
    path: 'esecuzione-multipla',
    canActivate: [HasProfileEnteGuard],
    data: { codicePagina: 'esecuzione_multipla' },
    loadChildren: () => import('./esecuzione-multipla/esecuzione-multipla.module').then(m => m.EsecuzioneMultiplaModule)
  },
  {
    path: 'caricamento-pratiche',
    canActivate: [HasProfileEnteGuard],
    canDeactivate: [PendingChangesGuard],
    data: { codicePagina: 'caricamento_pratiche', descrizione: 'common.ritorna_al_caricamento_pratiche' },
    component: CaricamentoPraticheComponent
  },
  {
    canActivate: [HasProfileEnteGuard],
    path: 'configurazione',
    loadChildren: () => import('./configurator/configurator.module').then(mod => mod.ConfiguratorModule),

  },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', redirectTo: '/not-found' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
