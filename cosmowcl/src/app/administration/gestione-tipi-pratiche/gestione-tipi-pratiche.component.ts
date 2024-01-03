/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection, ICosmoTableColumn,
  ICosmoTablePageRequest, ICosmoTablePageResponse, ICosmoTableReloadContext,
  ICosmoTableStoreAdapter, ICosmoTableStoreAdapterSaveContext } from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { Observable, of } from 'rxjs';
import { delay, map, mergeMap } from 'rxjs/operators';
import { PulsanteModale } from 'src/app/shared/components/modals/due-opzioni/due-opzioni.component';
import { TipoPratica } from 'src/app/shared/models/api/cosmobusiness/tipoPratica';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';
import { Constants } from '../../shared/constants/constants';
import { GestioneTipiPraticheService } from './gestione-tipi-pratiche.service';


@Component({
  selector: 'app-admin-gestione-tipi-pratiche',
  templateUrl: './gestione-tipi-pratiche.component.html',
  styleUrls: ['./gestione-tipi-pratiche.component.scss']
})
export class GestioneTipiPraticheComponent implements OnInit, OnDestroy {
  COMPONENT_NAME = 'GestioneTipiPraticheComponent';

  columns: ICosmoTableColumn[] = [{
      label: 'Codice', field: 'codice', canSort: false, canHide: false,
    }, {
      name: 'descrizione', label: 'Descrizione', field: 'descrizione', canSort: false, canFilter: true, defaultFilter: true
    }, {
      name: 'ente', label: 'Ente', field: 'ente', canSort: false,
      valueExtractor: row => row.ente.nome ?? '--'
    }, {
      name: 'azioni', label: 'Azioni',
      canHide: false,
      canSort: false,
      applyTemplate: true,
      size: CosmoTableColumnSize.SMALL
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
            expand: payload?.status?.expandedItemIdentifiers?.find(o => true) ?? null,
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
           expandedItemIdentifiers: params.expand ? [parseInt(params.expand, 10)] : null
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
    private securityService: SecurityService,
    private tipiPraticheService: GestioneTipiPraticheService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private modal: NgbModal,
  ) {
    this.logger.debug('creating component ' + this.COMPONENT_NAME);
  }

  ngOnInit(): void {
    this.logger.debug('initializing component ' + this.COMPONENT_NAME);
    this.refresh();
  }

  ngOnDestroy(): void {
    this.logger.debug('destroying component ' + this.COMPONENT_NAME);
  }



  refresh(inBackground = false) {
    if (this.table) {
      this.table.refresh(inBackground);
    }
  }

  aggiungi() {
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  modifica(e: TipoPratica) {
    this.router.navigate(['modifica', e.codice], { relativeTo: this.route });
  }

  elimina(e: TipoPratica) {
    this.modalService.scegli(
      this.translateService.instant('tipi_pratiche.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [e.descrizione ?? 'questa tipologia di pratica']),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.tipiPraticheService.delete(Utils.require(e.codice, 'codice')).subscribe(() => {
        this.refresh(false);
      }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
    }).catch(() => {});
  }


  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const output: Observable<ICosmoTablePageResponse> = this.securityService.getCurrentUser().pipe(
    delay(environment.httpMockDelay),
    mergeMap(utente => {
      const payload = {
        page: input.page ?? 0,
        size: input.size ?? 10,
        sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
          '-' : '+' + input.sort[0]?.property) : undefined,
        ...this.getFilterPayload(input, utente),
      };
      this.logger.debug('loading page with search params tipi pratiche', payload);
      return this.tipiPraticheService.getTipiPratica(JSON.stringify(payload));
  }),
      map(response => {
      return {
          content: response.tipiPratiche ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.tipiPratiche?.length,
          size: response.pageInfo?.pageSize,
          totalElements: response.pageInfo?.totalElements,
          totalPages: response.pageInfo?.totalPages,
        };
      })
    );

    return output;
  }

  getFilterPayload(input: ICosmoTablePageRequest, utente: UserInfoWrapper): any {
    const output: any = {
      filter: {}
    };
    if (utente.ente?.id && utente.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
      output.filter.idEnte = {
        eq: utente.ente?.id
      };
    }
    if (input.query) {
      output.filter.fullText = {
        ci: input.query
      };
    }
    return output;
  }



  esporta(e: TipoPratica) {
    this.tipiPraticheService.getOpzioniTenantPerEsportazione(e.codice).subscribe(opzioni => {

      // check opzioni
      if (opzioni.length < 1) {
        this.modalService.simpleError('nessun deploy di processo per questa pratica associata ad un particolare tenant.');

      } else if (opzioni.length === 1) {
        this.esportaDavvero(e, opzioni[0].codiceIpa);

      } else {
        // costruisci modale di scelta
        const buttons: PulsanteModale[] = opzioni.map(ente => ({
          testo: ente.nome,
          valore: ente.codiceIpa
        }));
        this.modalService.scegli(
          'Scegli quale processo esportare',
          'Ci sono più deploy di questo processo appartenenti a tenant diversi. Quale si desidera esportare?',
          buttons
        ).then(scelta => {
          if (scelta?.length) {
            this.esportaDavvero(e, scelta);
          }
        }).catch(() => {});
      }

    }, failure => {
      this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
    });
  }

  esportaDavvero(e: TipoPratica, tenantId: string) {
    this.tipiPraticheService.esporta(e.codice, tenantId).subscribe(result => {

      const element = document.createElement('a');
      element.setAttribute('href', 'data:application/octet-stream;charset=utf-8;base64,' + result.encoded);
      element.setAttribute('download', e.codice + '.cosmo');

      element.style.display = 'none';
      document.body.appendChild(element);

      element.click();

      document.body.removeChild(element);

    }, failure => {
      this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
    });
  }

  goBack(){
    this.securityService.getCurrentUser().subscribe(utente => {
      if (utente && utente.profilo && utente.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
        this.router.navigate(['/configurazione']);
      }else{
        this.router.navigate(['/amministrazione']);
      }
    });
  }
}
