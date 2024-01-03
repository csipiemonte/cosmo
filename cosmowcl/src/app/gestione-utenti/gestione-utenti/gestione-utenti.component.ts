/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UtentiService } from 'src/app/shared/services/utenti.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { Utils } from 'src/app/shared/utilities/utilities';
import { NGXLogger } from 'ngx-logger';
import { map, mergeMap } from 'rxjs/operators';
import { Utente } from 'src/app/shared/models/api/cosmoauthorization/utente';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection,
  ICosmoTableColumn, ICosmoTablePageRequest, ICosmoTablePageResponse,
  ICosmoTableReloadContext, ICosmoTableStoreAdapter,
  ICosmoTableStoreAdapterSaveContext } from 'ngx-cosmo-table';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Observable, of, Subscription } from 'rxjs';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';

@Component({
  selector: 'app-gestione-utenti',
  templateUrl: './gestione-utenti.component.html',
  styleUrls: ['./gestione-utenti.component.scss']
})
export class GestioneUtentiComponent implements OnInit, OnDestroy {

  componentName = 'GestioneUtentiComponent';

  loading = 0;
  loadingError: any = null;

  cercaUtente!: string;

  columns: ICosmoTableColumn[] = [{
      label: 'Codice fiscale', field: 'codiceFiscale', canSort: false, canHide: false,
    }, {
      label: 'Nome', field: 'nome', canSort: false,
    }, {
      label: 'Cognome', field: 'cognome', canSort: false,
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
  principal?: UserInfoWrapper;
  principalSubscription?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private logger: NGXLogger,
    private securityService: SecurityService,
    private utentiService: UtentiService,
    private router: Router,
    private modalService: ModalService,
    private translateService: TranslateService
  ) {}


  ngOnDestroy(): void {
    if (this.principalSubscription) {
      this.principalSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.principalSubscription = this.securityService.principal$.subscribe(principal => {
      this.principal = principal;
    });
    this.route.params.subscribe(() => {
      this.refresh();
    });
  }

  refresh(inBackground = false) {
    if (this.table) {
      this.table.refresh(inBackground);
    }
  }

  itsAMe(user: Utente) {
    return !!user && user.codiceFiscale === this.principal?.codiceFiscale;
  }

  aggiungi() {
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  elimina(utente: Utente) {
    if (!utente?.id) {
      return;
    }
    if (this.itsAMe(utente)) {
      return;
    }

    this.modalService.scegli(
      this.translateService.instant('common.eliminazione_utente_titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(this.translateService.instant('common.eliminazione_messaggio'),
          [utente.nome + ' ' + utente.cognome]),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      if (!utente?.id) {
        return;
      }
      this.utentiService.deleteUtente(utente.id).subscribe(() => {
        this.refresh(false);
      }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
    }).catch(() => {});
  }

  modifica(utente: Utente) {
    if (!utente?.id) {
      return;
    }
    this.router.navigate(['modifica', utente.id], { relativeTo: this.route });
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const output: Observable<ICosmoTablePageResponse> = this.securityService.getCurrentUser().pipe(
      mergeMap(utente => {
        const payload: any = {
          page: input.page ?? 0,
          size: input.size ?? 10,
          sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
            '-' : '+' + input.sort[0]?.property) : undefined,
          filter: {
            idEnte: {
              eq: utente.ente?.id
            },
          }
        };
        if (Utils.isNotBlank(input.query)) {
          payload.filter.fullText = { ci: input.query?.trim() };
        }
        return this.utentiService.getUtenti(JSON.stringify(payload));
      }),
      map(response => {
        return {
          content: response.utenti ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.utenti?.length,
          size: response.pageInfo?.pageSize,
          totalElements: response.pageInfo?.totalElements,
          totalPages: response.pageInfo?.totalPages,
        };
      })
    );

    return output;
  }

}
