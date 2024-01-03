/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Routes } from '@angular/router';
import { DiscoveryComponent } from './discovery/discovery.component';
import { HomeComponent } from './home/home.component';
import { ImportaProcessoComponent } from './importa-processo/importa-processo.component';
import { JsltSandboxComponent } from './jslt-sandbox/jslt-sandbox.component';


export const administationRoutes: Routes = [{
    path: '', component: HomeComponent
  }, {
    path: 'gestione-enti',
    loadChildren: () => import('./gestione-enti/gestione-enti.module').then(m => m.GestioneEntiModule)
  }, {
    path: 'gestione-fruitori',
    loadChildren: () => import('./gestione-fruitori/gestione-fruitori.module').then(m => m.GestioneFruitoriModule)
  }, {
    path: 'discovery', component: DiscoveryComponent
  }, {
    path: 'gestione-app-esterne',
    loadChildren: () => import('./gestione-app-esterne/gestione-app-esterne.module').then(m => m.GestioneAppEsterneModule)
  }, {
    path: 'deploy-processo', loadChildren: () => import('./deploy-processo/deploy-processo.module').then(m => m.DeployProcessoModule)
  }, {
    path: 'importa-processo', component: ImportaProcessoComponent
  }, {
    path: 'jslt-sandbox', component: JsltSandboxComponent
  }, {
    path: 'gestione-form-logici',
    loadChildren: () => import('./gestione-form-logici/gestione-form-logici.module').then(m => m.GestioneFormLogiciModule)
  }, {
    path: 'gestione-istanze-funzionalita-form-logici',
    loadChildren: () => import('./gestione-istanze-funzionalita-form-logici/gestione-istanze-funzionalita-form-logici.module')
      .then(m => m.GestioneIstanzeFunzionalitaFormLogiciModule)
  }, {
    path: 'gestione-tipologie-pratiche',
    loadChildren: () => import('./gestione-tipi-pratiche/gestione-tipi-pratiche.module')
      .then(m => m.GestioneTipiPraticheModule)
  },
  {
    path: 'gestione-template-report',
    loadChildren: () => import('./gestione-template-report/gestione-template-report.module')
      .then(m => m.GestioneTemplateReportModule)
  },
  {
    path: 'gestione-custom-form',
    loadChildren: () => import('./gestione-custom-form/gestione-custom-form.module')
      .then(m => m.GestioneCustomFormModule)
  },
  {
    path: 'gestione-helper',
    loadChildren: () => import('./gestione-helper/gestione-helper.module')
      .then(m => m.GestioneHelperModule)
  },
  {
    path: 'gestione-tags',
    loadChildren: () => import('./gestione-tags/gestione-tags.module')
      .then(m => m.GestioneTagsModule)
  },
  {
    path: 'gestione-parametri-di-sistema',
    loadChildren: () => import('./gestione-parametri-di-sistema/gestione-parametri-di-sistema.module')
      .then(m => m.GestioneParametriDiSistemaModule)
  },
  {
    path: 'task-automatici-in-errore',
    loadChildren: () => import('./task-automatici-in-errore/task-automatici-in-errore.module')
      .then(m => m.TaskAutomaticiInErroreModule)
  },
  {
    path: 'gestione-use-case-profili',
    loadChildren: () => import('./gestione-use-case-profili/gestione-use-case-profili.module')
      .then(m => m.GestioneUseCaseProfiliModule)
  },
  {
    path: 'variabili-di-filtro-tipologie-pratiche',
    loadChildren: () => import('./variabili-di-filtro-tipologie-pratiche/variabili-di-filtro-tipologie-pratiche.module')
      .then(m => m.VariabiliDiFiltroTipologiePraticheModule)
  },
  {
    path: 'gestione-template-fea',
    loadChildren: () => import('./gestione-template-fea/gestione-template-fea.module')
      .then(m => m.GestioneTemplateFeaModule)
  },
  {
    path: 'gestione-sigilli-elettronici',
    loadChildren: () => import('./gestione-sigilli-elettronici/gestione-sigilli-elettronici.module')
      .then(m => m.GestioneSigilliElettroniciModule)
  },
  {
    path: 'configurazioni-messaggi-notifiche',
    loadChildren: () => import('./configurazioni-messaggi-notifiche/configurazioni-messaggi-notifiche.module')
      .then(m => m.ConfigurazioniMessaggiNotificheModule)
  }
];
