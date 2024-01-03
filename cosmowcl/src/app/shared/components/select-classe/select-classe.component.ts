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
import { Utils } from '../../utilities/utilities';
import { Decodifica } from '../../models/decodifica';

@Component({
  selector: 'app-select-classe',
  templateUrl: './select-classe.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => SelectClasseComponent),
    multi: true
  }]
})
export class SelectClasseComponent extends RicercaEntityComponent<Decodifica> {

  classi: {codice: string, valore: string}[] = [
    { codice: 'primary', valore: 'Colore primario' },
    { codice: 'success', valore: 'Colore di successo' },
    { codice: 'warning', valore: 'Colore di avviso' },
    { codice: 'danger', valore: 'Colore di errore' },
    { codice: 'dark', valore: 'Colore scuro' },
    { codice: 'light', valore: 'Colore chiaro' },
    { codice: 'secondary', valore: 'Colore secondario' },
  ];

  constructor(
    private logger: NGXLogger,
  ) {
    super();
    this.logger.trace('building component');
  }

  get showIcons(): boolean {
    return false;
  }

  get showOptionsIcons(): boolean {
    return false;
  }

  getEntityDescriptionSingular(): string {
    return 'classe';
  }

  format(input: SelezioneEntity<Decodifica>): string {
    return input?.entity?.valore ?? '--';
  }

  getIcon(option: SelezioneEntity<Decodifica>): string | null {
    return null;
  }

  isEnriched(entry: SelezioneEntity<Decodifica>): boolean {
    return !!(entry?.entity?.valore);
  }

  clean(entity: Decodifica): Decodifica {
    return {
      ...entity,
      codice: Utils.require(entity.codice)
    };
  }

  buildNewFromText(text: string): Decodifica {
    return {
      codice: text.replace(/[^a-zA-Z0-9]+/gi, '-'),
      valore: text,
    };
  }

  search(term: string | null): Observable<SelezioneEntity<Decodifica>[]> {
    return of(this.classi).pipe(
      map(response => {
        return (response ?? [])
        //.filter(i => !(term?.trim()?.length) || (i.codice + ' ' + i.valore).toUpperCase().includes(term.trim().toUpperCase()))
        .map(i => {
          return { entity: this.clean(i) };
        });
      })
    );
  }

  displayNewOption(results: SelezioneEntity<Decodifica>[], term: string): boolean {
    return !!(term?.trim()?.length) && !results.some(c => c.entity?.codice === term);
  }

  enrich(entry: SelezioneEntity<Decodifica>): Observable<SelezioneEntity<Decodifica> | null> {
    if (!entry?.entity?.codice) {
      return of(null);
    }
    return of(this.classi).pipe(map(response => {
      return { entity: response.find(c => c.codice === entry.entity?.codice) };
    }));
  }
}
