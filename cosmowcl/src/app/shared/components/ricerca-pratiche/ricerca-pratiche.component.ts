/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormControl,
  FormGroup,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { SpinnerVisibilityService } from 'ng-http-loader';
import {
  CosmoTableComponent,
  CosmoTableFormatter,
  CosmoTableReloadReason,
  ICosmoTableAction,
  ICosmoTableActionDispatchingContext,
  ICosmoTableColumn,
  ICosmoTablePageRequest,
  ICosmoTablePageResponse,
  ICosmoTablePersistableStatusSnapshot,
  ICosmoTableReloadContext,
  ICosmoTableStatusSnapshot,
  ICosmoTableStoreAdapter,
  ICosmoTableStoreAdapterSaveContext,
} from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import {
  forkJoin,
  Observable,
  of,
  Subscription,
} from 'rxjs';
import {
  debounceTime,
  finalize,
  first,
  map,
  mergeMap,
  tap,
} from 'rxjs/operators';
import {
  TipologiaStatiPratica,
} from 'src/app/form-logici/models/tipologie-stati-pratica.model';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import {
  AssegnaAttivitaComponent,
} from '../../components/modals/assegna-attivita/assegna-attivita.component';
import {
  CondividiPraticaComponent,
} from '../../components/modals/condividi-pratica/condividi-pratica.component';
import { Constants } from '../../constants/constants';
import { UserInfoGruppo } from '../../models/api/cosmo/userInfoGruppo';
import { Attivita } from '../../models/api/cosmobusiness/attivita';
import { Pratica } from '../../models/api/cosmopratiche/pratica';
import {
  PraticheResponse,
} from '../../models/api/cosmopratiche/praticheResponse';
import {
  RiferimentoAttivita,
} from '../../models/api/cosmopratiche/riferimentoAttivita';
import { Decodifica } from '../../models/decodifica';
import { UserInfoWrapper } from '../../models/user/user-info';
import { BusService } from '../../services/bus.service';
import { ModalService } from '../../services/modal.service';
import {
  NotificationsWebsocketService,
} from '../../services/notifications-websocket.service';
import { PraticheService } from '../../services/pratiche.service';
import { SecurityService } from '../../services/security.service';
import { Utils } from '../../utilities/utilities';
import { ricercaPraticheTableConfig } from './ricerca-pratiche.table';
import { GestioneTipiPraticheService } from 'src/app/administration/gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { ConfigurazioniService } from '../../services/configurazioni.service';
import { FiltroVariabileProcessoRidotto } from '../../models/practices/filtro-variabile-processo-ridotto.model';
import { DateUtils } from '../../utilities/date-utils';
import { VariabileDiFiltro } from '../../models/api/cosmopratiche/variabileDiFiltro';
import { VariabiliDiFiltroService } from '../../services/variabili-di-filtro.service';
import { FormatoCampo } from 'src/app/administration/variabili-di-filtro-tipologie-pratiche/variabili-di-filtro-tipologie-pratiche.model';
import { FiltroCampo } from 'src/app/administration/variabili-di-filtro-tipologie-pratiche/variabili-di-filtro-tipologie-pratiche.model';
import { HelperService } from '../../services/helper.service';

export interface IRicercaPraticheStoreAdapterSaveContext extends ICosmoTableStoreAdapterSaveContext {
  filterCollapsed?: boolean;
  serializedFilters?: string;
  referer?: string;
}

export interface IRicercaPraticheStoreAdapterLoadedSnapshot extends ICosmoTablePersistableStatusSnapshot {
  filterCollapsed?: boolean;
  serializedFilters?: string;
  referer?: string;
}

export enum FiltriVariabiliProcesso {
  UGUALE_A = 'eq',
  DIVERSO_DA = 'ne',
  CONTIENE = 'ci',
  NON_CONTIENE = 'nci',
  INIZIA_CON = 'si',
  FINISCE_CON = 'ei',
  MINORE = 'lt',
  MINORE_UGUALE = 'lte',
  MAGGIORE = 'gt',
  MAGGIORE_UGUALE = 'gte',
  NULLO = 'defined'
}

export interface IRicercaPraticheStoreAdapter {
  save: (payload: IRicercaPraticheStoreAdapterSaveContext | null) => Observable<boolean>;
  load: () => Observable<IRicercaPraticheStoreAdapterLoadedSnapshot | null>;
}

@Component({
  selector: 'app-ricerca-pratiche',
  templateUrl: './ricerca-pratiche.component.html',
  styleUrls: ['./ricerca-pratiche.component.scss']
})
export class RicercaPraticheComponent implements OnInit, OnDestroy {

  @Input() readOnly?: boolean;
  @Input() selection?: 'single' | 'multi' | false;
  @Input() layout?: 'full' | 'compact';

  @Input() tipoRicerca: 'selezione' | 'associazione' = 'selezione';
  @Input() daAssociareA: number | null = null;
  @Input() tipologieStatiPraticaDaAssociare: TipologiaStatiPratica[] = [];

  @Input() filtraPerStatoPratica?: boolean;
  @Input() filtraPerStatoAvanzamento?: boolean;
  @Input() filtraPerTaskMassivo?: boolean;
  @Input() mostraFiltriDefault?: boolean;
  @Input() mostraVariabiliProcesso?: boolean;

  // store adapter - persistenza dello stato della tabella nella url query
  @Input() storeAdapter?: IRicercaPraticheStoreAdapter;
  @Input() tuttePratiche?: boolean;

  @Input() filterAdapter?: (filter: any) => any;
  @Input() actions?: ICosmoTableAction[];
  @Input() actionsStatusProvider?: ((action: ICosmoTableAction, status: ICosmoTableStatusSnapshot | null) => boolean);
  @Input() selectableStatusProvider?: ((row: any, status: ICosmoTableStatusSnapshot | null) => boolean) | null = null;
  @Input() allowSearch?: ((payload: any) => boolean) | null = null;

  @Output() action = new EventEmitter<ICosmoTableActionDispatchingContext>();

  private lastLoadedSnapshot: IRicercaPraticheStoreAdapterLoadedSnapshot | null = null;
  private lastEmittedTableStatus: ICosmoTablePersistableStatusSnapshot | null = null;

  private statusForAdapter: IRicercaPraticheStoreAdapterSaveContext = {
    status: null,
    filterCollapsed: undefined,
    serializedFilters: undefined,
  };

  // stati intermedi, caricamento e failure
  public loading = 0;
  public loadingError: any | null = null;
  public loadingFilters = 0;
  public loadingFiltersError: any | null = null;
  public filtersReady = false;
  public isCollapsed = false;

  // risultati della ricerca
  page: PraticheResponse | undefined = undefined;
  isAdminPrat = false;

  variabileSelezionata!: VariabileDiFiltro;

  numeroCheckbox = 0;
  // tabella dei risultati
  @ViewChild('table') table: CosmoTableComponent | null = null;
  columns = ricercaPraticheTableConfig.columns;
  @ViewChild('filtri') filtri: ElementRef | null = null;

  // filtri di ricerca
  filterForm!: FormGroup;
  activeFilter: any = null;
  clearedFilter: any = null;

  tipologiePratica: Decodifica[] = [];
  statiPratica: Decodifica[] = [];
  taskDisponibili: RiferimentoAttivita[] = [];

  variabiliFiltro: VariabileDiFiltro[] = [];

