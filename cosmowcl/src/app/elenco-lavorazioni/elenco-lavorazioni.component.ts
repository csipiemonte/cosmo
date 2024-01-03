/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Constants } from '../shared/constants/constants';
import { IRicercaPraticheStoreAdapter, IRicercaPraticheStoreAdapterLoadedSnapshot, IRicercaPraticheStoreAdapterSaveContext } from '../shared/components/ricerca-pratiche/ricerca-pratiche.component';

@Component({
  selector: 'app-elenco-lavorazioni',
  templateUrl: './elenco-lavorazioni.component.html',
  styleUrls: ['./elenco-lavorazioni.component.scss']
})
export class ElencoLavorazioniComponent {

  // store adapter - persistenza dello stato della tabella nella url query
  storeAdapter: IRicercaPraticheStoreAdapter = {
    save: (payload: IRicercaPraticheStoreAdapterSaveContext | null) => {
      this.router.navigate(
        [],
        {
          relativeTo: this.activatedRoute,
          queryParams: {
            order: payload?.status?.orderColumn,
            direction: payload?.status?.orderColumnDirection,
            page: payload?.status?.currentPage,
            expand: payload?.status?.expandedItemIdentifiers?.find(o => true) ?? null,
            checked: payload?.status?.checkedItemIdentifiers?.find(o => true) ?? null,
            clp: payload?.filterCollapsed ? '1' : '0',
            filter: payload?.serializedFilters,
            size: payload?.status?.pageSize,
          },
          queryParamsHandling: 'merge',
        });
      return of(true);
    },
    load: () => {
      return this.activatedRoute.queryParams.pipe(
        map(params => {
          const o: IRicercaPraticheStoreAdapterLoadedSnapshot = { // NOSONAR
            schemaVersion: Constants.STORAGE_SCHEMA_VERSION,
            orderColumn: params?.order,
            orderColumnDirection: params?.direction,
            currentPage: params?.page ? parseInt(params?.page, 10) : undefined,
            expandedItemIdentifiers: params?.expand ? [parseInt(params.expand, 10)] : undefined,
            filterCollapsed: params?.clp ? params.clp === '1' : undefined,
            referer: params?.ref,
            serializedFilters: params?.filter,
            pageSize: params?.size ? parseInt(params?.size, 10) : undefined,
            checkedItemIdentifiers: params?.checked ? [parseInt(params.checked, 10)] : undefined,
          };
          return o;
        })
      );
    }
  };

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
  ) {}

}
