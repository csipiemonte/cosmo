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
import { Gruppo } from '../../models/api/cosmoauthorization/gruppo';
import { GruppiService } from '../../services/gruppi.service';

@Component({
  selector: 'app-ricerca-gruppo',
  templateUrl: '../ricerca-entity/ricerca-entity.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaGruppoComponent),
    multi: true
  }]
})
export class RicercaGruppoComponent extends RicercaEntityComponent<Gruppo> {

  @Input() idEnte!: number;

  constructor(
    private logger: NGXLogger,
    private gruppiService: GruppiService,
  ) {
    super();
    this.logger.trace('building component');
  }

  getEntityDescriptionSingular(): string {
    return 'gruppo';
  }

  format(input: SelezioneEntity<Gruppo>): string {
    return input?.entity?.descrizione ?? '--';
  }

  getIcon(option: SelezioneEntity<Gruppo>): string | null {
    return 'fas fa-users';
  }

  isEnriched(entry: SelezioneEntity<Gruppo>): boolean {
    return !!(entry?.entity?.codice);
  }

  clean(entity: Gruppo): Gruppo {
    return {
      ...entity,
    };
  }

  inputSearch = (text$: Observable<string>) => {
    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());

    const clicksWithClosedPopup$ = this.click$.pipe(
      filter(() => !this.instance.isPopupOpen()),
      map(o => null)
    );

    const inputFocus$ = this.focus$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      mergeMap((term: string | null) => {
        return this.search(term);
      })
    );
  }

  search(term: string | null): Observable<SelezioneEntity<Gruppo>[]> {
    return this.gruppiService.getGruppi(JSON.stringify(this.getFilter(term ?? undefined))).pipe(
      map(response => {
        return (response.gruppi ?? []).map(i => {
          return { entity: this.clean(i) };
        });
      })
    );
  }

  enrich(entry: any): Observable<SelezioneEntity<Gruppo> | null> {
    if (!entry) {
      return of(null);
    }

    return this.gruppiService.getGruppo(entry).pipe(map(i => {
      return { entity: i.gruppo };
    }));
  }

  private getFilter(searchTerm?: string, value?: SelezioneEntity<Gruppo>) {
    const f: any = {
      filter: {},
      page: 0,
      size: 5,
      fields: 'id,codice,descrizione',
      sort: '+descrizione, +codice'
    };

    if (this.idEnte) {
      f.filter.idEnte = {
        eq: this.idEnte
      };
    }

    if (searchTerm?.length) {
      f.filter.fullText = {
        ci: searchTerm
      };
    }

    return f;
  }

  inputFormatter = (input: SelezioneEntity<Gruppo>) => {
    if (!input) {
      return '';
    }

    if (this.isEnriched(input)) {
      return this.format(input);
    }

    // needs enrichment
    this.enrich(input).subscribe(enriched => {
      const native = ((this.instance as any)._elementRef as ElementRef).nativeElement;
      native.value = enriched ? this.format(enriched) : '';
      this.internalControlValue = enriched;
      this.propagateChange(this.internalControlValue);
    });
    return '...';
  }

}
