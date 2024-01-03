/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { ErrorComponent } from './error/error.component';
import { ErrorExpiredComponent } from './error-expired/error-expired.component';
import { ErrorNoProfilazioneComponent } from './error-no-profilazione/error-no-profilazione.component';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { ErrorNotFoundComponent } from './error-not-found/error-not-found.component';
import { LoadingComponent } from './loading/loading.component';

export const errorRoutes = [
  { path: 'error', component: ErrorComponent, data: {codicePagina: 'errorPage'} },
  { path: 'error-expired', component: ErrorExpiredComponent, data: {codicePagina: 'errorPage'} },
  { path: 'nessuna-profilazione', component: ErrorNoProfilazioneComponent, data: {codicePagina: 'errorPage'} },
  { path: 'error-unauthorized', component: UnauthorizedComponent, data: {codicePagina: 'errorPage'} },
  { path: 'not-found', component: ErrorNotFoundComponent, data: {codicePagina: 'errorPage'} },
  { path: 'loading', component: LoadingComponent, data: {codicePagina: 'errorPage'} },
];
