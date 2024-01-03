/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, Observable, of } from 'rxjs';
import { delay, map, mergeMap, tap } from 'rxjs/operators';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { AggiornaIstanzaFunzionalitaFormLogicoRequest } from 'src/app/shared/models/api/cosmobusiness/aggiornaIstanzaFunzionalitaFormLogicoRequest';
import { CreaIstanzaFunzionalitaFormLogicoRequest } from 'src/app/shared/models/api/cosmobusiness/creaIstanzaFunzionalitaFormLogicoRequest';
import { IstanzaFunzionalitaFormLogico } from 'src/app/shared/models/api/cosmobusiness/istanzaFunzionalitaFormLogico';
import { IstanzaParametroFormLogico } from 'src/app/shared/models/api/cosmobusiness/istanzaParametroFormLogico';
import { TipologiaFunzionalitaFormLogico } from 'src/app/shared/models/api/cosmobusiness/tipologiaFunzionalitaFormLogico';
import { TipologiaParametroFormLogico } from 'src/app/shared/models/api/cosmobusiness/tipologiaParametroFormLogico';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';
import { GestioneIstanzeFunzionalitaFormLogiciService } from '../gestione-istanze-funzionalita-form-logici.service';
import Ajv from 'ajv';
import * as localizeIt from 'ajv-i18n/localize/it';
import { AggiornaIstanzaParametroFormLogico } from 'src/app/shared/models/api/cosmobusiness/aggiornaIstanzaParametroFormLogico';

