/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, Input, forwardRef } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Fruitore } from '../../models/api/cosmoauthorization/fruitore';
import { FruitoriService } from '../../services/fruitori.service';
import { RicercaEntityComponent, SelezioneEntity } from '../ricerca-entity/ricerca-entity.component';
import { Utils } from '../../utilities/utilities';

@Component({
  selector: 'app-ricerca-fruitore',
  templateUrl: '../ricerca-entity/ricerca-entity.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaFruitoreComponent),
    multi: true
  }]
})
export class RicercaFruitoreComponent extends RicercaEntityComponent<Fruitore> {

  @Input() filter?: (input: Fruitore) => boolean;
  @Input() promptNew?: (results: SelezioneEntity<Fruitore>[], term: string, newEntity: Fruitore) => boolean;
  @Input() fruitore?: Fruitore | undefined = undefined;

  constructor(
    private logger: NGXLogger,
    private fruitoriService: FruitoriService
  ) {
    super();
    this.logger.trace('building component');
  }

  getEntityDescriptionSingular(): string {
    return 'fruitore esterno';
  }

  format(input: SelezioneEntity<Fruitore>): string {
    return input?.entity?.nomeApp ?? '--';
  }

  getIcon(option: SelezioneEntity<Fruitore>): string | null {
    return 'fas fa-robot';
  }

  isEnriched(entry: SelezioneEntity<Fruitore>): boolean {
    return !!(entry?.entity?.nomeApp);
  }

  clean(entity: Fruitore): Fruitore {
    return {
      ...entity,
      nomeApp: Utils.require(entity.nomeApp),
      apiManagerId: Utils.require(entity.apiManagerId),
      id: Utils.require(entity.id),
      enti: undefined,
      autorizzazioni: undefined,
      endpoints: undefined,
      schemiAutenticazione: undefined,
    };
  }

  buildNewFromText(text: string): Fruitore {
    let codice = text.replace(/[^a-zA-Z0-9]+/gi, '-').toLowerCase();
    while (codice.startsWith('-')) {
      codice = codice.substr(1);
    }
    while (codice.endsWith('-')) {
      codice = codice.substr(0, codice.length - 1);
    }

    return {
      apiManagerId: codice,
      nomeApp: text,
    };
  }

  displayNewOption(results: SelezioneEntity<Fruitore>[], term: string, e: Fruitore): boolean {
    return false;
  }

  private getFilter(searchTerm?: string, value?: SelezioneEntity<Fruitore>) {
    const f: any = {
      filter: {},
      page: 0,
      size: 5,
      fields: 'id,nomeApp,apiManagerId',
      sort: '+nomeApp, +id'
    };
    if (searchTerm?.length) {
      f.filter.fullText = {
        ci: searchTerm
      };
    }
    if (value?.entity?.apiManagerId?.length) {
      f.filter.apiManagerId = {
        eq: value.entity.apiManagerId
      };
    }
    if (value?.entity?.id) {
      f.filter.id = {
        eq: value.entity.id
      };
    }
    return f;
  }

  search(term: string | null): Observable<SelezioneEntity<Fruitore>[]> {
    return this.fruitoriService.getFruitori(
      JSON.stringify(this.getFilter(term ?? undefined, this.fruitore ? {entity: this.fruitore} : undefined))).pipe(
      map(response => {
        return (response.fruitori ?? []).map(i => {
          return { entity: this.clean(i) };
        });
      })
    );
  }

  enrich(entry: SelezioneEntity<Fruitore>): Observable<SelezioneEntity<Fruitore> | null> {
    if (!entry?.entity?.id) {
      return of(null);
    }
    return this.fruitoriService.getFruitore(entry.entity.id).pipe(map(i => {
      return { entity: this.clean(i) };
    }));
  }

}
