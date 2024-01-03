/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { DatePipe } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  OnInit,
  Output,
} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import {
  ActivatedRoute,
  Router,
} from '@angular/router';

import { NGXLogger } from 'ngx-logger';
import {
  Observable,
  of,
} from 'rxjs';
import {
  AbstractReactiveFormComponent,
} from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { SecurityService } from 'src/app/shared/services/security.service';

import { TranslateService } from '@ngx-translate/core';

import { Decodifica } from '../shared/models/decodifica';

const TipoMatchContiene = 'cn';
const TipoMatchIniziaCon = 'sw';
const TipoMatchFinisceCon = 'ew';
type TipoMatch = 'cn' | 'sw' | 'ew';

interface FiltroRicercaActa {
    ricercaProtocollo: boolean;
    oggetto?: string;
    oggettoTipoMatch?: TipoMatch;
    paroleChiave?: string;
    paroleChiaveTipoMatch?: TipoMatch;
    numeroRepertorio?: string;
    numeroRepertorioTipoMatch?: TipoMatch;
    dataCronicaDa?: string;
    dataCronicaA?: string;
    numeroProtocollo?: string;
    numeroProtocolloTipoMatch?: TipoMatch;
    dataRegistrazioneProtocolloDa?: string;
    dataRegistrazioneProtocolloA?: string;
    aooProtocollante?: string;
    aooProtocollanteTipoMatch?: TipoMatch;
}

export interface FiltroRicercaActaOutput {
    ricercaPerProtocollo?: boolean;
    oggetto?: {
        lk?: string;
    };
    paroleChiave?: {
        lk?: string;
    };
    numeroRepertorio?: {
        lk?: string;
    };
    dataCronica?: {
        gte?: string;
        lte?: string;
    };
    numeroProtocollo?: {
        lk?: string;
    };
    dataRegistrazioneProtocollo?: {
        gte?: string;
        lte?: string;
    };
    aooProtocollante?: {
        lk?: string;
    };
}

