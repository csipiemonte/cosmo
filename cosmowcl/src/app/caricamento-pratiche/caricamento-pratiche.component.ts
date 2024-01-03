/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableFormatter, CosmoTableReloadReason, ICosmoTableColumn,
   ICosmoTablePageRequest, ICosmoTableReloadContext, ICosmoTableStatusSnapshot } from 'ngx-cosmo-table';
import { debounceTime, delay, distinctUntilChanged, finalize, groupBy, map, mergeAll, mergeMap, tap, toArray } from 'rxjs/operators';
import { CaricamentoPratica } from '../shared/models/api/cosmopratiche/caricamentoPratica';
import { CaricamentoPraticheService } from '../shared/services/caricamento-pratiche.service';
import { DateUtils } from '../shared/utilities/date-utils';
import { AggiungiCaricamentoPraticaModalComponent
  } from './aggiungi-caricamento-pratica-modal/aggiungi-caricamento-pratica-modal.component';
import { NotificationsWebsocketService } from '../shared/services/notifications-websocket.service';
import { Constants } from '../shared/constants/constants';
import { BehaviorSubject, Observable, ReplaySubject, Subject, Subscription, combineLatest, forkJoin, merge, of } from 'rxjs';
import { NotificationEvent } from '../shared/models/notifications/notification-event';
import { CaricamentiInBozzaModalComponent } from './caricamenti-in-bozza-modal/caricamenti-in-bozza-modal.component';
import { ConfigurazioniService } from '../shared/services/configurazioni.service';
import { StatoCaricamentoPratica } from '../shared/models/api/cosmopratiche/statoCaricamentoPratica';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiUrls } from '../shared/utilities/apiurls';
import { Utils } from '../shared/utilities/utilities';
import { HelperService } from '../shared/services/helper.service';

@Component({
  selector: 'app-caricamento-pratiche',
  templateUrl: './caricamento-pratiche.component.html',
  styleUrls: ['./caricamento-pratiche.component.scss']
})
export class CaricamentoPraticheComponent implements OnInit, OnDestroy {


  loading = 0;
  loadingError: any | null = null;
  exportRowMaxSize?: number;

  isCollapsed = true;

  statiCaricamentoDisponibili: StatoCaricamentoPratica[] = [];

  statoSelezionato: StatoCaricamentoPratica | null = null;

  nomeFile: string | null = null;

  @ViewChild('table') table: CosmoTableComponent | null = null;

  @ViewChildren('pratiche') pratiche: QueryList<CosmoTableComponent> | null = null;

  @ViewChildren('documentiProcessi') documentiProcessi: QueryList<CosmoTableComponent> | null = null;

  private notificationSubcritpion: Subscription | null = null;
  private notificationPraticheSubcritpion: Subscription | null = null;
  private canRefresh = false;




  columns: ICosmoTableColumn[] = [{
    name: 'statoCaricamentoPratica', label: 'Stato', field: 'statoCaricamentoPratica', canSort: false, canHide: false, applyTemplate: true,
    valueExtractor: row => row.statoCaricamentoPratica.descrizione ?? ''
  }, {
    label: 'Nome file', field: 'nomeFile', canSort: false,
  }, {
    name: 'Errore', label: 'Errore', field: 'errore', canSort: false
  }, {
    name: 'dataInserimento', label: 'Data Inserimento', field: 'dtInserimento', canSort: false,
    formatters: [{
      formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
     }, {
      format: (raw: any) => raw ?? '--',
    }],
    valueExtractor: raw => DateUtils.parse(raw.dtInserimento),
  }, {
    name: 'dataUltimaModifica', label: 'Data Ultima Modifica', field: 'dtUltimaModifica', canSort: false,
    formatters: [{
      formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
     }, {
      format: (raw: any) => raw ?? '--',
    }],
    valueExtractor: raw => DateUtils.parse(raw.dtUltimaModifica),
  }
];

columnsPratiche: ICosmoTableColumn[] = [{
  name: 'statoCaricamentoPratica', label: 'Stato', field: 'statoCaricamentoPratica', canSort: false, canHide: false, applyTemplate: true,
  valueExtractor: row => row.statoCaricamentoPratica.descrizione ?? ''
}, {
  label: 'Identificativo Pratica', field: 'identificativoPratica', canSort: false,
}, {
  name: 'descrizioneEvento', label: 'Descrizione Evento', field: 'descrizioneEvento', canSort: false,
}, {
  name: 'Errore', label: 'Errore', field: 'errore', canSort: false
}, {
  name: 'dataInserimento', label: 'Data Inserimento', field: 'dtInserimento', canSort: false,
  formatters: [{
    formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
   }, {
    format: (raw: any) => raw ?? '--',
  }],
  valueExtractor: raw => DateUtils.parse(raw.dtInserimento),
}, {
  name: 'dataUltimaModifica', label: 'Data Ultima Modifica', field: 'dtUltimaModifica', canSort: false,
  formatters: [{
    formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
   }, {
    format: (raw: any) => raw ?? '--',
  }],
  valueExtractor: raw => DateUtils.parse(raw.dtUltimaModifica),
}, {
  name: 'nomeFile', label: 'Nome File', field: 'nomeFile', defaultVisible: false, includeInExport: true
}, {
  name: 'dettaglio',
  canHide: false, canSort: false,
  applyTemplate: true, size: CosmoTableColumnSize.XXS
}
];

columnsDocProcessi: ICosmoTableColumn[] = [{
  name: 'statoCaricamentoPratica', label: 'Stato', field: 'statoCaricamentoPratica', canSort: false, canHide: false, applyTemplate: true,
  valueExtractor: row => row.statoCaricamentoPratica.descrizione ?? ''
}, {
  name: 'descrizioneEvento', label: 'Descrizione Evento', field: 'descrizioneEvento', canSort: false,
}, {
  name: 'errore', label: 'Errore', field: 'errore', canSort: false
}, {
  name: 'dataInserimento', label: 'Data Inserimento', field: 'dtInserimento', canSort: false,
  formatters: [{
    formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
   }, {
    format: (raw: any) => raw ?? '--',
  }],
  valueExtractor: raw => DateUtils.parse(raw.dtInserimento),
}, {
  name: 'dataUltimaModifica', label: 'Data Ultima Modifica', field: 'dtUltimaModifica', canSort: false,
  formatters: [{
    formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
   }, {
    format: (raw: any) => raw ?? '--',
  }],
  valueExtractor: raw => DateUtils.parse(raw.dtUltimaModifica),
}
];



