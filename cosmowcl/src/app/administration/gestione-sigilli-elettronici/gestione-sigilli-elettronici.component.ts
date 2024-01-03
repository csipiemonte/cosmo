/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { delay, map, mergeMap } from 'rxjs/operators';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection,
  ICosmoTableColumn, ICosmoTablePageRequest, ICosmoTablePageResponse, ICosmoTableReloadContext,
  ICosmoTableStoreAdapter, ICosmoTableStoreAdapterSaveContext } from 'ngx-cosmo-table';
import { Observable, of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { Utils } from 'src/app/shared/utilities/utilities';
import { SigilloElettronicoService } from 'src/app/shared/services/sigillo-elettronico.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { NGXLogger } from 'ngx-logger';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { environment } from 'src/environments/environment';
import { CredenzialiSigilloElettronico } from 'src/app/shared/models/api/cosmoecm/credenzialiSigilloElettronico';

@Component({
  selector: 'app-gestione-sigilli-elettronici',
  templateUrl: './gestione-sigilli-elettronici.component.html',
  styleUrls: ['./gestione-sigilli-elettronici.component.scss']
})
export class GestioneSigilliElettroniciComponent implements OnInit, OnDestroy {

  COMPONENT_NAME = 'GestioneSigilliElettroniciComponent';

  columns: ICosmoTableColumn[] = [{
    label: 'Alias', field: 'alias', canSort: false, canHide: false,
  }, {
    name: 'DelegatedUser', label: 'Utente delegato', field: 'delegatedUser', canSort: false, canFilter: true, defaultFilter: true
  }, {
    name: 'DelegatedDomain', label: 'Dominio delegato', field: 'delegatedDomain', canSort: false
  }, {
    name: 'Utente', label: 'Utente', field: 'utente', canSort: false
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
  private sigilloElettronicoService: SigilloElettronicoService,
  private securityService: SecurityService,
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

modifica(e: CredenzialiSigilloElettronico) {
  this.router.navigate(['modifica', e.id], { relativeTo: this.route });
}

elimina(e: CredenzialiSigilloElettronico) {
  this.modalService.scegli(
    this.translateService.instant('sigillo_elettronico.dialogs.elimina.titolo'),
    [{
      testo: Utils.parseAndReplacePlaceholders(
        this.translateService.instant('common.eliminazione_messaggio'), [e.alias ?? 'questo sigillo elettronico']),
      classe: 'text-danger'
    }], [
      {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
      {testo: 'Annulla', dismiss: true, defaultFocus: true}
    ]
  ).then(() => {
    this.sigilloElettronicoService.deleteSigilloElettronico(Utils.require(e.id)).subscribe(() => {
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
      ...this.getFilterPayload(input),
    };
    this.logger.debug('loading page with search params sigilli elettronici', payload);
    return this.sigilloElettronicoService.getSigilloElettronico(JSON.stringify(payload));
}),
    map(response => {
    return {
        content: response.sigilliElettronici ?? [],
        number: response.pageInfo?.page,
        numberOfElements: response.sigilliElettronici?.length,
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
    filter: {}
  };

  if (input.query) {
    output.filter.fulltext = {
      ci: input.query
    };
  }
  return output;
}


goBack(){
  this.router.navigate(['/amministrazione']);
}

}

