/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Observable } from "rxjs";
import { PageInfo } from "../models/api/cosmobusiness/pageInfo";

export class PaginationUtils {

  public static buildPaginazione(page: number, totalPages: number): any[] | null{

    if (totalPages > 1) {
      const paginazione = [];
      const minPage = page - 3;
      const maxPage = page + 3;

      // paginazione.push({
      //   text: 'paginazione.prima_pagina',
      //   number: 0,
      //   enabled: page > 0
      // });

      paginazione.push({
        text: '',
        // text: 'paginazione.pagina_precedente',
        number: page > 0 ? page - 1 : 0,
        enabled: page > 0,
        left: true,
        right: false
      });

      if (minPage > 0) {
        paginazione.push({
          text: 'paginazione.pagina_x',
          textArgs: '...',
          number: null,
          enabled: false,
          left: false,
          right: false
        });
      }

      for (let i = 0; i < totalPages; i++) {
        if (i >= minPage && i <= maxPage) {
          paginazione.push({
            text: 'paginazione.pagina_x',
            textArgs: i + 1,
            number: i,
            current: i === page,
            enabled: i !== page,
            left: false,
            right: false
          });
        }
      }

      if (maxPage < totalPages - 1) {
        paginazione.push({
          text: 'paginazione.pagina_x',
          textArgs: '...',
          number: null,
          enabled: false,
          left: false,
          right: false
        });
      }

      paginazione.push({
        // text: 'paginazione.pagina_successiva',
        text: '',
        number: page < (totalPages - 1) ? page + 1 : page,
        enabled: page < (totalPages - 1),
        left: false,
        right: true
      });
      // paginazione.push({
      //   text: 'paginazione.ultima_pagina',
      //   number: totalPages - 1,
      //   enabled: page < (totalPages - 1)
      // });

      return paginazione;

    } else {
      return null;
    }
  }

  fetchAllPages<T>(fetcher: (page: number, size: number) => Observable<{content: T[], pageInfo: PageInfo}> ): Observable<T[]> {
    const pageSize = 100;
    return new Observable(sub => {
      const out: T[] = [];
      let page = 0;

      fetcher(page, pageSize).subscribe(
        page => {
          // TODO

        }, fail => {
          sub.error(fail);
          sub.complete();
        }
      );

      sub.next(out);
      sub.complete();
    });
    
  }
}
