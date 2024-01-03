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
import { UtentiService } from '../../services/utenti.service';
import { GruppiService } from '../../services/gruppi.service';
import { SecurityService } from '../../services/security.service';
import { UserInfoWrapper } from '../../models/user/user-info';
import { Utente } from '../../models/api/cosmoauthorization/utente';
import { Gruppo } from '../../models/api/cosmoauthorization/gruppo';

export interface SelezioneUtenteGruppo {
  utente?: Partial<Utente>;
  gruppo?: Partial<Gruppo>;
  nuovo?: boolean;
}

@Component({
  selector: 'app-ricerca-utente',
  templateUrl: './ricerca-utente.component.html',
  styleUrls: ['./ricerca-utente.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => RicercaUtenteComponent),
    multi: true
  }]
})
export class RicercaUtenteComponent implements OnInit, OnDestroy, ControlValueAccessor {

  @Input() internalControlValue: SelezioneUtenteGruppo | null = null;
  @Input() disabled?: boolean;
  @Input() utenti?: boolean;
  @Input() gruppi?: boolean;
  @Input() utenteCorrente?: boolean;
  @Input() nuovo?: boolean;

  @Input() filtroUtenti?: (input: any) => any;
  @Input() filtroGruppi?: (input: any) => any;

  @ViewChild('nativeControl') nativeControl: ElementRef | null = null;
  @ViewChild('instance', {static: true}) instance!: NgbTypeahead;

  focus$ = new Subject<string>();
  click$ = new Subject<string>();

  minChars = 2;

  constructor(
    private logger: NGXLogger,
    private securityService: SecurityService,
    private utentiService: UtentiService,
    private gruppiService: GruppiService,
  ) {
    this.logger.trace('building component');
  }

  focus() {
    if (this.nativeControl?.nativeElement) {
      this.nativeControl.nativeElement.focus();
    }
  }

  clear() {
    this.writeValue(null);
  }

  get isPermettiNuovo(): boolean {
    return this.nuovo ?? false;
  }

  get isDisabled(): boolean {
    return this.disabled ?? false;
  }

  get isCercaUtenteCorrente(): boolean {
    return this.utenteCorrente ?? true;
  }

  get isCercaUtenti(): boolean {
    return this.utenti ?? true;
  }

  get isCercaGruppi(): boolean {
    return this.gruppi ?? true;
  }

