/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { ICosmoTableColumn, CosmoTableColumnSize, ICosmoTableStoreAdapter, ICosmoTableStoreAdapterSaveContext,
   CosmoTableComponent, ICosmoTablePageRequest, ICosmoTableReloadContext, ICosmoTablePageResponse,
    CosmoTableSortDirection } from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { of, Observable } from 'rxjs';
import { delay, map, mergeMap } from 'rxjs/operators';
import { Constants } from 'src/app/shared/constants/constants';
import { TipoPratica } from 'src/app/shared/models/api/cosmobusiness/tipoPratica';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { environment } from 'src/environments/environment';
import { GestioneTipiPraticheService } from '../gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { ConfigurazioniMessaggiNotificheService } from 'src/app/shared/services/configurazioni-messaggi-notifiche.service';
import { ConfigurazioneMessaggioNotifica } from 'src/app/shared/models/api/cosmonotifications/configurazioneMessaggioNotifica';

@Component({
  selector: 'app-configurazioni-messaggi-notifiche',
  templateUrl: './configurazioni-messaggi-notifiche.component.html',
  styleUrls: ['./configurazioni-messaggi-notifiche.component.scss']
})
export class ConfigurazioniMessaggiNotificheComponent implements OnInit, OnDestroy {

  COMPONENT_NAME = 'ConfigurazioniMessaggiNotificheComponent';

  columns: ICosmoTableColumn[] = [{
      label: 'Tipo messaggio', field: 'tipo messaggio', canSort: false, canHide: false,
      valueExtractor: row => row.tipoMessaggio.descrizione ?? ''
    }, {
      name: 'ente', label: 'Ente', field: 'ente', canSort: false,
      valueExtractor: row => row.ente.nome ?? ''
    }, {
      name: 'tipoPratica', label: 'Tipo pratica', field: 'tipoPratica', canSort: false,
      valueExtractor: row => row.tipoPratica?.descrizione ?? ''
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
    private configurazioniMessaggiNotificheService: ConfigurazioniMessaggiNotificheService,
    private modalService: ModalService,
    private translateService: TranslateService
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

  modifica(e: ConfigurazioneMessaggioNotifica) {
    this.router.navigate(['modifica', e.id], { relativeTo: this.route });
  }

  elimina(e: ConfigurazioneMessaggioNotifica) {
    this.modalService.scegli(
      this.translateService.instant('configurazioni_messaggi_notifiche.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), ['questa configurazione']),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.configurazioniMessaggiNotificheService.deleteConfigurazioneMessaggioNotifica(Utils.require(e.id, 'id')).subscribe(() => {
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
      this.logger.debug('loading page with search params configurazioni messaggi notifiche', payload);
      return this.configurazioniMessaggiNotificheService.getConfigurazioniMessaggiNotifiche(JSON.stringify(payload));
  }),
      map(response => {
      return {
          content: response.configurazioniMessaggi ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.configurazioniMessaggi?.length,
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

    if (input.query) {
      output.filter.fullText = {
        ci: input.query
      };
    }
    return output;
  }





  goBack(){
    this.securityService.getCurrentUser().subscribe(utente => {
      this.router.navigate(['/amministrazione']);
    });
  }

}
