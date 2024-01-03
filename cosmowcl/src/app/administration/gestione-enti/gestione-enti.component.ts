/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection, ICosmoTableColumn,
  ICosmoTablePageRequest, ICosmoTablePageResponse, ICosmoTableReloadContext, ICosmoTableStoreAdapter,
  ICosmoTableStoreAdapterSaveContext } from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, Observable, of } from 'rxjs';
import { delay, finalize, map, mergeMap } from 'rxjs/operators';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { EntiService } from 'src/app/shared/services/enti.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';


@Component({
  selector: 'app-admin-gestione-enti',
  templateUrl: './gestione-enti.component.html',
  styleUrls: ['./gestione-enti.component.scss']
})
export class GestioneEntiComponent implements OnInit, OnDestroy {
  COMPONENT_NAME = 'GestioneEntiComponent';

  columns: ICosmoTableColumn[] = [{
      label: 'Nome', field: 'nome', canSort: false, canHide: false,
    }, {
      label: 'Codice fiscale', field: 'codiceFiscale', canSort: false,
    }, {
      label: 'Codice IPA', field: 'codiceIpa', canSort: false,
    }, {
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
    private securityService: SecurityService,
    private entiService: EntiService,
    private modalService: ModalService,
    private translateService: TranslateService,
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

  modifica(e: Ente) {
    this.router.navigate(['modifica', e.id], { relativeTo: this.route });
  }

  elimina(e: Ente) {
    this.modalService.scegli(
      this.translateService.instant('enti.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [e.nome]),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.entiService.deleteEnte(Utils.require(e.id, 'id')).subscribe(() => {
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
          fields: 'id,nome,codiceIpa,codiceFiscale',
          sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
            '-' : '+' + input.sort[0]?.property) : undefined,
          ...this.getFilterPayload(utente, input),
        };
        this.logger.debug('loading page with search params', payload);
        return this.entiService.getEnti(JSON.stringify(payload));
      }),
      map(response => {
        return {
          content: response.enti ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.enti?.length,
          size: response.pageInfo?.pageSize,
          totalElements: response.pageInfo?.totalElements,
          totalPages: response.pageInfo?.totalPages,
        };
      })
    );

    return output;
  }

  getFilterPayload(utente: UserInfoWrapper, input: ICosmoTablePageRequest): any {
    const output: any = {
      filter: {},
    };

    if (input.query) {
      output.filter.fullText = {
        ci: input.query
      };
    }

    return output;
  }
}
