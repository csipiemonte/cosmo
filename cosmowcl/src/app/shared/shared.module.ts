/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DragDropModule } from '@angular/cdk/drag-drop';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { NgModule } from '@angular/core';
import {
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { RouterModule } from '@angular/router';

import { NgApexchartsModule } from 'ng-apexcharts';
import { ClipboardModule } from 'ngx-clipboard';
import { CosmoTableModule } from 'ngx-cosmo-table';
import { NgxJsonViewerModule } from 'ngx-json-viewer';
import { MarkdownModule } from 'ngx-markdown';
import { TimeagoModule } from 'ngx-timeago';
import { environment } from 'src/environments/environment';

import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {
  TranslateLoader,
  TranslateModule,
  TranslateService,
} from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import {
  ApprovazioneRifiutoComponent,
} from './components/approvazione-rifiuto/approvazione-rifiuto.component';
import {
  AssociaPraticheComponent,
} from './components/associa-pratiche/associa-pratiche.component';
import {
  CaricamentoFallitoComponent,
} from './components/caricamento-fallito/caricamento-fallito.component';
import {
  CaricamentoInCorsoComponent,
} from './components/caricamento-in-corso/caricamento-in-corso.component';
import {
  CertificatiFirmaComponent,
} from './components/certificati-firma/certificati-firma.component';
import {
  CertificatiService,
} from './components/certificati-firma/certificati/certificati.service';
import { CommentiComponent } from './components/commenti/commenti.component';
import {
  CommentoModalComponent,
} from './components/commenti/commento-modal/commento-modal.component';
import { CommentoComponent } from './components/commento/commento.component';
import {
  AggiungiDocumentoComponent,
} from './components/consultazione-documenti/aggiungi-documento/aggiungi-documento.component';
import {
  AllegatiComponent,
} from './components/consultazione-documenti/allegati/allegati.component';
import {
  ConsultazioneDocumentiComponent,
} from './components/consultazione-documenti/consultazione-documenti.component';
import {
  DettaglioSmistamentoComponent,
} from './components/consultazione-documenti/dettaglio-smistamento/dettaglio-smistamento.component';
import {
  DettaglioVerificaFirmeContainerComponent,
} from './components/consultazione-documenti/dettaglio-verifica-firme-container/dettaglio-verifica-firme-container.component';
import {
  DettaglioVerificaFirmeComponent,
} from './components/consultazione-documenti/dettaglio-verifica-firme/dettaglio-verifica-firme.component';
import {
  DocumentiService,
} from './components/consultazione-documenti/services/documenti.service';
import {
  CosmoEditorComponent,
} from './components/cosmo-editor/cosmo-editor.component';
import {
  CreaPraticaComponent,
} from './components/crea-pratica/crea-pratica.component';
import { DebugComponent } from './components/debug/debug.component';
import {
  DettaglioPraticaworkflowComponent,
} from './components/dettaglio-praticaworkflow/dettaglio-praticaworkflow.component';
import {
  DynamicInputComponent,
} from './components/dynamic-input/dynamic-input.component';
import { HelperComponent } from './components/helper/helper.component';
import {
  ImmagineWrapperComponent,
} from './components/immagine-wrapper/immagine-wrapper.component';
import {
  ImpostazioniFirmaComponent,
} from './components/impostazioni-firma/impostazioni-firma.component';
import {
  InfoPraticaComponent,
} from './components/info-pratica/info-pratica.component';
// tslint:disable-next-line: max-line-length
import {
  MessaggiControlliObbligatoriComponent,
} from './components/messaggi-controlli-obbligatori/messaggi-controlli-obbligatori.component';
import {
  AnteprimaDocumentoModalComponent,
} from './components/modals/anteprima-documento-modal/anteprima-documento-modal.component';
import {
  AssegnaAttivitaComponent,
} from './components/modals/assegna-attivita/assegna-attivita.component';
import {
  CondividiPraticaComponent,
} from './components/modals/condividi-pratica/condividi-pratica.component';
import {
  ConfermaRifiutaModalComponent,
} from './components/modals/conferma-rifiuta-modal/conferma-rifiuta-modal.component';
import {
  DueOpzioniComponent,
} from './components/modals/due-opzioni/due-opzioni.component';
import {
  ErrorModalComponent,
} from './components/modals/error-modal/error-modal.component';
import {
  HelperModalComponent,
} from './components/modals/helper-modal/helper-modal.component';
import {
  InfoModalComponent,
} from './components/modals/info-modal/info-modal.component';
import {
  SelezionaPraticaModalComponent,
} from './components/modals/seleziona-pratica-modal/seleziona-pratica-modal.component';
import {
  ShowRawDataModalComponent,
} from './components/modals/show-raw-data-modal/show-raw-data-modal.component';
import {
  NewsTickerComponent,
} from './components/news-ticker/news-ticker.component';
import {
  PraticheAssociateComponent,
} from './components/pratiche-associate/pratiche-associate.component';
import { RiassuntoComponent } from './components/riassunto/riassunto.component';
import {
  RicercaCustomFormComponent,
} from './components/ricerca-custom-form/ricerca-custom-form.component';
import {
  RicercaEnteComponent,
} from './components/ricerca-ente/ricerca-ente.component';
import {
  RicercaFruitoreComponent,
} from './components/ricerca-fruitore/ricerca-fruitore.component';
import {
  RicercaFunzionalitaFormLogicoComponent,
} from './components/ricerca-funzionalita-form-logico/ricerca-funzionalita-form-logico.component';
import {
  RicercaIstanzaFormLogicoComponent,
} from './components/ricerca-istanza-form-logico/ricerca-istanza-form-logico.component';
import {
  RicercaPraticheComponent,
} from './components/ricerca-pratiche/ricerca-pratiche.component';
import {
  RicercaStatoPraticaComponent,
} from './components/ricerca-stato-pratica/ricerca-stato-pratica.component';
import {
  RicercaTipoDocumentoComponent,
} from './components/ricerca-tipo-documento/ricerca-tipo-documento.component';
import {
  RicercaUtenteComponent,
} from './components/ricerca-utente/ricerca-utente.component';
import {
  SelectClasseComponent,
} from './components/select-classe/select-classe.component';
import {
  StoricoAttivitaComponent,
} from './components/storico-attivita/storico-attivita.component';
import {
  TaskModalComponent,
} from './components/task-modal/task-modal.component';
import {
  TemplateAlertDocumentiComponent,
} from './components/template-alert-documenti/template-alert-documenti.component';
import {
  TemplateFormatoDocumentiComponent,
} from './components/template-formato-documenti/template-formato-documenti.component';
import {
  TemplateStatoFirmaDocumentiComponent,
} from './components/template-stato-firma-documenti/template-stato-firma-documenti.component';
import {
  TemplateStatoFirmaUtenteCorrenteComponent,
} from './components/template-stato-firma-utente-corrente/template-stato-firma-utente-corrente';
import { HasUseCaseDirective } from './directives/has-use-case.directive';
import { MaskingDirective } from './directives/masking.directive';
import { PendingChangesGuard } from './guards/can-deactivate.guard';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { FilterPipe } from './pipes/filter.pipe';
import { ItemSortPipe } from './pipes/item-sort.pipe';
import { LimitPipe } from './pipes/limit.pipe';
import { TruncatePipe } from './pipes/truncate.pipe';
import { AsyncTaskModalService } from './services/async-task-modal.service';
import { AsyncTaskService } from './services/async-task.service';
import { AttivitaService } from './services/attivita.service';
import { ConfigurazioniService } from './services/configurazioni.service';
import {
  GestioneFormLogiciService,
} from './services/gestione-form-logici.service';
import { HelperService } from './services/helper.service';
import { LockService } from './services/lock.service';
import { ModalService } from './services/modal.service';
import {
  NotificationsWebsocketService,
} from './services/notifications-websocket.service';
import { NotificheService } from './services/notifiche.service';
import { PreferenzeEnteService } from './services/preferenze-ente.service';
import { RedirectService } from './services/redirect.service';
import { ReportService } from './services/report.service';
import { TaskService } from './services/task.service';
import { RedZoomModule } from 'ngx-red-zoom';
import { GestioneDocumentiDirective } from './components/consultazione-documenti/directives/gestione-documenti.directive';
import { FormFormioComponent } from './components/form-formio/form-formio.component';
import { FormioModule } from '@formio/angular';
import { DatiPraticaComponent } from './components/dati-pratica/dati-pratica.component';
import { RicercaTipoTagsComponent } from './components/ricerca-tipo-tags/ricerca-tipo-tags.component';
import { RicercaDettaglioTabsComponent } from './components/ricerca-dettaglio-tabs/ricerca-dettaglio-tabs.component';
import { RicercaGruppoComponent } from './components/ricerca-gruppo/ricerca-gruppo.component';
import { HtmlCustomModalComponent } from './components/modals/html-custom-modal/html-custom-modal.component';
import { VisualizzatorePdfComponent } from './components/visualizzatore-pdf/visualizzatore-pdf.component';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { PreviewDocumentoModalComponent } from './components/modals/preview-documento-modal/preview-documento-modal.component';
import { RisultatiFirmaModalComponent } from './components/risultati-firma-modal/risultati-firma-modal.component';
import { RisultatiFirmaModalService } from './services/risultati-firma-modal.service';
import { InfoDocumentoComponent } from './components/consultazione-documenti/info-documento/info-documento.component';
import { RicercaTipoPraticaComponent } from './components/ricerca-tipo-pratica/ricerca-tipo-pratica.component';
import { DettaglioInformazioniFeaComponent } from './components/consultazione-documenti/dettaglio-informazioni-fea/dettaglio-informazioni-fea.component';
import { TemplateStatoFeaDocumentiComponent } from './components/template-stato-fea-documenti/template-stato-fea-documenti.component';
import { DettaglioApprovazioniDocumentoComponent } from './components/consultazione-documenti/dettaglio-approvazioni-documento/dettaglio-approvazioni-documento.component';
import { SelezionaCertificatoComponent } from './components/certificati-firma/seleziona-certificato/seleziona-certificato.component';
import { AggiungiCertificatoModalComponent } from './components/certificati-firma/aggiungi-certificato-modal/aggiungi-certificato-modal.component';
import { TemplateStatoSigilloElettronicoDocumentiComponent } from './components/template-stato-sigillo-elettronico-documenti/template-stato-sigillo-elettronico-documenti.component';
import { DettaglioInformazioniSigilloElettronicoComponent } from './components/consultazione-documenti/dettaglio-informazioni-sigillo-elettronico/dettaglio-informazioni-sigillo-elettronico.component';
import { UploadFileModalComponent } from './components/modals/upload-file-modal/upload-file-modal.component';
import { UploadFileDirective } from './components/consultazione-documenti/directives/upload-file.directive';

// required for AOT compilation
export function HttpTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, environment.deployContextPath + '/assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    ConfermaRifiutaModalComponent,
    InfoModalComponent,
    ErrorModalComponent,
    ItemSortPipe,
    NewsTickerComponent,
    InfoPraticaComponent,
    FilterPipe,
    LimitPipe,
    TruncatePipe,
    AssegnaAttivitaComponent,
    CondividiPraticaComponent,
    ImpostazioniFirmaComponent,
    CaricamentoFallitoComponent,
    CaricamentoInCorsoComponent,
    CommentiComponent,
    CommentoModalComponent,
    CommentoComponent,
    HasUseCaseDirective,
    MaskingDirective,
    TemplateFormatoDocumentiComponent,
    TemplateAlertDocumentiComponent,
    CertificatiFirmaComponent,
    TemplateStatoFirmaDocumentiComponent,
    TemplateStatoFirmaUtenteCorrenteComponent,
    DettaglioVerificaFirmeComponent,
    DueOpzioniComponent,
    TaskModalComponent,
    RicercaUtenteComponent,
    RicercaEnteComponent,
    RicercaFruitoreComponent,
    ImmagineWrapperComponent,
    AnteprimaDocumentoModalComponent,
    DebugComponent,
    RicercaIstanzaFormLogicoComponent,
    RicercaFunzionalitaFormLogicoComponent,
    RicercaStatoPraticaComponent,
    RicercaTipoDocumentoComponent,
    SelectClasseComponent,
    DynamicInputComponent,
    RicercaPraticheComponent,
    RicercaCustomFormComponent,
    SelezionaPraticaModalComponent,
    ShowRawDataModalComponent,
    CosmoEditorComponent,
    MessaggiControlliObbligatoriComponent,
    AssociaPraticheComponent,
    DettaglioPraticaworkflowComponent,
    StoricoAttivitaComponent,
    RiassuntoComponent,
    ApprovazioneRifiutoComponent,
    PraticheAssociateComponent,
    CreaPraticaComponent,
    ConsultazioneDocumentiComponent,
    DettaglioSmistamentoComponent,
    DettaglioVerificaFirmeContainerComponent,
    AggiungiDocumentoComponent,
    AllegatiComponent,
    HelperComponent,
    GestioneDocumentiDirective,
    FormFormioComponent,
    DatiPraticaComponent,
    RicercaTipoTagsComponent,
    RicercaDettaglioTabsComponent,
    RicercaGruppoComponent,
    HtmlCustomModalComponent,
    RisultatiFirmaModalComponent,
    VisualizzatorePdfComponent,
    PreviewDocumentoModalComponent,
    InfoDocumentoComponent,
    RicercaTipoPraticaComponent,
    DettaglioInformazioniFeaComponent,
    TemplateStatoFeaDocumentiComponent,
    DettaglioApprovazioniDocumentoComponent,
    TemplateStatoSigilloElettronicoDocumentiComponent,
    DettaglioInformazioniSigilloElettronicoComponent,
    SelezionaCertificatoComponent,
    AggiungiCertificatoModalComponent,
    UploadFileModalComponent,
    UploadFileDirective
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    TimeagoModule.forRoot(),
    TranslateModule.forChild({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpTranslateLoader,
        deps: [HttpClient]
      }
    }),
    NgbModule,
    CosmoTableModule,
    ClipboardModule,
    CKEditorModule,
    MarkdownModule.forRoot(),
    NgApexchartsModule,
    DragDropModule,
    NgxJsonViewerModule,
    RedZoomModule,
    FormioModule,
    NgxExtendedPdfViewerModule,
  ],
  exports: [
    ItemSortPipe,
    FilterPipe,
    LimitPipe,
    TruncatePipe,
    TranslateModule,
    FormsModule,
    NgbModule,
    TimeagoModule,
    CosmoTableModule,
    CKEditorModule,
    ClipboardModule,
    MarkdownModule,
    NgApexchartsModule,
    ItemSortPipe,
    NewsTickerComponent,
    InfoPraticaComponent,
    ImpostazioniFirmaComponent,
    CaricamentoFallitoComponent,
    CaricamentoInCorsoComponent,
    CommentiComponent,
    CommentoComponent,
    HasUseCaseDirective,
    MaskingDirective,
    TemplateFormatoDocumentiComponent,
    TemplateAlertDocumentiComponent,
    CertificatiFirmaComponent,
    TemplateStatoFirmaDocumentiComponent,
    TemplateStatoFirmaUtenteCorrenteComponent,
    DettaglioVerificaFirmeComponent,
    RicercaUtenteComponent,
    RicercaEnteComponent,
    RicercaFruitoreComponent,
    ImmagineWrapperComponent,
    DebugComponent,
    RicercaIstanzaFormLogicoComponent,
    RicercaFunzionalitaFormLogicoComponent,
    RicercaStatoPraticaComponent,
    RicercaTipoDocumentoComponent,
    SelectClasseComponent,
    DynamicInputComponent,
    RicercaPraticheComponent,
    CosmoEditorComponent,
    MessaggiControlliObbligatoriComponent,
    DettaglioPraticaworkflowComponent,
    StoricoAttivitaComponent,
    RiassuntoComponent,
    ApprovazioneRifiutoComponent,
    PraticheAssociateComponent,
    CreaPraticaComponent,
    ConsultazioneDocumentiComponent,
    HelperComponent,
    NgxJsonViewerModule,
    GestioneDocumentiDirective,
    FormFormioComponent,
    DatiPraticaComponent,
    RicercaCustomFormComponent,
    RicercaTipoTagsComponent,
    RicercaDettaglioTabsComponent,
    RicercaGruppoComponent,
    RicercaTipoPraticaComponent
  ],
  providers: [
    RedirectService,
    AuthInterceptor,
    TranslateService,
    ModalService,
    NotificationsWebsocketService,
    PreferenzeEnteService,
    ConfigurazioniService,
    AttivitaService,
    TaskService,
    CertificatiService,
    LockService,
    AsyncTaskModalService,
    AsyncTaskService,
    NotificheService,
    PendingChangesGuard,
    GestioneFormLogiciService,
    ReportService,
    DocumentiService,
    HelperService,
    RisultatiFirmaModalService,
  ],
  entryComponents: [
    ConfermaRifiutaModalComponent,
    InfoModalComponent,
    ErrorModalComponent,
    CommentoModalComponent,
    TaskModalComponent,
    AnteprimaDocumentoModalComponent,
    SelezionaPraticaModalComponent,
    HelperModalComponent,
    ShowRawDataModalComponent,
    RisultatiFirmaModalComponent
  ]
})
export class SharedModule { }