  constructor(
    private caricamentoPraticheService: CaricamentoPraticheService,
    private modal: NgbModal,
    private notificationsWebsocketService: NotificationsWebsocketService,
    private configurazioniService: ConfigurazioniService,
    private router: Router,
    private route: ActivatedRoute,
    private helperService: HelperService
  ) {

  }


  ngOnInit(): void {

    this.loading++;
    this.loadingError = null;

    forkJoin({
      configurazione: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.EXPORT_ROW_SIZE),
      stati: this.caricamentoPraticheService.getStatiCaricamento()
    })
    .pipe(finalize(() => {this.loading--; }))
    .subscribe(response => {
      this.exportRowMaxSize = response.configurazione ? +response.configurazione :  undefined;
      this.statiCaricamentoDisponibili = response.stati.filter(stato => stato.codice?.includes('ELABORAZIONE')) ?? [];
    },
    failure => {
      this.loadingError = failure;
    });

    this.notificationSubcritpion = this.notificationsWebsocketService
      .whenEvent(Constants.APPLICATION_EVENTS.NOTIFICA_BATCH_CARICAMENTOPRATICHE)
      .subscribe(e => {
        this.smistaNotifiche(e);
        this.table?.refresh(true);
        const row = this.table?.getExpandedItems();
        row?.forEach(r => {
          const tabella = this.praticaCorrente(r);
          if (tabella){
            tabella?.refresh(true);
          }
        });
      });



