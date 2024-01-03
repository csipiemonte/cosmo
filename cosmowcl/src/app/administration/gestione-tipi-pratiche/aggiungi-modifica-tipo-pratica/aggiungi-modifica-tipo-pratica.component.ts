/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AbstractControl, AsyncValidatorFn, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, Observable, of } from 'rxjs';
import { debounceTime, delay, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { ClipboardService } from 'ngx-clipboard';
import { SpinnerVisibilityService } from 'ng-http-loader';
import { TipoPratica } from 'src/app/shared/models/api/cosmopratiche/tipoPratica';
import { AbstractReactiveFormComponent } from 'src/app/shared/components/proto/abstract-reactive-form.component';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { GestioneTipiPraticheService } from '../gestione-tipi-pratiche.service';
import { Constants } from 'src/app/shared/constants/constants';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';
import { AggiornaTipoPraticaRequest } from 'src/app/shared/models/api/cosmopratiche/aggiornaTipoPraticaRequest';
import { TipoDocumento } from 'src/app/shared/models/api/cosmopratiche/tipoDocumento';
import { NgbModal, NgbModalRef, NgbTypeaheadSelectItemEvent } from '@ng-bootstrap/ng-bootstrap';
import { ModificaStatoPraticaModalComponent } from './modifica-stato-pratica-modal/modifica-stato-pratica-modal.component';
import { SelezioneEntity } from 'src/app/shared/components/ricerca-entity/ricerca-entity.component';
import { RicercaStatoPraticaComponent } from 'src/app/shared/components/ricerca-stato-pratica/ricerca-stato-pratica.component';
import { ModificaTipoDocumentoModalComponent } from './modifica-tipo-documento-modal/modifica-tipo-documento-modal.component';
import { RicercaTipoDocumentoComponent } from 'src/app/shared/components/ricerca-tipo-documento/ricerca-tipo-documento.component';
import { TrasformazioneDatiPratica } from 'src/app/shared/models/api/cosmopratiche/trasformazioneDatiPratica';
import { ModificaTrasformazioneModalComponent } from './modifica-trasformazione-modal/modifica-trasformazione-modal.component';
import { AggiornaTipoPraticaTrasformazioneDatiRequest } from 'src/app/shared/models/api/cosmopratiche/aggiornaTipoPraticaTrasformazioneDatiRequest';
import { StatoPratica } from 'src/app/shared/models/api/cosmopratiche/statoPratica';
import { CreaTipoPraticaRequest } from 'src/app/shared/models/api/cosmopratiche/creaTipoPraticaRequest';
import { AggiornaTipoPraticaDocumentoRequest } from 'src/app/shared/models/api/cosmopratiche/aggiornaTipoPraticaDocumentoRequest';
import { TabsDettaglio } from 'src/app/shared/models/api/cosmopratiche/tabsDettaglio';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { RicercaDettaglioTabsComponent } from 'src/app/shared/components/ricerca-dettaglio-tabs/ricerca-dettaglio-tabs.component';
import { ModalStatus } from 'src/app/shared/enums/modal-status';
import { ConfigurazioniService } from 'src/app/shared/services/configurazioni.service';
import { ImpostazioniFirmaComponent } from 'src/app/shared/components/impostazioni-firma/impostazioni-firma.component';

@Component({
  selector: 'app-aggiungi-modifica-tipo-pratica',
  templateUrl: './aggiungi-modifica-tipo-pratica.component.html',
  styleUrls: ['./aggiungi-modifica-tipo-pratica.component.scss']
})
export class AggiungiModificaTipoPraticaComponent
  extends AbstractReactiveFormComponent<void, TipoPratica | null, CreaTipoPraticaRequest | AggiornaTipoPraticaRequest>
  implements OnInit, ComponentCanDeactivate {

  codiceCopied?: string;
  maxSize = 0;
  maxNumOfPixels = 0;
  imgPixels = 0;
  imgHeight = 0;
  imgWidth = 0;
  stati: StatoPratica[] = [];
  tipiDocumento: TipoDocumento[] = [];
  trasformazioni: TrasformazioneDatiPratica[] = [];
  dettTabs: TabsDettaglio[] = [];
  selezioneNuovoTab?: SelezioneEntity<TabsDettaglio> | null;
  isConfiguratore = false;
  imgURL: any;
  @ViewChild('ricercaStatoPraticaInput') ricercaStatoPraticaInput: RicercaStatoPraticaComponent | null = null;
  @ViewChild('ricercaTipoDocumentoInput') ricercaTipoDocumentoInput: RicercaTipoDocumentoComponent | null = null;
  @ViewChild('ricercaDettaglioTabs') ricercaDettaglioTabs: RicercaDettaglioTabsComponent | null = null;
  @ViewChild('impostazioniFirma') impostazioniFirmaComp!: ImpostazioniFirmaComponent;


  constructor(
    protected logger: NGXLogger,
    protected route: ActivatedRoute,
    protected router: Router,
    protected tipiPraticheService: GestioneTipiPraticheService,
    private configurazioniService: ConfigurazioniService,
    protected modalService: ModalService,
    protected modal: NgbModal,
    protected translateService: TranslateService,
    protected securityService: SecurityService,
    protected clipboardService: ClipboardService,
    protected spinner: SpinnerVisibilityService
  ) {
    super(logger, route, securityService);
  }



  ricercaStatoPraticaFilter = (f: StatoPratica) => !this.stati.some(c => c.codice === f.codice);

  ricercaTipoDocumentoFilter = (f: TipoDocumento) => !f.principali?.length &&
    !this.tipiDocumento.some(c => c.codice === f.codice)

  get isNuovo(): boolean {
    return !this.loadedData?.codice;
  }

  getDescrizioneTab(dTabs: TabsDettaglio): string {
    if (dTabs.codice) {
      return this.dettTabs.find(candidate => candidate.codice === dTabs.codice)?.descrizione ?? dTabs.descrizione ?? 'Dettaglio tab';
    }
    return dTabs.descrizione ?? 'Dettaglio tab';
  }


  checkDimensioniImmagine(): ValidatorFn {

    return (input: AbstractControl): { [key: string]: any } | null => {

        if (this.imgHeight <= this.maxNumOfPixels && this.imgWidth <= this.maxNumOfPixels && this.imgPixels <= this.maxSize) {
          return null;
        } else {
          return {
            dimensioniMassime: (this.imgHeight > this.maxNumOfPixels || this.imgWidth > this.maxNumOfPixels),
            numeroPixel: this.imgPixels > this.maxSize,
          };
        }
    };
  }



  validateCheckboxes(interfaccia: string, servizio: string): ValidatorFn {

    return (input: AbstractControl): { [key: string]: any } | null => {
      const group = input as FormGroup;
      if (group.controls) {
        const daInt = group.controls[interfaccia];
        const daServ = group.controls[servizio];
        if (daInt.value || daServ.value) {
          return null;
        } else {
          return {
            creabileDaInterfaccia: true,
            creabileDaServizio: true,
          };
        }
      }
      return null;
    };
  }

  isValid(): boolean {
    const raw = this.form?.getRawValue();

    if (raw?.ente?.ente?.id) {
      return this.allValid;
    }
    return false;
  }



  onChange(event: any) {
    if (event?.target?.files != null){
      const file = event.target.files;
      const mimeType = file[0].type;
      if (file.length === 0 || mimeType.match(/image\/*/) == null) {
       this.form?.get('immagine')?.markAsTouched();
      }
      const reader = new FileReader();
      reader.readAsDataURL(file[0]);
      reader.onload = () => {
        if (reader.result) {
          const Img = new Image();
          Img.src = URL.createObjectURL(file[0]);
          const encodeBase64Img = Utils.getEncodedBase64ValueFromArrayBufferString(reader.result.toString());
          this.imgURL = 'data:image/png;base64,' + encodeBase64Img;
          Img.onload = (e: any) => {
              const path = e.path || (e.composedPath && e.composedPath());
              this.imgHeight = path[0].height;
              this.imgWidth = path[0].width;
              this.imgPixels = file[0].size;
              this.form?.get('immagine')?.patchValue(encodeBase64Img);
              this.form?.get('immagine')?.markAsTouched();
              this.form?.get('immagine')?.updateValueAndValidity();
            };
        }
      };
    }

  }

  protected loadData(routeParams: any, inputData?: any): Observable<TipoPratica | null> {
    const idRoute = routeParams?.codice ?? undefined;
    return forkJoin({
      tipoPratica: idRoute ? this.tipiPraticheService.get(idRoute) : of(null),
      maxSize: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.LOGO_MAX_SIZE),
      maxPixel: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.PREFERENZE_ENTE.LOGO_MAX_NUM_OF_PIXELS),
    }).pipe(
      map(
        loaded => {
          this.trasformazioni = (loaded.tipoPratica?.trasformazioni ?? []);
          this.sortTrasformazioni();
          this.stati = (loaded.tipoPratica?.stati ?? [])
            .sort((e1, e2) => (e1.descrizione ?? '')?.localeCompare((e2.descrizione ?? '')));
          this.tipiDocumento = (loaded.tipoPratica?.tipiDocumento ?? [])
            .sort((e1, e2) => (e1.descrizione ?? '')?.localeCompare((e2.descrizione ?? '')));
          this.dettTabs = (loaded.tipoPratica?.tabsDettaglio ?? []).sort((a, b) => (a.ordine ?? 0) - (b.ordine ?? 0));
          this.maxSize = +(loaded.maxSize ?? 0);
          this.maxNumOfPixels = +(loaded.maxPixel ?? 0);
          return loaded.tipoPratica;
        })
    );
  }


  protected sortTrasformazioni(): void {
    this.trasformazioni.sort((t1, t2) => {
      const v1 = (t1.codiceFase === 'beforeProcessStart' ? - 1000000 : + 100000) + (t1.ordine ?? 0);
      const v2 = (t2.codiceFase === 'beforeProcessStart' ? - 1000000 : + 100000) + (t2.ordine ?? 0);
      return v1 - v2;
    });
  }


  protected buildForm(routeParams: any, inputData?: void, loadedData?: TipoPratica | null): FormGroup {
    this.imgPixels = 0;
    this.imgHeight = 0;
    this.imgWidth = 0;
    if (loadedData?.immagine){
      this.imgURL = 'data:image/png;base64,' + loadedData?.immagine;
    }
    const form = this.creaForm(loadedData);
    form.controls?.responsabileTrattamentoStardas.valueChanges.subscribe(respValue => {
      if (respValue) {
        form.controls?.overrideResponsabileTrattamento.enable();
      } else {
        form.controls?.overrideResponsabileTrattamento.disable();
        form.patchValue({ overrideResponsabileTrattamento: false });
      }
    });

    form.controls?.codiceFruitoreStardas.valueChanges.subscribe(respValue => {
      if (respValue) {
        form.controls?.overrideFruitoreDefault.enable();
      } else {
        form.controls?.overrideFruitoreDefault.disable();
        form.patchValue({ overrideFruitoreDefault: false });
      }
    });

    form.controls?.ente.valueChanges.subscribe(respValue => {
      if (this.hiddenCreazioneGruppo()) {
        form.patchValue({ gruppoCreatore: null });
      }
      if ( this.hiddenCreazioneGruppoSupervisore()) {
        form.patchValue({ gruppoSupervisore: null });
      }
    });

    if (this.principal?.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
      this.isConfiguratore = true;
      if (this.isNuovo){
        form?.controls?.ente.patchValue({ente: this.principal?.ente});
      }
    }

    this.logger.debug('build' + form.controls?.ente);

    this.aggiungiImpostazioniFirma(loadedData);

    return form;
  }

  private creaForm(loadedData?: TipoPratica | null): FormGroup{
    return new FormGroup({
      codice: new FormControl({ value: loadedData?.codice ?? '', disabled: !this.isNuovo }, [
        Validators.required,
        Validators.maxLength(255),
        Validators.pattern(Constants.PATTERNS.CODICE),
      ], [
        this.checkConflictingField('codice')
      ]),
      codiceApplicazioneStardas: new FormControl({ value: loadedData?.codiceApplicazioneStardas ?? '', disabled: false }, [
        Validators.maxLength(50),
        Validators.pattern(Constants.PATTERNS.CODICE),
      ]),
      processDefinitionKey: new FormControl({ value: loadedData?.processDefinitionKey ?? '', disabled: false }, [
        Validators.maxLength(255),
        Validators.pattern(Constants.PATTERNS.CODICE),
      ]),
      caseDefinitionKey: new FormControl({ value: loadedData?.caseDefinitionKey ?? '', disabled: false }, [
        Validators.maxLength(255),
        Validators.pattern(Constants.PATTERNS.CODICE),
      ]),
      responsabileTrattamentoStardas: new FormControl({ value: loadedData?.responsabileTrattamentoStardas ?? '', disabled: false }, [
        Validators.maxLength(100),
        Validators.pattern(Constants.PATTERNS.CODICE_FISCALE_UTENTE),
      ]),
      codiceFruitoreStardas: new FormControl({ value: loadedData?.codiceFruitoreStardas ?? '', disabled: false }, [
        Validators.maxLength(100),
      ]),
      registrazioneStilo: new FormControl({ value: loadedData?.registrazioneStilo ?? '', disabled: false }, [
        Validators.maxLength(10),
      ]),
      tipoUnitaDocumentariaStilo: new FormControl({ value: loadedData?.tipoUnitaDocumentariaStilo ?? '', disabled: false }, [
        Validators.maxLength(100),
      ]),
      descrizione: new FormControl({ value: loadedData?.descrizione ?? '', disabled: false }, [
        Validators.required,
        Validators.maxLength(500),
      ]),
      ente: new FormControl({value: {ente: loadedData?.ente}, disabled: false}, [
        Validators.required
      ]),
      creabileDaInterfaccia: new FormControl({ value: loadedData?.creabileDaInterfaccia ?? true, disabled: false }, [
        this.validateCheckboxes('creabileDaInterfaccia', 'creabileDaServizio')
      ]),
      creabileDaServizio: new FormControl({ value: loadedData?.creabileDaServizio ?? true, disabled: false }, [
        this.validateCheckboxes('creabileDaInterfaccia', 'creabileDaServizio')
      ]),
      overrideResponsabileTrattamento: new FormControl({
        value: loadedData?.overrideResponsabileTrattamento ?? false,
        disabled: !loadedData?.responsabileTrattamentoStardas ?? false
      }, []),
      overrideFruitoreDefault: new FormControl({
        value: loadedData?.overrideFruitoreDefault ?? false,
        disabled: !loadedData?.codiceFruitoreStardas ?? false
      }, []),
      customForm: new FormControl({ value: loadedData?.customForm?.codice, disabled: false }),
      immagine: new FormControl({ value: loadedData?.immagine ?? '', disabled: false }, [this.checkDimensioniImmagine()]),
      annullabile: new FormControl({ value: loadedData?.annullabile ?? true, disabled: false }, []),
      condivisibile: new FormControl({ value: loadedData?.condivisibile ?? true, disabled: false }, []),
      assegnabile: new FormControl({ value: loadedData?.assegnabile ?? true, disabled: false }, []),
      gruppoCreatore: new FormControl({ value: loadedData?.gruppoCreatore?.id, disabled: false }, []),
      gruppoSupervisore: new FormControl({ value: loadedData?.gruppoSupervisore?.id, disabled: false }, [])
    }, [
      this.requireOneOfCheckbox(['creabileDaInterfaccia', 'creabileDaServizio']),
      this.requireOneOf(['processDefinitionKey', 'caseDefinitionKey']),
      this.secondValidIfFirstHasValue('codiceApplicazioneStardas', 'responsabileTrattamentoStardas')
    ]);
  }

  aggiungiImpostazioniFirma(loadedData?: TipoPratica | null){
    setTimeout(() => {
      if (this.impostazioniFirmaComp){
        this.impostazioniFirmaComp.enteCertificatore =  loadedData?.enteCertificatore ?? null;
        this.impostazioniFirmaComp.tipoCredenzialiFirma = loadedData?.tipoCredenziale ?? null;
        this.impostazioniFirmaComp.tipoOTP = loadedData?.tipoOtp ?? null;
        this.impostazioniFirmaComp.profiloFEQ = loadedData?.profiloFEQ ?? null;
        this.impostazioniFirmaComp.sceltaMarcaTemporale = loadedData?.sceltaMarcaTemporale ?? null;
        this.impostazioniFirmaComp.setValoreSelezionatoImpFirma();
      }}, 0);
  }

  protected buildPayload(formValue: any): CreaTipoPraticaRequest | AggiornaTipoPraticaRequest {

    if (this.isNuovo) {
      return {
        codice: formValue?.codice,
        ...this.buildCommonPayload(formValue)
      } as CreaTipoPraticaRequest;
    } else {
      return {
        ...this.buildCommonPayload(formValue)
      } as AggiornaTipoPraticaRequest;
    }
  }

  private buildCommonPayload(formValue: any): Partial<CreaTipoPraticaRequest> | Partial<AggiornaTipoPraticaRequest> {
    return {
      descrizione: formValue?.descrizione,
      idEnte: formValue?.ente?.ente?.id,
      codiceApplicazioneStardas: formValue?.codiceApplicazioneStardas,
      processDefinitionKey: formValue?.processDefinitionKey,
      caseDefinitionKey: formValue?.caseDefinitionKey,
      creabileDaInterfaccia: formValue?.creabileDaInterfaccia ? true : false,
      creabileDaServizio: formValue?.creabileDaServizio ? true : false,
      annullabile: formValue?.annullabile ? true : false,
      condivisibile: formValue?.condivisibile ? true : false,
      assegnabile: formValue?.assegnabile ? true : false,
      registrazioneStilo: formValue?.registrazioneStilo,
      tipoUnitaDocumentariaStilo: formValue?.tipoUnitaDocumentariaStilo,
      stati: (this.stati ?? []).map(s => ({
        codice: s.codice,
        descrizione: Utils.require(s.descrizione),
        classe: s.classe,
      })),
      tipiDocumento: this.buildPayloadTipiDocumento(this.tipiDocumento),
      tabsDettaglio: (this.dettTabs ?? []).map(s => ({
        codice: s.codice,
        descrizione: s.descrizione,
        ordine: this.dettTabs.indexOf(s) + 1,

      })),

      trasformazioni: this.buildPayloadTrasformazioni(this.trasformazioni),

      codiceCustomForm: formValue?.customForm?.entity?.codice,
      responsabileTrattamentoStardas: formValue?.responsabileTrattamentoStardas,
      codiceFruitoreStardas: formValue?.codiceFruitoreStardas,
      overrideResponsabileTrattamento: formValue?.overrideResponsabileTrattamento ? true : false,
      overrideFruitoreDefault: formValue?.overrideFruitoreDefault ? true : false,
      idGruppoCreatore: formValue?.gruppoCreatore?.entity?.id,
      idGruppoSupervisore: formValue?.gruppoSupervisore?.entity?.id,
      immagine: formValue?.immagine,

      codiceEnteCertificatore: this.impostazioniFirmaComp.enteCertificatore?.codice,
      codiceTipoCredenziale: this.impostazioniFirmaComp.tipoCredenzialiFirma?.codice,
      codiceTipoOtp: this.impostazioniFirmaComp.tipoOTP?.codice,
      codiceProfiloFEQ: this.impostazioniFirmaComp.profiloFEQ?.codice,
      codiceSceltaMarcaTemporale: this.impostazioniFirmaComp.sceltaMarcaTemporale?.codice
    };
  }

  private buildPayloadTipiDocumento(input: TipoDocumento[]): AggiornaTipoPraticaDocumentoRequest[] {
    return (input ?? []).map(s => ({
      codice: s.codice,
      descrizione: Utils.require(s.descrizione),
      codiceStardas: s.codiceStardas,
      firmabile: s.firmabile,
      dimensioneMassima: s.dimensioneMassima,
      allegati: s.allegati?.length ? this.buildPayloadTipiDocumento(s.allegati ?? []) : [],
      formatiFile: s.formatiFile ?? []
    }));
  }

  private buildPayloadTrasformazioni(input: TrasformazioneDatiPratica[]): AggiornaTipoPraticaTrasformazioneDatiRequest[] {
    return (input ?? []).map(s => ({
      id: s.id,
      codiceFase: s.codiceFase,
      definizione: s.definizione,
      descrizione: s.descrizione,
      obbligatoria: s.obbligatoria,
      ordine: s.ordine,
    }));
  }

  protected onSubmit(payload: CreaTipoPraticaRequest | AggiornaTipoPraticaRequest): Observable<any> {
    let out: Observable<TipoPratica> | null = null;
    const crea = this.isNuovo;
    if (crea) {
      // crea nuovo
      out = this.tipiPraticheService.create(payload as CreaTipoPraticaRequest);
    } else {
      // modifica esistente
      out = this.tipiPraticheService.update(
        Utils.require(this.loadedData?.codice), payload as AggiornaTipoPraticaRequest);
    }

    return out.pipe(
      delay(environment.httpMockDelay),
      tap(result => {
        let messaggio = this.translateService.instant(
          'tipi_pratiche.dialogs.' + (crea ? 'creato' : 'modificato') + '.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.descrizione ?? '']);
        this.modalService.info(this.translateService.instant(
          'tipi_pratiche.dialogs.' + (crea ? 'creato' : 'modificato') + '.titolo'),
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

  eliminaImmagine(){
    this.imgURL = null;
    this.imgHeight = 0;
    this.imgPixels = 0;
    this.imgWidth = 0;
    this.form?.get('immagine')?.patchValue('');
    this.form?.get('immagine')?.updateValueAndValidity();
  }

  checkConflictingField(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{ [key: string]: any } | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'codice,descrizione,' + fieldName,
        filter: { id: this.loadedData ? { ne: this.loadedData.codice } : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.tipiPraticheService.getTipiPratica(filter)),
        map(response => {
          const conflicting = response.tipiPratiche?.find(candidate =>
            (!Utils.isDefined(this.loadedData?.codice) || this.loadedData?.codice !== candidate.codice) &&
            ((candidate as any)[fieldName] === v)
          );

          if (conflicting) {
            return {
              conflict: { field: fieldName, other: conflicting }
            };
          }
          return null;
        })
      );
    };
  }

  copied(controlName: string): boolean {
    return controlName === this.codiceCopied;
  }

  copy(controlName: string): void {
    if (!this.loadedData?.codice?.length) {
      return;
    }
    this.clipboardService.copy(this.getValue(controlName));
    this.codiceCopied = controlName;
    setTimeout(() => this.codiceCopied = undefined, 3000);
  }

  requireOneOfCheckbox(controlNames: string[]): ValidatorFn {

    return (input: AbstractControl): { [key: string]: any } | null => {
      if (!this.form) {
        return null;
      }

      const controls = controlNames.map(n => this.getControl(n));
      const numValues = controls.filter(c => !(c as FormControl).value).length;
      const atLeastOneDirty = !!controls.find(c => c?.touched);

      let out: ValidationErrors | null = null;

      if (numValues > 1) {
        out = { requireOneOfCheckbox: { none: false, tooMany: true } };
      }

      controls.forEach(c => {
        if (atLeastOneDirty) {
          c?.markAsDirty();
        }
        c?.updateValueAndValidity({ onlySelf: true, emitEvent: false });
      });

      if (out != null) {
        controls.forEach(c => {
          c?.setErrors({ ...(c.errors ?? {}), ...out });
        });
      }

      return out;
    };

  }

  requireOneOf(controlNames: string[]): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {
      if (!this.form) {
        return null;
      }

      const controls = controlNames.map(n => this.getControl(n));
      const numValues = controls.filter(c => Utils.isNotBlank((c as FormControl).value)).length;
      const atLeastOneDirty = !!controls.find(c => c?.touched);

      let out: ValidationErrors | null = null;

      if (!numValues) {
        out = { requireOneOf: { none: true, tooMany: false } };
      } else if (numValues > 1) {
        out = { requireOneOf: { none: false, tooMany: true } };
      }

      controls.forEach(c => {
        if (atLeastOneDirty) {
          c?.markAsDirty();
        }
        c?.updateValueAndValidity({ onlySelf: true, emitEvent: false });
      });

      if (out != null) {
        controls.forEach(c => {
          c?.setErrors({ ...(c.errors ?? {}), ...out });
        });
      }

      return out;
    };
  }

  secondValidIfFirstHasValue(first: string, second: string): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {
      if (!this.form || !first || !second) {
        return null;
      }
      const firstControl = this.getControl(first);
      const secondControl = this.getControl(second);

      let out: ValidationErrors | null = null;

      if (secondControl?.touched) {
        secondControl.markAsDirty();
      }
      firstControl?.updateValueAndValidity({ onlySelf: true, emitEvent: false });
      secondControl?.updateValueAndValidity({ onlySelf: true, emitEvent: false });

      if (!firstControl?.value && secondControl?.value) {
        out = { secondValidIfFirstHasValue: true };
        secondControl?.setErrors({ ...(secondControl?.errors ?? {}), ...out });
      }

      return out;
    };
  }

  modificaStato(entity: StatoPratica): void {
    const modal = this.buildModaleModificaStatoPratica(entity, false);

    modal.result.then((done: StatoPratica) => {
      Object.assign(entity, { ...done });
    }).catch(() => { });
  }

  eliminaStato(entity: StatoPratica): void {
    this.stati = this.stati.filter(c => c.codice !== entity.codice);
  }

  modificaTipoDocumento(entity: TipoDocumento): void {
    const modal = this.buildModaleModificaTipoDocumento(entity, false);

    modal.result.then((done) => {
      Object.assign(entity, { ...done.payload });
    }).catch(() => { });
  }

  eliminaTipoDocumento(entity: TipoDocumento): void {
    this.tipiDocumento = this.tipiDocumento.filter(c => c.codice !== entity.codice);
  }

  statoPraticaSelezionato(selezione: SelezioneEntity<StatoPratica>): void {
    if (!selezione.entity) {
      return;
    }
    setTimeout(() => this.ricercaStatoPraticaInput?.clear(), 200);

    if (selezione.nuovo) {
      const modal = this.buildModaleModificaStatoPratica(selezione.entity, true);
      modal.result.then(done => {
        this.stati.push(done);
      }).catch(() => { });
    } else {
      this.stati.push(selezione.entity);
    }
  }


  tipoDocumentoSelezionato(selezione: SelezioneEntity<TipoDocumento>): void {
    if (!selezione.entity) {
      return;
    }

    setTimeout(() => this.ricercaTipoDocumentoInput?.clear(), 200);


    if (selezione.nuovo) {
      this.ricercaTipoDocumentoInput?.setDisabled(true);
      const modal = this.buildModaleModificaTipoDocumento(selezione.entity, true);
      this.apriModaleTipoDocumento(modal);
    } else {
      this.tipiDocumento.push(selezione.entity);
    }
  }


  apriModaleTipoDocumento(modale: NgbModalRef): void {
    modale.result.then(done => {
      this.tipiDocumento.push(done);
      this.ricercaTipoDocumentoInput?.setDisabled(false);
    },
    (reason) => {
      if (ModalStatus.CONTINUA === reason.type) {
        this.apriModaleTipoDocumento(reason.done);
      } else {
        this.ricercaTipoDocumentoInput?.setDisabled(false);
      }

    }).catch(() => { });
  }



  private buildModaleModificaStatoPratica(entity: StatoPratica, isNuovo: boolean): NgbModalRef {
    const modal = this.modal.open(ModificaStatoPraticaModalComponent, {
      backdrop: true, keyboard: true, size: 'lg'
    });
    const comp = modal.componentInstance as ModificaStatoPraticaModalComponent;
    comp.input = () => of(entity);
    comp.isNuovo = isNuovo;
    comp.stati = this.stati;

    return modal;
  }

  private buildModaleModificaTipoDocumento(entity: TipoDocumento, isNuovo: boolean): NgbModalRef {
    this.ricercaTipoDocumentoInput?.nativeControl?.nativeElement.blur();
    const modal = this.modal.open(ModificaTipoDocumentoModalComponent, {
      backdrop: true, keyboard: true, size: 'lg'
    });
    const comp = modal.componentInstance as ModificaTipoDocumentoModalComponent;
    comp.input = () => of(entity);
    comp.isNuovo = isNuovo;
    comp.tipiDocumento = this.tipiDocumento;
    comp.entityPadre = entity;
    comp.codiceTipoPratica = this.form?.getRawValue()?.codice;
    return modal;
  }

  private buildModaleModificaTrasformazione(entity: TrasformazioneDatiPratica, isNuovo: boolean): NgbModalRef {
    const modal = this.modal.open(ModificaTrasformazioneModalComponent, {
      backdrop: true, keyboard: true, size: 'lg'
    });
    const comp = modal.componentInstance as ModificaTrasformazioneModalComponent;
    comp.input = () => of(entity);
    comp.isNuovo = isNuovo;

    return modal;
  }

  nuovaTrasformazione(): void {
    const codiceFase = 'beforeProcessStart';
    const entity: TrasformazioneDatiPratica = {
      id: 0,
      codiceTipoPratica: this.loadedData?.codice ?? '',
      definizione: '',
      codiceFase,
      ordine: (Math.max.apply(
        Math,
        this.trasformazioni.filter(x => x.codiceFase === codiceFase).map(o => o.ordine ?? 0)) + 1) ?? 1,
    };

    const modal = this.buildModaleModificaTrasformazione(entity, false);

    modal.result.then((done: TrasformazioneDatiPratica) => {
      Object.assign(entity, { ...done });
      this.trasformazioni.push(entity);
      this.sortTrasformazioni();
    }).catch(() => { });
  }

  modificaTrasformazione(entity: TrasformazioneDatiPratica): void {
    const modal = this.buildModaleModificaTrasformazione(entity, false);

    modal.result.then((done: TrasformazioneDatiPratica) => {
      Object.assign(entity, { ...done });
    }).catch(() => { });
  }

  eliminaTrasformazione(entity: TrasformazioneDatiPratica): void {
    this.trasformazioni = this.trasformazioni.filter(c => c !== entity);
  }

  eliminaDettTabs(indice: number): void {
    this.modalService.scegli(
      this.translateService.instant('tipi_pratiche.dialogs.elimina.tab_titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), ['questo tab']),
        classe: 'text-danger'
      }], [
      { testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt' },
      { testo: 'Annulla', dismiss: true, defaultFocus: true }
    ]
    ).then(() => {

      this.dettTabs.splice(indice, 1);
      this.markDirty();

    }).catch(() => { });
  }

  drop(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(this.dettTabs, event.previousIndex, event.currentIndex);

    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
  }

  tabSelezionatoChanged(event: NgbTypeaheadSelectItemEvent) {
    const sel = event.item?.entity;
    if (!!sel?.codice && !this.tabGiaPresente(sel)) {
      setTimeout(() => this.ricercaDettaglioTabs?.clear(), 200);
      this.markDirty();

      this.dettTabs.push(sel);
      this.selezioneNuovoTab = null;
    }
  }

  tabGiaPresente(dTabs: TabsDettaglio): boolean {
    if (!dTabs?.codice || !this.dettTabs?.length) {
      return false;
    }
    return !!this.dettTabs.find(candidate => candidate.codice === dTabs.codice);
  }

  tabNonAncoraAssociati = (input: SelezioneEntity<TabsDettaglio>[]) => {
    return (input || []).filter(i => !(i.entity?.codice?.length) ||
      !this.dettTabs.find(c => c.codice === i.entity?.codice));
  }

  get idEnte() {
    return this.form?.getRawValue().ente?.ente?.id;
  }

  hiddenCreazioneGruppo() {
    if (this.idEnte && this.form?.getRawValue().creabileDaInterfaccia) {
      return false;
    }
    return true;
  }

  hiddenCreazioneGruppoSupervisore(){
    if (this.idEnte) {
      return false;
    }
    return true;
  }

  aggiornaCreatore() {
    if (!!this.form?.getRawValue().creabileDaInterfaccia) {
      this.form?.patchValue({ gruppoCreatore: null });
    }
  }

  setValoreSelezionato(impostazioniFirma: any) {
    this.impostazioniFirmaComp.enteCertificatore = impostazioniFirma.enteCertificatore ?? null;
    this.impostazioniFirmaComp.tipoCredenzialiFirma = impostazioniFirma.tipoCredenzialiFirma ?? null;
    this.impostazioniFirmaComp.tipoOTP = impostazioniFirma.tipoOTP ?? null;
    this.impostazioniFirmaComp.profiloFEQ = impostazioniFirma.profiloFEQ ?? null;
    this.impostazioniFirmaComp.sceltaMarcaTemporale = impostazioniFirma.sceltaMarcaTemporale ?? null;
    this.impostazioniFirmaComp.setValoreSelezionatoImpFirma();
  }

}