@Component({
  selector: 'app-aggiungi-modifica-istanza',
  templateUrl: './aggiungi-modifica-istanza.component.html',
  styleUrls: ['./aggiungi-modifica-istanza.component.scss']
})
export class AggiungiModificaIstanzaComponent
  extends AbstractReactiveFormComponent<void, IstanzaFunzionalitaFormLogico | null,
    CreaIstanzaFunzionalitaFormLogicoRequest | AggiornaIstanzaFunzionalitaFormLogicoRequest>
  implements OnInit, ComponentCanDeactivate {
  COMPONENT_NAME = 'AggiungiModificaIstanzaComponent';

  tipologieFunzionalita: TipologiaFunzionalitaFormLogico[] = [];
  tipologiaFunzionalita: TipologiaFunzionalitaFormLogico | null = null;
  parametriDisponibili: TipologiaParametroFormLogico[] = [];
  valoriParametri: {[key: string]: string | null | undefined} = {};

  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected router: Router,
    protected securityService: SecurityService,
    protected modalService: ModalService,
    protected translateService: TranslateService,
    protected istanzeFunzionalitaFormLogiciService: GestioneIstanzeFunzionalitaFormLogiciService) {
    super(logger, route, securityService);
  }

  get isNuovo(): boolean {
    return !this.loadedData?.id;
  }

  protected loadData(routeParams: any, inputData?: any): Observable<IstanzaFunzionalitaFormLogico | null> {
    this.logger.debug(this.COMPONENT_NAME + ' loadData - fetch dati di input');
    const id = routeParams?.id ?? undefined;
    return forkJoin({
      istanza: id ? this.istanzeFunzionalitaFormLogiciService.getIstanza(id) : of(null),
      tipologie: this.istanzeFunzionalitaFormLogiciService.getTipologieIstanzeFunzionalita(),
    }).pipe(
      mergeMap(loaded => {
        const paramObs = loaded.istanza?.codice ?
          this.istanzeFunzionalitaFormLogiciService.getParametriPerTipologia(loaded.istanza.codice) : of([]);
        return paramObs.pipe(
          map(parametri => {
            this.parametriDisponibili = parametri;
            return loaded;
          })
        );
      }),
      map(loaded => {
        this.tipologieFunzionalita = loaded.tipologie;
        return loaded.istanza;
      })
    );
  }

  protected buildForm(routeParams: any, inputData?: never, loadedData?: IstanzaFunzionalitaFormLogico | null): Observable<FormGroup> {
    this.logger.debug(this.COMPONENT_NAME + ' buildForm - ricostruisco form');
    const form = new FormGroup({
      descrizione: new FormControl(this.loadedData?.descrizione ?? '', [
        Validators.required,
        Validators.maxLength(255),
      ]),
      tipologia: new FormControl(this.loadedData ?
        this.tipologieFunzionalita.find(t => t.codice === this.loadedData?.codice) ?? null : null, [
          Validators.required
      ]),
      parametri: new FormGroup({})
    });

    form.controls.tipologia.valueChanges.subscribe(v => {
      this.onTipologiaChanged(form, v?.codice).subscribe();
      if (v?.descrizione && !form.getRawValue()?.descrizione) {
        form.patchValue({descrizione: v.descrizione});
      }
    });
    return this.onTipologiaChanged(form, this.loadedData?.codice ?? null).pipe(map(() => form));
  }

  protected onReset() {
    this.logger.debug(this.COMPONENT_NAME + ' onReset - resetto valore parametri');
  }

  protected onTipologiaChanged(form: FormGroup, newTipologia: string | null): Observable<TipologiaParametroFormLogico[]> {
    this.logger.debug(this.COMPONENT_NAME + ' onTipologiaChanged - ricarico parametri disponibili per ' + newTipologia);

    return (newTipologia ? this.istanzeFunzionalitaFormLogiciService.getParametriPerTipologia(newTipologia) : of([]))
      .pipe(tap(parametri => {
        this.logger.debug(this.COMPONENT_NAME + ' onTipologiaChanged - parametri disponibili caricati, costruisco form per '
          + newTipologia + ' con ' + parametri.length + ' parametri');

        const parametriGroup = new FormGroup({});
        this.parametriDisponibili = parametri;
        this.importaValoriParametri(this.loadedData?.parametri);

        this.parametriDisponibili.forEach(param => {
          const validators: ValidatorFn[] = [];
          if (param.obbligatorio) {
            validators.push(Validators.required);
          }
          if (param.tipo === 'object') {
            if (param.schema) {
              validators.push(this.jsonSchema(param.schema));
            } else {
              validators.push(this.json());
            }
          }
          parametriGroup.addControl(param.codice, new FormControl(this.valoriParametri[param.codice] ?? null, validators));
        });

        form.setControl('parametri', parametriGroup);
      }));
  }

  protected buildPayload(formValue: any): CreaIstanzaFunzionalitaFormLogicoRequest | AggiornaIstanzaFunzionalitaFormLogicoRequest {
    if (this.isNuovo) {
      const funzionalita: CreaIstanzaFunzionalitaFormLogicoRequest = {
        codice: formValue.tipologia?.codice,
        descrizione: formValue.descrizione,
        parametri: this.buildPayloadParametri(formValue),
      };
      return funzionalita;
    } else {
      const funzionalita: AggiornaIstanzaFunzionalitaFormLogicoRequest = {
        codice: formValue.tipologia?.codice,
        descrizione: formValue.descrizione,
        parametri: this.buildPayloadParametri(formValue),
      };
      return funzionalita;
    }
  }

  protected buildPayloadParametri(formValue: any): AggiornaIstanzaParametroFormLogico[] {
    const out: AggiornaIstanzaParametroFormLogico[] = [];
    (this.parametriDisponibili ?? []).forEach(param => {
      const parVal = this.serializeParam((formValue?.parametri ?? {})[param.codice] ?? null, param);
      if (parVal !== null && parVal !== undefined) {
        out.push({
          chiave: param.codice,
          valore: parVal
        });
      }
    });
    return out;
  }

  protected onSubmit(payload: CreaIstanzaFunzionalitaFormLogicoRequest | AggiornaIstanzaFunzionalitaFormLogicoRequest): Observable<any> {
    let obs: Observable<any>;
    if (!this.isNuovo) {
      obs = this.istanzeFunzionalitaFormLogiciService.aggiornaIstanza(
        Utils.require(this.loadedData?.id), payload);
    } else {
      obs = this.istanzeFunzionalitaFormLogiciService.creaIstanza(payload);
    }

    return obs.pipe(
      delay(environment.httpMockDelay),
      tap(() => {
        this.modalService.info(
          this.translateService.instant('form_logici.istanze_funzionalita.' + (this.isNuovo ?
            'aggiungi_istanza_funzionalita' : 'modifica_istanza_funzionalita')),
          this.translateService.instant('form_logici.istanze_funzionalita.' + (this.isNuovo ?
            'salvataggio_istanza_funzionalita_msg' : 'aggiornamento_istanza_funzionalita_msg'))
        ).then(() => {
          this.tornaIndietro();
        }).catch(() => {});
      }, error => {
        this.modalService.simpleError(Utils.toMessage(error), error.error.errore)
          .then(() => {}).catch(() => {});
      })
    );
  }

  tornaIndietro() {
    window.history.back();
  }

  pulisciCampi() {
    this.reset().subscribe();
  }

  importaValoriParametri(parametri: IstanzaParametroFormLogico[] | null | undefined) {
    this.logger.debug(this.COMPONENT_NAME + ' importaValoriParametri - rilettura parametri');
    const newValues: {[key: string]: string | null | undefined} = {};
    if (this.parametriDisponibili?.length) {
      for (const definition of this.parametriDisponibili) {
        const value = parametri?.find(candidate => candidate.chiave === definition.codice)?.valore ?? undefined;
        if (definition.codice) {
          newValues[definition.codice] = this.parseParam(value, definition);
        }
      }
    }
    this.logger.debug(this.COMPONENT_NAME + ' importaValoriParametri - caricati parametri', newValues);
    this.valoriParametri = newValues;
  }

  json(): ValidatorFn {
    return (input: AbstractControl): {[key: string]: any} | null => {
      if (!input.value) {
        return null;
      }
      try {
        JSON.parse(input.value);
      } catch (err) {
        this.logger.warn(this.COMPONENT_NAME + ' invalid json', err);
        return { json: { message: 'Deve essere un JSON valido' } };
      }
      return null;
    };
  }

  jsonSchema(schema: string): ValidatorFn {
    return (input: AbstractControl): {[key: string]: any} | null => {
      if (!schema) {
        return null;
      }
      if (!input.value) {
        return null;
      }
      let inputValue = null;
      try {
        inputValue = JSON.parse(input.value);
      } catch (err) {
        this.logger.warn(this.COMPONENT_NAME + ' invalid json', err);
        return { jsonSchema: { message: 'Deve essere un JSON valido' } };
      }

      const ajv = new Ajv({strict: false}); // options can be passed, e.g. {allErrors: true}
      let validate: any;
      try {
        validate = ajv.compile(JSON.parse(schema));
      } catch (err) {
        this.logger.warn(this.COMPONENT_NAME + ' invalid schema for validation', err);
        return { jsonSchema: { message: 'Schema di validazione non corretto' } };
      }
      const valid = validate(inputValue);
      if (!valid) {
        localizeIt(validate.errors);
        return {
          jsonSchema: {
            errors: validate.errors,
            message: ajv.errorsText(validate.errors),
          }
        };
      }
      return null;
    };
  }

  private parseParam(raw: string | undefined | null, type: TipologiaParametroFormLogico | undefined) {
    if (!type) {
      this.logger.warn(this.COMPONENT_NAME + ' got param that should not be there', raw);
      return null;
    }

    if (raw === null || raw === undefined || typeof raw === 'undefined' ||
      ((typeof raw === 'string') && raw.trim().length < 1)) {
      return type.valoreDefault ?? null;
    }

    if (!type.tipo) {
      return raw;
    }

    if (type.tipo === 'object') {
      try {
        return JSON.stringify(JSON.parse(raw), null, 4);
      } catch (err) {
        this.logger.warn(this.COMPONENT_NAME + ' invalid JSON object encountered parsing param ' + raw, err);
        return null;
      }
    }

    if (type.tipo === 'number' || type.tipo === 'boolean') {
      try {
        return JSON.parse(raw);
      } catch (err) {
        this.logger.warn(this.COMPONENT_NAME + ' invalid JSON value encountered parsing param ' + raw, err);
        return null;
      }
    }

    if (type.tipo === 'text') {
      try {
        return JSON.parse(raw);
      } catch (err) {
        this.logger.warn(this.COMPONENT_NAME + ' invalid JSON value encountered parsing param ' + raw, err);
        return raw;
      }
    }

    return raw;
  }

  private serializeParam(raw: string | undefined | null, type: TipologiaParametroFormLogico | undefined) {
    if (!type) {
      this.logger.warn(this.COMPONENT_NAME + ' got param that should not be there', raw);
      return null;
    }

    if (raw === null || raw === undefined || typeof raw === 'undefined') {
      return null;
    }

    if (!type.tipo) {
      return raw;
    }

    if (type.tipo === 'object') {
      return raw;
    }

    if (type.tipo === 'number' || type.tipo === 'boolean') {
      return JSON.stringify(raw);
    }

    return raw;
  }

}
