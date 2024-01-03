/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DragDropModule } from '@angular/cdk/drag-drop';
import {
  HTTP_INTERCEPTORS,
  HttpClient,
  HttpClientModule,
} from '@angular/common/http';
import {
  ErrorHandler,
  NgModule,
} from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';

import { NgHttpLoaderModule } from 'ng-http-loader';
import {
  LoggerModule,
  NgxLoggerLevel,
} from 'ngx-logger';
import { MarkdownModule } from 'ngx-markdown';
import { ToastrModule } from 'ngx-toastr';

import {
  FormioAppConfig,
  FormioModule,
} from '@formio/angular';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {
  CreazionePraticaComponent,
} from './creazione-pratica/creazione-pratica.component';
import {
  DettaglioPraticaModule,
} from './dettaglio-pratica/dettaglio-pratica.module';
import {
  ErrorExpiredComponent,
} from './error-pages/error-expired/error-expired.component';
import {
  ErrorNoProfilazioneComponent,
} from './error-pages/error-no-profilazione/error-no-profilazione.component';
import {
  ErrorNotFoundComponent,
} from './error-pages/error-not-found/error-not-found.component';
import { ErrorComponent } from './error-pages/error/error.component';
import { LoadingComponent } from './error-pages/loading/loading.component';
import {
  UnauthorizedComponent,
} from './error-pages/unauthorized/unauthorized.component';
import { HomeModule } from './home/home.module';
import {
  IdentitaUtenteActaComponent,
} from './identita-utente-acta/identita-utente-acta.component';
import { FooterComponent } from './layout/footer/footer.component';
import { HeaderComponent } from './layout/header/header.component';
import {
  AppEsterneComponent,
} from './layout/menu/app-esterne/app-esterne.component';
import { MenuComponent } from './layout/menu/menu.component';
import { PreferenzeEnteModule } from './preferenze-ente/preferenze-ente.module';
import {
  PreferenzeUtenteModule,
} from './preferenze-utente/preferenze-utente.module';
import { ProfilazioneModule } from './profilazione/profilazione.module';
import {
  GlobalErrorHandler,
} from './shared/components/global-error-handler/global-error-handler.handler';
import { FormioConfig } from './shared/configuration/formio-config';
import { HasProfileEnteGuard } from './shared/guards/has-profile-ente.guard';
import { HasProfileGuard } from './shared/guards/has-profile.guard';
import { IsFormLogicoGuard } from './shared/guards/is-form-logico.guard';
import { AuthInterceptor } from './shared/interceptors/auth.interceptor';
import { SharedModule } from './shared/shared.module';
import { InfoUtenteComponent } from './info-utente/info-utente.component';
import { ConfiguratorComponent } from './configurator/configurator.component';
import { CaricamentoPraticheComponent } from './caricamento-pratiche/caricamento-pratiche.component';
import { CaricamentoPraticheDirective } from './caricamento-pratiche/caricamento-pratiche.directive';
import { AggiungiPraticheDocumentiModalComponent } from './caricamento-pratiche/aggiungi-pratiche-documenti-modal/aggiungi-pratiche-documenti-modal.component';
import { AggiungiCaricamentoPraticaModalComponent } from './caricamento-pratiche/aggiungi-caricamento-pratica-modal/aggiungi-caricamento-pratica-modal.component';
import { CaricamentiInBozzaModalComponent } from './caricamento-pratiche/caricamenti-in-bozza-modal/caricamenti-in-bozza-modal.component';

// required for AOT compilation
export function HttpTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, 'assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    ErrorComponent,
    ErrorNotFoundComponent,
    ErrorNoProfilazioneComponent,
    ErrorExpiredComponent,
    HeaderComponent,
    UnauthorizedComponent,
    FooterComponent,
    MenuComponent,
    CreazionePraticaComponent,
    AppEsterneComponent,
    LoadingComponent,
    IdentitaUtenteActaComponent,
    InfoUtenteComponent,
    CaricamentoPraticheComponent,
    CaricamentoPraticheDirective,
    AggiungiPraticheDocumentiModalComponent,
    AggiungiCaricamentoPraticaModalComponent,
    CaricamentiInBozzaModalComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    RouterModule,
    AppRoutingModule,
    HttpClientModule,
    SharedModule,
    ProfilazioneModule,
    HomeModule,
    FormsModule,
    DragDropModule,
    ReactiveFormsModule,
    PreferenzeEnteModule,
    PreferenzeUtenteModule,
    DettaglioPraticaModule,
    ToastrModule.forRoot({
      timeOut: 99999,
      extendedTimeOut: 1000,
      preventDuplicates: true,
      resetTimeoutOnDuplicate: true
    }), // ToastrModule added
    LoggerModule.forRoot({
      level: NgxLoggerLevel.DEBUG
    }),
    NgHttpLoaderModule.forRoot(),
    TranslateModule.forRoot({
      loader: {
        provide: HttpTranslateLoader,
        useFactory: HttpTranslateLoader,
        deps: [HttpClient]
      }
    }),
    MarkdownModule.forRoot(),
    FormioModule
  ],
  providers: [
    {
      provide: ErrorHandler,
      useClass: GlobalErrorHandler
    },
    HasProfileGuard,
    HasProfileEnteGuard,
    IsFormLogicoGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {provide: FormioAppConfig, useValue: FormioConfig}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
