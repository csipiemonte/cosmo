/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import { Component, ElementRef, forwardRef, Input } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { merge, Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, map, mergeMap } from 'rxjs/operators';
import { RicercaEntityComponent, SelezioneEntity } from '../ricerca-entity/ricerca-entity.component';
import { TipoPratica } from '../../models/api/cosmoecm/tipoPratica';
import { GestioneTipiPraticheService } from 'src/app/administration/gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { Ente } from '../../models/api/cosmoauthorization/ente';


@Component({
  selector: 'app-ricerca-tipo-pratica',
  templateUrl: '../ricerca-entity/ricerca-entity.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaTipoPraticaComponent),
    multi: true
  }]
})

export class RicercaTipoPraticaComponent extends RicercaEntityComponent<TipoPratica>  {

  @Input() promptNew?: (results: SelezioneEntity<TipoPratica>[], term: string, newEntity: TipoPratica) => boolean;
  @Input() ente: Ente | null = null;
  @Input() tipoPratiche: TipoPratica[] = [];

  constructor(
    private logger: NGXLogger,
    private gestioneTipiPraticheService: GestioneTipiPraticheService,
    ) {
      super();
      this.logger.trace('building component');
     }

     getEntityDescriptionSingular(): string {
      return 'tipo pratica';
    }

    format(input: SelezioneEntity<TipoPratica>): string {
      return input?.entity?.descrizione ?? '--';
    }

    getIcon(option: SelezioneEntity<TipoPratica>): string | null {
      return 'fas fa-scroll';
    }

    isEnriched(entry: SelezioneEntity<TipoPratica>): boolean {
      return !!(entry?.entity?.codice);
    }

    clean(entity: TipoPratica): TipoPratica {
      return {
        ...entity,
      };
    }


    search(term: string | null): Observable<SelezioneEntity<TipoPratica>[]> {
      return this.gestioneTipiPraticheService.listTipiPraticaByEnte(this.ente?.id).pipe(
        map(response => {
          this.tipoPratiche.forEach(tipoPratica => {
           if (!response.some(t => tipoPratica.codice === t.codice)){
          response.push(tipoPratica);
          }
          });
          return (response ?? [])
          .filter(i => !(term?.trim()?.length) || (i.codice + ' ' + i.descrizione).toUpperCase().includes(term.trim().toUpperCase()))
          .sort((e1, e2) => (e1.descrizione ?? '')?.localeCompare((e2.descrizione ?? '')))
          .slice(0, 5)
          .map(i => {
            return { entity: this.clean(i) };
          });
        })
      );
    }

    displayNewOption(results: SelezioneEntity<TipoPratica>[], term: string, e: TipoPratica): boolean {
      return !!(term?.trim()?.length) && !results.some(c => c.entity?.codice === term)
        && (!this.promptNew || this.promptNew(results, term, e));
    }

  enrich(entry: SelezioneEntity<TipoPratica>): Observable<SelezioneEntity<TipoPratica> | null> {
    if (!entry?.entity?.codice) {
      return of(null);
    }
    return this.gestioneTipiPraticheService.listTipiPraticaByEnte(this.ente?.id).pipe(map(response => {
      return { entity: response.find(c => c.codice === entry.entity?.codice) };
    }));
  }

}
