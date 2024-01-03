/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, Validators, FormControl, AbstractControl, AsyncValidatorFn } from '@angular/forms';
import { EntiService } from 'src/app/shared/services/enti.service';
import { combineLatest, forkJoin, Observable, of, Subject, merge } from 'rxjs';
import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { Utils } from 'src/app/shared/utilities/utilities';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { SecurityService } from 'src/app/shared/services/security.service';
import { debounceTime, delay, distinctUntilChanged, filter, map, mergeMap, switchMap} from 'rxjs/operators';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { RicercaUtenteComponent, SelezioneUtenteGruppo } from 'src/app/shared/components/ricerca-utente/ricerca-utente.component';
import { environment } from 'src/environments/environment';
import { UtentiService } from 'src/app/shared/services/utenti.service';
import { Constants } from 'src/app/shared/constants/constants';
import { CreaEnteRequest } from 'src/app/shared/models/api/cosmoauthorization/creaEnteRequest';
import { AggiornaEnteRequest } from 'src/app/shared/models/api/cosmoauthorization/aggiornaEnteRequest';
import { Profilo } from 'src/app/shared/models/api/cosmoauthorization/profilo';
import { ProfiliService} from 'src/app/shared/services/profili.service';
import { FilterPipe } from 'src/app/shared/pipes/filter.pipe';
import { EntiResponse } from 'src/app/shared/models/api/cosmoauthorization/entiResponse';
import { invalid } from 'moment';
import { Utente } from 'src/app/shared/models/api/cosmoauthorization/utente';
import { UtenteFilter } from 'src/app/gestione-utenti/utente-filter.pipe';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection,
  ICosmoTableColumn, ICosmoTablePageRequest, ICosmoTablePageResponse,
  ICosmoTableReloadContext, ICosmoTableStoreAdapter,
  ICosmoTableStoreAdapterSaveContext } from 'ngx-cosmo-table';
import { request } from 'http';

/*
 * ATTENZIONE - TODO - c'e' un sacco di boilerplate in questo componente.
 * occorre estendere AbstractReactiveFormComponent e rimuovere il codice comune,
 * come esempio si puo' usare aggiungi-modifica-form-logico.component.ts
 * oppure modifica-endpoint-fruitore.component.ts
 */
export interface UtenteAmministratore {
  id?: number;
  codiceFiscale?: string;
  nome?: string;
  cognome?: string;
  email?: string;
  telefono?: string;
}

@Component({
  selector: 'app-aggiungi-modifica-ente',
  templateUrl: './aggiungi-modifica-ente.component.html',
  styleUrls: ['./aggiungi-modifica-ente.component.scss']
})

export class AggiungiModificaEnteComponent implements OnInit, ComponentCanDeactivate {

  @ViewChild('instanceProfili', { static: true }) instanceProfili!: NgbTypeahead;
  focusProfili$ = new Subject<string>();
  clickProfili$ = new Subject<string>();

  @ViewChild('table') table: CosmoTableComponent | null = null;
  @ViewChild('prov') prov: CosmoTableComponent | null = null;

  @ViewChild('elemento') elemento: ElementRef | null = null;

  debug = environment.debug;
  idEnte?: number | null = null;
  enteForm!: FormGroup;
  ente?: Ente | null = null;
  profilo?: Profilo | null = null;
  dragAndDropIsTouched = false;
  principal?: UserInfoWrapper;
  profili: Profilo[] = [];
  amministratori: UtenteAmministratore[] = [];
  amministratoreDaModificare: UtenteAmministratore | null = null;
  initAmministratorePresente = false;
  utenteSelezionato?: SelezioneUtenteGruppo | null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private entiService: EntiService,
    private profiliService: ProfiliService,
    private utentiService: UtentiService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private securityService: SecurityService,
  ) {}

  loading = 0;
  loadingError: any = null;


columnsProv: ICosmoTableColumn[] = [{
    label: 'Nome', field: 'nome', canSort: false,
  }, {
    label: 'Cognome', field: 'cognome', canSort: false,
  }, {
    label: 'Email', field: 'email', canSort: false,
  }, {
    label: 'Telefono', field: 'telefono', canSort: false,
  }, {
    name: 'azioni', label: 'Azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.XXS
  },
];

