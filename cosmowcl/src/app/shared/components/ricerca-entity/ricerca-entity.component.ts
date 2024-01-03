/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, OnDestroy, Input, forwardRef, ViewChild, ElementRef, Output, EventEmitter, Directive } from '@angular/core';
import { NgbTypeahead, NgbTypeaheadSelectItemEvent } from '@ng-bootstrap/ng-bootstrap';
import { ControlValueAccessor } from '@angular/forms';
import { Observable, merge, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, map, mergeMap, switchMap } from 'rxjs/operators';

export interface SelezioneEntity<T> {
  entity?: T;
  nuovo?: boolean;
}

@Directive()
export abstract class RicercaEntityComponent<T> implements OnInit, OnDestroy, ControlValueAccessor {

  @Input() internalControlValue: SelezioneEntity<T> | null = null;
  @Input() disabled?: boolean;
  @Input() icons?: boolean;
  @Input() nuovo?: boolean;
  @Input() disabledPlaceholder = '--';

  @Input() postSearchFilter?: (results: SelezioneEntity<T>[]) => SelezioneEntity<T>[];

  @Output() selectItem = new EventEmitter<NgbTypeaheadSelectItemEvent>();

  @ViewChild('nativeControl') nativeControl: ElementRef | null = null;
  @ViewChild('instance', {static: true}) instance!: NgbTypeahead;

  focus$ = new Subject<string>();
  click$ = new Subject<string>();

  minChars = 2;

  constructor() {}

  get isPermettiNuovo(): boolean {
    return this.nuovo ?? false;
  }

  abstract getEntityDescriptionSingular(): string;

  abstract format(input: SelezioneEntity<T>): string;

  abstract isEnriched(entry: SelezioneEntity<T>): boolean;

  abstract enrich(entry: SelezioneEntity<T>): Observable<SelezioneEntity<T> | null>;

  abstract clean(entity: T): Partial<T>;

  abstract search(term: string | null): Observable<SelezioneEntity<T>[]>;

  abstract getIcon(option: SelezioneEntity<T>): string | null;

  displayNewOption(results: SelezioneEntity<T>[], term: string, newEntity: T): boolean {
    return !!(term?.trim()?.length);
  }

  buildNewFromText(text: string): T {
    throw new Error('NOT SUPPORTED');
  }

  clear(): void {
    this.writeValue({entity: undefined, nuovo: undefined});
  }

  get showIcons(): boolean {
    return this.icons ?? true;
  }

  get showOptionsIcons(): boolean {
    return true;
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
      return this.disabledPlaceholder;
    } else if (this.isPermettiNuovo) {
      return 'digita o cerca ' + this.getEntityDescriptionSingular() + ' ...';
    } else {
      return 'cerca ' + this.getEntityDescriptionSingular() + ' ...';
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

  selectItemInternal($event: NgbTypeaheadSelectItemEvent) {
    this.selectItem.emit($event);
    this.propagateChange($event.item);
  }

  get controlValue() {
    return this.internalControlValue;
  }

  set controlValue(val) {
    this.internalControlValue = val;
    this.propagateChange(this.internalControlValue);
  }

  writeValue(value: SelezioneEntity<T>) {
    if (value !== undefined) {
      if (value) {
        if (this.isEnriched(value)) {
          this.internalControlValue = value;
        } else {
          this.internalControlValue = value;
          this.enrich(value).subscribe(enriched => {
            this.internalControlValue = enriched;
          }, error => {
            this.internalControlValue = null;
            this.propagateChange(this.internalControlValue);
          });
        }
      } else {
        this.internalControlValue = value;
      }
    }
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  inputFormatter = (input: SelezioneEntity<T>) => {
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
      switchMap((term: string | null) => {
        return this.search(term).pipe(
          map(
            results => {
              if (this.postSearchFilter) {
                results = this.postSearchFilter(results);
              }
              if (this.isPermettiNuovo && term?.trim().length) {
                const wouldBuild = this.buildNewFromText(term.trim());
                if (this.displayNewOption(results, term, wouldBuild)) {
                  return [...results, { entity: wouldBuild, nuovo: true }];
                }
                return results;
              } else {
                return results;
              }
            }
          )
        );
      })
    );
  }

}
