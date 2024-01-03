/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Routes } from '@angular/router';
import { ConfiguratorComponent } from './configurator.component';

export const configurationRoutes: Routes = [
    {path: '', component: ConfiguratorComponent},
    {path: 'tipologie-pratiche', loadChildren: () =>
        import('../administration/gestione-tipi-pratiche/gestione-tipi-pratiche.module').then(m => m.GestioneTipiPraticheModule)},
    {path: 'deploy-processo', loadChildren: () => import('../administration/deploy-processo/deploy-processo.module')
        .then(m => m.DeployProcessoModule)},
    {path: 'gestione-form-logici', loadChildren:  () => import('../administration/gestione-form-logici/gestione-form-logici.module')
    .then(m => m.GestioneFormLogiciModule)},
    { path: 'variabili-di-filtro-tipologie-pratiche',
        loadChildren: () => import('../administration/variabili-di-filtro-tipologie-pratiche/variabili-di-filtro-tipologie-pratiche.module')
          .then(m => m.VariabiliDiFiltroTipologiePraticheModule)
    },
    { path: 'gestione-template-fea',
        loadChildren: () => import('../administration/gestione-template-fea/gestione-template-fea.module')
          .then(m => m.GestioneTemplateFeaModule)
    }
];
