/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AbstractControl, AsyncValidatorFn, FormArray, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { SecurityService } from 'src/app/shared/services/security.service';
import { EntiService } from 'src/app/shared/services/enti.service';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, from, Observable, of } from 'rxjs';
import { debounceTime, delay, distinctUntilChanged, finalize, find, map, mergeMap, switchMap, tap, timeout } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { GestioneFormLogiciService } from '../../../shared/services/gestione-form-logici.service';
import { Constants } from 'src/app/shared/constants/constants';
import { IstanzaFunzionalitaFormLogico } from 'src/app/shared/models/api/cosmobusiness/istanzaFunzionalitaFormLogico';
import { FormLogico } from 'src/app/shared/models/api/cosmobusiness/formLogico';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { ClipboardService } from 'ngx-clipboard';
import { SelezioneEntity } from 'src/app/shared/components/ricerca-entity/ricerca-entity.component';
import { SpinnerVisibilityService } from 'ng-http-loader';
import { NgbTypeaheadSelectItemEvent } from '@ng-bootstrap/ng-bootstrap';
import { AggiornaFormLogicoRequest } from 'src/app/shared/models/api/cosmobusiness/aggiornaFormLogicoRequest';
import { CreaFormLogicoRequest } from 'src/app/shared/models/api/cosmobusiness/creaFormLogicoRequest';
import { Utils } from 'src/app/shared/utilities/utilities';
import { AggiornaFormLogicoIstanzaFunzionalitaRequest } from 'src/app/shared/models/api/cosmobusiness/aggiornaFormLogicoIstanzaFunzionalitaRequest';
import { TipologiaParametroFormLogico } from 'src/app/shared/models/api/cosmobusiness/tipologiaParametroFormLogico';
import { GestioneIstanzeFunzionalitaFormLogiciService } from '../../gestione-istanze-funzionalita-form-logici/gestione-istanze-funzionalita-form-logici.service';
import { TipologiaFunzionalitaFormLogico } from 'src/app/shared/models/api/cosmobusiness/tipologiaFunzionalitaFormLogico';
import Ajv from 'ajv';
import * as localizeIt from 'ajv-i18n/localize/it';
import { AggiornaIstanzaParametroFormLogico } from 'src/app/shared/models/api/cosmobusiness/aggiornaIstanzaParametroFormLogico';
import { RiferimentoEnte } from 'src/app/shared/models/api/cosmopratiche/riferimentoEnte';
import { relativeTimeThreshold } from 'moment';

interface IstanzaFunzionalitaFormLogicoWithParametri extends IstanzaFunzionalitaFormLogico {
  anagraficaParametri: TipologiaParametroFormLogico[];
}

