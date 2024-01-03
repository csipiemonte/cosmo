/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, OnDestroy, Input, forwardRef, ViewChild, ElementRef } from '@angular/core';
import { NgbTypeahead, NgbTypeaheadSelectItemEvent } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable, merge, Subject, of, forkJoin } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, map, mergeMap } from 'rxjs/operators';
import { SecurityService } from '../../services/security.service';
import { Ente } from '../../models/api/cosmoauthorization/ente';
import { EntiService } from '../../services/enti.service';

export interface SelezioneEnte {
  ente?: Partial<Ente>;
}

@Component({
  selector: 'app-ricerca-ente',
  templateUrl: './ricerca-ente.component.html',
  styleUrls: ['./ricerca-ente.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaEnteComponent),
    multi: true
  }]
})
export class RicercaEnteComponent implements OnInit, OnDestroy, ControlValueAccessor {

  @Input() internalControlValue: SelezioneEnte | null = null;
  @Input() disabled?: boolean;

  @ViewChild('nativeControl') nativeControl: ElementRef | null = null;
  @ViewChild('instance', {static: true}) instance!: NgbTypeahead;

  focus$ = new Subject<string>();
  click$ = new Subject<string>();

  @Input() minChars = 2;

  constructor(
    private logger: NGXLogger,
    private securityService: SecurityService,
    private entiService: EntiService,
  ) {
    this.logger.trace('building component');
  }

  focus() {
    if (this.nativeControl?.nativeElement) {
      this.nativeControl.nativeElement.focus();
    }
  }

  get isDisabled(): boolean {
    return this.disabled ?? false;
  }

  get placeholder(): string {
    if (this.isDisabled) {
      return 'nessun ente';
    } else {
      return 'cerca ente ...';
    }
  }

  propagateChange = (_: any) => {};
  propagateTouched = (_: any) => {};

  registerOnChange(fn: any) {
    this.propagateChange = fn;
  }

  registerOnTouched(fn: any) {
    this.propagateTouched = fn;
  }

  blur($event: any) {
    if (!!this.propagateTouched) {
      this.propagateTouched($event);
    }
  }

  change($event: any) {
    this.propagateChange(this.internalControlValue);
  }

  selectItem($event: NgbTypeaheadSelectItemEvent) {
    this.propagateChange($event.item);
  }

  get controlValue() {
    return this.internalControlValue;
  }

  set controlValue(val) {
    this.internalControlValue = val;
    this.propagateChange(this.internalControlValue);
  }

  writeValue(value: SelezioneEnte) {
    if (value !== undefined) {
      if (value) {
        if (this.isEnriched(value)) {
          this.internalControlValue = value;
        } else {
          this.internalControlValue = value;
          this.enrich(value).subscribe(enriched => {
            this.internalControlValue = enriched;
          });
        }
      } else {
        this.internalControlValue = value;
      }
    }
  }

  ngOnInit(): void {
    this.logger.trace('initializing component');
  }

  ngOnDestroy(): void {
    this.logger.trace('destroying component');
  }

  inputFormatter = (input: SelezioneEnte) => {
    if (!input) {
      return '--';
    }

    if (this.isEnriched(input)) {
      return this.format(input);
    }

    // needs enrichment
    this.enrich(input).subscribe(enriched => {
      const native = ((this.instance as any)._elementRef as ElementRef).nativeElement;
      native.value = enriched ? this.format(enriched) : '--';
      this.internalControlValue = enriched;
      this.propagateChange(this.internalControlValue);
    });
    return '...';
  }

  private format(input: SelezioneEnte): string {
    return (input.ente ? (input.ente.nome) : '???') ?? '???';
  }

  inputFocusOut(event: any): void {
    if (!this.internalControlValue) {
      this.internalControlValue = null;
      this.propagateChange(this.internalControlValue);
    }
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
        return this.doSearch(term);
      })
    );
  }

  doSearch(term: string | null): Observable<SelezioneEnte[]> {
    if (this.minChars > 0 && (!term || term.length < this.minChars)) {
      return of([]);
    }

    return this.securityService.getCurrentUser().pipe(
      mergeMap(user => {
        return forkJoin({
          response: this.entiService.getEnti(
            JSON.stringify(this.getFilter(term ?? undefined))),
        })
        .pipe(
          map(response => {
            return [
              ...(response?.response?.enti ?? []).map(e => {
                return {ente: this.cleanEnte(e) } as SelezioneEnte;
              }).sort((e1, e2) => this.format(e1).localeCompare(this.format(e2))),
            ];
          })
        );
      })
    );
  }

  private getFilter(searchTerm?: string, value?: SelezioneEnte) {
    const f: any = {
      filter: {},
      page: 0,
      size: 5,
      fields: 'id,nome,codiceIpa,codiceFiscale',
      sort: '+nome, +id'
    };
    if (searchTerm?.length) {
      f.filter.fullText = {
        ci: searchTerm
      };
    }
    if (value?.ente?.codiceFiscale?.length) {
      f.filter.codiceFiscale = {
        eq: value.ente.codiceFiscale
      };
    }
    if (value?.ente?.id) {
      f.filter.id = {
        eq: value.ente.id
      };
    }
    return f;
  }

  private isEnriched(entry: SelezioneEnte): boolean {
    return !!(entry.ente?.nome);
  }

  private enrich(entry: SelezioneEnte): Observable<SelezioneEnte | null> {
    if (!entry) {
      return of(null);
    }
    if (entry.ente) {
      return this.entiService.getEnti(JSON.stringify(this.getFilter(undefined, entry))).pipe(
        map(response => {
          if (response.enti?.length === 1) {
            return {
              ente: this.cleanEnte(response.enti[0])
            };
          } else if ((response.enti?.length ?? 0) > 1) {
            throw new Error('Troppi enti trovati');
          } else {
            return null;
          }
        })
      );
    }
    return of(null);
  }

  private cleanEnte(ente: Ente): Partial<Ente> {
    return {
      ...ente,
      preferenze: undefined,
      gruppi: undefined,
    };
  }
}
