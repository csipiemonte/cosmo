/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  OnInit,
  ViewChild,
} from '@angular/core';
import {
  ActivatedRoute,
  Router,
} from '@angular/router';

import {
  CosmoTableColumnSize,
  CosmoTableComponent,
  CosmoTableSortDirection,
  ICosmoTableColumn,
  ICosmoTablePageRequest,
  ICosmoTablePageResponse,
  ICosmoTableReloadContext,
  ICosmoTableStoreAdapter,
  ICosmoTableStoreAdapterSaveContext,
} from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import {
  Observable,
  of,
} from 'rxjs';
import {
  map,
  mergeMap,
} from 'rxjs/operators';
import { Gruppo } from 'src/app/shared/models/api/cosmoauthorization/gruppo';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { GruppiService } from 'src/app/shared/services/gruppi.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';

import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-gestione-gruppi',
  templateUrl: './gestione-gruppi.component.html',
  styleUrls: ['./gestione-gruppi.component.scss']
})
export class GestioneGruppiComponent implements OnInit {

  componentName = 'GestioneGruppiComponent';

  loading = 0;
  loadingError: any | null = null;

  columns: ICosmoTableColumn[] = [{
      label: 'Nome gruppo', field: 'nome', canSort: false, canHide: false,
    }, {
      label: 'Descrizione', field: 'descrizione', canSort: false,
    }, {
      label: 'Codice', field: 'codice', canSort: false,
    }, {
      name: 'numeroUtenti', label: 'Numero utenti', canSort: false, valueExtractor: (row: Gruppo) => row.utenti?.length ?? 0
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

  constructor(private route: ActivatedRoute,
              private logger: NGXLogger,
              private gruppiService: GruppiService,
              private securityService: SecurityService,
              private router: Router,
              private modalService: ModalService,
              private translateService: TranslateService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.refresh();
    });
  }

  refresh(inBackground = false) {
    if (this.table) {
      this.table.refresh(inBackground);
    }
  }

  aggiungi() {
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  modifica(gruppo: Gruppo) {
    this.router.navigate(['modifica', gruppo.id], { relativeTo: this.route });
  }

  elimina(gruppo: Gruppo) {
    this.modalService.scegli(
      this.translateService.instant('gruppi.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [gruppo.nome]),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.gruppiService.deleteGruppo(gruppo.id).subscribe(() => {
        this.refresh(false);
      }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
    }).catch(() => {});
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const output: Observable<ICosmoTablePageResponse> = this.securityService.getCurrentUser().pipe(
      mergeMap(utente => {
        const payload = {
          page: input.page ?? 0,
          size: input.size ?? 10,
          sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
            '-' : '+' + input.sort[0]?.property) : undefined,
          ...this.getFilterPayload(utente, input),
        };
        this.logger.debug('loading page with search params', payload);
        return this.gruppiService.getGruppi(JSON.stringify(payload));
      }),
      map(response => {
        return {
          content: response.gruppi ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.gruppi?.length,
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
      filter: {
        idEnte: {
          eq: Utils.require(utente?.ente?.id, 'idEnte')
        }
      },
    };

    if (input.query) {
      output.filter.fullText = {
        ci: input.query
      };
    }

    return output;
  }
}
