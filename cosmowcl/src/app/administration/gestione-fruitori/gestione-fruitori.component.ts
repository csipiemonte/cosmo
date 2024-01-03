/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  OnDestroy,
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
  delay,
  map,
  mergeMap,
} from 'rxjs/operators';
import {
  EndpointFruitore,
} from 'src/app/shared/models/api/cosmoauthorization/endpointFruitore';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import {
  Fruitore,
} from 'src/app/shared/models/api/cosmoauthorization/fruitore';
import {
  SchemaAutenticazioneFruitore,
} from 'src/app/shared/models/api/cosmoauthorization/schemaAutenticazioneFruitore';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { FruitoriService } from 'src/app/shared/services/fruitori.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';

import {
  ModificaEndpointFruitoreComponent,
} from './modifica-endpoint/modifica-endpoint-fruitore.component';
import {
  ModificaSchemiAutorizzazioneFruitoreComponent,
} from './modifica-schemi-auth/modifica-schemi-auth-fruitore.component';

@Component({
  selector: 'app-admin-gestione-fruitori',
  templateUrl: './gestione-fruitori.component.html',
  styleUrls: ['./gestione-fruitori.component.scss']
})
export class GestioneFruitoriComponent implements OnInit, OnDestroy {
  COMPONENT_NAME = 'GestioneFruitoriComponent';

  columns: ICosmoTableColumn[] = [{
      label: 'Nome', field: 'nomeApp', canSort: false, canHide: false,
    }, {
      label: 'Api Manager ID', field: 'apiManagerId', canSort: false,
    }, {
      label: 'URL', field: 'url', canSort: false,
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
    private fruitoriService: FruitoriService,
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

  modifica(e: Fruitore) {
    this.router.navigate(['modifica', e.id], { relativeTo: this.route });
  }

  aggiungiSchemaAuth(e: Fruitore) {
    const comp = this.modal.open(ModificaSchemiAutorizzazioneFruitoreComponent, {
      backdrop: true,
      keyboard: true,
      size: 'lg'
    });
    const compInstance = comp.componentInstance as ModificaSchemiAutorizzazioneFruitoreComponent;
    compInstance.idFruitore = e.id;
    comp.result.then(() => {
      this.refresh();
    }, () => {});
  }

  modificaSchemaAuth(e: Fruitore, o: SchemaAutenticazioneFruitore) {
    const comp = this.modal.open(ModificaSchemiAutorizzazioneFruitoreComponent, {
      backdrop: true,
      keyboard: true,
      size: 'lg'
    });
    const compInstance = comp.componentInstance as ModificaSchemiAutorizzazioneFruitoreComponent;
    compInstance.existing = o;
    compInstance.idFruitore = e.id;
    comp.result.then(() => {
      this.refresh();
    }, () => {});
  }

  eliminaSchemaAuth(e: Fruitore, o: SchemaAutenticazioneFruitore) {
    this.modalService.scegli(
      this.translateService.instant('common.eliminazione_utente_titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(this.translateService.instant('common.eliminazione_messaggio'),
          ['questo schema di autenticazione']),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.fruitoriService.eliminaSchemaAutenticazioneFruitore(Utils.require(e.id), o.id).subscribe(() => {
        this.refresh();
      }, failure => {
        this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
      });
    }).catch(() => {});
  }

  testSchemaAuth(e: Fruitore, o: SchemaAutenticazioneFruitore) {
    this.fruitoriService.testSchemaAutenticazioneFruitore(o.id).subscribe(result => {
      this.modalService.showRawData('Risultato', result);
    }, failure => {
      this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
    });
  }

  isTestable(o: SchemaAutenticazioneFruitore): boolean {
    return o?.tipo?.codice === 'TOKEN';
  }

  aggiungiEndpoint(e: Fruitore) {
    const comp = this.modal.open(ModificaEndpointFruitoreComponent, {
      backdrop: true,
      keyboard: true,
      size: 'lg'
    });
    const compInstance = comp.componentInstance as ModificaEndpointFruitoreComponent;
    compInstance.idFruitore = e.id;
    comp.result.then(() => {
      this.refresh();
    }, () => {});
  }

  modificaEndpoint(e: Fruitore, o: EndpointFruitore) {
    const comp = this.modal.open(ModificaEndpointFruitoreComponent, {
      backdrop: true,
      keyboard: true,
      size: 'lg'
    });
    const compInstance = comp.componentInstance as ModificaEndpointFruitoreComponent;
    compInstance.idFruitore = e.id;
    compInstance.existing = o;
    comp.result.then(() => {
      this.refresh();
    }, () => {});
  }

  eliminaEndpoint(e: Fruitore, o: any) {
    this.modalService.scegli(
      this.translateService.instant('common.eliminazione_utente_titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(this.translateService.instant('common.eliminazione_messaggio'),
          ['questo endpoint']),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.fruitoriService.eliminaEndpointFruitore(Utils.require(e.id), o.id).subscribe(() => {
        this.refresh();
      }, failure => {
        this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
      });
    }).catch(() => {});
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
      this.fruitoriService.deleteFruitore(Utils.require(e.id, 'id')).subscribe(() => {
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
          ...this.getFilterPayload(utente, input),
        };
        this.logger.debug('loading page with search params', payload);
        return this.fruitoriService.getFruitori(JSON.stringify(payload));
      }),
      map(response => {
        return {
          content: response.fruitori ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.fruitori?.length,
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

  schemaAuthInUso(fruitore: Fruitore, schema: SchemaAutenticazioneFruitore): boolean {
    if (!fruitore?.endpoints?.length) {
      return false;
    }
    return !!fruitore.endpoints.find(e => e.schemaAutenticazione?.id === schema.id);
  }
}
