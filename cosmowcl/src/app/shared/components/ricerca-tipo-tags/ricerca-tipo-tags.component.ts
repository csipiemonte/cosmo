/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import { Component, forwardRef, Input } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Utils } from '../../utilities/utilities';
import { RicercaEntityComponent, SelezioneEntity } from '../ricerca-entity/ricerca-entity.component';
import { TipoTag } from '../../models/api/cosmoauthorization/tipoTag';
import { TagsService } from '../../services/tags.service';


@Component({
  selector: 'app-ricerca-tipo-tags',
  templateUrl: '../ricerca-entity/ricerca-entity.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaTipoTagsComponent),
    multi: true
  }]
})

export class RicercaTipoTagsComponent extends RicercaEntityComponent<TipoTag> {



  @Input() promptNew?: (results: SelezioneEntity<TipoTag>[], term: string, newEntity: TipoTag) => boolean;
  @Input() tipiTag: TipoTag[] = [];


  constructor(
    private logger: NGXLogger,
    private tagsService: TagsService,
    ) {
      super();
      this.logger.trace('building component');
     }

     getEntityDescriptionSingular(): string {
      return 'tipo tag';
    }

    format(input: SelezioneEntity<TipoTag>): string {
      return input?.entity?.descrizione ?? '--';
    }

    getIcon(option: SelezioneEntity<TipoTag>): string | null {
      return 'fas fa-tags';
    }

    isEnriched(entry: SelezioneEntity<TipoTag>): boolean {
      return !!(entry?.entity?.codice);
    }

    clean(entity: TipoTag): TipoTag {
      return {
        ...entity,
      };
    }

    buildNewFromText(text: string): TipoTag {
      let codice = text.replace(/[^a-zA-Z0-9]+/gi, '_');
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

    search(term: string | null): Observable<SelezioneEntity<TipoTag>[]> {
      return this.tagsService.getTipiTag().pipe(
        map(response => {
          this.tipiTag.forEach(tipoTag => {
           if (!response.some(t => tipoTag.codice === t.codice)){
          response.push(tipoTag);
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

    displayNewOption(results: SelezioneEntity<TipoTag>[], term: string, e: TipoTag): boolean {
      return !!(term?.trim()?.length) && !results.some(c => c.entity?.codice === term)
        && (!this.promptNew || this.promptNew(results, term, e));
    }

  enrich(entry: SelezioneEntity<TipoTag>): Observable<SelezioneEntity<TipoTag> | null> {
    if (!entry?.entity?.codice) {
      return of(null);
    }
    return this.tagsService.getTipiTag().pipe(map(response => {
      return { entity: response.find(c => c.codice === entry.entity?.codice) };
    }));
  }

}