  filtersString = [
    { codice: FiltriVariabiliProcesso.UGUALE_A, descrizione: 'Uguale a' },
    { codice: FiltriVariabiliProcesso.DIVERSO_DA, descrizione: 'Diverso da' },
    { codice: FiltriVariabiliProcesso.CONTIENE, descrizione: 'Contiene' },
    { codice: FiltriVariabiliProcesso.NON_CONTIENE, descrizione: 'Non contiene' },
    { codice: FiltriVariabiliProcesso.INIZIA_CON, descrizione: 'Inizia con' },
    { codice: FiltriVariabiliProcesso.FINISCE_CON, descrizione: 'Finisce con' },
    { codice: FiltriVariabiliProcesso.NULLO, nullo: true, descrizione: 'E\' nullo' },
    { codice: FiltriVariabiliProcesso.NULLO, nullo: false, descrizione: 'Non e\' nullo' }
  ];

  filtersDateNumbers = [
    { codice: FiltriVariabiliProcesso.UGUALE_A, descrizione: 'Uguale a' },
    { codice: FiltriVariabiliProcesso.DIVERSO_DA, descrizione: 'Diverso da' },
    { codice: FiltriVariabiliProcesso.MINORE, descrizione: 'Minore a' },
    { codice: FiltriVariabiliProcesso.MINORE_UGUALE, descrizione: 'Minore o uguale a' },
    { codice: FiltriVariabiliProcesso.MAGGIORE, descrizione: 'Maggiore a' },
    { codice: FiltriVariabiliProcesso.MAGGIORE_UGUALE, descrizione: 'Maggiore o uguale a' },
    { codice: FiltriVariabiliProcesso.NULLO, nullo: true, descrizione: 'E\' nullo' },
    { codice: FiltriVariabiliProcesso.NULLO, nullo: false, descrizione: 'Non e\' nullo' }
  ];

  dateValidationStack = 0;
  gruppiUtente!: UserInfoGruppo[];

  oggettoRegexp = Constants.OGGETTO_REGEX_VALIDATION;
  maskingRegexp = Constants.OGGETTO_REGEX_MASKING;
  dataRegexp = Constants.DATA_REGEX_VALIDATION;

  lastSearchedFilter: any;
  taskMassivi: Decodifica[] = [];

  exportRowMaxSize?: number;

  filtriVariabiliProcesso?: Array<FiltroVariabileProcessoRidotto> = [];

  // subscriptions
  private notificationSubcritpion: Subscription | null = null;
  private praticheSubscription!: Subscription;

  private principal?: UserInfoWrapper;

  // store adapter - persistenza dello stato della tabella nella url query
  internalStoreAdapter: ICosmoTableStoreAdapter = {
    save: (payload: ICosmoTableStoreAdapterSaveContext | null) => {
      this.lastEmittedTableStatus = payload?.status ?? null;
      this.statusForAdapter.status = this.lastEmittedTableStatus;
      this.emitStatusSnapshot();
      return of(true);
    },
    load: () => {
      if (this.storeAdapter) {
        return this.storeAdapter.load().pipe(
          tap(loaded => {
            if (loaded) {
              Object.assign(this.statusForAdapter, loaded);
            }
          })
        );
      }
      return of(null);
    }
  };


  constructor(
    private logger: NGXLogger,
    private praticheService: PraticheService,
    private tipiPraticaService: GestioneTipiPraticheService,
    private variabiliDiFiltroService: VariabiliDiFiltroService,
    private busService: BusService,
    private securityService: SecurityService,
    private modal: NgbModal,
    private modalService: ModalService,
    private notificationsWebsocketService: NotificationsWebsocketService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private spinner: SpinnerVisibilityService,
    private configurazioniService: ConfigurazioniService,
    public helperService: HelperService,
    public route: ActivatedRoute
  ) { }

  get rawFilter(): any {
    return this.filterForm?.getRawValue();
  }

  get effectiveFiltraPerStatoPratica() {
    return this.filtraPerStatoPratica ?? true;
  }

  get effectiveFiltraPerStatoAvanzamento() {
    return this.filtraPerStatoAvanzamento ?? true;
  }

  get effectiveFiltraPerTaskMassivo() {
    return this.filtraPerTaskMassivo ?? false;
  }

  get mostraVariabiliProcess() {
    return this.mostraVariabiliProcesso ?? false;
  }

  get fullLayout(): boolean {
    return (this.layout ?? 'full') === 'full';
  }

  get compactLayout(): boolean {
    return this.layout === 'compact';
  }

  get allowSelection(): boolean {
    return (this.selection ?? false) !== false;
  }

  get singleSelection(): boolean {
    return this.allowSelection && this.selection === 'single';
  }

  get multiSelection(): boolean {
    return this.allowSelection && this.selection === 'multi';
  }

  get isReadOnly(): boolean {
    return this.readOnly ?? false;
  }

  get isAssociazione(): boolean {
    return this.tipoRicerca === 'associazione';
  }

  get enableFilterButton(): boolean {
    if (!this.filterForm) {
      return false;
    }
    if (!(this.filterForm?.valid)) {
      return false;
    }

    if (this.effectiveFiltraPerTaskMassivo) {
      const taskMassivoCtrl = this.resolveControl('taskMassivo');
      if (!taskMassivoCtrl || !taskMassivoCtrl.enabled || !taskMassivoCtrl.value || !taskMassivoCtrl.valid) {
        return false;
      }
    }

    return true;
  }

  get variabiliDiFiltroAssociate(): any {
    return (this.filterForm.get('variabili') as FormArray);
  }


  isFiltroNull(i: number) {
    const variabileSelezionata = this.getControl('variabili[' + i + '].valore')?.value;
    return variabileSelezionata.filtro?.codice === FiltriVariabiliProcesso.NULLO;
  }

  selectionChangeHandler(row: any[]): void {
    // NOP
  }


  public get selectedItems(): any[] {
    return this.table?.getStatusSnapshot()?.checkedItems ?? [];
  }

