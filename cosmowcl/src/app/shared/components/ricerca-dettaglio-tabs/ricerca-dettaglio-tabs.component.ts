/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, forwardRef, Input } from '@angular/core';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { NGXLogger } from 'ngx-logger';
import { map } from 'rxjs/operators';
import { TabsDettaglio } from '../../models/api/cosmopratiche/tabsDettaglio';
import { RicercaEntityComponent, SelezioneEntity } from '../ricerca-entity/ricerca-entity.component';
import { TabsDettaglioService } from '../../services/tabs-dettaglio.service';
import { Utils } from '../../utilities/utilities';

@Component({
  selector: 'app-ricerca-dettaglio-tabs',
  templateUrl: '../ricerca-entity/ricerca-entity.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaDettaglioTabsComponent),
    multi: true
  }]
})
export class RicercaDettaglioTabsComponent extends RicercaEntityComponent<TabsDettaglio> {

  @Input() dettTab: TabsDettaglio[] = [];



  constructor(
    private logger: NGXLogger,
    private tabsDettaglioService: TabsDettaglioService,
  ) {
    super();
    this.logger.trace('building component');
  }

  getEntityDescriptionSingular(): string {
    return 'dettaglio tabs';
  }

  format(input: SelezioneEntity<TabsDettaglio>): string {
    return input?.entity?.descrizione ?? '--';
  }
  getIcon(option: SelezioneEntity<TabsDettaglio>): string | null {
    return ' ';
  }

  isEnriched(entry: SelezioneEntity<TabsDettaglio>): boolean {
    return !!(entry?.entity?.descrizione);
  }

  clean(entity: TabsDettaglio) {
    return {
      ...entity,
    };
  }

  search(term: string | null): Observable<SelezioneEntity<TabsDettaglio>[]> {
    return this.tabsDettaglioService.getTabsDettaglio().pipe(
      map(response => {
        return (response ?? [])
        .filter(i => !term?.trim()?.length
         || (((i.codice ?? '') + (i.descrizione ?? '') + (i.ordine ?? '')).toUpperCase().indexOf(term.trim().toUpperCase()) !== -1))
        .map(i => {
          return { entity: this.clean(i) };
        });
      })
    );
  }

  enrich(entry: SelezioneEntity<TabsDettaglio>): Observable<SelezioneEntity<TabsDettaglio> | null> {
    if (!entry?.entity?.codice) {
      return of(null);
    }
    return this.tabsDettaglioService.getTabsDettaglio().pipe(map(i => {
      return { entity: i.find(c => c.codice === entry?.entity?.codice) };
    }));
  }
}
