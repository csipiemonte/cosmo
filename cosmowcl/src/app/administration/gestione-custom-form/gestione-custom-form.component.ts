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
  delay,
  map,
  mergeMap,
} from 'rxjs/operators';
import { CustomForm } from 'src/app/shared/models/api/cosmopratiche/customForm';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';

import { TranslateService } from '@ngx-translate/core';

import { environment } from '../../../environments/environment';
import { CustomFormService } from '../../shared/services/customForm.service';
import { SecurityService } from '../../shared/services/security.service';

@Component({
  selector: 'app-admin-gestione-custom-form',
  templateUrl: './gestione-custom-form.component.html',
  styleUrls: ['./gestione-custom-form.component.scss']
})
export class GestioneCustomFormComponent implements OnInit {
  COMPONENT_NAME = 'GestioneCustomFormComponent';
  loading = 0;
  loadingError: any | null = null;
  loaded = false;
  isProduction = environment.production;

  columns: ICosmoTableColumn[] = [{
    label: 'Codice', field: 'codice', canSort: false, canHide: false,
  }, {
    label: 'Descrizione', field: 'descrizione', canSort: false,
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

  customForms: CustomForm[] = [];

  constructor(
    private logger: NGXLogger,
    private securityService: SecurityService,
    private route: ActivatedRoute,
    private router: Router,
    private customFormService: CustomFormService,
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
          fields: 'codice,descrizione',
          sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
            '-' : '+' + input.sort[0]?.property) : undefined,
          ...this.getFilterPayload(input),
        };
        this.logger.debug('loading page with search params', payload);
        return this.customFormService.getCustomForms(JSON.stringify(payload));
      }),
      map(response => {
        return {
          content: response.customForms ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.customForms?.length,
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

  elimina(cf: CustomForm) {
    this.modalService.scegli(
      this.translateService.instant('custom_form.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [cf.codice ?? '']),
        classe: 'text-danger'
      }], [
      { testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt' },
      { testo: 'Annulla', dismiss: true, defaultFocus: true }
    ]
    ).then(proceed => {
      this.customFormService.delete(Utils.require(cf.codice, 'codice')).subscribe(() => {
        this.refresh(false);
      }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
    }).catch(() => {});
  }

  modifica(cf: CustomForm) {
    this.router.navigate(['modifica', cf.codice], { relativeTo: this.route });
  }

  importa(cf: CustomForm) {
    this.modalService.scegli(
      'Aggiornamento definizione form',
      [{
        testo: 'Sei sicuro di voler sovrascrivere la definizione di questo custom form con la versione presente sul modeler esterno?',
      }], [
      { testo: 'Importa da dev', valore: 'http://dev-cosmobe.csi.it/formio', classe: 'btn-outline-warning' },
      { testo: 'Importa da xp', valore: 'http://xp-cosmobe.csi.it/formio', classe: 'btn-outline-warning' },
      { testo: 'Annulla', dismiss: true, defaultFocus: true }
    ]
    ).then(proceed => {
      this.customFormService.importa(cf.codice, proceed).subscribe(() => {
        this.modalService.simpleInfo('Definizione aggiornata correttamente.');
      }, failure => {
        this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore);
      });
    }).catch(() => {});
  }
}