  get placeholder(): string {
    const allowUtenti = this.isCercaUtenti;
    const allowGruppi = this.isCercaGruppi;
    if (this.isDisabled) {
      if (allowUtenti && allowGruppi) {
        return 'Nessun utente o gruppo';
      } else if (allowUtenti) {
        return 'Nessun utente';
      } else if (allowGruppi) {
        return 'Nessun gruppo';
      }
    } else if (!this.isPermettiNuovo) {
      if (allowUtenti && allowGruppi) {
        return 'Cerca utente o gruppo ...';
      } else if (allowUtenti) {
        return 'Cerca utente ...';
      } else if (allowGruppi) {
        return 'Cerca gruppo ...';
      }
    } else {
      if (allowUtenti && allowGruppi) {
        return 'Digita o cerca utente o gruppo ...';
      } else if (allowUtenti) {
        return 'Digita o cerca utente ...';
      } else if (allowGruppi) {
        return 'Digita o cerca gruppo ...';
      }
    }
    return '';
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

  writeValue(value: SelezioneUtenteGruppo | null) {
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

  inputFormatter = (input: SelezioneUtenteGruppo) => {
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

  private format(input: SelezioneUtenteGruppo): string {
    return (input.utente ?
      ((input.utente.nome ?? '') + (input.utente.cognome ? ' ' + input.utente.cognome : '')) :
      input.gruppo ? input.gruppo.descrizione : '???') ?? '???';
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

  doSearch(term: string | null): Observable<SelezioneUtenteGruppo[]> {
    if (!term || term.length < this.minChars) {
      return of([]);
    }

    return this.securityService.getCurrentUser().pipe(
      mergeMap(user => {
        return forkJoin({
          responseUtenti: this.isCercaUtenti ? this.utentiService.getUtenti(
            JSON.stringify(this.getFilterUtenti(user, term))) : of(null),
          responseGruppi: this.isCercaGruppi ? this.gruppiService.getGruppi(
            JSON.stringify(this.getFilterGruppi(user, term))) : of(null)
        })
        .pipe(
          map(response => {
            return [
              ...(response?.responseUtenti?.utenti ?? []).map(u => {
                return { utente: this.cleanUtente(u) } as SelezioneUtenteGruppo;
              }).sort((e1, e2) => this.format(e1).localeCompare(this.format(e2))),
              ...(response?.responseGruppi?.gruppi ?? []).map(g => {
                return { gruppo: this.cleanGruppo(g) } as SelezioneUtenteGruppo;
              }).sort((e1, e2) => this.format(e1).localeCompare(this.format(e2)))
            ];
          }),
          map(response => {
            if (!this.isPermettiNuovo || !term.length) {
              return response;
            }
            if (response.find(r => r.utente && (
                r.utente?.nome?.toUpperCase() === term.toUpperCase() ||
                r.utente?.cognome?.toUpperCase() === term.toUpperCase() ||
                this.format(r).toUpperCase() === term.toUpperCase()
              ))) {
              return response;
            }
            return [
              ...response,
              {
                utente: {
                  nome: term
                },
                nuovo: true
              }
            ];
          })
        );
      })
    );
  }

  private getFilterUtenti(user: UserInfoWrapper, searchTerm?: string, value?: SelezioneUtenteGruppo) {
    const f: any = {
      filter: {
        idEnte: user.ente ? { eq: user.ente?.id } : undefined
      },
      page: 0,
      size: 5,
      sort: '+fullName, +id'
    };
    if (searchTerm?.length) {
      f.filter.fullText = {
        ci: searchTerm
      };
    }
    if (value?.utente?.codiceFiscale?.length) {
      f.filter.codiceFiscale = {
        eq: value.utente.codiceFiscale
      };
    }
    if (value?.utente?.id) {
      f.filter.id = {
        eq: value.utente.id
      };
    }
    if (!this.isCercaUtenteCorrente) {
      f.filter.codiceFiscale = f.filter.codiceFiscale ?? {};
      f.filter.codiceFiscale.nin = [ user.codiceFiscale ];
    }
    if (this.filtroUtenti) {
      f.filter = this.filtroUtenti(f.filter);
    }
    return f;
  }

  private getFilterGruppi(user: UserInfoWrapper, searchTerm?: string, value?: SelezioneUtenteGruppo) {
    const f: any = {
      filter: {
        idEnte: user.ente ? { eq: user.ente?.id } : undefined
      },
      page: 0,
      size: 5,
      sort: 'nome'
    };
    if (searchTerm?.length) {
      f.filter.fullText = {
        ci: searchTerm
      };
    }
    if (value?.gruppo?.codice?.length) {
      f.filter.name = {
        eq: value.gruppo.codice
      };
    }
    if (value?.gruppo?.id) {
      f.filter.id = {
        eq: value.gruppo.id
      };
    }
    if (this.filtroGruppi) {
      f.filter = this.filtroGruppi(f.filter);
    }

    return f;
  }

  private isEnriched(entry: SelezioneUtenteGruppo): boolean {
    return !!(entry.utente?.nome) || !!(entry.gruppo?.descrizione);
  }

  private enrich(entry: SelezioneUtenteGruppo): Observable<SelezioneUtenteGruppo | null> {
    if (!entry) {
      return of(null);
    }
    if (entry.utente) {
      return this.securityService.getCurrentUser().pipe(
        mergeMap(user => {
          return this.utentiService.getUtenti(JSON.stringify(this.getFilterUtenti(user, undefined, entry)));
        }),
        map(utentiResponse => {
          if (utentiResponse.utenti?.length === 1) {
            return {
              utente: this.cleanUtente(utentiResponse.utenti[0])
            };
          } else if ((utentiResponse.utenti?.length ?? 0) > 1) {
            throw new Error('Troppi utenti trovati');
          } else {
            return null;
          }
        })
      );
    }
    if (entry.gruppo) {
      return this.securityService.getCurrentUser().pipe(
        mergeMap(user => {
          return this.gruppiService.getGruppi(JSON.stringify(this.getFilterGruppi(user, undefined, entry)));
        }),
        map(gruppiResponse => {
          if (gruppiResponse.gruppi?.length === 1) {
            return {
              gruppo: this.cleanGruppo(gruppiResponse.gruppi[0])
            };
          } else if ((gruppiResponse.gruppi?.length ?? 0) > 1) {
            throw new Error('Troppi gruppi trovati');
          } else {
            return null;
          }
        })
      );
    }
    return of(null);
  }

  private cleanUtente(utente: Utente): Partial<Utente> {
    return {
      ...utente,
      enti: undefined,
      profili: undefined,
      preferenze: undefined,
      gruppi: undefined,
    };
  }

  private cleanGruppo(gruppo: Gruppo): Partial<Gruppo> {
    return {
      ...gruppo,
      ente: undefined,
      utenti: undefined,
    };
  }
}
