/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  OnDestroy,
  OnInit,
  QueryList,
  ViewChild,
  ViewChildren,
} from '@angular/core';

import { SpinnerVisibilityService } from 'ng-http-loader';
import {
  CosmoTableColumnSize,
  CosmoTableComponent,
  CosmoTableFormatter,
  ICosmoTableColumn,
  ICosmoTablePageRequest,
  ICosmoTablePageResponse,
} from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import {
  forkJoin,
  from,
  Observable,
  of,
  throwError,
} from 'rxjs';
import {
  delay,
  finalize,
  map,
  mergeMap,
  tap,
} from 'rxjs/operators';
import { environment } from 'src/environments/environment';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { Pratica } from '../shared/models/api/cosmobusiness/pratica';
import {
  RiferimentoOperazioneAsincrona,
} from '../shared/models/api/cosmobusiness/riferimentoOperazioneAsincrona';
import {
  DocumentiSempliciActaResponse,
} from '../shared/models/api/cosmoecm/documentiSempliciActaResponse';
import {
  DocumentoFisicoActa,
} from '../shared/models/api/cosmoecm/documentoFisicoActa';
import {
  DocumentoSempliceActa,
} from '../shared/models/api/cosmoecm/documentoSempliceActa';
import { IdentitaUtente } from '../shared/models/api/cosmoecm/identitaUtente';
import {
  ImportaDocumentiActaDocumentoRequest,
} from '../shared/models/api/cosmoecm/importaDocumentiActaDocumentoRequest';
import {
  ImportaDocumentiActaRequest,
} from '../shared/models/api/cosmoecm/importaDocumentiActaRequest';
import { OperazioneAsincronaWrapper } from '../shared/models/async';
import { Decodifica } from '../shared/models/decodifica';
import {
  AsyncTaskModalService,
} from '../shared/services/async-task-modal.service';
import { AsyncTaskService } from '../shared/services/async-task.service';
import { ModalService } from '../shared/services/modal.service';
import {
  TipiDocumentiService,
} from '../shared/services/tipi-documenti/tipi-documenti.service';
import { Utils } from '../shared/utilities/utilities';
import {
  FiltriRicercaActaComponent,
  FiltroRicercaActaOutput,
} from './filtri-ricerca-acta.component';
import { RicercaActaService } from './ricerca-acta.service';
import { ModaleParentComponent } from '../modali/modale-parent-component';
import { HelperService } from '../shared/services/helper.service';

interface DocumentoSempliceActaWrapper extends DocumentoSempliceActa {
  selected: boolean;
  documentiFisici: DocumentoFisicoActaWrapper[];
  codiceTipoDocumento?: string;
}

interface DocumentoFisicoActaWrapper extends DocumentoFisicoActa {
  selected: boolean;
  codiceTipoDocumento?: string;
}

@Component({
  selector: 'app-ricerca-acta',
  templateUrl: './ricerca-acta.component.html',
  styleUrls: ['./ricerca-acta.component.scss']
})
export class RicercaActaComponent extends ModaleParentComponent implements OnInit, OnDestroy {
  COMPONENT_NAME = 'RicercaActaComponent';

  tipiDocumento: Decodifica[] = [];