@Component({
  selector: 'app-aggiungi-modifica-form-logico',
  templateUrl: './aggiungi-modifica-form-logico.component.html',
  styleUrls: ['./aggiungi-modifica-form-logico.component.scss']
})
export class AggiungiModificaFormLogicoComponent
  extends AbstractReactiveFormComponent<void, FormLogico | null, CreaFormLogicoRequest | AggiornaFormLogicoRequest>
  implements OnInit, ComponentCanDeactivate
{
  istanze: IstanzaFunzionalitaFormLogicoWithParametri[] = [];
  codiceCopied = false;
  selezioneNuovaIstanza?: SelezioneEntity<IstanzaFunzionalitaFormLogico> | null;

  tipologieFunzionalita: TipologiaFunzionalitaFormLogico[] = [];

  cacheParametri: {[key: string]: TipologiaParametroFormLogico[]} = {};

  isConfiguratore = false;

  private funzionalitaMultiIstanza = 'CUSTOM-FORM';

  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected router: Router,
    protected formLogiciService: GestioneFormLogiciService,
    protected entiService: EntiService,
    protected modalService: ModalService,
    protected translateService: TranslateService,
    protected securityService: SecurityService,
    protected clipboardService: ClipboardService,
    protected spinner: SpinnerVisibilityService,
    protected istanzeFunzionalitaFormLogiciService: GestioneIstanzeFunzionalitaFormLogiciService,
  ) {
    super(logger, route, securityService);
  }


  getDescrizioneIstanza(istanza: IstanzaFunzionalitaFormLogicoWithParametri): string {
    if (istanza.codice) {
      return this.tipologieFunzionalita.find(candidate => candidate.codice === istanza.codice)?.descrizione ?? istanza.descrizione ?? 'Funzionalità';
    }
    return istanza.descrizione ?? 'Funzionalità';
  }

  funzionalitaNonAncoraAssociate = (input: SelezioneEntity<TipologiaFunzionalitaFormLogico>[]) => {
    return (input || []).filter(i => !(i.entity?.codice?.length) ||
     !this.istanze.find(c => c.codice === i.entity?.codice && !i.entity?.multiIstanza ));
  }

  get isNuovo(): boolean {
    return !this.loadedData?.id;
  }



  protected loadData(routeParams: any, inputData?: any): Observable<FormLogico | null> {
    const idFormLogico = routeParams?.id ?? undefined;
    return forkJoin({
      formLogico: idFormLogico ? this.formLogiciService.getFormLogico(idFormLogico) : of(null),
      tipologie: this.istanzeFunzionalitaFormLogiciService.getTipologieIstanzeFunzionalita(),
    }).pipe(
      mergeMap(loaded => {
        this.tipologieFunzionalita = loaded.tipologie;
        const forkJoinAnagraficheParametri = loaded.formLogico?.istanzeFunzionalita?.length ?
          forkJoin(loaded.formLogico.istanzeFunzionalita
            .map(istanza => this.getParametriPerTipologia(Utils.require(istanza.codice)))) : of(null);
        return forkJoin({
          formLogico: of(loaded.formLogico),
          parametri: forkJoinAnagraficheParametri
        });
      }),
      map(
        loaded => {
          this.istanze = (loaded.formLogico?.istanzeFunzionalita ?? []).map(i => ({
            ...i,
            anagraficaParametri: this.cacheParametri[Utils.require(i.codice)] })
          );
          return loaded.formLogico;
      })
    );
  }

  protected buildForm(routeParams: any, inputData?: void, loadedData?: FormLogico | null): FormGroup {
    const form = new FormGroup({
      codice: new FormControl({value: loadedData?.codice ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(30),
        Validators.pattern(Constants.PATTERNS.CODICE),
      ]),
      descrizione: new FormControl({value: loadedData?.descrizione ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(100),
      ]),
      eseguibileMassivamente: new FormControl({value: loadedData?.eseguibileMassivamente || false, disabled: false}, [
        Validators.required
      ]),
      wizard: new FormControl({value: loadedData?.wizard || false, disabled: false}, [Validators.required]),
      ente: new FormControl({value: {ente: loadedData?.riferimentoEnte}, disabled: false}, [])
    }, [this.checkOneEsecuzioneMultipla()]);

    const istanze = new FormArray([]);

    (this.istanze ?? [])
      .sort((i1, i2) => (i1.ordine ?? 0) - (i2.ordine ?? 0))
      .forEach(istanza => {
        const istanzaFormGroup = this.buildIstanzaFormGroup(istanza);
        istanze.push(istanzaFormGroup);
      });

    form.addControl('istanze', istanze);


    form.controls.eseguibileMassivamente.valueChanges.subscribe(
      em => {
        if (em === false){
          (this.getControl('istanze') as FormArray).controls.forEach(ist => {
            (ist as FormGroup).controls.eseguiMassivamente.patchValue(false);
            (ist as FormGroup).controls.eseguiMassivamente.disable({onlySelf: true, emitEvent: false});
            (ist as FormGroup).controls.eseguiMassivamente.updateValueAndValidity({onlySelf: true, emitEvent: false});
          });
        }else{
          (this.getControl('istanze') as FormArray).controls.forEach(ist => {
            (ist as FormGroup).controls.eseguiMassivamente.enable({onlySelf: true, emitEvent: false});
            (ist as FormGroup).controls.eseguiMassivamente.updateValueAndValidity({onlySelf: true, emitEvent: false});
          });
        }
      }
    );
    form.controls.codice.valueChanges.subscribe(r => {

      this.checkEnte();

    });

    form.controls.ente.valueChanges.subscribe(r => {

      this.checkEnte();
    });

    this.securityService.getCurrentUser().subscribe(utente => {
      if (utente?.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
        this.isConfiguratore = true;
        this.entiService.getEnte(utente.ente?.id as number).subscribe(result => {
          form.controls.ente.patchValue({ente: result.ente});
        });
      }});
    return form;
  }

  abilitaEsecuzioneMultipla(index: number){
    const istanze = (this.getControl('istanze') as FormArray)?.controls;
    if (this.tipologieFunzionalita.find(esec => esec.codice === (istanze[index] as FormGroup)
      .controls?.codice.value && esec.eseguibileMassivamente === true)){
      return true;
    }

    return false;
  }

  protected buildIstanzaFormGroup(istanza: IstanzaFunzionalitaFormLogicoWithParametri, nuovaIstanza ?: boolean): FormGroup {

    const istanzaFormGroup = new FormGroup({
      id: new FormControl({value: istanza?.id ?? null, disabled: true}),
      codice: new FormControl({value: istanza?.codice ?? '', disabled: true}),
      descrizione: new FormControl({value: istanza?.descrizione ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(500),
      ]),
      eseguiMassivamente: new FormControl({ value: nuovaIstanza === true ? false : ((istanza.eseguibileMassivamente &&
         this.loadedData?.eseguibileMassivamente) ?? false), disabled: nuovaIstanza === true ?
         (this.getControl('eseguibileMassivamente')?.value === false || (this.getControl('istanze') as FormArray).controls
         .filter(elem => (elem as FormGroup).controls?.eseguiMassivamente.value === true).length > 0)
         : (!this.loadedData?.eseguibileMassivamente || (!istanza.eseguibileMassivamente
          && this.loadedData?.istanzeFunzionalita?.find( elem => elem.eseguibileMassivamente === true))) })
    });

    istanzaFormGroup.controls.eseguiMassivamente.valueChanges.subscribe(
      istanzaMassiva => {
        if (this.getControl('eseguibileMassivamente')?.value === true){
          if (istanzaMassiva === true){
            (this.getControl('istanze') as FormArray).controls.forEach(ist => {
              if ((ist as FormGroup).controls.eseguiMassivamente.value === false){
                (ist as FormGroup).controls.eseguiMassivamente.disable({onlySelf: true, emitEvent: false});
                (ist as FormGroup).controls.eseguiMassivamente.updateValueAndValidity({onlySelf: true, emitEvent: false});
              }
            });
          }else{
            (this.getControl('istanze') as FormArray).controls.forEach(ist => {
              (ist as FormGroup).controls.eseguiMassivamente.enable({onlySelf: true, emitEvent: false});
              (ist as FormGroup).controls.eseguiMassivamente.updateValueAndValidity({onlySelf: true, emitEvent: false});
            });
          }
        }
      }
    );

    const parametri = new FormGroup({});
    (istanza.anagraficaParametri ?? []).forEach(anagraficaParametro => {
      const parametroValorizzato = istanza.parametri?.find(p => p.chiave === anagraficaParametro.codice);

      const validators: ValidatorFn[] = [];
      if (anagraficaParametro.obbligatorio) {
        validators.push(Validators.required);
      }
      if (anagraficaParametro.tipo === 'object') {
        if (anagraficaParametro.schema) {
          validators.push(this.jsonSchema(anagraficaParametro.schema));
        } else {
          validators.push(this.json());
        }
      }

      parametri.addControl(anagraficaParametro.codice, new FormGroup({
        codice: new FormControl({value: anagraficaParametro.codice, disabled: true}),
        valore: new FormControl({
          value: this.parseParam(parametroValorizzato?.valore, anagraficaParametro) ?? '',
          disabled: false
        }, validators),
      }));
    });

    istanzaFormGroup.addControl('parametri', parametri);

    return istanzaFormGroup;
  }

  protected buildPayload(formValue: any): CreaFormLogicoRequest | AggiornaFormLogicoRequest {
    const payloadIstanze = this.buildPayloadIstanze(formValue);

    if (this.isNuovo) {
      const out: CreaFormLogicoRequest = {
        codice: formValue?.codice,
        descrizione: formValue?.descrizione,
        eseguibileMassivamente: formValue?.eseguibileMassivamente || false,
        istanzeFunzionalita: payloadIstanze,
        wizard: formValue?.wizard || false,
        riferimentoEnte: formValue?.ente?.ente
      };
      return out;
    } else {
      const out: AggiornaFormLogicoRequest = {
        codice: formValue?.codice,
        descrizione: formValue?.descrizione,
        eseguibileMassivamente: formValue?.eseguibileMassivamente || false,
        istanzeFunzionalita: payloadIstanze,
        wizard: formValue?.wizard || false,
        riferimentoEnte: formValue?.ente?.ente
      };
      return out;
    }
  }

  protected buildPayloadIstanze(formValue: any): AggiornaFormLogicoIstanzaFunzionalitaRequest[] {
    const payloadIstanze: AggiornaFormLogicoIstanzaFunzionalitaRequest[] = [];
    const istanze = (formValue.istanze ?? []);
    for (let i = 0; i < istanze.length; i ++) {
      const istanza = istanze[i];
      const req: AggiornaFormLogicoIstanzaFunzionalitaRequest = {
        id: istanza.id,
        ordine: i,
        codice: istanza.codice,
        descrizione: istanza.descrizione,
        eseguibileMassivamente: istanza.eseguiMassivamente,
        parametri: this.buildPayloadParametri(istanza),
      };
      payloadIstanze.push(req);
    }


    return payloadIstanze;
  }

  protected buildPayloadParametri(istanza: any): AggiornaIstanzaParametroFormLogico[] {
    const out: AggiornaIstanzaParametroFormLogico[] = [];
    Object.keys(istanza.parametri ?? {}).forEach(key => {
      const anagraficaParametro = this.cacheParametri[istanza.codice].find(p => p.codice === key);
      const param: AggiornaIstanzaParametroFormLogico = {
        chiave: key,
        valore: this.serializeParam(istanza.parametri[key].valore, anagraficaParametro) ?? undefined,
      };
      out.push(param);
    });
    return out;
  }

  protected onSubmit(payload: CreaFormLogicoRequest | AggiornaFormLogicoRequest): Observable<any> {

    let out: Observable<FormLogico> | null = null;
    const crea = this.isNuovo;

    if (crea) {
      // crea nuovo
      out = this.formLogiciService.creaFormLogico(payload as CreaFormLogicoRequest);
    } else {
      // modifica esistente
      out = this.formLogiciService.aggiornaFormLogico(
        Utils.require(this.loadedData?.id), payload as AggiornaFormLogicoRequest);
    }

    return out.pipe(
      delay(environment.httpMockDelay),
      tap(result => {
        let messaggio = this.translateService.instant(
          'form_logici.dialogs.' + (crea ? 'creato' : 'modificato') + '.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.descrizione ?? '']);
        this.modalService.info(this.translateService.instant(
          'form_logici.dialogs.' + (crea ? 'creato' : 'modificato') + '.titolo'),
          messaggio).then(
            () => {
              this.tornaIndietro();
            }
          ).catch(() => { });
      }, err => {
        this.modalService.simpleError(Utils.toMessage(err), err.error.errore);
      })
    );
  }

  tornaIndietro() {
    window.history.back();
  }


  checkConflictingField(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,descrizione,' + fieldName,
        filter: { id: this.loadedData ? { ne: this.loadedData.id } : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(

        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.formLogiciService.getFormLogici(filter)),
        map(response => {
          if (response.formLogici?.length) {
            return {
              conflict: { field: fieldName, other: response.formLogici[0] }
            };
          }
          return null;
        })
      );
    };
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.istanze, event.previousIndex, event.currentIndex);

    // move in form
    const array = this.getControl('istanze') as FormArray;
    if (array) {
      moveItemInArray(array.controls, event.previousIndex, event.currentIndex);
    }

    this.markDirty();
  }

  copyCodice(): void {
    if (!this.loadedData?.codice?.length) {
      return;
    }
    this.clipboardService.copy(this.loadedData.codice);
    this.codiceCopied = true;
    setTimeout(() => this.codiceCopied = false, 3000);
  }

  eliminaParametroIstanza(istanza: IstanzaFunzionalitaFormLogicoWithParametri, indice: number,
                          parametro: TipologiaParametroFormLogico): void {

    const group = this.getControl('istanze[' + indice + '].parametri') as FormGroup;
    if (group) {
      group.removeControl(parametro.codice);
    }
  }

  eliminaIstanza(istanza: IstanzaFunzionalitaFormLogico, indice: number): void {
    this.modalService.scegli(
      this.translateService.instant('form_logici.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), ['questa istanza']),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {

      this.istanze.splice(indice, 1);
      const array = this.getControl('istanze') as FormArray;
      if (array) {
        array.removeAt(indice);
      }

      if (this.getControl('eseguibileMassivamente')?.value === true &&
      array.controls.filter(elem => (elem as FormGroup).controls?.eseguiMassivamente.value === true).length === 0){
        array.controls.forEach(
          elemento => {
            (elemento as FormGroup).controls.eseguiMassivamente.enable({onlySelf: true, emitEvent: false});
            (elemento as FormGroup).controls.eseguiMassivamente.updateValueAndValidity({onlySelf: true, emitEvent: false});
          }
        );
      }

      this.markDirty();

    }).catch(() => {});
  }

  istanzaSelezionataChanged(event: NgbTypeaheadSelectItemEvent) {
    const sel = event.item?.entity;
    if (!!sel?.codice && !this.istanzaGiaPresente(sel)) {
      this.spinner.show();
      this.getParametriPerTipologia(sel.codice).pipe(
        finalize(() => this.spinner.hide())
      )
      .subscribe(parametri => {
        this.markDirty();

        const nuovaIstanzaConParametri: IstanzaFunzionalitaFormLogicoWithParametri = {
          ...sel,
          anagraficaParametri: parametri,
        };

        const istanzaFormGroup = this.buildIstanzaFormGroup(nuovaIstanzaConParametri, true);
        (this.getControl('istanze') as FormArray).push(istanzaFormGroup);

        this.istanze.push(nuovaIstanzaConParametri);
        this.selezioneNuovaIstanza = null;
      });
    }
  }

  istanzaGiaPresente(istanza: TipologiaFunzionalitaFormLogico): boolean {
    if (!istanza?.codice || !this.istanze?.length) {
      return false;
    }
    return !!this.istanze.find(candidate => candidate.codice === istanza.codice && !istanza.multiIstanza);
  }

  private getParametriPerTipologia(codice: string): Observable<TipologiaParametroFormLogico[]> {
    if (!this.funzionalitaMultiIstanza.match(codice) && this.cacheParametri[codice]) {
      return of(this.cacheParametri[codice]);
    }
    return this.istanzeFunzionalitaFormLogiciService.getParametriPerTipologia(codice).pipe(
      tap(parametri => {
        this.cacheParametri[codice] = parametri;
      })
    );
  }

  json(): ValidatorFn {
    return (input: AbstractControl): {[key: string]: any} | null => {
      if (!input.value) {
        return null;
      }
      try {
        JSON.parse(input.value);
      } catch (err) {
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
        return { jsonSchema: { message: 'Deve essere un JSON valido' } };
      }

      const ajv = new Ajv({strict: false}); // options can be passed, e.g. {allErrors: true}
      let validate: any;
      try {
        validate = ajv.compile(JSON.parse(schema));
      } catch (err) {
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
      this.logger.warn('got param that should not be there', raw);
      return null;
    }

    if (raw === null || raw === undefined || typeof raw === 'undefined' ||
      ((typeof raw === 'string') && raw.trim().length < 1)) {
      if (type.valoreDefault?.length) {
        raw = type.valoreDefault ?? null;
      } else {
        return null;
      }
    }

    if (!type.tipo) {
      return raw;
    }

    if (type.tipo === 'object') {
      try {
        return JSON.stringify(JSON.parse(raw), null, 4);
      } catch (err) {
        this.logger.warn('invalid JSON object encountered parsing param ' + raw, err);
        return null;
      }
    }

    if (type.tipo === 'number' || type.tipo === 'boolean') {
      try {
        return JSON.parse(raw);
      } catch (err) {
        this.logger.warn('invalid JSON value encountered parsing param ' + raw, err);
        return null;
      }
    }
    if (type.tipo === 'text') {
      // try {
      //   return JSON.parse(raw);
      // } catch (err) {
      //   this.logger.warn('invalid JSON value encountered parsing param ' + raw, err);
      //   return raw;
      // }
      return raw as string;
    }

    return raw;
  }

  private serializeParam(raw: any | undefined | null, type: TipologiaParametroFormLogico | undefined) {
    if (!type) {
      throw new Error('got param that should not be there: ' + raw);
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

    if (type.tipo === 'select' && raw.entity) {
      let val = 'id';
      if (type.valoreDefault?.length) {
        val = type.valoreDefault;
      }
      return raw.entity[val];
    }


    return raw;
  }


  checkEnte(): void{
    const codice = this.getControl('codice');
    const ente = this.getControl('ente');
    const requestPayload: any = {
      filter: {codice: { eq: codice?.value }}
    };
    if (ente?.value?.ente?.id ){
      const fieldFilter: any = { eq: ente?.value?.ente?.id };
      requestPayload.filter.idEnte = fieldFilter;
    }
    this.formLogiciService.getFormLogici(JSON.stringify(requestPayload)).subscribe(
      response => {
        ente?.setErrors(null);
        if (response.formLogici?.find(form => form.id !== this.loadedData?.id && (ente?.value?.ente?.id || !form.riferimentoEnte))){
          ente?.setErrors({conflict: { field: 'ente', other: response.formLogici[0], hasEnte: !!ente?.value?.ente?.id }});
        }
      }
    );
  }

  checkOneEsecuzioneMultipla(): ValidatorFn{
    return (input: AbstractControl): { [key: string]: any } | null => {
      const group = input as FormGroup;
      if (group.controls){
        if (group.controls?.eseguibileMassivamente.value === false || !group.controls?.istanze){
          return null;
        }
        const istanze = (group.controls?.istanze as FormArray)?.controls
        .filter(elem => (elem as FormGroup).controls?.eseguiMassivamente.value === true);
        if (istanze.length === 0){
          return { almenoUnaEsecuzioneMassiva : true };
        }
        if (istanze.length > 1){
          return { piuEsecuzioniMassive : true};
        }
      }
      return null;
    };
  }

  goBack(){
    if (this.isConfiguratore){
      this.router.navigate(['/configurazione']);
    }else{
      this.router.navigate(['/amministrazione']);
    }
  }

}
