/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DragDropModule } from '@angular/cdk/drag-drop';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import {
  RouterModule,
  Routes,
} from '@angular/router';

import { SharedModule } from 'src/app/shared/shared.module';

import { NgIdleModule } from '@ng-idle/core';

import {
  EsecuzioneMultiplaService,
} from '../esecuzione-multipla/esecuzione-multipla.service';
import { RicercaActaModule } from '../ricerca-acta/ricerca-acta.module';
import {
  DocumentiService,
} from '../shared/components/consultazione-documenti/services/documenti.service';
import { CustomFormService } from '../shared/services/customForm.service';
import {
  TipiDocumentiService,
} from '../shared/services/tipi-documenti/tipi-documenti.service';
import {
  SimpleFormContainerComponent,
} from '../simple-form/simple-form-container/simple-form-container.component';
import { SimpleFormModule } from '../simple-form/simple-form.module';
import { ApprovazioneComponent } from './approvazione/approvazione.component';
import {
  AssociazionePraticheComponent,
} from './associazione-pratiche/associazione-pratiche.component';
import {
  CollaborazioneComponent,
} from './collaborazione/collaborazione.component';
import { CommentiFormLogiciComponent } from './commenti/commenti.component';
import {
  ConsultazioneDocumentiFormLogiciComponent,
} from './consultazione-documenti/consultazione-documenti.component';
import {
  CreazionePraticaComponent,
} from './creazione-pratica/creazione-pratica.component';
import { CustomFormComponent } from './custom-form/custom-form.component';
import {
  FirmaDigitaleComponent,
} from './firma-digitale/firma-digitale.component';
import {
  FirmaDocumentiComponent,
} from './firma-documenti/firma-documenti.component';
import { FormLogiciComponent } from './form-logici.component';
import { FormLogiciResolver } from './form-logici.resolver';
import {
  GenerazioneReportComponent,
} from './generazione-report/generazione-report.component';
import {
  GestioneDocumentiComponent,
} from './gestione-documenti/gestione-documenti.component';
import {
  ModalIdleExpiredComponent,
} from './modal-idle-expired/modal-idle-expired.component';
import {
  ModalIdleWarningComponent,
} from './modal-idle-warning/modal-idle-warning.component';
import {
  PianificazioneAttivitaComponent,
} from './pianificazione-attivita/pianificazione-attivita.component';
import { SceltaComponent } from './scelta/scelta.component';
import { FormLogiciService } from './services/form-logici.service';
import { TabsService } from './services/tabs.service';
import { TabBadgeComponent } from './tab-badge/tab-badge.component';
import { TabsDirective } from './tabs/tabs.directive';
import { SimpleFormComponent } from './simple-form/simple-form.component';
import { FunzionalitaMultiIstanzaService } from '../shared/services/funzionalitaMultiIstanza.service';
import { AssegnazioneTagsComponent } from './assegnazione-tags/assegnazione-tags.component';
import { FirmaFeaComponent } from './firma-fea/firma-fea.component';
import { FeaService } from '../shared/services/fea.service';

export const formLogiciRoutes: Routes = [
  {
    path: '',
    component: FormLogiciComponent,
    resolve: {
      formLogiciContext: FormLogiciResolver
    },
  },
  {
    path: 'simple-form',
    component: SimpleFormComponent
  }
];

@NgModule({
  declarations: [
    ApprovazioneComponent,
    PianificazioneAttivitaComponent,
    GestioneDocumentiComponent,
    FirmaDocumentiComponent,
    FormLogiciComponent,
    TabsDirective,
    FirmaDigitaleComponent,
    CollaborazioneComponent,
    CommentiFormLogiciComponent,
    ConsultazioneDocumentiFormLogiciComponent,
    ModalIdleWarningComponent,
    ModalIdleExpiredComponent,
    TabBadgeComponent,
    AssociazionePraticheComponent,
    GenerazioneReportComponent,
    CustomFormComponent,
    CreazionePraticaComponent,
    SceltaComponent,
    SimpleFormComponent,
    AssegnazioneTagsComponent,
    FirmaFeaComponent,
  ],
  imports: [
    CommonModule,
    SharedModule,
    SimpleFormModule,
    RouterModule.forChild(formLogiciRoutes),
    DragDropModule,
    ReactiveFormsModule,
    NgIdleModule.forRoot(),
    RicercaActaModule,
  ],
  providers: [
    TabsService,
    FormLogiciResolver,
    FormLogiciService,
    DocumentiService,
    TipiDocumentiService,
    CustomFormService,
    EsecuzioneMultiplaService,
    FunzionalitaMultiIstanzaService,
    FeaService
  ],
  entryComponents: [
    ModalIdleWarningComponent,
    ModalIdleExpiredComponent,
  ]
})
export class FormLogiciModule { }
