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
  ICosmoTableReloadContext,
  ICosmoTableStoreAdapter,
  ICosmoTableStoreAdapterSaveContext,
} from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import {
  of,
} from 'rxjs';
import {
  map,
} from 'rxjs/operators';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';

import { TranslateService } from '@ngx-translate/core';

import { environment } from '../../../environments/environment';
import { TagsService } from '../../shared/services/tags.service';
import { Tag } from 'src/app/shared/models/api/cosmoauthorization/tag';

@Component({
  selector: 'app-gestione-tags',
  templateUrl: './gestione-tags.component.html',
  styleUrls: ['./gestione-tags.component.scss']
})
export class GestioneTagsComponent implements OnInit {
  COMPONENT_NAME = 'GestioneTagsComponent';
  loading = 0;
  loadingError: any | null = null;
  loaded = false;
  isProduction = environment.production;

  columns: ICosmoTableColumn[] = [{
    name: 'tipotag', label: 'Tipo tag', canSort: false, canHide: false, valueExtractor: (row: Tag) => row.tipoTag?.descrizione ?? '--'
  }, {
    name: 'valore', label: 'Valore', field: 'descrizione', canSort: false,
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

  tag: Tag[] = [];

  constructor(
    private logger: NGXLogger,
    private route: ActivatedRoute,
    private router: Router,
    private tagsService: TagsService,
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
    const payload = {
      page: input.page ?? 0,
      size: input.size ?? 10,
      // fields: 'codice,descrizione,id',
      sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
        '-' : '+' + input.sort[0]?.property) : undefined,
      ...this.getFilterPayload(input),
    };
    this.logger.debug('loading page with search params', payload);
    return this.tagsService.getTags(JSON.stringify(payload)).pipe(
      map(response => {
        return {
          content: response.elementi ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.elementi?.length,
          size: response.pageInfo?.pageSize,
          totalElements: response.pageInfo?.totalElements,
          totalPages: response.pageInfo?.totalPages,
        };
      })
    );
  }

  getFilterPayload(input: ICosmoTablePageRequest): any {
    const output: any = {
      filter: {},
    };

    if (input.query) {
      output.filter.descrizione = {
        ci: input.query
      };
    }

    return output;
  }

  elimina(t: Tag) {
    this.modalService.scegli(
      this.translateService.instant('form_tag.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [t.codice ?? '']),
        classe: 'text-danger'
      }], [
      { testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt' },
      { testo: 'Annulla', dismiss: true, defaultFocus: true }
    ]
    ).then(() => {
      if (!t?.id) {
        return;
      }
      this.tagsService.deleteTag(Utils.require(t.id, 'id')).subscribe(() => {
        this.refresh(false);
      }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
    }).catch(() => { });
  }

  aggiungi() {
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  modifica(t: Tag) {
    this.router.navigate(['modifica', t.id], { relativeTo: this.route });
  }

}
