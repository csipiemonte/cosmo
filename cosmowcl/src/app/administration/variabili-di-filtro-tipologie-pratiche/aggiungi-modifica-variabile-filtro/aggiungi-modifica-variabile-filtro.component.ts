/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, merge, Observable, of, Subject } from 'rxjs';
import { debounceTime, delay, distinctUntilChanged, filter, map, mergeMap, switchMap, tap } from 'rxjs/operators';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { Constants } from 'src/app/shared/constants/constants';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { FormatoVariabileDiFiltro } from 'src/app/shared/models/api/cosmopratiche/formatoVariabileDiFiltro';
import { TipoFiltro } from 'src/app/shared/models/api/cosmopratiche/tipoFiltro';
import { TipoPratica } from 'src/app/shared/models/api/cosmopratiche/tipoPratica';
import { VariabileDiFiltro } from 'src/app/shared/models/api/cosmopratiche/variabileDiFiltro';
import { EntiService } from 'src/app/shared/services/enti.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { VariabiliDiFiltroService } from 'src/app/shared/services/variabili-di-filtro.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';
import { GestioneTipiPraticheService } from '../../gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { FiltroCampo, FormatoCampo } from '../variabili-di-filtro-tipologie-pratiche.model';

@Component({
  selector: 'app-aggiungi-modifica-variabile-filtro',
  templateUrl: './aggiungi-modifica-variabile-filtro.component.html',
  styleUrls: ['./aggiungi-modifica-variabile-filtro.component.scss']
})
export class AggiungiModificaVariabileFiltroComponent
extends AbstractReactiveFormComponent<void, VariabileDiFiltro | null, VariabileDiFiltro>
implements OnInit, ComponentCanDeactivate {
  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected router: Router,
    protected securityService: SecurityService,
    protected entiService: EntiService,
    protected tipiPraticheService: GestioneTipiPraticheService,
    protected translateService: TranslateService,
    protected variabiliDiFiltroService: VariabiliDiFiltroService,
    protected modalService: ModalService
    ) {
      super(logger, route, securityService);
    }

  get isNuovo(): boolean {
    return !this.loadedData?.id;
  }


  @ViewChild('instanceTipoPratiche', { static: true }) instanceTipoPratiche!: NgbTypeahead;
  @ViewChild('elemRef') elemRef!: ElementRef;
  focusTipoPratiche$ = new Subject<string>();
  clickTipoPratiche$ = new Subject<string>();
  oldEnte!: number;
  tipiPratiche: TipoPratica[] = [];

  validating = false;
  isConfiguratore = false;
  entePresente = false;
  formatiDisponibili: FormatoVariabileDiFiltro[] = [];
  tipiFiltro: TipoFiltro[] = [];
  formatterTipoPratica = (result: TipoPratica) => result.descrizione;

  protected loadData(routeParams: any, inputData?: any): Observable<VariabileDiFiltro | null> {
    const idVariabile = routeParams?.id ?? undefined;
    return forkJoin({
      variabileDiFiltro: idVariabile ? this.variabiliDiFiltroService.getVariabileDiFiltro(idVariabile) : of(null),
      formati: this.variabiliDiFiltroService.getFormatiVariabili(),
      tipiFiltro: this.variabiliDiFiltroService.getTipiFiltriVariabili()
    }).pipe(map(elem => {
      this.formatiDisponibili = elem.formati;
      this.tipiFiltro = elem.tipiFiltro;
      return elem.variabileDiFiltro;
    }
    ));
  }

  protected buildForm(routeParams: any, inputData?: void, loadedData?: VariabileDiFiltro | null): FormGroup {
    const form = new FormGroup({
      nome: new FormControl({ value: loadedData?.nome ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(100)
      ], [this.checkAlreadyExistsNome('nomeFiltro')]),
      label: new FormControl({ value: loadedData?.label ?? '', disabled: false}, [
        Validators.required,
        Validators.maxLength(100)
      ], [this.checkAlreadyExistsLabel('labelFiltro')]),
      ente: new FormControl({value: {ente: loadedData?.tipoPratica?.ente ?? null},  disabled: !this.isNuovo}, [
        Validators.required
      ], []),
      tipoPratica: new FormControl({value: loadedData?.tipoPratica ?? null, disabled: true}, [
        Validators.required
      ], []),
      tipoFiltro: new FormControl({ value: loadedData?.tipoFiltro ?
        this.tipiFiltro.find(elem => elem.codice === loadedData.tipoFiltro?.codice) ?? null : null, disabled: this.isNuovo
        || loadedData?.formato?.codice === FormatoCampo.STRINGA || loadedData?.formato?.codice === FormatoCampo.BOOLEANO
        || loadedData?.formato?.codice === FormatoCampo.JSON_STRINGA || loadedData?.formato?.codice === FormatoCampo.JSON_BOOLEANO },
        [Validators.required]),
      formatoVariabile: new FormControl({ value: loadedData?.formato ?
        this.formatiDisponibili.find(elem => elem.codice === loadedData.formato?.codice) ?? null : null, disabled: false },
        [Validators.required]),
      aggiungiARisultatoRicerca: new FormControl({ value: loadedData?.aggiungereARisultatoRicerca, disabled: false }, [])
    }, [], []);

    form.controls?.ente.valueChanges.subscribe(
      value => this.getTipiPratiche(value)
    );

    form.controls?.tipoPratica.valueChanges.subscribe(
      () => {
        const labelForm = this.form?.get('label');
        const nomeForm = this.form?.get('nome');
        labelForm?.updateValueAndValidity({ onlySelf: true, emitEvent: false });
        nomeForm?.updateValueAndValidity({ onlySelf: true, emitEvent: false });
      }
    );

    form.controls?.formatoVariabile.valueChanges.subscribe(
      elem => {
        if (elem.codice === FormatoCampo.STRINGA || elem.codice === FormatoCampo.BOOLEANO
          || elem.codice === FormatoCampo.JSON_STRINGA || elem.codice === FormatoCampo.JSON_BOOLEANO){
          if (this.form){
            this.form.get('tipoFiltro')?.patchValue(this.tipiFiltro.find(val => val.codice === FiltroCampo.SINGOLO));
            this.form.get('tipoFiltro')?.disable();
          }
        }else{
          if (this.form){
            this.form.get('tipoFiltro')?.enable();
          }
        }
      }
    );

    this.securityService.getCurrentUser().subscribe(utente => {
      if (utente?.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
        this.isConfiguratore = true;
        if (this.isNuovo){
          this.entiService.getEnte(utente.ente?.id as number).subscribe(result => {
            form.controls.ente.patchValue({ente: result.ente});
          });
        }
      }});

    return form;
  }

  protected buildPayload(formValue: any): VariabileDiFiltro {

      const out: VariabileDiFiltro = {
        nome: formValue?.nome,
        label: formValue?.label,
        tipoPratica: formValue?.tipoPratica,
        formato: formValue?.formatoVariabile,
        tipoFiltro: formValue?.tipoFiltro,
        aggiungereARisultatoRicerca: formValue?.aggiungiARisultatoRicerca
      };

      return out;

  }

  protected onSubmit(payload: VariabileDiFiltro): Observable<any> {
    let out: Observable<VariabileDiFiltro> | null = null;
    const crea = this.isNuovo;
    if (crea) {
      // crea nuovo
      out = this.variabiliDiFiltroService.create(payload);
    } else {
      // modifica esistente
      out = this.variabiliDiFiltroService.update(
        Utils.require(this.loadedData?.id), payload);
    }

    return out.pipe(
      delay(environment.httpMockDelay),
      tap(result => {

        let messaggio = this.translateService.instant(
          'variabili_filtro.dialogs.' + (crea ? 'creato' : 'modificato') + '.messaggio');

        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.label ?? '']);

        this.modalService.info(this.translateService.instant(
          'variabili_filtro.dialogs.' + (crea ? 'creato' : 'modificato') + '.titolo'),
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

  getTipiPratiche(value: any) {
    this.form?.get('tipoPratica')?.patchValue(null);
    this.tipiPratiche = [];
    if (value.ente) {
      this.validating = true;
      this.form?.get('tipoPratica')?.enable();
      return this.tipiPraticheService.listTipiPraticaByEnte(value.ente.id)
        .subscribe(response => {
        this.tipiPratiche = response;
        this.validating = false;
      });
    } else {
      this.form?.get('tipoPratica')?.disable();
    }
  }


  searchTipiPratiche = (text$: Observable<string>) => {

    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());

    const clicksWithClosedPopup$ = this.clickTipoPratiche$.pipe(
      filter(() => this.instanceTipoPratiche && !this.instanceTipoPratiche.isPopupOpen()),
      map(o => null)
    );

    const inputFocus$ = this.focusTipoPratiche$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      mergeMap((term: string | null) => {
        return of(this.tipiPratiche ?
          this.tipiPratiche.filter(v => !(term?.length) || v.descrizione && v.descrizione.toLowerCase()?.indexOf(term.toLowerCase()) > -1)
            .sort((c1, c2) => (c1?.descrizione ?? '').localeCompare(c2?.descrizione ?? ''))
            .slice(0, 10)
          : []);
      })
    );
  }

  checkAlreadyExistsNome(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{ [key: string]: any } | null> => {

      const v = (input as FormControl).value;

      if (!v) {
        return of(null);
      }

      const form = this.form?.getRawValue();

      const requestPayload: any = {
        filter: { nomeFiltro: this.loadedData ? { ne: this.loadedData.nome } : undefined },
      };

      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;


      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(val => this.variabiliDiFiltroService.getVariabiliDiFiltro(val)),
        map(response => {

          if (response.variabiliDiFiltro?.find(elem => (!this.loadedData || this.loadedData?.nome !== elem.nome)
          && elem.tipoPratica?.codice === form.tipoPratica?.codice)) {
            return {
              conflict: { field: 'nome' }
            };
          }

          return null;
        })
      );
    };
  }



  checkAlreadyExistsLabel(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{ [key: string]: any } | null> => {

      const v = (input as FormControl).value;

      if (!v) {
        return of(null);
      }

      const form = this.form?.getRawValue();

      const requestPayload: any = {
        filter: { labelFiltro: this.loadedData ? { ne: this.loadedData.label } : undefined },
      };

      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;


      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(val => this.variabiliDiFiltroService.getVariabiliDiFiltro(val)),
        map(response => {

          if (response.variabiliDiFiltro?.find(elem => (!this.loadedData || this.loadedData?.label !== elem.label)
          && elem.tipoPratica?.codice === form.tipoPratica?.codice)) {
            return {
              conflict: { field: 'label' }
            };
          }

          return null;
        })
      );
    };
  }

  get allValid(): boolean{
    const form = this.form?.getRawValue();
    return this.form?.valid && !this.validating && form && form.ente && form.ente.ente &&
     form.ente.ente.id && form.tipoPratica && form.tipoPratica.codice;
  }

  annullaModifiche(){
    this.form?.get('tipoPratica')?.disable();
    this.pulisciCampi();
  }

  tornaIndietro() {
    window.history.back();
  }

}