    this.notificationPraticheSubcritpion = this.notificationsWebsocketService
      .whenEvent(Constants.APPLICATION_EVENTS.NOTIFICA_BATCH_CARICAMENTOPRATICHE_PRATICA)
      .subscribe(e => {
        this.smistaNotifichePratiche(e);
        this.table?.refresh(true);
        const row = this.table?.getExpandedItems();
        row?.forEach(r => {
          const tabella = this.praticaCorrente(r);
          if (tabella){
            tabella?.refresh(true);
          }
        });
      });


  }

  ngOnDestroy(): void {
    if (this.notificationSubcritpion) {
      this.notificationSubcritpion.unsubscribe();
    }

    if (this.notificationPraticheSubcritpion) {
      this.notificationPraticheSubcritpion.unsubscribe();
    }

  }

  cancellaRicerca(){
    this.statoSelezionato = null;
    this.nomeFile = null;
    this.table?.refresh(false);
  }

  filtra(){
    this.table?.refresh(false);
  }

  goToDettaglio(idPratica: number){
    this.router.navigate(['pratica', idPratica]);
  }

  praticaCorrente(row: CaricamentoPratica) {
    return this.pratiche?.find(table => table.tableContext?.id === row?.id);
  }
  docProcessoCorrente(row: CaricamentoPratica) {
    console.log(row);
    return this.documentiProcessi?.find(table => table.tableContext?.id === row?.id);
  }

  isExpanded(row: CaricamentoPratica){
    return this.table?.isExpanded(row);
  }

  smistaNotifichePratiche(e: NotificationEvent){


    if (e.payload?.evento === 'Pratica in errore'){
      this.notificationsWebsocketService.dispatchByType({title: e.payload?.nomeFile ?? '', message: e.payload?.identificativo ?
       'Errore creazione pratica con identificativo ' + e.payload?.identificativo : 'Errore creazione pratica' , type: 'DANGER'});

    }else if (e.payload?.evento === 'Pratica creata con errore'){
      this.notificationsWebsocketService.dispatchByType({title: e.payload?.nomeFile ?? '', message: e.payload?.identificativo ?
       'Pratica con identificativo ' + e.payload?.identificativo + ' creata con errore' : 'Pratica creata con errore' , type: 'WARNING'});

    }else{
      this.notificationsWebsocketService.dispatchByType({title: e.payload?.nomeFile ?? '', message: e.payload?.identificativo ?
       'Creata pratica con identificativo ' + e.payload?.identificativo : 'Creata pratica', type: 'SUCCESS'});
    }

  }

  smistaNotifiche(e: NotificationEvent){


    if (e.payload?.evento === 'Elaborazione in errore'){
      this.notificationsWebsocketService.dispatchByType({title: e.payload?.nomeFile ?? '',
       message: e.payload?.evento ?? '', type: 'DANGER'});
    }else if (e.payload?.evento === 'Elaborazione completata con errore'){
      this.notificationsWebsocketService.dispatchByType({title: e.payload?.nomeFile ?? '',
       message: e.payload?.evento ?? '', type: 'WARNING'});
    }else{
      this.notificationsWebsocketService.dispatchByType({title: e.payload?.nomeFile ?? '',
       message: e.payload?.evento ?? '', type: 'SUCCESS'});
    }

  }

  downloadTemplatePratiche(){
    Utils.download(ApiUrls.TEMPLATE.replace('{file}', 'template_pratiche.xlsx'));
  }

  downloadTemplateArchiviZip(){
    Utils.download(ApiUrls.TEMPLATE.replace('{file}', 'documenti_da_caricare.xlsx'));
  }


  aggiungiCaricamentoPratica(){
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(AggiungiCaricamentoPraticaModalComponent, {backdrop: 'static', keyboard: false, size: 'xl'});
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'aggiungi-caricamento-pratiche';
    modalRef.result.finally(() => this.table?.refresh()).catch();

  }

  caricamentiInBozza(){
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(CaricamentiInBozzaModalComponent, {backdrop: 'static', keyboard: false, size: 'xl'});
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'aggiungi-caricamento-in-bozza';
    modalRef.result.finally(() => this.table?.refresh()).catch();
  }


  expandableStatusProvider = (row: CaricamentoPratica, status: ICosmoTableStatusSnapshot | null) => {
    return row.hasElaborazioni;
  }

  private getFilterPayload(){
    const output: any = {
      filter: {},
    };
    if (this.statoSelezionato !== null){
      output.filter.codiceStatoCaricamentoPratica = {
        eq: this.statoSelezionato.codice
      };
    }
    if (this.nomeFile !== null && this.nomeFile !== ''){
      output.filter.nomeFile = {
        ci: this.nomeFile
      };
    }

    return output;
  }



  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {

    const payload = {
      page: input.page ?? 0,
      size: input.size ?? 10,
      ...this.getFilterPayload()
    };


    return this.caricamentoPraticheService.getCaricamentiPratica(JSON.stringify(payload)).pipe(
      map(response => ({
        content: response.caricamentoPratiche ?? [],
        number: response.pageInfo?.page,
        numberOfElements: response.caricamentoPratiche?.length,
        size: response.pageInfo?.pageSize,
        totalElements: response.pageInfo?.totalElements,
        totalPages: response.pageInfo?.totalPages,
      }))
    );
  }

  dataProviderPratiche = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {

    const payload = {
      page: input.page ?? 0,
      size: input.size ?? 10
    };
    const exportData = context?.reason === CosmoTableReloadReason.EXPORT ? true : false;

    return this.caricamentoPraticheService.getCaricamentiPraticaId(JSON.stringify(payload), input.context?.id?.toString(), exportData).pipe(
      map(response =>

        ({
        content: response.caricamentoPratiche ?? [],
        number: response.pageInfo?.page,
        numberOfElements: response.caricamentoPratiche?.length,
        size: response.pageInfo?.pageSize,
        totalElements: response.pageInfo?.totalElements,
        totalPages: response.pageInfo?.totalPages,
      }))
    );
  }


  getBadgeClass(row: CaricamentoPratica){
    return row.statoCaricamentoPratica?.codice?.includes('ERRORE') ? ( row.statoCaricamentoPratica?.codice === 'ELABORAZIONE_COMPLETATA_CON_ERRORE' || row.statoCaricamentoPratica?.codice === 'PRATICA_CREATA_CON_ERRORE' ? 'warning' : 'danger') : ( row.statoCaricamentoPratica?.codice === 'CARICAMENTO_COMPLETATO' ? 'primary' : 'success');
  }


  tornaIndietro() {
    // this.router.navigate(['back'], { relativeTo: this.route });
    window.history.back();
  }

  canShowDetailButton(row: any) {
    return row.visualizzaDettaglio;
  }


}
