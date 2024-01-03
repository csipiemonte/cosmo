/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Routes } from '@angular/router';
import { ApprovazioneMultiplaComponent } from './approvazione/approvazione-multipla.component';
import { EsecuzioneMultiplaComponent } from './esecuzione-multipla.component';
import { FirmaMultiplaComponent } from './firma/firma-multipla.component';
import { FormsComponent } from './forms/forms.component';

export const esecuzioneMultiplaRoute: Routes = [
  { path: '', component: EsecuzioneMultiplaComponent },
  { path: 'approvazione', component: ApprovazioneMultiplaComponent, data: { codicePagina: 'approvazione_multipla'} },
  { path: 'firma', component: FirmaMultiplaComponent, data: { codicePagina: 'firma_multipla'}},
  { path: 'forms/:type', component: FormsComponent, data: { codicePagina: 'forms_multipla'}}
];
