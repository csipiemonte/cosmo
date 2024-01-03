/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  QueryList,
  ViewChildren,
} from '@angular/core';

import { SpinnerVisibilityService } from 'ng-http-loader';
import {
  CosmoTableColumnSize,
  CosmoTableComponent,
  CosmoTableFormatter,
  CosmoTableReloadReason,
  ICosmoTableColumn,
  ICosmoTablePageRequest,
  ICosmoTablePageResponse,
  ICosmoTableReloadContext,
  ICosmoTableStatusSnapshot,
} from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, of } from 'rxjs';
import {
  debounceTime,
  filter,
  finalize,
  map,
} from 'rxjs/operators';
import {
  FirmaDigitaleComponent,
} from 'src/app/form-logici/firma-digitale/firma-digitale.component';
import {
  TipiDocumentoObbligatori,
} from 'src/app/form-logici/gestione-documenti/models/tipi-documento-obbligatori.models';
import { Constants } from 'src/app/shared/constants/constants';
import {
  TipoDocumento,
} from 'src/app/shared/models/api/cosmobusiness/tipoDocumento';
import {
  ContenutoDocumento,
} from 'src/app/shared/models/api/cosmoecm/contenutoDocumento';
import {
  DocumentoDTO,
  TipoContenutoDocumentoEnum,
  TipoContenutoDocumentoFirmatoEnum,
} from 'src/app/shared/models/documento/documento.model';
import {
  StatoDocumentoEnum,
} from 'src/app/shared/models/stato-documento/stato-documento.model';
import { ModalService } from 'src/app/shared/services/modal.service';
import {
  NotificationsWebsocketService,
} from 'src/app/shared/services/notifications-websocket.service';
import { Utils } from 'src/app/shared/utilities/utilities';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';

import { ProfiloFEQ } from '../../models/api/cosmoauthorization/profiloFEQ';
import { Task } from '../../models/api/cosmobusiness/task';
import { Documento } from '../../models/api/cosmoecm/documento';
import {
  VerificaTipologiaDocumentiSalvati,
} from '../../models/api/cosmoecm/verificaTipologiaDocumentiSalvati';
import { Pratica } from '../../models/api/cosmopratiche/pratica';
import {
  MessaggioControlliObbligatori,
  TipoMessaggio,
} from '../../models/messaggi-controlli-obbligatori/messaggio-controlli-obbligatori';
import { ConfigurazioniService } from '../../services/configurazioni.service';
import { SecurityService } from '../../services/security.service';
import {
  TipiDocumentiService,
} from '../../services/tipi-documenti/tipi-documenti.service';
import { MessaggioModale } from '../modals/due-opzioni/due-opzioni.component';
import {
  AggiungiDocumentoComponent,
} from './aggiungi-documento/aggiungi-documento.component';
import { DocumentiService } from './services/documenti.service';
import { PreviewDocumentoModalComponent } from '../modals/preview-documento-modal/preview-documento-modal.component';
import { FirmaFeaComponent } from 'src/app/form-logici/firma-fea/firma-fea.component';
import { ActivatedRoute } from '@angular/router';
import { HelperService } from '../../services/helper.service';
import { DateUtils } from '../../utilities/date-utils';

@Component({
  selector: 'app-consultazione-documenti',
  templateUrl: './consultazione-documenti.component.html',
  styleUrls: ['./consultazione-documenti.component.scss']
})
export class ConsultazioneDocumentiComponent implements OnInit {

  @Input() esecuzioneMultipla = false;
  @Input() idPratica = 0;
  @Input() pratica!: Pratica;
  @Input() soloLettura = true;
  @Input() selezionaDocumenti = false;
  @Input() identificativo = 'consultazione-documenti';
  @Input() daFirmare = false;
  @Input() firmaFea = false;
  @Input() listaDocObbligatori: TipiDocumentoObbligatori[] = [];
  @Input() soloFirmabili = false;
  @Input() listaDocDaFirmareObbl: TipiDocumentoObbligatori[] = [];
  @Input() tipiDocNonEliminabili: TipoDocumento[] = [];
  @Input() tipiAllNonEliminabili: TipoDocumento[] = [];
  @Input() tipiDocCaricabili: TipoDocumento[] = [];
  @Input() tipiAllCaricabili: TipoDocumento[] = [];
  @Input() infoTask: Task | undefined;
  @Input() embedded?: boolean;
  @Input() idDocumentiSelezionati: number[] = [];
  @Input() verificaValiditaFirme = false;
  @Input() verificaDataDocObbligatori = false;
  @Input() codiceModaleAnteprima = 'anteprima-documento';
  @Input() codiceModalePreview = 'preview-documento';
  @Input() codiceModaleInfoSigillo = 'info-sigillo';
  @Input() codiceModaleInfoFea = 'info-fea';
  @Input() codiceModaleInfoFirma = 'info-firma';
  @Output() numDocumentiDaFirmare = new EventEmitter<number>();
  @Output() messaggiObbligatori = new EventEmitter<MessaggioControlliObbligatori[]>();
  @Output() totDocumenti = new EventEmitter<number>();
  @Output() firmabile = new EventEmitter<{ idPratica: number, firmabile: boolean, idAttivita: number }>();
  @Output() firmeUtenteValide = new EventEmitter<boolean>();
  @Output() attesaElaborazioneDocumenti = new EventEmitter<boolean>();
  firmatarioFea = '';

