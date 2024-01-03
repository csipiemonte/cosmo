/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, forwardRef, Input } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { RicercaEntityComponent, SelezioneEntity } from '../ricerca-entity/ricerca-entity.component';
import { PraticheService } from '../../services/pratiche.service';
import { StatoPratica } from '../../models/api/cosmopratiche/statoPratica';
import { Utils } from '../../utilities/utilities';
import { GestioneTipiPraticheService } from 'src/app/administration/gestione-tipi-pratiche/gestione-tipi-pratiche.service';


@Component({
  selector: 'app-ricerca-stato-pratica',
  templateUrl: '../ricerca-entity/ricerca-entity.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaStatoPraticaComponent),
    multi: true
  }]
})
export class RicercaStatoPraticaComponent extends RicercaEntityComponent<StatoPratica> {

  @Input() filter?: (input: StatoPratica) => boolean;
  @Input() statiPratica: StatoPratica [] = [];


  constructor(
    private logger: NGXLogger,
    private service: PraticheService,
    protected tipiPraticheService: GestioneTipiPraticheService,
  ) {
    super();
    this.logger.trace('building component');
  }

  getEntityDescriptionSingular(): string {
    return 'stato';
  }

  format(input: SelezioneEntity<StatoPratica>): string {
    return input?.entity?.descrizione ?? '--';
  }

  getIcon(option: SelezioneEntity<StatoPratica>): string | null {
    return 'fas fa-toggle-on';
  }

  isEnriched(entry: SelezioneEntity<StatoPratica>): boolean {
    return !!(entry?.entity?.descrizione);
  }

  clean(entity: StatoPratica): StatoPratica {
    return {
      ...entity,
      codice: Utils.require(entity.codice)
    };
  }

  buildNewFromText(text: string): StatoPratica {
    let codice = text.replace(/[^a-zA-Z0-9_]+/gi, '-');
    while (codice.startsWith('-')) {
      codice = codice.substr(1);
    }
    while (codice.endsWith('-')) {
      codice = codice.substr(0, codice.length - 1);
    }

    return {
      codice,
      descrizione: text,
    };
  }

  search(term: string | null): Observable<SelezioneEntity<StatoPratica>[]> {
    return this.service.listStatiPratica().pipe(
      map(response => {
        this.statiPratica.forEach(statoPratica => {
          if (!response.some(t => statoPratica.codice === t.codice)){
         response.push(statoPratica);
         }
         });
        return (response ?? [])
        .filter(i => !(term?.trim()?.length) || (i.codice + ' ' + i.descrizione).toUpperCase().includes(term.trim().toUpperCase()))
        .filter(i => this.filter ? this.filter(i) : true)
        .sort((e1, e2) => (e1.descrizione ?? '')?.localeCompare((e2.descrizione ?? '')))
        .slice(0, 5)
        .map(i => {
          return { entity: this.clean(i) };
        });
      })
    );
  }

  displayNewOption(results: SelezioneEntity<StatoPratica>[], term: string): boolean {
      return !!(term?.trim()?.length) && !results.some(c => c.entity?.codice === term )
        && !this.statiPratica.some(i => i.codice === term);

  }

  enrich(entry: SelezioneEntity<StatoPratica>): Observable<SelezioneEntity<StatoPratica> | null> {
    if (!entry?.entity?.codice) {
      return of(null);
    }
    return this.service.listStatiPratica().pipe(map(response => {
      return { entity: response.find(c => c.codice === entry.entity?.codice) };
    }));
  }
}