@Component({
    selector: 'app-filtri-ricerca-acta',
    templateUrl: './filtri-ricerca-acta.component.html'
  })
  export class FiltriRicercaActaComponent
    extends AbstractReactiveFormComponent<
        FiltroRicercaActa,
        FiltroRicercaActa,
        FiltroRicercaActaOutput
    >
    implements OnInit {

    // tslint:disable-next-line: no-output-on-prefix
    @Output() ricerca =  new EventEmitter<FiltroRicercaActaOutput>();

    tipiMatch: Decodifica[] = [{
        codice: TipoMatchContiene, valore: 'Contiene'
    }, {
        codice: TipoMatchIniziaCon, valore: 'Inizia con'
    }, {
        codice: TipoMatchFinisceCon, valore: 'Finisce con'
    }];

    tipiMatchCO: Decodifica[] = [{
        codice: TipoMatchContiene, valore: 'Contiene'
    }];

    existing?: FiltroRicercaActa = {
        ricercaProtocollo: false
    };

    searchAttempted = false;

    constructor(
      protected logger: NGXLogger,
      protected route: ActivatedRoute,
      protected router: Router,
      protected translateService: TranslateService,
      protected securityService: SecurityService,
      protected cdr: ChangeDetectorRef,
      protected datePipe: DatePipe,
    ) {
      super(logger, route, securityService);
    }

    hoverCerca() {
        this.searchAttempted = true;
    }

    pulisciCampi() {
        const pre = this.ricercaProtocolloSelected;
        this.reset().subscribe(() => {
            // ripristino il tipo di ricerca selezionato
            this.form?.patchValue({
                ricercaProtocollo: pre
            });
            this.form?.markAsPristine();
            this.form?.markAsUntouched();
        });
    }

    protected reloadOnRouteParams(): boolean {
      // essendo un modale non deve dipendere dai route params
      return false;
    }

    protected getInputData(): Observable<FiltroRicercaActa | null | undefined> {
      return of(this.existing);
    }

    protected buildForm(routeParams: any, inputData?: FiltroRicercaActa): FormGroup {
      const form = new FormGroup({
        ricercaProtocollo: new FormControl({value: inputData?.ricercaProtocollo ?? false, disabled: false}, [
            Validators.required
        ]),

        oggetto: new FormControl({value: inputData?.oggetto ?? '', disabled: false}, [
            Validators.maxLength(1000)
        ]),
        oggettoTipoMatch: new FormControl({value: inputData?.oggettoTipoMatch ?? TipoMatchContiene, disabled: false}, [
            this.requiredIf(() => !this.ricercaProtocolloSelected && this.hasValue('oggetto'))
        ]),
        paroleChiave: new FormControl({value: inputData?.paroleChiave ?? '', disabled: false}, [
            Validators.maxLength(1000),
        ]),
        paroleChiaveTipoMatch: new FormControl({value: inputData?.paroleChiaveTipoMatch ?? TipoMatchContiene, disabled: false}, [
            this.requiredIf(() => !this.ricercaProtocolloSelected && this.hasValue('paroleChiave'))
        ]),
        numeroRepertorio: new FormControl({value: inputData?.numeroRepertorio ?? '', disabled: false}, [
            Validators.maxLength(1000),
        ]),
        numeroRepertorioTipoMatch: new FormControl({value: inputData?.numeroRepertorioTipoMatch ?? TipoMatchContiene, disabled: false}, [
            this.requiredIf(() => !this.ricercaProtocolloSelected && this.hasValue('numeroRepertorio'))
        ]),
        dataCronicaDa: new FormControl({value: inputData?.dataCronicaDa ||
            this.datePipe.transform(
                new Date(new Date().getTime() - 7 * (24 * 60 * 60 * 1000)),
                'yyyy-MM-dd'
            ), disabled: false}, [
            this.dateFormat(),
            this.before('dataCronicaA'),
            this.maxDays('dataCronicaA', 30)
        ]),
        dataCronicaA: new FormControl({value: inputData?.dataCronicaA ||
            this.datePipe.transform(
                new Date(),
                'yyyy-MM-dd'
            ), disabled: false}, [
            this.dateFormat(),
            this.after('dataCronicaDa'),
            this.maxDays('dataCronicaDa', 30)
        ]),

        numeroProtocollo: new FormControl({value: inputData?.numeroProtocollo ?? '', disabled: false}, [
            Validators.maxLength(1000),
            this.requiredIf(() => this.ricercaProtocolloSelected)
        ]),
        numeroProtocolloTipoMatch: new FormControl({value: inputData?.numeroProtocolloTipoMatch ?? TipoMatchContiene, disabled: false}, [
            Validators.maxLength(1000),
            this.requiredIf(() => this.ricercaProtocolloSelected)
        ]),
        dataRegistrazioneProtocolloDa: new FormControl({value: inputData?.dataRegistrazioneProtocolloDa ?? '', disabled: false}, [
            this.dateFormat(),
            this.before('dataRegistrazioneProtocolloA')
        ]),
        dataRegistrazioneProtocolloA: new FormControl({value: inputData?.dataRegistrazioneProtocolloA ?? '', disabled: false}, [
            this.dateFormat(),
            this.after('dataRegistrazioneProtocolloDa')
        ]),
        aooProtocollante: new FormControl({value: inputData?.aooProtocollante ?? '', disabled: false}, [
            Validators.maxLength(1000),
            this.requiredIf(() => this.ricercaProtocolloSelected)
        ]),
        aooProtocollanteTipoMatch: new FormControl({value: inputData?.aooProtocollanteTipoMatch ?? TipoMatchContiene, disabled: false}, [
            Validators.maxLength(1000),
            this.requiredIf(() => this.ricercaProtocolloSelected)
        ]),
      }, [
          this.businessConditionOne()
      ]);

      form.controls.oggettoTipoMatch.disable();
      form.controls.numeroRepertorioTipoMatch.disable();

      form.controls.ricercaProtocollo.valueChanges.subscribe(() => {
        (form.controls.oggetto as FormControl).updateValueAndValidity();
        (form.controls.oggettoTipoMatch as FormControl).updateValueAndValidity();
        (form.controls.paroleChiave as FormControl).updateValueAndValidity();
        (form.controls.paroleChiaveTipoMatch as FormControl).updateValueAndValidity();
        (form.controls.numeroRepertorio as FormControl).updateValueAndValidity();
        (form.controls.numeroRepertorioTipoMatch as FormControl).updateValueAndValidity();
        (form.controls.dataCronicaDa as FormControl).updateValueAndValidity();
        (form.controls.dataCronicaA as FormControl).updateValueAndValidity();
        (form.controls.numeroProtocollo as FormControl).updateValueAndValidity();
        (form.controls.numeroProtocolloTipoMatch as FormControl).updateValueAndValidity();
        (form.controls.dataRegistrazioneProtocolloDa as FormControl).updateValueAndValidity();
        (form.controls.dataRegistrazioneProtocolloA as FormControl).updateValueAndValidity();
        (form.controls.aooProtocollante as FormControl).updateValueAndValidity();
        (form.controls.aooProtocollanteTipoMatch as FormControl).updateValueAndValidity();

        this.cdr.detectChanges();
      });

      this.cdr.detectChanges();
      return form;
    }

    protected buildPayload(raw: any): FiltroRicercaActaOutput {
        const ricercaProtocollo = !!raw?.ricercaProtocollo;

        const transformTextMatcher = (value: string, type: TipoMatch) => {
            if (!value?.trim().length) {
                return undefined;
            }
            switch (type) {
                case TipoMatchContiene:
                    return { lk: '"*' + value + '*"' };
                case TipoMatchFinisceCon:
                    return { lk: '"*' + value + '"' };
                case TipoMatchIniziaCon:
                    return { lk: '"' + value + '*"' };
                default:
                    return { lk: value };
            }
        };

        const transformTextMatcherPlain = (value: string) => {
            if (!value?.trim().length) {
                return undefined;
            }
            return { lk: value };
        };

        return {
            ricercaPerProtocollo: ricercaProtocollo,
            oggetto: ricercaProtocollo ? undefined : transformTextMatcherPlain(raw.oggetto),
            paroleChiave: ricercaProtocollo ? undefined : transformTextMatcher(raw.paroleChiave, raw.paroleChiaveTipoMatch),
            numeroRepertorio: ricercaProtocollo ? undefined : transformTextMatcherPlain(raw.numeroRepertorio),
            dataCronica: ricercaProtocollo ? undefined : {
                gte: raw.dataCronicaDa || undefined,
                lte: raw.dataCronicaA || undefined,
            },
            numeroProtocollo: !ricercaProtocollo ? undefined : transformTextMatcher(raw.numeroProtocollo, raw.numeroProtocolloTipoMatch),
            dataRegistrazioneProtocollo: !ricercaProtocollo ? undefined : {
                gte: raw.dataRegistrazioneProtocolloDa || undefined,
                lte: raw.dataRegistrazioneProtocolloA || undefined,
            },
            aooProtocollante: !ricercaProtocollo ? undefined : transformTextMatcher(raw.aooProtocollante, raw.aooProtocollanteTipoMatch),
        };
    }

    protected onSubmit(payload: FiltroRicercaActaOutput): Observable<FiltroRicercaActaOutput> {
        this.ricerca.next(payload);

        return of(payload);
    }

    get ricercaProtocolloSelected(): boolean {
        return this.getValue('ricercaProtocollo') === true;
    }

    businessConditionOne(): ValidatorFn {
        // almeno un filtro tra oggetto, parole chiave o numero repertorio e' obbligatorio,
        // a meno di non valorizzare il periodo completo
        return (input: AbstractControl): {[key: string]: any} | null => {
            if (!input) {
                return null;
            }
            
            const rawValue = (input as FormGroup).getRawValue() as FiltroRicercaActa;
            if (rawValue.ricercaProtocollo) {
                return null;
            }

            const oggettoValorizzato = (!!rawValue.oggetto?.length);
            const paroleChiaveValorizzato = (!!rawValue.paroleChiave?.length);
            const numeroRepertorioValorizzato = (!!rawValue.numeroRepertorio?.length);
            const dataCronicaCompletaValorizzata = (!!rawValue.dataCronicaDa?.length) && (!!rawValue.dataCronicaA?.length);

            if (!dataCronicaCompletaValorizzata && !oggettoValorizzato && !paroleChiaveValorizzato && !numeroRepertorioValorizzato) {
                return {
                    businessConditionOne: true
                };
            }
            return null;
        };
      }

}
