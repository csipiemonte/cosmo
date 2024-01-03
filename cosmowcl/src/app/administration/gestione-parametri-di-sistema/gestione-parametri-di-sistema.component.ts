/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import {
  CosmoTableColumnSize, CosmoTableComponent,
  CosmoTableSortDirection, ICosmoTableColumn, ICosmoTablePageRequest,
  ICosmoTablePageResponse, ICosmoTableReloadContext, ICosmoTableStoreAdapter,
  ICosmoTableStoreAdapterSaveContext
} from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { Observable, of } from 'rxjs';
import { GestioneParametriDiSistemaService } from 'src/app/shared/services/gestione-parametri-di-sistema.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { environment } from '../../../environments/environment';
import { delay, map, mergeMap } from 'rxjs/operators';
import { SecurityService } from 'src/app/shared/services/security.service';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { ParametroDiSistema } from 'src/app/shared/models/api/cosmoauthorization/parametroDiSistema';
import { Utils } from 'src/app/shared/utilities/utilities';
import { Constants } from 'src/app/shared/constants/constants';

@Component({
  selector: 'app-gestione-parametri-di-sistema',
  templateUrl: './gestione-parametri-di-sistema.component.html',
  styleUrls: ['./gestione-parametri-di-sistema.component.scss']
})
export class GestioneParametriDiSistemaComponent implements OnInit {
  COMPONENT_NAME = 'GestioneParametriDiSistemaComponent';

  columns: ICosmoTableColumn[] = [{
    name: 'chiave', label: 'Chiave', field: 'chiave', canSort: false, canHide: false,
  }, {
    name: 'valore', label: 'Valore', field: 'valore', canSort: false,
  }, {
    name: 'descrizione', label: 'Descrizione', field: 'descrizione', canSort: false,
  }, {
    name: 'azioni', label: 'Azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.XS
  }
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
          return {
            currentPage: params.page ? parseInt(params.page, 10) : undefined,
            pageSize: params.size ? parseInt(params.size, 10) : undefined,
            query: params.q,
          } as any;
        })
      );
    }
  };

  @ViewChild('table') table: CosmoTableComponent | null = null;

  parametroDiSistema: ParametroDiSistema[] = [];


  constructor(
    private logger: NGXLogger,
    private route: ActivatedRoute,
    private router: Router,
    private securityService: SecurityService,
    private gestioneParametriDiSistema: GestioneParametriDiSistemaService,
    private modalService: ModalService,
    private translateService: TranslateService) {
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
          sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
            '-' : '+' + input.sort[0]?.property) : undefined,
          ...this.getFilterPayload(utente, input),
        };
        this.logger.debug('loading page with search params', payload);
        return this.gestioneParametriDiSistema.getParametriDiSistema(JSON.stringify(payload));
      }),
      map(response => {
        return {
          content: response.parametriDiSistema ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.parametriDiSistema?.length,
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

  elimina(p: ParametroDiSistema) {
    this.modalService.scegli(
      this.translateService.instant('gestione_parametri_di_sistema.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [p.chiave ?? '']),
        classe: 'text-danger'
      }], [
      { testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt' },
      { testo: 'Annulla', dismiss: true, defaultFocus: true }
    ]
    ).then(() => {
      if (!p?.chiave) {
        return;
      }


      const linkAssistenza = Constants.EMAIL_ASSISTENZA;
      let messaggio = this.translateService.instant('gestione_parametri_di_sistema.messaggio_riavvio');
      messaggio = Utils.parseAndReplacePlaceholders(messaggio, [linkAssistenza, linkAssistenza]);


      this.modalService.htmlModal(this.translateService.instant('gestione_parametri_di_sistema.form_gestione_parametri_di_sistema'),
        messaggio).then(
          () => {
            this.gestioneParametriDiSistema.deleteParametroDiSistema(Utils.require(p.chiave, 'chiave')).subscribe(() => {
              this.refresh(false);
            }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
          }
        ).catch(() => { });

    }).catch(() => { });
  }

  aggiungi() {
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  modifica(p: ParametroDiSistema) {
    this.router.navigate(['modifica', p.chiave], { relativeTo: this.route });
  }
}
