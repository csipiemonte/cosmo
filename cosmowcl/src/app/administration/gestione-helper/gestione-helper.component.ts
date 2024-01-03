/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize,
         CosmoTableComponent,
         CosmoTableSortDirection,
         ICosmoTableColumn,
         ICosmoTablePageRequest,
         ICosmoTablePageResponse,
         ICosmoTableReloadContext,
         ICosmoTableStoreAdapter,
         ICosmoTableStoreAdapterSaveContext
} from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { UploadFileModalComponent } from 'src/app/shared/components/modals/upload-file-modal/upload-file-modal.component';
import { Helper } from 'src/app/shared/models/api/cosmonotifications/helper';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-admin-gestione-helper',
  templateUrl: './gestione-helper.component.html',
  styleUrls: ['./gestione-helper.component.scss']
})
export class GestioneHelperComponent implements OnInit {

  COMPONENT_NAME = 'GestioneHelperComponent';
  loading = 0;
  loadingError: any;
  loaded = false;
  isProduction = environment.production;
  legamiHelper: Map<string, string> = new Map<string, string>([
    ['Tab', 'codiceTab'],
    ['Form', 'codiceForm'],
    ['Modale', 'codiceModale']
  ]);

  columns: ICosmoTableColumn[] = [
    {
      label: 'Descrizione',  field: 'codicePagina.descrizione', canSort: false, canHide: false,
    },
    {
      name: 'Tab', label: 'Tab', applyTemplate: true, canSort: false, canHide: false, size: CosmoTableColumnSize.XXS
    },
    {
      name: 'Form', label: 'Form',  applyTemplate: true, canSort: false, canHide: false, size: CosmoTableColumnSize.XXS
    },
    {
      name: 'Modale', label: 'Modale',  applyTemplate: true, canSort: false, canHide: false, size: CosmoTableColumnSize.XXS
    },
     {
      name: 'azioni', label: 'Azioni',
      canHide: false,
      canSort: false,
      applyTemplate: true,
      size: CosmoTableColumnSize.XXS
    },
  ];

  // store adapter - persistenza dello stato della tabella nella url query
  storeAdapter: ICosmoTableStoreAdapter = {
    save: (payload: ICosmoTableStoreAdapterSaveContext | null) => {
      this.router.navigate(
        [],
        {
          relativeTo: this.route,
          queryParams: {
            page: payload?.status?.currentPage,
            size: payload?.status?.pageSize,
            q: payload?.status?.query,
          },
          queryParamsHandling: 'merge',
        });
      return of(true);
    },
    load: () => {
      return this.route.queryParams.pipe(
        map(params => {
          const out = {
            currentPage: params.page ? parseInt(params.page, 10) : undefined,
            pageSize: params.size ? parseInt(params.size, 10) : undefined,
            query: params.q,
          } as any;
          return out;
        })
      );
    }
  };

  @ViewChild('table') table: CosmoTableComponent | null = null;

  constructor(
    private logger: NGXLogger,
    private route: ActivatedRoute,
    private router: Router,
    private helperService: HelperService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private modal: NgbModal
  ) {
    this.logger.debug('creating component ' + this.COMPONENT_NAME);
}


  ngOnInit(): void {
    this.logger.debug('initializing component ' + this.COMPONENT_NAME);
  }

  refresh(inBackground = false) {
    if (this.table) {
      this.table.refresh(inBackground);
    }
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const payload = {
      page: input.page ?? 0,
      size: input.size ?? 10,
      fields: 'id,codicePagina,codiceTab,codiceForm,codiceModale',
      sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
        '-' : '+' + input.sort[0]?.property) : undefined,
      ...this.getFilterPayload(input),
    };
    this.logger.debug('loading page with search params', payload);

    const output: Observable<ICosmoTablePageResponse> = this.helperService.getHelpers(JSON.stringify(payload)).pipe(
      // finalize(() => this.loading --),
      map(response => {
        return {
          content: response.helpers ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.helpers?.length,
          size: response.pageInfo?.pageSize,
          totalElements: response.pageInfo?.totalElements,
          totalPages: response.pageInfo?.totalPages,
        };
      })
    );

    return output;
  }

  getFilterPayload(input: ICosmoTablePageRequest): any {
    const output: any = {
      filter: {},
    };

    if (input.query) {
      output.filter.codicePagina = {
        ci: input.query
      };
    }

    return output;
  }

  aggiungiViaInterfaccia() {
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  aggiungiViaImport() {
    const ngbModalOptions: NgbModalOptions = {
      keyboard: true,
      size: 'xl',
    };
    const modalRef = this.modal.open(UploadFileModalComponent, ngbModalOptions);

    modalRef.componentInstance.title = 'CARICAMENTO FILE PER IMPORT HELPER';
    modalRef.componentInstance.mimetypeList = ['application/json'];

    modalRef.result.then(() => {
        this.refresh();
    });
  }

  modifica(h: Helper) {
    this.router.navigate(['modifica', h.id], { relativeTo: this.route });
  }

  elimina(h: Helper) {
    this.modalService.scegli(
      this.translateService.instant('helper.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [h.codicePagina?.descrizione ?? '']),
        classe: 'text-danger'
      }], [
      { testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt' },
      { testo: 'Annulla', dismiss: true, defaultFocus: true }
    ]
    ).then(() => {
      this.helperService.delete(Utils.require(h.id, 'codice')).subscribe(() => {
        this.refresh(false);
      }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
    }).catch(() => { });
  }

  esporta(helper: Helper) {
    this.helperService.esporta(helper.id ?? 0).subscribe(result => {
      const element = document.createElement('a');
      const formatFilename = !helper.codiceForm ? !helper.codiceTab ? helper.codicePagina?.codice
                                : helper.codiceTab?.codice : helper.codiceForm?.codice;
      element.setAttribute('href', 'data:application/octet-stream;charset=utf-8;base64,' + result.encoded);
      element.setAttribute('download', formatFilename + '.json');

      element.style.display = 'none';
      document.body.appendChild(element);

      element.click();

      document.body.removeChild(element);

    }, failure => {
      this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
    });
  }

  getRowTitle(row: any) {
    return row.descrizione;
  }


}