  defaultDimensioneMassimaAnteprimaPdf = 10 * 1024 * 1024; // 10 MB

  isCollapsed = true;

  tipologia: string | null = null;
  titoloNomeFile: string | null = null;
  formato: string | null = null;
  docsDaFirmare: boolean | null = null;

  cfUser!: string;

  maxNumeroDocumenti: number | null = null;
  maxDimensioneAnteprimaPdf: number | null = null;
  numeroDocumenti = 0;
  messaggiControlliObbligatori: MessaggioControlliObbligatori[] = [];
  listaDocsSoloFirmabile: Documento[] = [];
  loading = 0;
  exportRowMaxSize?: number;

  columns: ICosmoTableColumn[] = [
    { label: 'tipologia', field: 'tipo.descrizione', canSort: false },
    {
      name: 'titolo_nome_file', label: 'titolo/nome file', field: 'titoloNomeFile', canSort: true,
      valueExtractor: row => row.titolo ?? row.contenutoEffettivo?.nomeFile,
      formatters: [{ format: (raw: any) => raw ?? '--', }],
    },
    {
      name: 'ultima_modifica', label: 'ultima modifica', field: 'ultimaModifica', canSort: true,
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
       }, {
        format: (raw: any) => raw ?? '--',
      }],
      valueExtractor: raw => DateUtils.parse(raw.ultimaModifica)
    },
    { name: 'formato', label: 'formato', applyTemplate: true,
      valueExtractor: row => (row as DocumentoDTO).contenutoEffettivo?.formatoFile?.descrizione ?? '--',
    },
    { name: 'stato', label: '', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.SMALL,
      valueExtractor: row => (row as DocumentoDTO).stato?.descrizione ?? '--',
      includeInExport: false,
    },
    { name: 'infoDocumento', label: '', canSort: false, canHide: false, applyTemplate: true, size: CosmoTableColumnSize.XXS },
    { name: 'aggiungiAllegato', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS },
    { name: 'dettaglio', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XS },
    { name: 'modifica', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS },
    { name: 'cancella', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS },
  ];

  childColumns: ICosmoTableColumn[] = [
    { name: 'iconaAllegato', canHide: false, canSort: false, applyTemplate: true, size: CosmoTableColumnSize.XXS },
    ...this.columns.filter(c => c.name !== 'aggiungiAllegato')
  ];

  @ViewChildren('table') tables: QueryList<CosmoTableComponent> | null = null;

  constructor(
    private logger: NGXLogger,
    private documentiService: DocumentiService,
    private tipiDocumentiService: TipiDocumentiService,
    private translateService: TranslateService,
    private modalService: ModalService,
    private notificationsWebsocketService: NotificationsWebsocketService,
    private securityService: SecurityService,
    private modal: NgbModal,
    private datePipe: DatePipe,
    private spinner: SpinnerVisibilityService,
    private configurazioniService: ConfigurazioniService,
    private helperService: HelperService,
    private route: ActivatedRoute,
  ) { }

  get mainTable(): CosmoTableComponent | null {
    const table = this.tables?.find(e =>
      !(e.tableContext?.parentId)
    );
    return table as any as CosmoTableComponent ?? null;
  }

  get subTables(): CosmoTableComponent[] {
    const tables = this.tables?.filter(e =>
      !!(e.tableContext?.parentId)
    );
    return tables ?? [];
  }

  get documentiSelezionati(): DocumentoDTO[] {
    if (!this.tables) {
      return [];
    }
    let sel: DocumentoDTO[] = [];
    this.tables.forEach(table => {
      const tableSelected = table.getStatusSnapshot()?.checkedItems ?? [];
      if (tableSelected.length) {
        sel = [...sel, ...tableSelected];
      }
    });
    return sel;
  }

  ngOnInit(): void {



    if (this.idPratica > 0) {
      // TODO sostituire con BusService
      this.documentiService.getCercaDocumenti().subscribe(ricerca => {
        if (ricerca) {
          this.refresh(true);
        }
      });

      // batch caricamento index, smistamento stardas e sigillo elttronico
      this.notificationsWebsocketService
        .whenEvents([
          Constants.APPLICATION_EVENTS.DOCUMENTI_CARICATI,
          Constants.APPLICATION_EVENTS.DOCUMENTI_SMISTATI,
          Constants.APPLICATION_EVENTS.DOCUMENTI_SIGILLATI
        ]
        ).pipe(
          filter(e => e.payload.idPratica === this.idPratica),
          debounceTime(1000)
        )
        .subscribe(() => this.refresh(true));
    }

    this.securityService.principal$.subscribe(newPrincipal => {
      this.cfUser = newPrincipal.codiceFiscale;
      this.firmatarioFea = newPrincipal.nome + ' ' + newPrincipal.cognome;
    });

    this.refresh();

    forkJoin({
      documenti: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.MAX_PAGE_SIZE),
      exportRow: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.EXPORT_ROW_SIZE),
      dimensioneAnteprima: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.DOCUMENTI_PDF_ANTEPRIMA_MAX_SIZE)
    }).subscribe(response => {
      if (response?.documenti) {
        this.maxNumeroDocumenti = +response.documenti;
      }
      if (response?.exportRow) {
        this.exportRowMaxSize = +response.exportRow;
      }
      if (response?.dimensioneAnteprima && (+response.dimensioneAnteprima) < (this.defaultDimensioneMassimaAnteprimaPdf)){
        this.maxDimensioneAnteprimaPdf = (+response.dimensioneAnteprima) * 1024 * 1024;
      }else{
        this.maxDimensioneAnteprimaPdf = this.defaultDimensioneMassimaAnteprimaPdf;
      }
    });
  }

  refresh(background?: boolean) {
    this.mainTable?.refresh(background ?? false);
  }

  expandableStatusProvider = (row: DocumentoDTO, status: ICosmoTableStatusSnapshot | null) => {
    return row.allegati?.length;
  }



  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    if (input.context?.parentId) {
      const parentId = input.context.parentId as number;
      return of(this.mainTable?.getDataSnapshot()?.find((doc: DocumentoDTO) => doc.id === parentId)?.allegati ?? []).pipe(
        map(response => {
          const copy: ICosmoTablePageResponse = {
            content: response.map((doc: DocumentoDTO) => this.documentiService.mapDocumento(doc)),
            number: response.length,
            numberOfElements: response.length ?? 0,
            size: response.pageInfo?.pageSize ?? 0,
            totalElements: response.length ?? 0,
            totalPages: 1
          };
          return copy;
        })
      );

    }

    this.loading++;




    const filtro = this.creaFiltro(
      input.page ?? 0,
      input.size ?? 1000,
      input.sort?.length ? (input.sort[0]?.property + ' ' + input.sort[0]?.direction) : undefined,
      input.context?.parentId as number | undefined
    );
    const filtroDaFirmare = this.creaFiltroDocDaFirmare(false);

    const exportData = context?.reason === CosmoTableReloadReason.EXPORT ? true : false;

    return this.documentiService.getDocumenti(this.soloFirmabili || !!this.docsDaFirmare, filtro, filtroDaFirmare, exportData).pipe(
      map(response => {
        const copy: ICosmoTablePageResponse = {
          content: response.documenti as any[],
          number: response.documenti?.length,
          numberOfElements: response.documenti?.length ?? 0,
          size: response.pageInfo?.pageSize ?? 0,
          totalElements: response.pageInfo?.totalElements ?? 0,
          totalPages: response.pageInfo?.totalPages
        };

        const numeroDocumentiTotali = this.getNumeroTotaleDocs(copy.content);
        this.totDocumenti.emit(numeroDocumentiTotali);
        this.numeroDocumenti = numeroDocumentiTotali;
        this.controlloTipiDocumento();
        if (this.soloFirmabili) {
          this.listaDocsSoloFirmabile = this.getTuttiDocumentiDaFirmare(copy.content);
          this.controlloTipiDocumentoDaFirmare(this.listaDocsSoloFirmabile);
          this.numDocumentiDaFirmare.emit(this.listaDocsSoloFirmabile.length);
          this.firmabile.emit({
            idPratica: this.idPratica,
            firmabile: copy.content.length > 0,
            idAttivita: +(this.infoTask?.id ?? 0)
          });
        }
        this.checkElaborazioneIndex(copy.content.map((doc: DocumentoDTO) => this.documentiService.mapDocumento(doc)));
        this.checkValiditaFirme(copy.content.map((doc: DocumentoDTO) => this.documentiService.mapDocumento(doc)));
        this.settaDocumentiSelezionati(response.documenti ?? []);

        return copy;
      }), finalize(() => {
        this.loading--;
      })
    );
  }

  private getNumeroTotaleDocs(response: Documento[]) {
    let tot = 0;
    response.forEach(doc => {
      tot += ((doc.allegati?.length) ?? 0 ) + 1;
    });
    return tot;
  }

  settaDocumentiSelezionati(documenti: DocumentoDTO[]) {

    if (this.selezionaDocumenti && documenti.length > 0 && this.idDocumentiSelezionati.length > 0) {
      documenti.forEach(documento => {
        if (documento.id && this.idDocumentiSelezionati.includes(documento.id)) {
          this.mainTable?.toggleChecked(documento);
        }
      });
    }
  }

  private creaFiltro(page: number, size: number, sort: any, idParent?: number, tutti?: boolean): string {
    const filters: any = {};
    filters.idPratica = { eq: this.idPratica };

    if (this.firmaFea && (this.soloFirmabili || !!this.docsDaFirmare || tutti)){
      this.formato = 'application/pdf';
      filters.codiceTipoFirma = { nin: [TipoContenutoDocumentoFirmatoEnum.FIRMA_DIGITALE,
      TipoContenutoDocumentoFirmatoEnum.SIGILLO_ELETTRONICO]};
    }

    if (idParent) {
      filters.idParent = { eq: idParent };
    } else {
      filters.idParent = { defined: false };
    }

    if (this.tipologia) {
      filters.tipo = { ci: this.tipologia };
    }
    if (this.titoloNomeFile) {
      filters.titoloNomeFile = { ci: this.titoloNomeFile };
    }

    if (this.formato) {
      filters.formato = { ci: this.formato };
    }

    const filtri = { filter: filters, page, size, sort };
    return JSON.stringify(filtri);
  }

  private creaFiltroDocDaFirmare(tutti: boolean){
    const filters: any = {};
    filters.creationTime = this.infoTask?.createTime;
    filters.tutti = tutti;
    filters.tipologieDocumenti = this.listaDocDaFirmareObbl.map(tipo => tipo.codice);

    return JSON.stringify(filters);
  }

  cancellaRicerca() {
    this.tipologia = null;
    this.titoloNomeFile = null;
    this.formato = null;
    this.docsDaFirmare = null;
    this.refresh();
  }

  filtra() {
    this.refresh();
  }

  download(doc: DocumentoDTO, contenuto: ContenutoDocumento) {
    this.documentiService.downloadContenutoDocumento(doc.id || 0, contenuto.id || 0).subscribe(
      res => {
        Utils.download(res);
      },
      error => {
        let messaggioErrore = this.translateService.instant('errori.download_file');

        if (doc?.titolo) {
          messaggioErrore = Utils.parseAndReplacePlaceholders(
            this.translateService.instant('errori.download_file_msg'),
            [doc.titolo]);
        }

        this.mostraErrore(
          this.translateService.instant('errori.download_file'),
          messaggioErrore,
          error.error.errore
        );
      }
    );
  }

  preview(doc: DocumentoDTO, contenuto: ContenutoDocumento) {
    if (contenuto.formatoFile?.mimeType === 'application/pdf' && this.maxDimensioneAnteprimaPdf && contenuto.dimensione &&
        contenuto.dimensione <= this.maxDimensioneAnteprimaPdf){
      this.apriVisualizzatore(doc, contenuto);
    }else{
      alert();
      this.documentiService.previewContenutoDocumento(doc.id || 0, contenuto.id || 0).subscribe(
        res => Utils.preview(res),
        error => this.errorePreview(doc, contenuto, error.error.errore)
      );
    }

  }

  errorePreview(doc: DocumentoDTO, contenuto: ContenutoDocumento, eccezione: string) {
    let messaggioErrore = this.translateService.instant('errori.preview_file');

    if (doc?.titolo) {
      messaggioErrore = Utils.parseAndReplacePlaceholders(
        this.translateService.instant('errori.preview_file_msg'),
        [doc.titolo]);
    }

    this.mostraErrore(
      this.translateService.instant('errori.preview_file'),
      messaggioErrore,
      eccezione
    );
  }

  cancellaDocumento(doc: DocumentoDTO) {
    const modalText: MessaggioModale[] = [];
    if (doc?.allegati?.length) {
      modalText.push({
        testo: 'Attenzione: la cancellazione del documento principale comporta la cancellazione di tutti i suoi allegati.',
        classe: 'text-danger'
      });
    }
    modalText.push({ testo: 'Sei sicuro di voler eliminare il documento ?' });

    this.modalService.scegli(
      'conferma eliminazione',
      modalText,
      [
        { testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt' },
        { testo: 'Annulla', dismiss: true, defaultFocus: true }
      ]
    ).then(
      () => {
        if (doc?.id) {
          this.documentiService.cancellaDocumento(doc.id).subscribe(
            () => {
              this.modalService.info(
                this.translateService.instant('documenti.elimina_documento'),
                this.translateService.instant('documenti.eliminazione_documento')
              ).then(() => {
                this.refresh();
                this.documentiService.setCercaDocumenti(true);
                }).catch(() => { });
            },
            error => {
              const nome = doc.titolo ? doc.titolo :
                doc.contenutoEffettivo?.nomeFile
                  ? doc.contenutoEffettivo.nomeFile : '--';

              const messaggioErrore = Utils.toMessage(error, Utils.parseAndReplacePlaceholders(
                this.translateService.instant('errori.eliminazione_file_msg'),
                [nome]));

              this.mostraErrore(
                this.translateService.instant('errori.eliminazione_file'),
                messaggioErrore,
                error.error.errore
              );
            }
          );
        }
      }
    ).catch(() => { });
  }

  getClassForDocumentRow(document: DocumentoDTO): string {
    switch (document.stato?.codice) {
      case StatoDocumentoEnum.ELABORATO: return 'table-elaborato';
      case StatoDocumentoEnum.ACQUISITO: return 'table-secondary';
      case StatoDocumentoEnum.IN_ELABORAZIONE: return 'table-secondary';
      default: return 'table-danger';
    }
  }

  selectionChangeHandler(documentiSelezionati: DocumentoDTO[], idParent?: number) {
    if (!idParent) {
      // this.documentiSelezionati = documentiSelezionati; // TODO REPLACE
    }
  }

  disableFirmaButton() {
    if (!this.daFirmare || this.documentiSelezionati.length === 0) {
      return true;
    } else {
      const docDaFirmare = this.documentiDaFirmare();
      return docDaFirmare.length === 0;
    }
  }

  private areAllDocsOnIndex(docs: DocumentoDTO[]) {
    let res = true;
    for (const doc of docs) {
      if (doc.contenuti?.filter(d => d.tipo?.codice === TipoContenutoDocumentoEnum.ORIGINALE).length === 0) {
        res = false;
        break;
      }
    }
    return res;
  }

  disableFirmaTuttiButton() {
    const mainT: DocumentoDTO[] | null | undefined =  this.mainTable?.getDataSnapshot();
    let canSignAllDocs = true;

    if (mainT) {
      canSignAllDocs = this.areAllDocsOnIndex(mainT);
    }
    return !this.daFirmare
      || !canSignAllDocs
      || this.numeroDocumenti === 0
      || (this.maxNumeroDocumenti && this.numeroDocumenti > this.maxNumeroDocumenti);
  }

  firmaDocumenti() {
    const documentiDaFirmare = this.documentiDaFirmare();
    this.creazioneModale(documentiDaFirmare);
  }

  getTuttiDocumentiDaFirmare(documenti: DocumentoDTO[]){
    const docs: DocumentoDTO[] = [];
    documenti.forEach(
      documento => {
        docs.push(this.documentiService.mapDocumento(documento));
        documento.allegati?.forEach(
          allegato => docs.push(this.documentiService.mapDocumento(allegato))
        );
      }
    );
    return this.filtraDocumentiPerFirma(docs);
  }

  firmaTuttiDocumenti() {
    this.spinner.show();

    const filtro = this.creaFiltro(0, 1000, undefined, undefined, true);
    const filtroDaFirmare = this.creaFiltroDocDaFirmare(true);

    this.documentiService.getDocumentiDaFirmare(filtro, filtroDaFirmare, false)
    .pipe(
      finalize(() => this.spinner.hide()))
    .subscribe(
      response => {
        if (response?.documenti) {

          const documenti = this.getTuttiDocumentiDaFirmare(response.documenti);

          this.creazioneModale(documenti);

        } else {
          this.mostraErrore(
            this.translateService.instant('firma_digitale.firma_documenti'),
            this.translateService.instant('errori.impossibile_firmare_documenti')
          );
        }
      },
      errore => {
        this.mostraErrore(
          this.translateService.instant('firma_digitale.firma_documenti'),
          Utils.toMessage(errore, this.translateService.instant('errori.impossibile_firmare_documenti')),
          errore.error.errore
        );
      });
  }

  documentiDaFirmare(): DocumentoDTO[] {
    if (this.areAllDocsOnIndex(this.documentiSelezionati)){
      if (this.soloFirmabili && this.soloFirmabili === true) {
        return this.filtraDocumentiPerFirma(this.documentiSelezionati);
      } else {

        if (this.firmaFea){
          return this.documentiSelezionati.filter(documento => documento.tipo?.firmabile
            && documento.contenutoEffettivo?.formatoFile?.mimeType === 'application/pdf' &&
            (!documento.contenuti?.find(cont => cont.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FIRMA_DIGITALE
              || cont.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.SIGILLO_ELETTRONICO)));
        }else{
          return this.documentiSelezionati.filter(documento => documento.tipo?.firmabile &&
            ((!documento.contenutoFirmato && documento.contenutoOriginale && (documento.contenutoOriginale.infoVerificaFirme?.length === 0
              || documento.contenutoOriginale?.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.SIGILLO_ELETTRONICO
              || (documento.contenutoOriginale?.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FIRMA_DIGITALE  &&
                documento.contenutoOriginale.infoVerificaFirme?.find(i => i.codiceFiscaleFirmatario !== this.cfUser))))
              || (documento.contenutoFirmato &&
              documento.contenutoFirmato?.tipoContenutoFirmato?.codice !== TipoContenutoDocumentoFirmatoEnum.FIRMA_DIGITALE)
              || (documento.contenutoFirmato?.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FIRMA_DIGITALE &&
                documento.contenutoFirmato?.infoVerificaFirme?.find(i => i.codiceFiscaleFirmatario !== this.cfUser))));
        }

      }
    }


    return [];
  }

  profiliFeqAbilitati(docsDaFirmare: DocumentoDTO[]): string[] {
    const output: string[] = [];

    const profiliFeq: ProfiloFEQ[] = [];

    docsDaFirmare.forEach(docDaFirmare => {

      if (!docDaFirmare.contenutoFirmato  && docDaFirmare.contenutoOriginale) {
        docDaFirmare.contenutoOriginale.formatoFile?.formatoFileProfiloFeqTipoFirma?.forEach(profiloFeqTipoFirma => {
          if (!docDaFirmare.contenutoOriginale?.tipoFirma ||
            profiloFeqTipoFirma.tipoFirma.codice === docDaFirmare.contenutoOriginale?.tipoFirma?.codice) {
            profiliFeq.push(profiloFeqTipoFirma.profiloFeq);
          }
        });

      }else if (docDaFirmare.contenutoFirmato &&
        docDaFirmare.contenutoFirmato?.tipoContenutoFirmato?.codice !== TipoContenutoDocumentoFirmatoEnum.FIRMA_DIGITALE){
        docDaFirmare.contenutoFirmato.formatoFile?.formatoFileProfiloFeqTipoFirma?.forEach(profiloFeqTipoFirma => {
          profiliFeq.push(profiloFeqTipoFirma.profiloFeq);
        });
      } else if (docDaFirmare.contenutoFirmato?.infoVerificaFirme?.find(i => i.codiceFiscaleFirmatario !== this.cfUser)) {
        docDaFirmare.contenutoFirmato.formatoFile?.formatoFileProfiloFeqTipoFirma?.forEach(profiloFeqTipoFirma => {
          if (profiloFeqTipoFirma.tipoFirma.codice === docDaFirmare.contenutoFirmato?.tipoFirma?.codice) {
            profiliFeq.push(profiloFeqTipoFirma.profiloFeq);
          }
        });
      }
    });

    profiliFeq.reduce((prev, curr) => prev.set(curr.codice, (prev.get(curr.codice) || 0) + 1), new Map()).forEach((value, key) => {
      if (value === docsDaFirmare.length) {
        output.push(key);
      }
    });

    return output;

  }

  creazioneModale(documentiDaFirmare: DocumentoDTO[]) {
    const data = this.helperService.searchHelperRef(this.route);
    if (this.firmaFea === true){
      const modalRef = this.modal.open(FirmaFeaComponent, { size: 'xl', backdrop: 'static' });
      const documentiTemplate = [];
      for (const doc of documentiDaFirmare){
        documentiTemplate.push({documento: doc});
      }

      modalRef.componentInstance.documenti = documentiTemplate;
      modalRef.componentInstance.pratica = this.pratica;
      modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
      modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
      modalRef.componentInstance.codiceModale = 'firma-fea';
      modalRef.result.then(() => { },
      (reason) => {
        if ('finish_to_sign_docs' === reason) {
          this.mainTable?.uncheckAll();
          this.refresh();
        }
      });
    }else{
      const modalRef = this.modal.open(FirmaDigitaleComponent, { size: 'xl', backdrop: 'static' });
      modalRef.componentInstance.documenti = documentiDaFirmare;
      modalRef.componentInstance.profiliFeqAbilitati = this.profiliFeqAbilitati(documentiDaFirmare);
      modalRef.componentInstance.codiceTipoPratica = this.pratica.tipo?.codice;
      modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
      modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
      modalRef.componentInstance.codiceModale = 'firma-digitale';
      modalRef.result.then(() => { },
      (reason) => {
        if ('finish_to_sign_docs' === reason) {
          this.mainTable?.uncheckAll();
          this.refresh();
        }
      });
    }
  }

  aggiungiAllegato(parent: DocumentoDTO) {
    const modalRef = this.modal.open(AggiungiDocumentoComponent, {
      size: 'xl'
      /*, backdrop: 'static'*/
    });
    const data = this.helperService.searchHelperRef(this.route);
    modalRef.componentInstance.idPratica = parent.idPratica;
    modalRef.componentInstance.parent = parent;
    modalRef.componentInstance.tipologieDocumentoCaricabili = this.tipiDocCaricabili;
    modalRef.componentInstance.tipologieAllegatiCaricabili = this.tipiAllCaricabili;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'aggiungi-allegato';
  }

  checkAllChanged(newCheckState: boolean, parentId: number) {
    if (parentId) {
      return;
    }

    const subTables = this.subTables;
    if (subTables?.length) {
      subTables.forEach(subTable => {
        if (newCheckState) {
          subTable.checkAll();
        } else {
          subTable.uncheckAll();
        }
      });
    }
  }

  itemCheckChanged(payload: [DocumentoDTO, boolean], parentId: number) {
    if (parentId) {
      return;
    }
    const row = payload[0];
    const checkedStatus = payload[1];
    if (row?.id) {
      const isExpanded = this.mainTable?.isExpanded(row);
      if (checkedStatus && !isExpanded) {
        this.mainTable?.expand(row);
      } else {
        // const subtable = this.findSubtable(row.id);
        // if (subtable) {
        //  subtable.setAllChecked(checkedStatus);
        // }
      }
    }
  }

  private findSubtable(parentId: number): CosmoTableComponent | undefined {
    return this.tables?.find(e =>
      (e.tableContext?.parentId) === parentId
    );
  }

  private mostraErrore(titolo: string, testo: string, eccezione?: string) {
    this.modalService.error(
      titolo,
      testo,
      eccezione
    )
      .then(() => { })
      .catch(() => { });
  }

  clickOnAnteprima(documento: DocumentoDTO): void {
    const doc = documento.contenutoEffettivo;
    if (doc) {
      this.preview(documento, doc);
    }
  }

  private controlloTipiDocumento() {
    this.messaggiControlliObbligatori = [];

    const tipologieObbligatorie: VerificaTipologiaDocumentiSalvati[] = [];

    this.listaDocObbligatori.forEach(docObb => {

      if (docObb.codicePadre){
        tipologieObbligatorie.push({ codiceTipologiaDocumento: docObb.codice, codiceTipologiaDocumentoPadre: docObb.codicePadre });
      } else {
        tipologieObbligatorie.push({ codiceTipologiaDocumento: docObb.codice });
      }
  });

    if (tipologieObbligatorie.length === 0){
      this.messaggiObbligatori.emit(this.messaggiControlliObbligatori);
      return;
    }

    const request = JSON.stringify({
      idPratica: this.idPratica,
      tipologieDocumenti: tipologieObbligatorie,
      creationTime: this.infoTask?.createTime,
      verificaDataDocObbligatori: this.verificaDataDocObbligatori
    });

    this.tipiDocumentiService.getTipologieDocumentiSalvati(request).subscribe(response => {

      const tipiDocObbligatori: string[] = [];
      const tipiDocFacoltativi: string[] = [];

      this.listaDocObbligatori.forEach(docObblig => {
        const tipoDoc = response.find(res => res.codiceTipologiaDocumento === docObblig.codice);

        if (tipoDoc){
          if (!tipoDoc.presente){
            if (docObblig.obbligatorio === 'true') {
              tipiDocObbligatori.push( tipoDoc.descrizioneTipologiaDocumento ?? tipoDoc.codiceTipologiaDocumento);
            } else {
              tipiDocFacoltativi.push( tipoDoc.descrizioneTipologiaDocumento ?? tipoDoc.codiceTipologiaDocumento);
            }
          }
        } else{
          if (docObblig.obbligatorio === 'true') {
            tipiDocObbligatori.push(docObblig.descrizione ?? docObblig.codice);
          } else {
            tipiDocFacoltativi.push(docObblig.descrizione ?? docObblig.codice);
          }
        }
      });

      if (tipiDocObbligatori.length > 0) {
        tipiDocObbligatori.sort();
        // tslint:disable-next-line:max-line-length
        const message = Utils.parseAndReplacePlaceholders(this.translateService.instant('errori.tipi_doc_obbligatori'), [tipiDocObbligatori.join(', ')]);
        this.messaggiControlliObbligatori.push(
          { messaggio: message, tipoMessaggio: TipoMessaggio.ERROR, tipiDocumento: tipiDocObbligatori });
      }
      if (tipiDocFacoltativi.length > 0) {
        tipiDocFacoltativi.sort();
        // tslint:disable-next-line:max-line-length
        const messageF = Utils.parseAndReplacePlaceholders(this.translateService.instant('errori.tipi_doc_facoltativi'), [tipiDocFacoltativi.join(', ')]);
        this.messaggiControlliObbligatori.push(
          { messaggio: messageF, tipoMessaggio: TipoMessaggio.WARNING, tipiDocumento: tipiDocFacoltativi });
      }

      this.messaggiObbligatori.emit(this.messaggiControlliObbligatori);
    });

  }

  private getDocumentiFirmati(docsPerTipologia: Documento[]){
    return docsPerTipologia.filter(doc => {
      const contFirmati = this.firmaFea ? doc.contenuti?.filter(contenuto => contenuto.tipo?.codice === TipoContenutoDocumentoEnum.FIRMATO
        && contenuto.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FEA
        && this.firmatarioFea === contenuto.infoFirmaFea?.firmatario) :
        doc.contenuti?.filter(contenuto => contenuto.tipo?.codice === TipoContenutoDocumentoEnum.FIRMATO
          && contenuto.tipoContenutoFirmato?.codice !== TipoContenutoDocumentoFirmatoEnum.FEA
          && contenuto.infoVerificaFirme?.find(verifica => verifica.codiceFiscaleFirmatario === this.cfUser
            && verifica.dataApposizione && this.infoTask?.createTime
            && new Date(verifica.dataApposizione).getTime() >= new Date(this.infoTask.createTime).getTime()));
      return contFirmati && contFirmati?.length > 0;
    });
  }

  private controlloTipiDocumentoDaFirmare(docs: Documento[]) {
    const tipologieObbligatorie: VerificaTipologiaDocumentiSalvati[] = [];
    this.listaDocDaFirmareObbl.forEach(docObb => {
      if (docObb.codicePadre){
        tipologieObbligatorie.push({ codiceTipologiaDocumento: docObb.codice, codiceTipologiaDocumentoPadre: docObb.codicePadre });
      } else{

        tipologieObbligatorie.push({ codiceTipologiaDocumento: docObb.codice });
      }
    });

    if (tipologieObbligatorie.length === 0){
      this.messaggiObbligatori.emit(this.messaggiControlliObbligatori);
      return;
    }

    const daFirmareObbligatori: string[] = [];
    const daFirmareFacoltativi: string[] = [];
    this.listaDocDaFirmareObbl.forEach(docObblig => {
      const docsPerTipologia = docs.filter(doc => doc.tipo?.codice === docObblig.codice);
      const docsFirmati = this.getDocumentiFirmati(docsPerTipologia);

      if (docsPerTipologia.length > docsFirmati.length ){
        if (docObblig.obbligatorio === 'true') {
          daFirmareObbligatori.push( docObblig.descrizione ?? docObblig.codice);
        } else {
          daFirmareFacoltativi.push( docObblig.descrizione ?? docObblig.codice);
        }
      }
    });

    if (daFirmareObbligatori.length > 0) {
      daFirmareObbligatori.sort();
      // tslint:disable-next-line:max-line-length
      const message = Utils.parseAndReplacePlaceholders(this.translateService.instant('errori.tipi_doc_da_firmare_obbligatori'), [daFirmareObbligatori.join(', ')]);
      this.messaggiControlliObbligatori.push(
        { messaggio: message, tipoMessaggio: TipoMessaggio.ERROR, tipiDocumento: daFirmareObbligatori });
    }
    if (daFirmareFacoltativi.length > 0) {
      daFirmareFacoltativi.sort();
      // tslint:disable-next-line:max-line-length
      const messageF = Utils.parseAndReplacePlaceholders(this.translateService.instant('errori.tipi_doc_da_firmare_facoltativi'), [daFirmareFacoltativi.join(', ')]);
      this.messaggiControlliObbligatori.push(
        { messaggio: messageF, tipoMessaggio: TipoMessaggio.WARNING, tipiDocumento: daFirmareFacoltativi });
    }

    this.messaggiObbligatori.emit(this.messaggiControlliObbligatori);
  }

  private filtraDocumentiPerFirma(documenti: Array<Documento>) {
    const res: Array<Documento> = [];
    documenti = documenti.filter(x => this.listaDocDaFirmareObbl.find(o => o.codice === x.tipo?.codice));
    documenti?.forEach(doc => {
      const contenuto = doc.contenuti?.reduce((a, b) =>
        a && b && a.dtInserimento && b.dtInserimento &&
          (a.tipo?.codice === TipoContenutoDocumentoEnum.FIRMATO || a.tipo?.codice === TipoContenutoDocumentoEnum.ORIGINALE) &&
          a.dtInserimento > b.dtInserimento ? a : b);
      if ((contenuto && !this.firmaFea  && (!contenuto.infoVerificaFirme ||
        ((contenuto.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.SIGILLO_ELETTRONICO ||
          !contenuto.infoVerificaFirme.find(x => x.codiceFiscaleFirmatario === this.cfUser)) ||
          (!this.docsDaFirmare && contenuto.infoVerificaFirme.find(x => x.codiceFiscaleFirmatario === this.cfUser
            && x.dataApposizione && this.infoTask?.createTime &&
            new Date(x.dataApposizione).getTime() >= new Date(this.infoTask.createTime).getTime()))))) ||
            (contenuto && this.firmaFea && (contenuto && contenuto.formatoFile?.codice === 'application/pdf' )
            && !doc.contenuti?.find(cont => cont.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.FIRMA_DIGITALE
            || cont.tipoContenutoFirmato?.codice === TipoContenutoDocumentoFirmatoEnum.SIGILLO_ELETTRONICO))) {
        res.push(doc);
      }
    });

    return res;
  }

  canViewAddAttachment(doc: Documento) {
    return doc.tipo?.allegati && doc.tipo.allegati.length > 0;
  }

  isTipoDocEliminabile(tipoDoc: TipoDocumento) {
    return !tipoDoc
      || !(this.tipiDocNonEliminabili.find(tipoDocumento => tipoDocumento.codice === tipoDoc.codice) ||
           this.tipiAllNonEliminabili.find(tipiAllegato => tipiAllegato.codice === tipoDoc.codice &&
              tipiAllegato.principali && tipoDoc.principali &&
              tipiAllegato.principali.find(p => tipoDoc.principali?.find(pp => p.codice === pp.codice))));
  }

  modificaDocumento(documento: Documento, parentId?: number) {
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(AggiungiDocumentoComponent, { size: 'lg' /*, backdrop: 'static'*/ });
    modalRef.componentInstance.idPratica = this.idPratica;
    modalRef.componentInstance.documento = documento;
    modalRef.componentInstance.tipologieDocumentoCaricabili = this.tipiDocCaricabili;
    modalRef.componentInstance.tipologieAllegatiCaricabili = this.tipiAllCaricabili;
    if (parentId) {
      const docParent = this.mainTable?.getStatusSnapshot()?.dataSnapshot?.find(doc => doc.id === parentId);
      modalRef.componentInstance.parent = docParent;
    }
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'modifica-documento';

  }

  private apriVisualizzatore(documento: DocumentoDTO, contenuto: ContenutoDocumento) {
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(PreviewDocumentoModalComponent, { size: 'xl', backdrop: 'static' });
    modalRef.componentInstance.doc = documento;
    modalRef.componentInstance.contenuto = contenuto;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = this.codiceModalePreview;

  }

  /*
     Verifica che le firme apposte sui documenti presenti per la pratica in gestione,
     non siano scadute
  */
  private checkValiditaFirme(documenti: Array<DocumentoDTO>) {
    const message = this.translateService.instant('errori.validita_firme');
    for (const documento of documenti) {
      const test = documento?.contenutoEffettivo?.infoVerificaFirme?.find(verificaFirme =>
        verificaFirme.codiceErrore && verificaFirme.codiceErrore?.length > 0);
      if (test) {
        this.firmeUtenteValide.emit(false);
        this.messaggiControlliObbligatori.push({
          messaggio: message,
          tipoMessaggio: this.verificaValiditaFirme ? TipoMessaggio.ERROR : TipoMessaggio.WARNING });
        this.messaggiObbligatori.emit(this.messaggiControlliObbligatori);
        return;
      }
    }
    this.firmeUtenteValide.emit(true);
  }

  /*
    Verifica che tutti i documenti importati siano stati elaborati su index
  */
  private checkElaborazioneIndex(documenti: Array<DocumentoDTO>) {
    const message = this.translateService.instant('errori.attesa_elaborazione_documenti');
    for (const documento of documenti) {
      let test = documento.stato?.codice !== StatoDocumentoEnum.ELABORATO;
      if (!test && documento.allegati) {
        for (const attachment of documento.allegati) {
          if (attachment.stato?.codice !== StatoDocumentoEnum.ELABORATO) {
            test = true;
            break;
          }
        }
      }
      if (test) {
        this.attesaElaborazioneDocumenti.emit(true);
        this.messaggiControlliObbligatori.push({
          messaggio: message,
          tipoMessaggio: TipoMessaggio.ERROR});
        this.messaggiObbligatori.emit(this.messaggiControlliObbligatori);
        return;
      }
    }
    this.attesaElaborazioneDocumenti.emit(false);
  }
}
