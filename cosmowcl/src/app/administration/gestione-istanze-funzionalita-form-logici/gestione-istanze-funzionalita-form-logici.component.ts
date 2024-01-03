/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection,
   ICosmoTableColumn, ICosmoTablePageRequest, ICosmoTablePageResponse, ICosmoTableStoreAdapter, ICosmoTableStoreAdapterSaveContext } from 'ngx-cosmo-table';
import {  of } from 'rxjs';
import { map, } from 'rxjs/operators';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { GestioneIstanzeFunzionalitaFormLogiciService } from './gestione-istanze-funzionalita-form-logici.service';
import { FunzionalitaFormLogico } from 'src/app/shared/models/api/cosmopratiche/funzionalitaFormLogico';
import { Utils } from 'src/app/shared/utilities/utilities';
import { IstanzaFunzionalitaFormLogico } from 'src/app/shared/models/api/cosmobusiness/istanzaFunzionalitaFormLogico';

@Component({
  selector: 'app-gestione-istanze-funzionalita-form-logici',
  templateUrl: './gestione-istanze-funzionalita-form-logici.component.html',
  styleUrls: ['./gestione-istanze-funzionalita-form-logici.component.scss']
})
export class GestioneIstanzeFunzionalitaFormLogiciComponent implements OnInit {

  columns: ICosmoTableColumn[] = [{
    label: 'Codice', field: 'codice', canSort: false, canHide: false,
  }, {
    label: 'Descrizione', field: 'descrizione', canSort: false,
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
  private route: ActivatedRoute,
  private router: Router,
  private securityService: SecurityService,
  private istanzeFormLogiciService: GestioneIstanzeFunzionalitaFormLogiciService,
  private modalService: ModalService,
  private translateService: TranslateService,
  private modal: NgbModal,
) {
}

ngOnInit(): void {
  this.refresh();
}

refresh(inBackground = false) {
  if (this.table) {
    this.table.refresh(inBackground);
  }
}

aggiungi() {
  this.router.navigate(['nuovo'], { relativeTo: this.route });
}

modifica(funzionalita: IstanzaFunzionalitaFormLogico) {
  this.router.navigate(['modifica', funzionalita.id], { relativeTo: this.route });
}

elimina(funzionalita: IstanzaFunzionalitaFormLogico) {
  this.modalService.scegli(
    this.translateService.instant('enti.dialogs.elimina.titolo'),
    [{
      testo: Utils.parseAndReplacePlaceholders(
        this.translateService.instant('common.eliminazione_messaggio'), [funzionalita.codice ?? '--']),
      classe: 'text-danger'
    }], [
      {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
      {testo: 'Annulla', dismiss: true, defaultFocus: true}
    ]
  ).then(() => {
    this.istanzeFormLogiciService.deleteIstanza(Utils.require(funzionalita.id, 'id')).subscribe(() => {
      this.refresh(false);
    }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
  }).catch(() => {});
}

dataProvider = (input: ICosmoTablePageRequest) => {

  const payload = {
    page: input.page ?? 0,
    size: input.size ?? 10,
    sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
      '-' : '+' + input.sort[0]?.property) : undefined,
    fields: 'id, codice, descrizione',
    ...this.getFilterPayload(input),
  };

  return this.istanzeFormLogiciService.getIstanze(JSON.stringify(payload)).pipe(
    map(response => {
      const copy: ICosmoTablePageResponse = {
        content: response.istanze ?? [],
        number: response.pageInfo?.page,
        numberOfElements: response.istanze?.length,
        size: response.pageInfo?.pageSize,
        totalElements: response.pageInfo?.totalElements,
        totalPages: response.pageInfo?.totalPages,
      };
      return copy;
    })
  );
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

}
