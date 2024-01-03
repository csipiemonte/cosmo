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
import { Constants } from 'src/app/shared/constants/constants';
import { FormLogico } from 'src/app/shared/models/api/cosmobusiness/formLogico';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';
import { GestioneFormLogiciService } from '../../shared/services/gestione-form-logici.service';


@Component({
  selector: 'app-admin-gestione-form-logico',
  templateUrl: './gestione-form-logici.component.html',
  styleUrls: ['./gestione-form-logici.component.scss']
})
export class GestioneFormLogiciComponent implements OnInit, OnDestroy {
  COMPONENT_NAME = 'GestioneFormLogiciComponent';

  isConfiguratore = false;

  columns: ICosmoTableColumn[] = [{
      label: 'Codice', field: 'codice', canSort: false, canHide: false,
    }, {
      label: 'Descrizione', field: 'descrizione', canSort: false,
    },
    {
      label: 'Ente', field: 'riferimentoEnte.nome', canSort: false,
    },
    {
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
    private formLogiciService: GestioneFormLogiciService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private modal: NgbModal,
  ) {
    this.logger.debug('creating component ' + this.COMPONENT_NAME);
  }

  ngOnInit(): void {
    this.logger.debug('initializing component ' + this.COMPONENT_NAME);

    this.securityService.getCurrentUser().subscribe(utente => {
      if (utente && utente.profilo && utente.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
        this.isConfiguratore = true;
      }
    });

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

  modifica(e: FormLogico) {
    this.router.navigate(['modifica', e.id], { relativeTo: this.route });
  }

  elimina(e: FormLogico) {
    this.modalService.scegli(
      this.translateService.instant('form_logici.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [e.descrizione ?? 'questo form logico']),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.formLogiciService.deleteFormLogico(Utils.require(e.id, 'id')).subscribe(() => {
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
          fields: 'id, codice, descrizione, riferimentoEnte',
          ...this.getFilterPayload(utente, input),
        };
        this.logger.debug('loading page with search params', payload);
        return this.formLogiciService.getFormLogici(JSON.stringify(payload));
      }),
      map(response => {
        return {
          content: response.formLogici ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.formLogici?.length,
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

    if (this.isConfiguratore){
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

  goBack(){
    if (this.isConfiguratore){
      this.router.navigate(['/configurazione']);
    }else{
      this.router.navigate(['/amministrazione']);
    }
  }

}