  async ngOnInit() {
    // init filters
    this.loadingFilters++;
    this.loadingFiltersError = null;

    this.isCollapsed = !(this.mostraFiltriDefault ?? false);

    this.buildColumns();

    const reloadStatusObs = (this.storeAdapter ? this.storeAdapter.load().pipe(first()) : of(null));

    forkJoin({
      statusSnapshot: reloadStatusObs,
      utente: this.securityService.getCurrentUser(),
      tipologiePratica: this.fetchTipologie(),
      statiPratica: this.fetchStati(null),
      configurazione: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.EXPORT_ROW_SIZE)
    }).pipe(
      mergeMap(loaded => {
        this.lastLoadedSnapshot = loaded.statusSnapshot;
        this.isCollapsed = loaded.statusSnapshot?.filterCollapsed ?? !(this.mostraFiltriDefault ?? false);

        this.exportRowMaxSize = loaded.configurazione ? +loaded.configurazione : undefined;

        this.principal = loaded.utente;
        this.tipologiePratica = loaded.tipologiePratica;
        this.statiPratica = loaded.statiPratica;

        if (this.isAssociazione && this.tipologieStatiPraticaDaAssociare.length > 0) {

          const tipologiePraticaTemp: Decodifica[] = [];

          this.tipologieStatiPraticaDaAssociare.forEach(tipoStati => {
            tipologiePraticaTemp.push(...this.tipologiePratica.filter(item => item.codice === tipoStati.tipologia));
          });

          this.tipologiePratica = tipologiePraticaTemp;
        }

        return this.initForm();
      }),
      finalize(() => {
        this.loadingFilters--;
        this.cdr.detectChanges();
      })
    )
      .subscribe(async () => {
        this.filtersReady = true;
        this.handleSubscriptions();
        this.refresh(true);
        this.cdr.detectChanges();
      }, fail => {
        this.loadingFiltersError = fail;
        console.error('error loading filters', fail);
        this.cdr.detectChanges();
      });
  }

  public toggleCollapse() {
    this.isCollapsed = !this.isCollapsed;

    this.statusForAdapter.filterCollapsed = !!this.isCollapsed;
    this.emitStatusSnapshot();
  }

  ngOnDestroy(): void {
    if (this.notificationSubcritpion) {
      this.notificationSubcritpion.unsubscribe();
    }
    if (this.praticheSubscription) {
      this.praticheSubscription.unsubscribe();
    }
  }

  private canExecuteSearch(payload: any): boolean {
    if (this.allowSearch) {
      return this.allowSearch(payload);
    }
    return true;
  }

  private handleSubscriptions() {
    this.praticheSubscription = this.busService.getCercaPratiche().subscribe(ricerca => {
      if (ricerca) {
        this.refresh();
      }
    });
    this.notificationSubcritpion = this.notificationsWebsocketService
      .whenEvent(Constants.APPLICATION_EVENTS.NOTIFICA_ULTIME_LAVORATE)
      .pipe(
        debounceTime(1000)
      )
      .subscribe(e => {
        this.refresh(true);
        this.cdr.detectChanges();
      });

    this.securityService.principal$.pipe(
      mergeMap(() => this.securityService.hasUseCases(['ADMIN_PRAT'], false))
    ).subscribe(result => this.isAdminPrat = result);

    this.securityService.principal$.pipe(
      mergeMap(() => this.securityService.getCurrentUser())
    ).subscribe(result => this.gruppiUtente = (result.gruppi ?? []));
  }

  private initForm(): Observable<boolean> {
    // verifica parametri di inizializzazione

    this.filterForm = new FormGroup({
      oggetto: new FormControl(null),
      riassunto: new FormControl(null),
      tipologia: new FormControl(null),
      stato: new FormControl(null),
      taskMassivo: new FormControl(null, this.effectiveFiltraPerTaskMassivo ? [Validators.required] : []),
      dataAperturaPraticaDa: new FormControl(null, [
        this.dateFormat(), this.before('dataAperturaPraticaA')]),
      dataAperturaPraticaA: new FormControl(null, [
        this.dateFormat(), this.after('dataAperturaPraticaDa')]),
      dataUltimaModificaDa: new FormControl(null, [
        this.dateFormat(), this.before('dataUltimaModificaA')]),
      dataUltimaModificaA: new FormControl(null, [
        this.dateFormat(), this.after('dataUltimaModificaDa')]),
      dataUltimoCambioStatoDa: new FormControl(null, [
        this.dateFormat(), this.before('dataUltimoCambioStatoA')]),
      dataUltimoCambioStatoA: new FormControl(null, [
        this.dateFormat(), this.after('dataUltimoCambioStatoDa')]),
      tutte: new FormControl(false),
      tutteGroup: new FormGroup({
        inCorso: new FormControl(null),
        concluse: new FormControl(null),
        annullate: new FormControl(null),
      }, this.effectiveFiltraPerStatoAvanzamento ? [
        this.validaCheckboxGroup('tutteGroup')
      ] : []),
      nonTutteGroup: new FormGroup({
        preferite: new FormControl(null),
        inEvidenza: new FormControl(null),
        daLavorare: new FormControl(null),
        preferiteGroup: new FormGroup({
          inCorso: new FormControl(null),
          concluse: new FormControl(null),
          annullate: new FormControl(null),
        }, this.effectiveFiltraPerStatoAvanzamento ? [
          this.validaCheckboxGroup('preferiteGroup')
        ] : []),
        inEvidenzaGroup: new FormGroup({
          inCorso: new FormControl(null),
          concluse: new FormControl(null),
          annullate: new FormControl(null),
        }, this.effectiveFiltraPerStatoAvanzamento ? [
          this.validaCheckboxGroup('inEvidenzaGroup')
        ] : []),
        daLavorareGroup: new FormGroup({
          inCorso: new FormControl(null),
        })
      }, this.effectiveFiltraPerStatoAvanzamento ? [this.validaCheckboxGroup('nonTutteGroup')] : []),
    });

    const variabili = new FormArray([]);
    this.filterForm.setControl('variabili', variabili);
    this.clearedFilter = this.filterForm.getRawValue();

    // initialize
    this.filterForm.patchValue(this.getStartingValue());

    // subscribe to change updates
    this.filterForm.controls.tutte.valueChanges.subscribe(() =>
      this.tutteChanged().subscribe());

    this.filterForm.controls.tipologia.valueChanges.subscribe(() =>
      this.tipologiaChanged().subscribe());

    const nonTutteGroup = this.filterForm.controls.nonTutteGroup as FormGroup;

    nonTutteGroup.controls.preferite.valueChanges.subscribe(() =>
      this.preferiteChanged().subscribe());
    nonTutteGroup.controls.inEvidenza.valueChanges.subscribe(() =>
      this.inEvidenzaChanged().subscribe());
    nonTutteGroup.controls.daLavorare.valueChanges.subscribe(() =>
      this.daLavorareChanged().subscribe());

    return of(true).pipe(
      mergeMap(() => this.tipologiaChanged()),
      mergeMap(() => this.tutteChanged()),
      tap(() => {
        this.activeFilter = this.filterForm.getRawValue();
      })
    );
  }

  getClearedFilterValue(): any {
    return Utils.mergeDeep({
      ...this.clearedFilter,
    }, {
      tutte: false,
      nonTutteGroup: {
        daLavorare: true,
        daLavorareGroup: {
          inCorso: true
        }
      }
    });
  }

  getStartingValue(): any {
    const referer = this.lastLoadedSnapshot?.referer;
    const filterString = this.lastLoadedSnapshot?.serializedFilters;
    if (filterString) {
      const loadedFilter = JSON.parse(atob(filterString));
      return loadedFilter;
    }

    // initialize
    if (referer === Constants.PRATICHE.PREFERITE) {
      return {
        ...this.clearedFilter,
        tutte: false,
        nonTutteGroup: {
          preferite: true,
          preferiteGroup: {
            inCorso: true,
            concluse: true,
          }
        }
      };
    } else if (referer === Constants.PRATICHE.IN_EVIDENZA) {
      return {
        ...this.clearedFilter,
        tutte: false,
        nonTutteGroup: {
          inEvidenza: true,
          inEvidenzaGroup: {
            inCorso: true,
            concluse: true,
          }
        }
      };
    } else if (referer === Constants.PRATICHE.IN_LAVORAZIONE) {
      return {
        ...this.clearedFilter,
        tutte: false,
        nonTutteGroup: {
          daLavorare: true,
          daLavorareGroup: {
            inCorso: true,
          }
        }
      };
    } else {
      return this.getClearedFilterValue();
    }
  }

  displayInvalid(name: string): boolean {
    if (!this.filterForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return control && control.touched && control.invalid;
  }

  hasError(name: string, type: string): any {
    if (!this.filterForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return control.errors && control.errors[type] ?
      (control.errors[type] ?? true) : false;
  }

  hasValue(name: string): any {
    if (!this.filterForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return !!control.value;
  }

  getControl(name: string): AbstractControl | undefined {
    if (!this.filterForm) {
      return undefined;
    }
    return this.resolveControl(name) ?? undefined;
  }

  /*
  private resolveControl(name: string): AbstractControl {
    let actual: any = this.filterForm;
    for (const token of name.split('.')) {
      const newControl = actual.controls[token];
      if (!newControl) {
        throw new Error('Controllo non trovato nel form: ' + name);
      }
      actual = newControl;
    }
    return actual as AbstractControl;
  }
  */
  protected resolveControl(name: string): AbstractControl | undefined {
    if (!name?.length) {
      return this.filterForm;
    }

    let actual: any = this.filterForm;
    for (const token of name.split('.')) {
      let namePart = token;
      let indexPart = null;
      if (token.indexOf('[') !== -1) {
        [namePart, indexPart] = [token.substr(0, token.indexOf('[')), token.substr(token.indexOf('['))];
      }
      const newControl = actual.controls[namePart];
      if (!newControl) {
        return undefined;
      }
      actual = newControl;
      if (indexPart) {
        const indexes = indexPart.split('[')
          .filter(o => !!o)
          .map(o => o.endsWith(']') ? o.substr(0, o.length - 1) : o)
          .map(v => v.match(/^[0-9]+$/) ? parseInt(v, 10) : v);

        for (const ip2 of indexes) {
          actual = actual.controls[ip2];
          if (!actual) {
            return undefined;
          }
        }
      }
    }
    return actual as AbstractControl;
  }

  validaCheckboxGroup(name: string): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {
      const group = input as FormGroup;
      // console.log('VALIDATING ' + name + ' WITH DATA ', JSON.stringify(input.value));
      if (group.enabled) {
        let atLeastOne = false;
        for (const control of Object.values(group.controls)) {
          if (control instanceof FormControl && control.value) {
            atLeastOne = true;
            break;
          }
        }
        if (!atLeastOne) {
          return { atLeastOne: true, in: name };
        }
      }
      return null;
    };
  }

  validaCoppiaDate(nameDa: string, nameA: string): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {
      const group = input as FormGroup;
      const controlDa = group.controls[nameDa];
      const controlA = group.controls[nameA];
      if (controlDa?.value && controlA?.value) {
        const daDate = new Date(controlDa.value);
        const aDate = new Date(controlA.value);
        if (aDate.getTime() < daDate.getTime()) {
          return {
            coppiaDate: true
          };
        }
      }
      return null;
    };
  }

  dateFormat(): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {

      if (input.value) {
        try {
          const thisDate = new Date(input.value);
          if (thisDate.getFullYear() > 9999) {
            return {
              dateFormat: true
            };
          }
        } catch (err) {
          this.logger.warn('invalid date value:', JSON.stringify(input.value));
        }
      }
      return null;
    };
  }

  before(otherControlName: string): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {
      if (!this.filterForm) {
        return null;
      }
      const otherControl = this.resolveControl(otherControlName);
      if (input.value && otherControl && otherControl.value) {
        const thisDate = new Date(input.value);
        const otherDate = new Date(otherControl.value);
        if (thisDate.getTime() - otherDate.getTime() > 0) {
          return {
            before: { otherDate }
          };
        }
      }
      if (otherControl?.value && this.dateValidationStack < 4) {
        this.dateValidationStack++;
        otherControl.updateValueAndValidity({
          emitEvent: false
        });
        this.dateValidationStack--;
      }

      return null;
    };
  }

  after(otherControlName: string): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {
      if (!this.filterForm) {
        return null;
      }
      const otherControl = this.resolveControl(otherControlName);
      if (input.value && otherControl && otherControl.value) {
        const thisDate = new Date(input.value);
        const otherDate = new Date(otherControl.value);
        if (thisDate.getTime() - otherDate.getTime() < 0) {
          return {
            after: { otherDate }
          };
        }
      }
      if (otherControl?.value && this.dateValidationStack < 4) {
        this.dateValidationStack++;
        otherControl.updateValueAndValidity({
          emitEvent: false
        });
        this.dateValidationStack--;
      }

      return null;
    };
  }

  checkDate(): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {

      const group = input as FormGroup;

      if (group.controls) {
        const dataRangeDa = group.controls.dataRangeDa?.value;
        const dataRangeA = group.controls.dataRangeA?.value;
        if (dataRangeDa && dataRangeA) {
          const dataDa = new Date(dataRangeDa);
          const dataA = new Date(dataRangeA);

          if (dataDa.getTime() > dataA.getTime()) {
            return {
              dateInvalide: true
            };
          }
        }
      }
      return null;
    };
  }

  checkNumber(): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {

      const group = input as FormGroup;

      if (group.controls) {
        const numeroDa = group.controls.numeroRangeDa?.value ?? null;
        const numeroA = group.controls.numeroRangeA?.value ?? null;

        if (numeroDa !== null && numeroA !== null && numeroDa > numeroA) {
          return {
            numeriInvalidi: true
          };
        }
      }
      return null;
    };
  }

  requireOneOf(firstControl: string, secondControl: string): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {

      const group = input as FormGroup;

      if (group.controls) {
        const firstElement = group.controls[firstControl]?.value ?? null;
        const secondElement = group.controls[secondControl]?.value ?? null;
        if ((firstElement === null || firstElement === '') && (secondElement === null || secondElement === '')) {
          return {
            requiredOneOf: true
          };
        }
      }
      return null;
    };
  }




  private fetchTipologie(): Observable<Decodifica[]> {
    return this.tipiPraticaService.listTipiPraticaByEnte().pipe(
      map(response => {
        return response
          .filter(o => !!o.codice)
          .sort((o1, o2) => (o1.descrizione?.toUpperCase() ?? '').localeCompare((o2.descrizione?.toUpperCase() ?? '')))
          .map(dto => {
            return { codice: dto.codice ?? '', valore: (dto.descrizione ?? dto.codice ?? '') };
          });
      })
    );
  }

  private fetchStati(tipologia: string | null): Observable<Decodifica[]> {
    if (!tipologia) {
      return of([]);
    }
    return this.praticheService.listStatiPraticaByTipo(tipologia).pipe(
      map(response => {
        return response
          .filter(o => !!o.codice)
          .sort((o1, o2) => (o1.descrizione?.toUpperCase() ?? '').localeCompare((o2.descrizione?.toUpperCase() ?? '')))
          .map(dto => {
            return { codice: dto.codice ?? '', valore: (dto.descrizione ?? dto.codice ?? '') };
          });
      })
    );
  }


  private fetchTasks(tipologia: string | null): Observable<RiferimentoAttivita[]> {
    if (!tipologia) {
      return of([]);
    }
    return this.praticheService.listTaskPossibili(this.getFilterPayloadImmediate()).pipe(
      map(response => {
        return response
          .filter(o => !!o.nome)
          .sort((o1, o2) => (o1.nome?.toUpperCase() ?? '').localeCompare((o2.nome?.toUpperCase() ?? '')));
      })
    );
  }

  tipologiaChanged(): Observable<any> {
    const statoSelezionatoPrima = this.filterForm.controls?.stato?.value;
    const taskMassivoSelezionatoPrima = this.filterForm.controls?.taskMassivo?.value;

    this.filterForm.patchValue({ taskMassivo: null });

    return of(null).pipe(
      tap(() => this.spinner.show()),
      mergeMap(() => forkJoin({
        stati: this.effectiveFiltraPerStatoPratica ? this.fetchStati(this.filterForm.controls?.tipologia?.value ?? null) : of(null),
        taskPossibili: this.effectiveFiltraPerTaskMassivo ?
          this.fetchTasks(this.filterForm.controls?.tipologia?.value ?? null) : of(null),
        variabiliFiltro: this.filterForm.controls?.tipologia?.value ?
          this.variabiliDiFiltroService.getVariabileDiFiltroTipoPratica(this.filterForm.controls?.tipologia?.value) : of(null)
      })),
      map(data => {

        this.variabiliFiltro = (data.variabiliFiltro ?? []).sort((c1, c2) => (c1.label ?? '').localeCompare(c2.label ?? ''));
        this.numeroCheckbox = this.variabiliFiltro.filter((v, i, a) => a.findIndex(t => t.nome === v.nome) === i).length;
        const variabili = new FormArray([]);
        this.filterForm.setControl('variabili', variabili);
        if (this.variabiliFiltro.length > 0) {
          const variabile = new FormGroup({ variabile: new FormControl(null), valore: new FormControl({ value: null }) });
          variabile.controls.variabile.valueChanges.subscribe(
            elem => this.buildNuovoValoreVariabile(variabile, elem)
          );
          this.numeroCheckbox--;
          (this.filterForm.get('variabili') as FormArray).push(variabile);
        }

        if (this.effectiveFiltraPerStatoPratica) {
          // elaboro gli stati possibili
          let stati = data.stati ?? [];

          if (this.isAssociazione && this.tipologieStatiPraticaDaAssociare.length > 0) {
            if (this.filterForm.controls?.tipologia?.value) {
              const tipoStati = this.tipologieStatiPraticaDaAssociare
                .find(item => item.tipologia === this.filterForm.controls?.tipologia?.value);

              if (tipoStati && tipoStati.stati.length > 0) {
                const statiTemp: Decodifica[] = [];

                tipoStati.stati.forEach(stato => {
                  statiTemp.push(...stati.filter(item => item.codice === stato));
                });
                stati = statiTemp;
              }
            }
          }

          this.statiPratica = stati;

          this.filterForm.patchValue({
            stato: statoSelezionatoPrima ?
              this.statiPratica.find(o => o.codice === statoSelezionatoPrima)?.codice ?? null : null
          });

          if (!this.statiPratica.length) {
            this.filterForm.controls.stato.disable();
          } else {
            this.filterForm.controls.stato.enable();
          }
        }

        if (this.effectiveFiltraPerTaskMassivo) {
          // elaboro i task disponibili
          const taskDisponibili = data.taskPossibili ?? [];
          this.taskDisponibili = taskDisponibili;
          const mapped: Decodifica[] = [];
          for (const taskDisponibile of taskDisponibili) {
            mapped.push({ codice: taskDisponibile.nome ?? '', valore: taskDisponibile.nome ?? '' });
          }
          this.taskMassivi = mapped;

          let valueNow = taskMassivoSelezionatoPrima ?
            mapped.find(o => o.codice === taskMassivoSelezionatoPrima)?.codice ?? null : null;
          if (!valueNow && mapped.length === 1) {
            valueNow = mapped[0].codice;
          }

          this.filterForm.patchValue({
            taskMassivo: valueNow
          });

          if (!mapped.length) {
            this.filterForm.controls.taskMassivo.disable();
          } else {
            this.filterForm.controls.taskMassivo.enable();
          }
          this.filterForm.updateValueAndValidity();
        }
      }),
      finalize(() => this.spinner.hide())
    );
  }

  tutteChanged(): Observable<any> {
    const tutte = this.filterForm.controls.tutte;
    const tutteGroup = this.filterForm.controls.tutteGroup as FormGroup;
    const nonTutteGroup = this.filterForm.controls.nonTutteGroup as FormGroup;

    if (tutte.value) {
      nonTutteGroup.disable();
      tutteGroup.enable();
      nonTutteGroup.patchValue({
        preferite: false,
        inEvidenza: false,
        daLavorare: false,
      });
    } else {
      tutteGroup.disable();
      nonTutteGroup.enable();
      tutteGroup.patchValue({
        inCorso: false,
        concluse: false,
        annullate: false,
      });
    }

    return of(true).pipe(
      mergeMap(() => forkJoin([
        this.preferiteChanged(),
        this.inEvidenzaChanged(),
        this.daLavorareChanged(),
      ]))
      // mergeMap(() => this.preferiteChanged())
    );
  }

  preferiteChanged(): Observable<any> {
    const nonTutteGroup = this.filterForm.controls.nonTutteGroup as FormGroup;
    const preferite = nonTutteGroup.controls.preferite;
    const preferiteGroup = nonTutteGroup.controls.preferiteGroup as FormGroup;

    if (preferite.value) {
      preferiteGroup.enable();
    } else {
      preferiteGroup.disable();
      preferiteGroup.patchValue({
        inCorso: false,
        concluse: false,
        annullate: false,
      });
    }

    return of(true);
  }

  inEvidenzaChanged(): Observable<any> {
    const nonTutteGroup = this.filterForm.controls.nonTutteGroup as FormGroup;
    const inEvidenza = nonTutteGroup.controls.inEvidenza;
    const inEvidenzaGroup = nonTutteGroup.controls.inEvidenzaGroup as FormGroup;

    if (inEvidenza.value) {
      inEvidenzaGroup.enable();
    } else {
      inEvidenzaGroup.disable();
      inEvidenzaGroup.patchValue({
        inCorso: false,
        concluse: false,
        annullate: false,
      });
    }

    return of(true);
  }

  daLavorareChanged(): Observable<any> {
    const nonTutteGroup = this.filterForm.controls.nonTutteGroup as FormGroup;
    const daLavorare = nonTutteGroup.controls.daLavorare;
    const daLavorareGroup = nonTutteGroup.controls.daLavorareGroup as FormGroup;

    daLavorareGroup.patchValue({
      inCorso: daLavorare.value,
    });

    daLavorareGroup.disable();
    return of(true);
  }

  getFilterPayload(): any {
    if (!this.activeFilter) {
      throw new Error('Form non pronto');
    }

    const rawValue = {
      ...this.activeFilter
    };

    const filterPayloadFromRaw = this.getFilterPayloadFromRaw(rawValue);

    if (this.tuttePratiche) {
      filterPayloadFromRaw.filter.tuttePratiche = true;
    }
    return filterPayloadFromRaw;
  }

  getFilterPayloadImmediate(): any {
    if (!this.filterForm) {
      throw new Error('Form non pronto');
    }

    const rawValue = {
      ...this.filterForm.getRawValue()
    };

    const filterPayloadFromRaw = this.getFilterPayloadFromRaw(rawValue);

    if (this.tuttePratiche) {
      filterPayloadFromRaw.filter.tuttePratiche = true;
    }
    return filterPayloadFromRaw;

  }

  getFilterPayloadFromRaw(rawValue: any): any {

    const output: any = {
      filter: {
        oggetto: Utils.isNotBlank(rawValue.oggetto) ? { ci: rawValue.oggetto } : undefined,
        riassunto: Utils.isNotBlank(rawValue.riassunto) ? { ci: rawValue.riassunto } : undefined,
        tipologia: Utils.isNotBlank(rawValue.tipologia) ? { eq: rawValue.tipologia } : undefined,
        stato: this.effectiveFiltraPerStatoPratica && Utils.isNotBlank(rawValue.stato) ? { eq: rawValue.stato } : undefined,
        taskMassivo: this.effectiveFiltraPerTaskMassivo && Utils.isNotBlank(rawValue.taskMassivo) ? rawValue.taskMassivo : undefined,
        dataAperturaPratica: {
          gte: this.toServerDate(rawValue.dataAperturaPraticaDa),
          lte: this.toServerDate(rawValue.dataAperturaPraticaA),
        },
        dataUltimaModifica: {
          gte: this.toServerDate(rawValue.dataUltimaModificaDa),
          lte: this.toServerDate(rawValue.dataUltimaModificaA),
        },
        dataUltimoCambioStato: {
          gte: this.toServerDate(rawValue.dataUltimoCambioStatoDa),
          lte: this.toServerDate(rawValue.dataUltimoCambioStatoA),
        },
        groups: (this.effectiveFiltraPerStatoAvanzamento ? {
          tutte: rawValue.tutte ? {
            inCorso: rawValue.tutteGroup?.inCorso,
            concluse: rawValue.tutteGroup?.concluse,
            annullate: rawValue.tutteGroup?.annullate,
          } : undefined,
          preferite: rawValue.nonTutteGroup?.preferite ? {
            inCorso: rawValue.nonTutteGroup?.preferiteGroup?.inCorso,
            concluse: rawValue.nonTutteGroup?.preferiteGroup?.concluse,
            annullate: rawValue.nonTutteGroup?.preferiteGroup?.annullate,
          } : undefined,
          inEvidenza: rawValue.nonTutteGroup?.inEvidenza ? {
            inCorso: rawValue.nonTutteGroup?.inEvidenzaGroup?.inCorso,
            concluse: rawValue.nonTutteGroup?.inEvidenzaGroup?.concluse,
            annullate: rawValue.nonTutteGroup?.inEvidenzaGroup?.annullate,
          } : undefined,
          daLavorare: rawValue.nonTutteGroup?.daLavorare ? {
            inCorso: rawValue.nonTutteGroup?.daLavorareGroup?.inCorso,
          } : undefined,
        } : {
          tutte: {
            inCorso: rawValue.tutteGroup?.inCorso,
          }
        }),
        daAssociareA: { eq: this.daAssociareA },
        tipologieStatiDaAssociare: this.isAssociazione &&
          ((!Utils.isNotBlank(rawValue.tipologia) && !Utils.isNotBlank(rawValue.stato)) ||
            (Utils.isNotBlank(rawValue.tipologia) && !Utils.isNotBlank(rawValue.stato)))
          && this.tipologieStatiPraticaDaAssociare.length > 0
          ? this.tipologieStatiPraticaDaAssociare : undefined,
        variabiliProcesso: rawValue.variabili ? this.buildFiltriVariabili(rawValue.variabili) : null
      },
    };


    if (this.filterAdapter) {
      output.filter = this.filterAdapter(output.filter);
    }



    return output;
  }

  buildFiltriVariabili(variabili: any[]): any[] {

    return variabili.filter(elem => elem?.variabile?.nome).map(variabile => {
      let nome = variabile?.variabile?.nome;
      let alberaturaJson = null;

      if (variabile.variabile.formato.codice.startsWith('json-')) {
        const [nomeJson, ...alberatura] = variabile.variabile?.nome.split('.');
        alberaturaJson = alberatura.join('.');
        nome = nomeJson;
      }

      const out: any = { nomeVariabile: nome };
      if (alberaturaJson) {
        out.alberaturaJson = alberaturaJson;
      }
      if (variabile?.variabile && (variabile?.variabile?.formato?.codice === FormatoCampo.STRINGA
        || variabile?.variabile?.formato?.codice === FormatoCampo.JSON_STRINGA)) {
        const variabileString: any = {};
        variabileString[variabile?.valore?.filtro?.codice] = variabile?.valore?.filtro?.codice === FiltriVariabiliProcesso.NULLO ?
          !variabile?.valore?.filtro?.nullo : variabile?.valore?.testo;
        out.singolo = { variabileString };
      }

      if (variabile?.variabile && variabile?.variabile?.formato?.codice === FormatoCampo.DATA
        || variabile?.variabile?.formato?.codice === FormatoCampo.JSON_DATA) {
        if (variabile?.variabile?.tipoFiltro?.codice === FiltroCampo.SINGOLO) {
          const variabileNumber: any = {};
          variabileNumber[variabile?.valore?.filtro?.codice] = variabile?.valore?.filtro?.codice === FiltriVariabiliProcesso.NULLO ?
            !variabile?.valore?.filtro?.nullo : variabile?.valore?.dataSingola;
          out.singolo = { variabileData: variabileNumber };
        }
        out.rangeDa = variabile?.valore?.dataRangeDa ? { variabileData: { gte: this.toServerDate(variabile?.valore?.dataRangeDa) } } : null;
        out.rangeA = variabile?.valore?.dataRangeA ? { variabileData: { lte: this.toServerDate(variabile?.valore?.dataRangeA) } } : null;

      }

      if (variabile?.variabile && variabile?.variabile?.formato?.codice === FormatoCampo.NUMERICO
        || variabile?.variabile?.formato?.codice === FormatoCampo.JSON_NUMERICO) {
        if (variabile?.variabile?.tipoFiltro?.codice === FiltroCampo.SINGOLO) {
          const variabileNumber: any = {};
          variabileNumber[variabile?.valore?.filtro?.codice] = variabile?.valore?.filtro?.codice === FiltriVariabiliProcesso.NULLO ?
            !variabile?.valore?.filtro?.nullo : variabile?.valore?.numeroSingolo;
          out.singolo = { variabileNumerica: variabileNumber };
        }
        out.rangeDa = variabile?.valore?.numeroRangeDa !== null ? { variabileNumerica: { gte: variabile?.valore?.numeroRangeDa } } : null;
        out.rangeA = variabile?.valore?.numeroRangeA !== null ? { variabileNumerica: { lte: variabile?.valore?.numeroRangeA } } : null;

      }

      if (variabile?.variabile && variabile?.variabile?.formato?.codice === FormatoCampo.BOOLEANO
        || variabile?.variabile && variabile?.variabile?.formato?.codice === FormatoCampo.JSON_BOOLEANO) {
        out.singolo = variabile?.valore?.veroFalso ? { variabileBoolean: { eq: variabile?.valore?.veroFalso } } : null;
      }

      return out;
    });
  }

  toServerDate(input: Date | undefined | null): string | undefined {
    if (!input) {
      return undefined;
    }
    return new Date(input).toISOString().substr(0, 10);
  }

  azzera(): void {
    // this.scrollToTheTable();
    this.filterForm.setValue(
      this.getClearedFilterValue()
    );

    this.activeFilter = this.filterForm.getRawValue();

    this.statusForAdapter.serializedFilters = undefined;
    this.emitStatusSnapshot();

    this.filtriVariabiliProcesso = [];
    this.buildColumns();
    if (this.table) {
      this.table.reset();
    }
    this.cdr.detectChanges();
  }

  filtra(): void {

    this.scrollToTheTable();

    /* a seconda dell'implementazione che verra' scelta, i filtri della sezione dinamica
       relativa ad una tipologia di pratica, saranno presenti anch'essi nell'active filter
       o comunque presenti in un'altra struttura similare
    */
    this.activeFilter = this.filterForm.getRawValue();
    const variabili: any = {
      ... this.filterForm.getRawValue()
    };
    this.filtriVariabiliProcesso = (variabili?.variabili as any[]).filter(x => x.variabile?.label).map(elem => {

      if (elem.variabile.formato.codice.startsWith('json-')) {
        const [nomeJson, ...alberatura] = elem.variabile?.nome.split('.');
        const alberaturaJson = alberatura.join('.');
        if (alberaturaJson) {
          return {
            nome: nomeJson, label: elem.variabile?.label, alberaturaJson, visualizza: elem.variabile?.aggiungereARisultatoRicerca ?? false,
            formato: elem.variabile?.formato?.codice
          };
        }
      }
      return {
        nome: elem.variabile?.nome, label: elem.variabile?.label, visualizza: elem.variabile?.aggiungereARisultatoRicerca ?? false,
        formato: elem.variabile?.formato?.codice
      };

    });


    /* quindi popolare l'array filtriVariabiliProcesso inserendo il nome della variabile
       e la visualizzazione o meno (in formato booleano) in elenco per ogni filtro presente
    */

    this.buildColumns();

    const cleaned = JSON.stringify(Utils.cleanObject(this.activeFilter));
    const queryFilter = btoa(cleaned);

    this.statusForAdapter.serializedFilters = queryFilter;
    this.emitStatusSnapshot();


    if (this.table) {
      this.table.reset();
    }
    this.cdr.detectChanges();

  }

  buildColumns(): void {
    let out = ricercaPraticheTableConfig.columns
      .filter(c => {
        if (this.isReadOnly) {
          if (['favorite', 'dettaglio', 'azioni_dropdown', 'azioni_menu'].includes(c.name ?? '')) {
            return false;
          }
        }
        if (this.compactLayout) {
          if (['dataCambioStato', 'dataAggiornamentoPratica'].includes(c.name ?? '')) {
            return false;
          }
        }
        return true;
      })
      .map(o => o);


    if (this.filtriVariabiliProcesso && this.filtriVariabiliProcesso?.length > 0) {
      const customFields = this.addVariabiliProcessoColums();
      if (customFields.length > 0) {
        // viene scartata la prima posizione in quanto e' presente l'icona di selezione dei preferiti
        // quindi viene ricercata la posizione dell'ultimo campo con una label
        const lastPositionBeforeCustomFields = this.getLastPositionBeforeCustomFields(out);
        const colonneTastiAzione = out.slice(lastPositionBeforeCustomFields + 1, out.length);
        const colonneClassiche = out.slice(0, lastPositionBeforeCustomFields + 1);
        out = [...colonneClassiche, ...customFields, ...colonneTastiAzione];
      }
    }
    if (this.table) {
      this.table.bridge.visibleColumnsSelectionChanged(out);
    }
    this.columns = out;
  }

  private getLastPositionBeforeCustomFields(out: ICosmoTableColumn[]) {
    return !this.isReadOnly ? out.slice(1, out.length).findIndex(o => !o.label) : out.length;
  }

  private addVariabiliProcessoColums(): ICosmoTableColumn[] {

    const customColumns: ICosmoTableColumn[] = [];
    const variabiliVisualizzabili = this.filtriVariabiliProcesso?.filter(filtro => filtro.visualizza === true);
    variabiliVisualizzabili?.forEach(vv => {
      const field: ICosmoTableColumn = { name: vv.nome, label: vv.label, field: vv.nome, canSort: true };
      // TO DO: utilizzare la costante legata al DTO che verrà creato in futuro
      if (vv.formato === 'data') {
        field.formatters = [{
          formatter: CosmoTableFormatter.DATE, arguments: 'dd/MM/yyyy',
        }, {
          format: (raw: any) => raw ?? '',
        }];
        field.valueExtractor = raw => DateUtils.parse(Utils.getPropertyValue(raw, vv.nome));
      }

      customColumns.push(field);
    });

    return customColumns;
  }

  get effectiveColumns(): ICosmoTableColumn[] {
    return this.columns;
  }

  onAction(context: ICosmoTableActionDispatchingContext) {
    this.action.next(context);
  }

  refresh(inBackground = false) {
    if (this.table) {
      this.table.refresh(inBackground);
    }
    this.cdr.detectChanges();
  }

  getDefaultPageSize(): number {
    if (this.compactLayout) {
      return 5;
    }
    return 10;
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const filter = this.getFilterPayload();
    // this.loading ++;
    const payload = {
      page: input.page ?? 0,
      size: input.size ?? this.getDefaultPageSize(),
      sort: input.sort?.length ? (input.sort[0]?.property + ' ' + input.sort[0]?.direction) : undefined,
      ...filter,
    };

    if (!this.canExecuteSearch(payload)) {
      return of({
        content: [],
        number: 0,
        numberOfElements: 0,
        size: 1,
        totalElements: 0,
        totalPages: 1,
      });
    }

    this.logger.debug('loading page with search params', payload);

    const exportData = context?.reason === CosmoTableReloadReason.EXPORT ? true : false;

    const output: Observable<ICosmoTablePageResponse> = this.praticheService.search(payload, exportData).pipe(
      // finalize(() => this.loading --),
      map(response => {
        this.lastSearchedFilter = filter.filter;

        if (response.pratiche && this.filtriVariabiliProcesso && this.filtriVariabiliProcesso.length > 0) {
          response.pratiche.forEach(p => {
            p.variabiliProcesso?.forEach(vp => {
              Utils.setPropertyValue(p, vp.nome ?? '', vp.valore);
            });
          });
        }

        return {
          content: response.pratiche ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.pratiche?.length,
          size: response.pageInfo?.pageSize,
          totalElements: response.pageInfo?.totalElements,
          totalPages: response.pageInfo?.totalPages,
        };
      })
    );

    return output;
  }


  togglePreference(p: Pratica) {
    this.assertNotReadOnly();
    this.praticheService.setPreferredStatus(p, !p.preferita).subscribe(() => {
      this.busService.setCercaPratiche(true);
    });
  }

  goToDettaglio(p: Pratica) {
    this.assertNotReadOnly();
    this.router.navigate(['pratica', p.id]);
  }

  doAttivita(a: Attivita) {
    this.assertNotReadOnly();
    if (a.linkAttivitaEsterna) {
      window.open(a.linkAttivitaEsterna, '_blank');
    } else {
      this.router.navigate([a.linkAttivita]);
    }
  }

  canAssign(p: Pratica) {
    return !!p?.attivita?.length && !p.attivita.find(a => !!a.parent) && !!p.tipo?.assegnabile;
  }

  assignPratica(p: Pratica) {
    this.assertNotReadOnly();
    if (!p?.id) {
      return;
    }
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(AssegnaAttivitaComponent);
    modalRef.componentInstance.idAttivita = null;
    modalRef.componentInstance.idPratica = p.id;
    modalRef.componentInstance.codicePagina = data?.snapshot.data?.codicePagina;
    modalRef.componentInstance.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    modalRef.componentInstance.codiceModale = 'assegna-pratica-elenco-lavorazioni';
    modalRef.result.then(result => {
      if (result === Constants.OK_MODAL) {
        this.table?.refresh(false);
      }
    });
  }

  annullaPratica(p: Pratica) {
    this.assertNotReadOnly();
    if (!p.id) {
      return;
    }
    const titolo = 'Conferma annullamento pratica';
    const descrizione = 'Si conferma l\'annullamento della pratica ' + p.oggetto + ' ?';

    this.modalService.confermaRifiuta(titolo, descrizione).then(
      () => {
        if (p.id) {
          this.praticheService.annullaPratica(p.id).subscribe(response => {
            if (response.ok) {
              this.modalService.info('Esito', 'Cancellazione pratica andata a buon fine', 'chiudi').then(
                () => this.busService.setCercaPratiche(true)
              );
            }
            else {
              this.modalService.error('Esito', 'Cancellazione fallita: ' + response.statusText, undefined, 'chiudi');
            }
          });
        }
      }
    );
  }

  buildNuovoValoreVariabile(nuovaVariabile: FormGroup, elem: any) {

    if (elem && (elem?.formato?.codice === FormatoCampo.BOOLEANO || elem?.formato?.codice === FormatoCampo.JSON_BOOLEANO)) {
      nuovaVariabile.setControl('valore', new FormGroup({
        veroFalso: new FormControl(null, Validators.required)
      }));
    }
    if (elem && (elem?.formato?.codice === FormatoCampo.STRINGA || elem?.formato?.codice === FormatoCampo.JSON_STRINGA)) {
      const valore = new FormGroup({
        filtro: new FormControl(null, Validators.required),
        testo: new FormControl(null, Validators.required)
      });
      nuovaVariabile.setControl('valore', valore);
      valore.controls.filtro.valueChanges.subscribe(
        elemento => {
          if (elemento && elemento.codice === FiltriVariabiliProcesso.NULLO) {
            valore.removeControl('testo');
          } else if (!valore.controls.testo) {
            valore.addControl('testo', new FormControl(null, Validators.required));
          }
        }
      );
    }
    if (elem && (elem?.formato?.codice === FormatoCampo.NUMERICO || elem?.formato?.codice === FormatoCampo.JSON_NUMERICO)
      && elem?.tipoFiltro?.codice === FiltroCampo.SINGOLO) {

      const valore = new FormGroup({
        filtro: new FormControl(null, Validators.required),
        numeroSingolo: new FormControl(null, Validators.required)
      });
      nuovaVariabile.setControl('valore', valore);
      valore.controls.filtro.valueChanges.subscribe(
        elemento => {
          if (elemento && elemento.codice === FiltriVariabiliProcesso.NULLO) {
            valore.removeControl('numeroSingolo');
          } else if (!valore.controls.numeroSingolo) {
            valore.addControl('numeroSingolo', new FormControl(null, Validators.required));
          }
        }
      );

    }
    if (elem && (elem?.formato?.codice === FormatoCampo.NUMERICO || elem?.formato?.codice === FormatoCampo.JSON_NUMERICO)
      && elem?.tipoFiltro?.codice === FiltroCampo.RANGE) {
      nuovaVariabile.setControl('valore', new FormGroup({
        numeroRangeDa: new FormControl(null),
        numeroRangeA: new FormControl(null)
      }, [this.checkNumber(), this.requireOneOf('numeroRangeDa', 'numeroRangeA')]));

    }

    if (elem && (elem?.formato?.codice === FormatoCampo.DATA || elem?.formato?.codice === FormatoCampo.JSON_DATA)
      && elem?.tipoFiltro?.codice === FiltroCampo.SINGOLO) {
      const valore = new FormGroup({
        filtro: new FormControl(null, Validators.required),
        dataSingola: new FormControl(null, Validators.required)
      });
      nuovaVariabile.setControl('valore', valore);
      valore.controls.filtro.valueChanges.subscribe(
        elemento => {
          if (elemento && elemento.codice === FiltriVariabiliProcesso.NULLO) {
            valore.removeControl('dataSingola');
          } else if (!valore.controls.dataSingola) {
            valore.addControl('dataSingola', new FormControl(null, Validators.required));
          }
        }
      );
    }
    if (elem && (elem?.formato?.codice === FormatoCampo.DATA || elem?.formato?.codice === FormatoCampo.JSON_DATA)
      && elem?.tipoFiltro?.codice === FiltroCampo.RANGE) {
      nuovaVariabile.setControl('valore', new FormGroup({
        dataRangeDa: new FormControl(null, [this.dateFormat()]),
        dataRangeA: new FormControl(null, [this.dateFormat()])
      }, [this.checkDate(), this.requireOneOf('dataRangeDa', 'dataRangeA')]));
    }


  }

  variabiliDisponibili(i: number): VariabileDiFiltro[] {
    const variabileSelezionata = this.getControl('variabili[' + i + '].variabile')?.value as VariabileDiFiltro;
    const variabili = this.getControl('variabili') as FormArray;

    return this.variabiliFiltro.filter(elem => elem?.nome === variabileSelezionata?.nome ||
      !variabili.controls.map(x => (x as FormGroup)?.controls.variabile?.value?.nome).find(y => y === elem?.nome));
  }



  aggiungiVariabile() {
    const nuovaVariabile = new FormGroup({ variabile: new FormControl(null), valore: new FormGroup({}) });

    nuovaVariabile.controls.variabile?.valueChanges.subscribe(
      elem => this.buildNuovoValoreVariabile(nuovaVariabile, elem)
    );

    (this.filterForm.get('variabili') as FormArray)?.push(nuovaVariabile);

    this.numeroCheckbox--;

  }

  eliminaVariabile(i: number) {
    const variabili = this.filterForm.get('variabili') as FormArray;
    this.numeroCheckbox++;

    variabili.removeAt(i);
  }

  isFromFruitoreEsterno(p: any): boolean {
    return !!(p.fruitore);
  }

  hasAttivita(p: any): boolean {
    return !!(p.attivita?.length);
  }

  hasAttivitaAssegnate(p: any): boolean {
    for (const a of p.attivita ?? []) {
      for (const ass of a.assegnazione ?? []) {
        if (ass.assegnatario && (!ass.campiTecnici.dtFineVal || ass.campiTecnici.dtFineVal > Date.now())) {
          return true;
        } else {
          const gruppo = this.gruppiUtente.find(o => +ass.idGruppo === o.id);
          if (gruppo && (!ass.campiTecnici.dtFineVal || ass.campiTecnici.dtFineVal > Date.now())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  isConclusaOAnnullata(p: any): boolean {
    return this.isConclusa(p) || this.isAnnullata(p);
  }

  isConclusa(p: any): boolean {
    return !!p.dataFinePratica;
  }

  isAnnullata(p: any): boolean {
    return !!p.dataCancellazione;
  }

  isAnnullabile(p: any): boolean {
    return !!p.tipo?.annullabile && this.isAdminPrat;
  }

  getBadgeClass(p: Pratica) {
    if (this.isAnnullata(p)) {
      return 'danger';
    }

    const coll = p.attivita?.find(att => att.hasChildren) || p.attivita?.find(att => att.parent);

    if (p?.stato?.classe?.length) {
      if (coll) {
        return p.stato.classe + ' fas fa-hands-helping fas-hands';
      }
      return p.stato.classe;
    } else {
      if (coll) {
        return 'primary fas fa-hands-helping fas-hands';
      }
    }

    return 'primary';
  }

  getStatusText(p: Pratica) {
    if (this.isAnnullata(p)) {
      return 'Annullata';
    }

    const coll = p.attivita?.find(att => att.hasChildren) || p.attivita?.find(att => att.parent);
    if (coll) {
      return p.stato?.descrizione ? '\u00A0\u00A0' + p.stato?.descrizione : ' ';
    }
    return p.stato?.descrizione;
  }

  canShare(pratica: Pratica): boolean {
    return ['B1', 'B2', 'B3', 'E1', 'E2'].some(
      clause => (pratica.visibilita ?? []).indexOf(clause) !== -1) && !!pratica.tipo?.condivisibile;
  }

  condividiPratica(p: Pratica) {
    this.assertNotReadOnly();
    if (!p?.id) {
      return;
    }
    const data = this.helperService.searchHelperRef(this.route);
    const modalRef = this.modal.open(CondividiPraticaComponent);
    const component = modalRef.componentInstance as CondividiPraticaComponent;
    component.idPratica = p.id;
    component.codicePagina = data?.snapshot.data?.codicePagina;
    component.codiceTab = data?.snapshot.data?.isTab ? data.snapshot.queryParams.tab ?? '' : '';
    component.codiceModale = 'condividi-pratica-elenco-lavorazioni';
    modalRef.result.then(() => { }).catch(() => { });
  }

  private emitStatusSnapshot(): void {
    if (this.storeAdapter) {
      this.storeAdapter.save(this.statusForAdapter).subscribe(); // TODO subscribe
    }
  }

  private assertNotReadOnly(): void {
    if (this.isReadOnly) {
      throw new Error('Component is in read-only mode.');
    }
  }


  private scrollToTheTable(): void {
    if (this.filtri?.nativeElement) {
      this.filtri.nativeElement.scrollIntoView();
    }

  }
}
