/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, ElementRef, forwardRef, Input, OnInit } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { NG_VALUE_ACCESSOR } from '@angular/forms';
import { merge, Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, map, mergeMap } from 'rxjs/operators';
import { RicercaEntityComponent, SelezioneEntity } from '../ricerca-entity/ricerca-entity.component';
import { CustomFormService } from '../../../shared/services/customForm.service';
import { CustomForm } from '../../models/api/cosmopratiche/customForm';

@Component({
  selector: 'app-ricerca-custom-form',
  templateUrl: '../ricerca-entity/ricerca-entity.component.html',
  styleUrls: ['../ricerca-entity/ricerca-entity.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaCustomFormComponent),
    multi: true
  }]
})
export class RicercaCustomFormComponent extends RicercaEntityComponent<CustomForm>{

  @Input() senzaAssociazioneConTipoPratica = false;


  constructor(
    private logger: NGXLogger,
    private customFormService: CustomFormService,
  ) {
    super();
    this.logger.trace('building component');
  }

  getEntityDescriptionSingular(): string {
    return 'custom form';
  }

  format(input: SelezioneEntity<CustomForm>): string {
    return input?.entity?.descrizione ?? '--';
  }

  getIcon(option: SelezioneEntity<CustomForm>): string | null {
    return 'fas fa-box-open';
  }

  isEnriched(entry: SelezioneEntity<CustomForm>): boolean {
    return !!(entry?.entity?.codice);
  }

  clean(entity: CustomForm): CustomForm {
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

  search(term: string | null): Observable<SelezioneEntity<CustomForm>[]> {
    return this.customFormService.getCustomForms(JSON.stringify(this.getFilter(term ?? undefined))).pipe(
      map(response => {
        return (response.customForms ?? []).map(i => {
          return { entity: this.clean(i) };
        });
      })
    );
  }

  enrich(entry: any): Observable<SelezioneEntity<CustomForm> | null> {
    if (!entry) {
      return of(null);
    }

    return this.customFormService.get(entry).pipe(map(i => {
      return { entity: i };
    }));
  }

  private getFilter(searchTerm?: string, value?: SelezioneEntity<CustomForm>) {
    const f: any = {
      filter: {},
      page: 0,
      size: 5,
      fields: 'id,codice,descrizione',
      sort: '+descrizione, +codice'
    };

    if (searchTerm?.length) {
      f.filter.fullText = {
        ci: searchTerm
      };
    }

    if (value?.entity?.codice) {
      f.filter.codice = {
        eq: value.entity?.codice
      };
    }

    if (this.senzaAssociazioneConTipoPratica){
      f.filter.senzaAssociazioneConTipoPratica = {
        defined: this.senzaAssociazioneConTipoPratica
      };
    }
    return f;
  }

  inputFormatter = (input: SelezioneEntity<CustomForm>) => {
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