  columns: ICosmoTableColumn[] = [
    /*
    {
      label: 'Numero', field: 'numero', canSort: true,
      canHide: false,
      size: CosmoTableColumnSize.XS,
      valueExtractor: (doc: DocumentoSempliceActa) => doc.classificazioni?.length ?
        doc.classificazioni.map(c => c.numero).join(', ') : null,
      formatters: [{ format: (raw: any) => raw ?? '--', }],
    },
    */
    {
      label: 'Numero repertorio', field: 'numRepertorio', canSort: true,
      canHide: false,
      size: CosmoTableColumnSize.MEDIUM,
      formatters: [{ format: (raw: any) => raw ?? '--', }],
    }, {
      label: 'Oggetto', field: 'oggetto', canSort: true,
      canHide: true,
      size: CosmoTableColumnSize.MEDIUM,
      formatters: [{ format: (raw: any) => raw ?? '--', }],
    }, {
      label: 'Indice di classificazione esteso', field: 'indiceClassificazioneEstesa', canSort: true,
      canHide: true,
      size: CosmoTableColumnSize.MEDIUM,
      formatters: [{ format: (raw: any) => raw ?? '--', }],
    }, {
      label: 'Data creazione', field: 'dataCreazione', canSort: true,
      canHide: true,
      size: CosmoTableColumnSize.SMALL,
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy'
      }, {
        format: (raw: any) => raw ?? '--',
      }],
    }, {
      label: 'Tipo documento', name: 'tipoDocumento',
      canHide: false, canFilter: false, canSort: false,
      size: CosmoTableColumnSize.MEDIUM,
      showLabelInTable: false,
      applyTemplate: true
    }
  ];

  columnsDocFisici: ICosmoTableColumn[] = [
    {
      label: 'Progressivo', field: 'progressivo', canSort: true,
      canHide: false,
      formatters: [{ format: (raw: any) => raw ?? '--', }],
    }, {
      label: 'Descrizione', field: 'descrizione', canSort: true,
      canHide: false,
      formatters: [{ format: (raw: any) => raw ?? '--', }],
    }, {
      label: 'Data memorizzazione', field: 'dataMemorizzazione', canSort: true,
      canHide: true,
      formatters: [{
        formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy'
      }, {
        format: (raw: any) => raw ?? '--',
      }],
    }, {
      label: 'Tipo documento', name: 'tipoDocumento',
      canHide: false, canFilter: false, canSort: false,
      showLabelInTable: false,
      applyTemplate: true
    },
  ];

  loading = 0;
  fetching = 0;
  loadingError: any | null = null;
  loaded = false;

  data: any = {};

  filtriAttuali?: FiltroRicercaActaOutput;

  @ViewChild('filtri') filtri: FiltriRicercaActaComponent | null = null;
  @ViewChild('table') table: CosmoTableComponent | null = null;
  @ViewChildren('tableDocFisici') tableDocFisici: QueryList<CosmoTableComponent> | null = null;

  private identitaSelezionata!: IdentitaUtente;
  private pratica!: Pratica;
  codicePagina!: string;
  codiceTab!: string;

  constructor(
    private logger: NGXLogger,
    private service: RicercaActaService,
    private modalService: ModalService,
    private asyncTaskService: AsyncTaskService,
    private asyncTaskModalService: AsyncTaskModalService,
    private spinner: SpinnerVisibilityService,
    public modal: NgbActiveModal,
    private tipiDocumentiService: TipiDocumentiService,
    public helperService: HelperService,
  ) {
    super(helperService);
    this.logger.debug('creating component ' + this.COMPONENT_NAME);
    this.setModalName('ricerca-acta');
  }

  configure(pratica: Pratica, identita: IdentitaUtente) {
    try {
      this.pratica = pratica;
      this.identitaSelezionata = identita;
      if (!pratica?.id) {
        throw new Error('ID pratica non fornito');
      }
      if (!pratica?.tipo?.codice) {
        throw new Error('Tipo pratica non fornito');
      }
      if (!identita) {
        throw new Error('Identita\' ACTA non fornita');
      }
      if (!identita?.identificativoAOO) {
        throw new Error('Identita\' ACTA non valida (formato errato)');
      }
    } catch (e) {
      setTimeout(() => {
        this.modalService.simpleError(Utils.toMessage(e));
        this.modal.dismiss('initialization_failed');
      }, 500);
    }
  }

  closeModal() {
    this.modal.dismiss('click_on_back_arrow');
  }

  get abilitaImporta(): boolean {
    const payloadSelezionati = this.getDocumentiSelezionati().documenti;
    if (!payloadSelezionati?.length) {
      return false;
    }
    return payloadSelezionati?.every(ds => ds.documentiFisici.every(df => !!df.codiceTipoDocumento?.length));
  }

  getTipoDocumentoSelezionato(row: DocumentoSempliceActaWrapper, subrow: DocumentoFisicoActaWrapper | null): Decodifica | null {
    const found = (subrow ?? row).codiceTipoDocumento;
    if (found) {
      return this.tipiDocumento.find(c => c.codice === found) ?? null;
    }
    return null;
  }

  setTipoDocumentoSelezionato(row: DocumentoSempliceActaWrapper, subrow: DocumentoFisicoActaWrapper | null, codice: string): void {
    (subrow ?? row).codiceTipoDocumento = codice;
  }

  isMultiSelect(row: DocumentoSempliceActa): boolean {
    return (row.documentiFisici?.length ?? 0) > 1;
  }

  rootTableCheckAll(checkAll: boolean): void {
    for (const subtable of this.tableDocFisici ?? []) {
      if (checkAll) {
        subtable.checkAll();
      } else {
        subtable.uncheckAll();
      }
    }
  }

  getDocumentiSelezionati(): ImportaDocumentiActaRequest {
    const out: ImportaDocumentiActaRequest = {
      idPratica: Utils.require(this.pratica?.id, 'idPratica'),
      documenti: []
    };

    let checkedRootItem: DocumentoSempliceActaWrapper;
    for (checkedRootItem of this.table?.getStatusSnapshot()?.checkedItems ?? []) {
      if (this.isMultiSelect(checkedRootItem) || !checkedRootItem.documentiFisici?.length) {
        continue;
      }
      const newEl: ImportaDocumentiActaDocumentoRequest = {
        id: Utils.require(checkedRootItem.id, 'id documento semplice'),
        documentiFisici: [{
          id: Utils.require(checkedRootItem.documentiFisici![0].id, 'id documento fisico'),
          codiceTipoDocumento: this.getTipoDocumentoSelezionato(checkedRootItem, null)?.codice as string,
        }]
      };
      out.documenti.push(newEl);
    }

    for (const subtable of this.tableDocFisici ?? []) {
      const ctx = subtable.tableContext as DocumentoSempliceActaWrapper;
      if (!this.isMultiSelect(ctx)) {
        continue;
      }
      const checkedSubitems = subtable.getStatusSnapshot()?.checkedItems ?? [] as DocumentoFisicoActa[];

      if (checkedSubitems.length) {
        const newEl: ImportaDocumentiActaDocumentoRequest = {
          id: Utils.require(ctx.id, 'id'),
          documentiFisici: []
        };

        for (const checkedSubItem of checkedSubitems) {
          newEl.documentiFisici.push({
            id: Utils.require(checkedSubItem.id, 'id documento fisico'),
            codiceTipoDocumento: this.getTipoDocumentoSelezionato(ctx, checkedSubItem)?.codice as string,
          });
        }

        out.documenti.push(newEl);
      }
    }

    return out;
  }

  expandableStatusProvider = (row: DocumentoSempliceActa): boolean => {
    return this.isMultiSelect(row);
  }

  selectableStatusProvider = (row: DocumentoSempliceActa): boolean => {
    return !this.isMultiSelect(row) && (row.documentiFisici?.length ?? 0) === 1;
  }

  forceRowExpansion = (row: DocumentoSempliceActa): boolean => {
    return this.isMultiSelect(row);
  }

  ngOnInit(): void {
    this.logger.debug('initializing component ' + this.COMPONENT_NAME);
    this.refresh();
    this.searchHelperModale(this.codicePagina, this.codiceTab);
  }

  updateFiltriRicerca(valore: FiltroRicercaActaOutput) {
    this.logger.info('filtri cambiati', valore);
    this.filtriAttuali = valore;
    this.table?.reset();
  }

  ngOnDestroy(): void {
    this.logger.debug('destroying component ' + this.COMPONENT_NAME);
  }

  refresh() {
    this.loading ++;
    this.loadingError = null;
    this.loaded = false;

    forkJoin({
      tipiDocumento: this.tipiDocumentiService.getTipiDocumentiAll(this.pratica.tipo!.codice)
    })
    .pipe(
      delay(environment.httpMockDelay),
      finalize(() => {
        this.loading--;
      })
    )
    .subscribe(response => {
      this.logger.debug('loaded data', response);

      this.tipiDocumento = response.tipiDocumento.map(raw => ({
        codice: raw.codice,
        valore: raw.descrizione ?? raw.codice
      }));

      this.loaded = true;
    }, failure => {
      this.loadingError = failure;
    });
  }

  search($event: any): void {
    if (this.fetching) {
      return;
    }
    this.table?.refresh();
  }

  private hashIdentitaSelezionata(): string {
    if (!this.identitaSelezionata) {
      throw new Error('nessuna identita\' selezionata');
    }
    return JSON.stringify({
      identificativoAOO: this.identitaSelezionata.identificativoAOO,
      identificativoNodo: this.identitaSelezionata.identificativoNodo,
      identificativoStruttura: this.identitaSelezionata.identificativoStruttura
    });
  }

  dataProvider = (input: ICosmoTablePageRequest) => {
    Utils.require(this.identitaSelezionata, 'identitaSelezionata');
    const filtri = this.filtriAttuali;
    if (!filtri) {
      return of({
        content: [],
        number: 0,
        numberOfElements: 0,
        size: 0,
        totalElements: 0,
        totalPages: 1
      });
    }

    return of(null).pipe(
      tap(() => {
        this.fetching++;
        this.spinner.show();
      }),
      mergeMap(() => this.service.eseguiRicerca(
        this.hashIdentitaSelezionata(),
        filtri,
        input.page ?? 0,
        input.size ?? 10,
        'documentiFisici'
      )),
      mergeMap(operazione => this.waitWithSpinner(operazione)),
      map((responseRaw: OperazioneAsincronaWrapper<string>) => {
        this.logger.debug('finished waiting for search', responseRaw);
        return JSON.parse(responseRaw.risultato!) as DocumentiSempliciActaResponse;
      }),
      map(response => {
        const documenti = (response.items ?? []).map(raw => {
          const mapped: DocumentoSempliceActaWrapper = {
            ...raw,
            selected: false,
            codiceTipoDocumento: undefined,
            documentiFisici: (raw.documentiFisici ?? []).map(raw2 => {
              const mapped2: DocumentoFisicoActaWrapper = {
                ...raw2,
                selected: false,
                codiceTipoDocumento: undefined,
              };
              return mapped2;
            })
          };
          return mapped;
        });
        this.data = documenti;

        const copy: ICosmoTablePageResponse = {
          content: documenti,
          number: response.pageInfo?.page ?? 0,
          numberOfElements: documenti?.length ?? 0,
          size: response.pageInfo?.pageSize ?? 0,
          totalElements: response.pageInfo?.totalElements ?? 0,
          totalPages: response.pageInfo?.totalPages
        };
        return copy;
      }), finalize(() => {
        this.fetching--;
        this.spinner.hide();
      })
    );
  }

  private waitWithSpinner(op: RiferimentoOperazioneAsincrona): Observable<any> {
    if (!op?.uuid) {
      return throwError('No operation UUID');
    }

    let interval: NodeJS.Timer | undefined;
    return of(op.uuid).pipe(
      mergeMap(uuid => {
        const watcher = this.asyncTaskService.watcher(uuid);
        watcher.start();

        // Questo serve perche' le request asincrone che sono in cascata
        // col pipe corrente forzano un clear dello stato di visibilita'
        // anche se e' stato chiamato show().
        interval = setInterval(() => this.spinner.show(), 100);

        return watcher.result;
      }),
      finalize(() => {
        if (interval) {
          clearInterval(interval);
        }
        this.spinner.hide();
      })
    );
  }

  importa(): void {
    Utils.require(this.identitaSelezionata, 'identitaSelezionata');

    const selezionati = this.getDocumentiSelezionati();
    if (!selezionati.documenti.length) {
      return;
    }

    this.spinner.show();
    this.service.importa(this.hashIdentitaSelezionata(), selezionati).pipe(
      tap(() => this.spinner.hide()),
      mergeMap(operazione =>
        from(this.asyncTaskModalService.open({
          taskUUID: operazione.uuid,
          showMessages: false,
        }).result)
     )
    ).subscribe(asyncResult => {
      (this.modalService.simpleInfo('Importazione dei documenti completata con successo.')
        .then(() => 1)
        .catch(() => 0)).then(() => {
          this.modal.close({
            import: true,
            asyncResult,
          });
        });

    }, failure => {
      this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
    });
  }
}
