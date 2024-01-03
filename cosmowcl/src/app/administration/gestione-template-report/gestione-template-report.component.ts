/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection, ICosmoTableColumn, ICosmoTablePageRequest,
  ICosmoTablePageResponse, ICosmoTableReloadContext,
  ICosmoTableStoreAdapter, ICosmoTableStoreAdapterSaveContext } from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { Observable, of } from 'rxjs';
import { delay, map, mergeMap } from 'rxjs/operators';
import { TemplateReport } from 'src/app/shared/models/api/cosmoecm/templateReport';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { TemplateService } from 'src/app/shared/services/template.service';
import { environment } from 'src/environments/environment';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-admin-gestione-template-report',
  templateUrl: './gestione-template-report.component.html',
  styleUrls: ['./gestione-template-report.component.scss']
})
export class GestioneTemplateReportComponent implements OnInit {
  COMPONENT_NAME = 'GestioneTemplateReportComponent';

  columns: ICosmoTableColumn[] = [{
      label: 'Ente', field: 'ente.nome', canSort: false, canHide: false,
    }, {
      label: 'Tipo pratica', field: 'tipoPratica.descrizione', canSort: false,
    }, {
      label: 'Codice', field: 'codice', canSort: false,
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
    private templateService: TemplateService,
    private modalService: ModalService,
    private translateService: TranslateService
  ) {
    this.logger.debug('creating component ' + this.COMPONENT_NAME);
  }

  ngOnInit() {
    this.logger.debug('initializing component ' + this.COMPONENT_NAME);
  }

  refresh(inBackground = false) {
    if (this.table) {
      this.table.refresh(inBackground);
    }
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const output: Observable<ICosmoTablePageResponse> = this.securityService.getCurrentUser().pipe(
      delay(environment.httpMockDelay),
      mergeMap(utente => {
        const payload = {
          page: input.page ?? 0,
          size: input.size ?? 10,
          fields: 'id,ente,tipoPratica,codice',
          sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
            '-' : '+' + input.sort[0]?.property) : undefined,
          ...this.getFilterPayload(utente, input),
        };
        this.logger.debug('loading page with search params', payload);
        return this.templateService.getTemplates(JSON.stringify(payload));
      }),
      map(response => {
        return {
          content: response.templateReport ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.templateReport?.length,
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
      output.filter.codice = {
        ci: input.query
      };
    }

    return output;
  }

  aggiungi() {
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  modifica(tr: TemplateReport) {
    this.router.navigate(['modifica', tr.id], { relativeTo: this.route });
  }

  elimina(tr: TemplateReport) {
    this.modalService.scegli(
      this.translateService.instant('template_report.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [tr.codice ?? '']),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.templateService.delete(Utils.require(tr.id, 'id')).subscribe(() => {
        this.refresh(false);
      }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
    }).catch(() => {});
  }

}