columns: ICosmoTableColumn[] = [{
  label: 'Nome', field: 'nome', canSort: false,
}, {
  label: 'Cognome', field: 'cognome', canSort: false,
}, {
  label: 'Email', field: 'email', canSort: false,
}, {
  label: 'Telefono', field: 'telefono', canSort: false,
}, {
  name: 'azioni',
  canHide: false,
  canSort: false,
  applyTemplate: true,
  size: CosmoTableColumnSize.XXS
},
];

// store adapter - persistenza dello stato della tabella nella url query
storeAdapter: ICosmoTableStoreAdapter = {
  save: (payload: ICosmoTableStoreAdapterSaveContext | null) => {
    this.router.navigate(
      [],
      {
        relativeTo: this.route,
        queryParams: {
          page: payload?.status?.currentPage,
          size: payload?.status?.pageSize,
          q: payload?.status?.query,
        },
        queryParamsHandling: 'merge',
      });
    return of(true);
  },
  load: () => {
    return this.route.queryParams.pipe(
      map(params => {
        const out = {
          currentPage: params.page ? parseInt(params.page, 10) : undefined,
          pageSize: params.size ? parseInt(params.size, 10) : undefined,
          query: params.q,
        } as any;
        return out;
      })
    );
  }
};

refresh(inBackground = false) {
  if (this.table) {
    this.table.refresh(inBackground);
  }
}


  ricercaUtenteFilter = (filtro: any) => {
    const admins = this.amministratori.filter(val => val.codiceFiscale  && (!this.amministratoreDaModificare || 
      val.codiceFiscale !== this.amministratoreDaModificare?.codiceFiscale)).map(elem => elem.codiceFiscale);

    if (admins.length === 0){
      return filtro;
    }

    filtro.codiceFiscale = filtro.codiceFiscale || {};
    filtro.codiceFiscale.nin = [...(filtro.codiceFiscale.nin ?? []), ...admins];
    return filtro;
  }

  formatterProfilo = (result: Profilo) => result.descrizione;

  searchProfili = (text$: Observable<string>) => {

    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());

    const clicksWithClosedPopup$ = this.clickProfili$.pipe(
      filter(() => this.instanceProfili && !this.instanceProfili.isPopupOpen()),
      map(o => null)
    );

    const inputFocus$ = this.focusProfili$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      switchMap((searchText) => {
        if (!searchText || searchText.length === 0) {
          return of(this.profili);
        } else {
          const profiliFiltrati = this.profili.filter(v => v.descrizione &&
            v.descrizione.toLowerCase().indexOf(searchText.toLowerCase()) > -1)
            .sort((c1, c2) => (c1.descrizione ?? '').localeCompare(c2.descrizione ?? '')).slice(0, 10);
          return of(profiliFiltrati);
        }
      })
    );
  }


  // @HostListener allows us to also guard against browser refresh, close, etc.
  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    // insert logic to check if there are pending changes here;
    // returning true will navigate without confirmation
    // returning false will show a confirm dialog before navigating away
    if (this.dragAndDropIsTouched || this.enteForm?.dirty) {
      return false;
    }
    return true;
  }

  get isNuovo(): boolean {
    return !this.ente;
  }

  checkProfilo(val: string) {
    const raw = this.enteForm.getRawValue();
    const control = this.getControl('profilo');
    if (val && !raw.profilo) {
      control?.setErrors({invalid: true});
    }else{
      control?.setErrors(null);
    }
  }

  ngOnInit(): void {
    if (this.isNuovo){
      this.getProfili(null);
    }
    combineLatest([this.securityService.getCurrentUser(), this.route.params]).pipe(
      map(latest => {
        return {
          principal: latest[0],
          idEnte: latest[1]?.id ? +latest[1].id : undefined,
        };
      }),
      mergeMap(data => {
        return forkJoin({
          enteCurrent: data.idEnte ? this.entiService.getEnte(data.idEnte) : of(null),
        }).pipe(
          map(moreData => {
            return {
              ...data,
              ...moreData
            };
          })
        );
      })
    ).subscribe(loaded => {
      this.principal = loaded.principal;
      this.idEnte = loaded.idEnte ?? undefined;
      if (loaded.enteCurrent) {
        this.ente = loaded.enteCurrent.ente as Ente;

        if (this.ente) {
          if (this.ente.codiceProfiloDefault && this.ente.codiceProfiloDefault !== ''){
            this.getProfili(this.ente.codiceProfiloDefault);
          }else{
            this.getProfili(null);
            this.initForm();
          }
        }
      } else {
        this.getProfili(null);
        this.ente = undefined;
        this.initForm();
      }
    });

    this.route.params.subscribe(() => {
        this.refresh();
      });

    this.prov?.refresh(false);
  }

  private initForm() {
    let nome = '';
    let codiceIpa = '';
    let codiceFiscale = '';

    if (this.ente) {
      nome = this.ente.nome;
      codiceIpa = this.ente.codiceIpa ?? '';
      codiceFiscale = this.ente.codiceFiscale ?? '';
    }

    this.enteForm = new FormGroup({
      anagrafica: new FormGroup({
        nome: new FormControl({value: nome, disabled: false}, [
          Validators.required,
          Validators.maxLength(255),
        ], [
          this.checkConflictingFieldEnte('nome', 'eqic')
        ]),
        codiceIpa: new FormControl({value: codiceIpa, disabled: !this.isNuovo}, [
          Validators.required,
          Validators.maxLength(10),
        ], [
          this.checkConflictingFieldEnte('codiceIpa', 'eqic')
        ]),
        codiceFiscale: new FormControl({value: codiceFiscale, disabled: !this.isNuovo}, [
          Validators.required,
          Validators.pattern(Constants.PATTERNS.CODICE_FISCALE_ENTE),
          Validators.maxLength(16),
        ], [
          this.checkConflictingFieldEnte('codiceFiscale', 'eqic')
        ]),
      }),
      profilo: new FormControl(this.profilo),
      utenteAmministratore:  new FormGroup({
        utente: new FormControl(null, [], [this.checkUtenteAssociato()]),
        email: new FormControl(null, [Validators.pattern(Constants.PATTERNS.EMAIL)]),
        telefono: new FormControl(null, [Validators.pattern(Constants.PATTERNS.TELEFONO)]),
      })
    });

    (this.enteForm.controls.utenteAmministratore as FormGroup).controls.utente.valueChanges
      .subscribe(v => this.onUtenteAmministratoreChanged(v));


  }

  getProfili(codice: string | null){
    this.profiliService.getProfili(JSON.stringify({
      filter: {
        assegnabile: {
          defined: true,
          eq: true,
        }
      }
    })).subscribe(profili => {
      this.profili = profili.profili as Array<Profilo>;
      if (codice){
        this.profilo = this.profili.filter(v => v.codice === codice)[0];
        this.initForm();
      }
    });
  }


  get utenteValid(): boolean{
    const ctrlName = 'nuovoUtenteAmministratore';
    const nuovoUtenteAmministratoreGroup = this.enteForm.controls[ctrlName];
    const utenteAmministratoreGroup = this.enteForm.controls.utenteAmministratore;
    const raw = this.enteForm.getRawValue();

    if (nuovoUtenteAmministratoreGroup){
      return raw.utenteAmministratore.utente && utenteAmministratoreGroup.valid && nuovoUtenteAmministratoreGroup.valid
            && raw.nuovoUtenteAmministratore.cognome && raw.nuovoUtenteAmministratore.codiceFiscale && raw.utenteAmministratore.email;
    }else{
      return  raw.utenteAmministratore.utente && utenteAmministratoreGroup.valid && raw.utenteAmministratore.email;
    }

  }

  get emailRequired(): boolean{
    const utenteAmministratoreGroup = this.enteForm.controls.utenteAmministratore;
    const raw = this.enteForm.getRawValue();

    return raw.utenteAmministratore.utente && utenteAmministratoreGroup.valid && !raw.utenteAmministratore.email;

  }

  get isAlmenoUnAmministratore(): boolean{
    return this.initAmministratorePresente || this.amministratori.length !== 0;
  }



  onUtenteAmministratoreChanged(newValue: SelezioneUtenteGruppo) {
    const ctrlName = 'nuovoUtenteAmministratore';
    const nuovoUtenteAmministratoreGroup = this.enteForm.controls[ctrlName];
    const shouldBePresent = !!(newValue?.nuovo);
    if (!shouldBePresent && !!nuovoUtenteAmministratoreGroup) {
      this.enteForm.removeControl(ctrlName);
    } else if (shouldBePresent && !nuovoUtenteAmministratoreGroup) {
      this.enteForm.addControl(ctrlName, new FormGroup({
        cognome: new FormControl(null, [
          Validators.required,
          Validators.maxLength(255)
        ]),
        codiceFiscale: new FormControl(null, [
          Validators.required,
          Validators.pattern(Constants.PATTERNS.CODICE_FISCALE_UTENTE),
          Validators.maxLength(16),
        ], [
          this.checkCodiceFiscaleEsistente()
        ])
      }));
    }
    const raw = this.enteForm.getRawValue();

    const utenteEnte = this.utenteSelezionato?.utente?.enti?.find(x => x.ente?.id === this.idEnte);
    if ((!raw.utenteAmministratore?.utente ||
      raw.utenteAmministratore?.utente?.utente?.codiceFiscale !== this.utenteSelezionato?.utente?.codiceFiscale) &&
      utenteEnte?.email === raw.utenteAmministratore.email){
        if (this.amministratoreDaModificare){
          (this.enteForm.controls.utenteAmministratore as FormGroup).controls.email.patchValue(this.amministratoreDaModificare?.email);
        }else{
          (this.enteForm.controls.utenteAmministratore as FormGroup).controls.email.patchValue(null);
        }
    }
    if ((!raw.utenteAmministratore?.utente ||
      raw.utenteAmministratore?.utente?.utente?.codiceFiscale !== this.utenteSelezionato?.utente?.codiceFiscale) &&
    utenteEnte?.telefono === raw.utenteAmministratore.telefono){
      if (this.amministratoreDaModificare){
        (this.enteForm.controls.utenteAmministratore as FormGroup).controls.telefono.patchValue(this.amministratoreDaModificare?.telefono);
      }else{
        (this.enteForm.controls.utenteAmministratore as FormGroup).controls.telefono.patchValue(null);
      }
    }

    if (!raw.utenteAmministratore?.utente || raw.utenteAmministratore.nuovo ||
       raw.utenteAmministratore?.utente?.utente?.codiceFiscale !== this.utenteSelezionato?.utente?.codiceFiscale){
      this.utenteSelezionato = null;
    }
  }


  tornaIndietro() {
    // this.router.navigate(['back'], { relativeTo: this.route });
    window.history.back();
  }

  addUser(){
    const raw = this.enteForm.getRawValue();
    const user: UtenteAmministratore = {
      telefono: raw.utenteAmministratore?.telefono,
      email: raw.utenteAmministratore?.email
    };
    if (raw.utenteAmministratore?.utente.nuovo){
      user.codiceFiscale = raw.nuovoUtenteAmministratore?.codiceFiscale;
      user.nome = raw.utenteAmministratore?.utente?.utente.nome ?? '';
      user.cognome = raw.nuovoUtenteAmministratore?.cognome;
    }else{
      user.id = raw.utenteAmministratore?.utente?.utente.id;
      user.nome = raw.utenteAmministratore?.utente?.utente.nome;
      user.cognome = raw.utenteAmministratore?.utente?.utente.cognome;
      user.codiceFiscale = raw.utenteAmministratore?.utente?.utente.codiceFiscale;
    }
    if (this.amministratoreDaModificare) {
      this.amministratori[this.amministratori.indexOf(this.amministratoreDaModificare)] = user;
    }else{
      this.amministratori.push(user);
    }
    this.amministratoreDaModificare = null;
    this.prov?.refresh(false);
    this.clearValues();
  }

  resetUser(){
    this.amministratoreDaModificare = null;
    this.clearValues();

  }

  modificaUtente(utente: any){
    this.amministratoreDaModificare = utente;
    if (!utente?.id){
      (this.enteForm.controls.utenteAmministratore as FormGroup).controls.utente.patchValue({utente: {nome: utente.nome}, nuovo: true});
      (this.enteForm.controls.nuovoUtenteAmministratore as FormGroup).controls.cognome.patchValue(utente.cognome);
      (this.enteForm.controls.nuovoUtenteAmministratore as FormGroup).controls.codiceFiscale.patchValue(utente.codiceFiscale);
    }else{
      (this.enteForm.controls.utenteAmministratore as FormGroup).controls.utente.patchValue({utente:
        {id: utente.id, nome: utente.nome, cognome: utente.cognome, codiceFiscale: utente.codiceFiscale}, nuovo: false});
    }
    (this.enteForm.controls.utenteAmministratore as FormGroup).controls.email.patchValue(utente.email);
    (this.enteForm.controls.utenteAmministratore as FormGroup).controls.telefono.patchValue(utente.telefono);

  }

  clearValues(){
    this.utenteSelezionato = null;
    (this.enteForm.controls.utenteAmministratore as FormGroup).controls.utente.patchValue(null);
    (this.enteForm.controls.utenteAmministratore as FormGroup).controls.email.patchValue(null);
    (this.enteForm.controls.utenteAmministratore as FormGroup).controls.telefono.patchValue(null);
    if (this.enteForm.controls?.nuovoUtenteAmministratore){
      (this.enteForm.controls?.nuovoUtenteAmministratore as FormGroup).controls.cognome.patchValue(null);
      (this.enteForm.controls?.nuovoUtenteAmministratore as FormGroup).controls.codiceFiscale.patchValue(null);
    }
  }

  eliminaUtente(userToRemove: any){
    const valueToRemove = this.amministratori.filter(x => x.codiceFiscale).find(elem =>
        elem.codiceFiscale === userToRemove.codiceFiscale);
    if (valueToRemove) {
      this.amministratori.splice(this.amministratori.indexOf(valueToRemove), 1);
    }
    this.prov?.refresh(false);

  }


  onSubmit() {
    // e' una modifica dell'utente
    if (this.ente) {
      this.submitAggiorna();
    }
    // e' un nuovo utente
    else {
      this.submitCrea();
    }
  }

  clearDirty(): void {
    this.dragAndDropIsTouched = false;
    if (this.enteForm) {
      this.enteForm.markAsPristine();
    }
  }

  isValid(name: string): boolean {
    if (!this.enteForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.valid;
  }

  isInvalid(name: string): boolean {
    if (!this.enteForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.invalid;
  }

  isTouched(name: string): boolean {
    if (!this.enteForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.touched;
  }

  isDirty(name: string): boolean {
    if (!this.enteForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.dirty;
  }

  isValidating(name: string): boolean {
    if (!this.enteForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control as FormControl).status === 'PENDING';
  }

  displayValidating(name: string): boolean {
    if (!this.enteForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.touched || control.dirty) && (control as FormControl).status === 'PENDING';
  }

  displayInvalid(name: string): boolean {
    if (!this.enteForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.dirty || control.touched) && control.invalid;
  }

  getError(name: string, type: string): any {
    if (!this.enteForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.errors && control.errors[type] ?
      (control.errors[type] ?? true) : null;
  }

  hasError(name: string, type: string): any {
    return this.getError(name, type) !== null;
  }

  hasValue(name: string): boolean {
    if (!this.enteForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return Utils.isNotBlank(control.value);
  }

  getValue(name: string): any {
    if (!this.enteForm) {
      return null;
    }
    const control = this.resolveControl(name) as FormControl;
    return control.value;
  }

  getControl(name: string): AbstractControl | undefined {
    if (!this.enteForm) {
      return undefined;
    }
    return this.resolveControl(name) ?? undefined;
  }

  private resolveControl(name: string): AbstractControl | undefined {
    let actual: any = this.enteForm;
    for (const token of name.split('.')) {
      const newControl = actual.controls[token];
      if (!newControl) {
        return undefined;
      }
      actual = newControl;
    }
    return actual as AbstractControl;
  }


  checkCodiceFiscaleEsistente(): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }
      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,nome,cognome,codiceFiscale',
        filter: {},
      };
      const fieldFilter: any = {};
      fieldFilter.eqic = v;
      requestPayload.filter.codiceFiscale = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(data => this.utentiService.getUtenti(data)),
        map(response => {
          if (response.utenti?.length || this.amministratori.filter(x => x.codiceFiscale).find(elem => elem.codiceFiscale === v &&
            (this.amministratoreDaModificare === null || elem.codiceFiscale !== this.amministratoreDaModificare?.codiceFiscale))){
            return {
              conflict: {
                field: 'codiceFiscale'
              }
            };
          }
          return null;
        })
      );
    };

  }


  checkUtenteAssociato(): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v || !this.idEnte || !v.utente?.codiceFiscale) {
        return of(null);
      }
      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,nome,cognome,codiceFiscale',
        filter: {profili: [ {eq: 'ADMIN'}]},
      };
      const fieldFilter: any = {};
      fieldFilter.eqic = v.utente?.codiceFiscale;
      requestPayload.filter.codiceFiscale = fieldFilter;
      requestPayload.filter.idEnte = {eq: this.idEnte + '' };
      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        mergeMap(data => {
          return forkJoin({
            utenti: this.utentiService.getUtenti(data),
            utenteSelezionato: this.utentiService.getUtenteValidita(v.utente.id, this.idEnte as number)
          });
        }),
        map(response => {
          if (response.utenti?.utenti?.length && (this.amministratoreDaModificare === null ||
            response.utenti.utenti.filter(x => x.codiceFiscale)
            .find(elem => elem.codiceFiscale !== this.amministratoreDaModificare?.codiceFiscale))){
            return {
              conflict: {
                field: 'utente'
              }
            };
          }
          if (this.utenteSelezionato?.utente?.codiceFiscale !== response.utenteSelezionato?.utente?.codiceFiscale &&
            (this.amministratoreDaModificare === null ||
            this.amministratoreDaModificare.codiceFiscale !== response.utenteSelezionato?.utente?.codiceFiscale)){
          response.utenteSelezionato?.utente?.enti.filter(ente => {
            if (ente.ente && ente.ente.id === this.idEnte) {
              (this.enteForm.controls.utenteAmministratore as FormGroup).controls.email.patchValue(ente.email);
              (this.enteForm.controls.utenteAmministratore as FormGroup).controls.telefono.patchValue(ente.telefono);
            }
          });
          this.utenteSelezionato = {utente: response.utenteSelezionato?.utente};
          }

          if (this.elemento){
            this.elemento?.nativeElement.focus();
          }


          return null;
        })
        );
    };

  }


  checkConflictingFieldEnte(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,nome,' + fieldName,
        filter: { id: this.idEnte ? { ne: this.idEnte } : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(data => this.entiService.getEnti(data)),
        map(response => {
          if (response.enti?.length) {
            return {
              conflict: {
                field: fieldName,
                otherValue: (response.enti[0] as any)[fieldName],
                otherId: response.enti[0].id,
                otherName: response.enti[0].nome,
              }
            };
          }
          return null;
        })
      );
    };
  }



  buildPayloadCreazione(): CreaEnteRequest {
    if (!this.enteForm) {
      throw new Error('Form non trovato');
    }
    const raw = this.enteForm.getRawValue();

    const payload: CreaEnteRequest = {
      nome: raw.anagrafica?.nome,
      codiceFiscale: raw.anagrafica?.codiceFiscale,
      codiceIpa: raw.anagrafica?.codiceIpa,
      utentiAmministratori: [],
      nuoviUtentiAmministratori: [],
      codiceProfiloDefault: raw.profilo?.codice
    };

    this.amministratori.forEach(
      value => {
        if (value.id){
          payload?.utentiAmministratori?.push({id: value?.id, telefono: value?.telefono, email: value?.email});
        }else{
          payload?.nuoviUtentiAmministratori?.push({nome: value?.nome, cognome: value?.cognome,
            codiceFiscale: value?.codiceFiscale, telefono: value?.telefono, email: value?.email});
        }
      }
    );


    return payload;
  }

  submitCrea() {
    this.entiService.creaEnte(this.buildPayloadCreazione()).subscribe(
      result => {
        this.clearDirty();
        let messaggio = this.translateService.instant('enti.dialogs.creato.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.ente?.nome ?? '']);
        this.modalService.info(this.translateService.instant('enti.dialogs.creato.titolo'),
          messaggio).then(
            () => {
              this.tornaIndietro();
            }
          ).catch(() => { });
      },
      error => {
        this.modalService.simpleError(Utils.toMessage(error), error.error.errore).then().catch(() => {});
      }
    );
  }

  buildPayloadModifica(): AggiornaEnteRequest {
    if (!this.enteForm) {
      throw new Error('Form non trovato');
    }
    const raw = this.enteForm.getRawValue();

    const payload: AggiornaEnteRequest = {
      nome: raw.anagrafica?.nome,
      codiceProfiloDefault: raw.profilo?.codice,
      utentiAmministratori: [],
      nuoviUtentiAmministratori: [],
    };

    this.amministratori.forEach(
      value => {
        if (value.id){
          payload?.utentiAmministratori?.push({id: value?.id, telefono: value?.telefono, email: value?.email});
        }else{
          payload?.nuoviUtentiAmministratori?.push({nome: value?.nome, cognome: value?.cognome, codiceFiscale: value?.codiceFiscale,
            telefono: value?.telefono, email: value?.email});
        }
      }
    );


    return payload;
  }

  submitAggiorna() {
    if (!this.idEnte) {
      return;
    }
    this.entiService.aggiornaEnte(this.idEnte, this.buildPayloadModifica()).subscribe(
      result => {
        this.clearDirty();
        let messaggio = this.translateService.instant('enti.dialogs.modificato.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.ente?.nome ?? '']);
        this.modalService.info(this.translateService.instant('enti.dialogs.modificato.titolo'),
          messaggio).then(
            () => {
              this.tornaIndietro();
            }
          ).catch(() => { });
      },
      error => {
        this.modalService.simpleError(Utils.toMessage(error), error.error.errore).then().catch(() => {});
      }
    );
  }


  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const payload: any = {
      page: input.page ?? 0,
      size: input.size ?? 10,
      sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
        '-' : '+' + input.sort[0]?.property) : undefined,
    };
    if (Utils.isNotBlank(input.query)) {
      payload.filter.fullText = { ci: input.query?.trim() };
    }
    payload.filter = {profili: [{ eq: 'ADMIN' }], idEnte: { eq: this.idEnte}};
    const output: Observable<ICosmoTablePageResponse> = this.utentiService.getUtenti(JSON.stringify(payload)).pipe(
        map(response => {
          if (response.pageInfo?.totalElements && response.pageInfo?.totalElements > 0){
            this.initAmministratorePresente = true;
          }
          const amministratoriDefinitivi: UtenteAmministratore[] = [];
          response.utenti?.forEach(u => {
            const utente: UtenteAmministratore = {
              id: u.id,
              nome: u.nome,
              cognome: u.cognome,
              codiceFiscale: u.codiceFiscale,
              email: u?.enti[0].email ?? '',
              telefono: u?.enti[0].telefono ?? ''
            };
            amministratoriDefinitivi.push(utente);
          });
          return {
            content: amministratoriDefinitivi ?? [],
            number: response.pageInfo?.page,
            numberOfElements: response.utenti?.length,
            size: response.pageInfo?.pageSize,
            totalElements: response.pageInfo?.totalElements,
            totalPages: response.pageInfo?.totalPages,
          };
        })
      );

    return output;
  }


}
