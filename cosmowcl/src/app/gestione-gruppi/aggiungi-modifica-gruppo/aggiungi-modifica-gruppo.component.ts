/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import {
  Component,
  HostListener,
  OnInit,
  ViewChild,
} from '@angular/core';
import {
  AbstractControl,
  AsyncValidatorFn,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import {
  CosmoTableColumnSize,
  CosmoTableComponent,
  CosmoTableSortDirection,
  ICosmoTableColumn,
  ICosmoTablePageRequest,
  ICosmoTablePageResponse,
  ICosmoTableReloadContext,
} from 'ngx-cosmo-table';
import {
  combineLatest,
  Observable,
  of,
} from 'rxjs';
import {
  debounceTime,
  delay,
  distinctUntilChanged,
  map,
  mergeMap,
  switchMap,
} from 'rxjs/operators';
import {
  ComponentCanDeactivate,
} from 'src/app/shared/guards/can-deactivate.guard';
import {
  AggiornaGruppoRequest,
} from 'src/app/shared/models/api/cosmoauthorization/aggiornaGruppoRequest';
import {
  CreaGruppoRequest,
} from 'src/app/shared/models/api/cosmoauthorization/creaGruppoRequest';
import { Gruppo } from 'src/app/shared/models/api/cosmoauthorization/gruppo';
import {
  RiferimentoUtente,
} from 'src/app/shared/models/api/cosmoauthorization/riferimentoUtente';
import { Utente } from 'src/app/shared/models/api/cosmoauthorization/utente';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { GruppiService } from 'src/app/shared/services/gruppi.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { UtentiService } from 'src/app/shared/services/utenti.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

import { TranslateService } from '@ngx-translate/core';
import { GestioneTipiPraticheService } from 'src/app/administration/gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { TipoPratica } from 'src/app/shared/models/api/cosmobusiness/tipoPratica';

@Component({
  selector: 'app-aggiungi-modifica-gruppo',
  templateUrl: './aggiungi-modifica-gruppo.component.html',
  styleUrls: ['./aggiungi-modifica-gruppo.component.scss']
})
export class AggiungiModificaGruppoComponent implements OnInit, ComponentCanDeactivate {

  gruppo?: Gruppo;
  idGruppo?: number;
  user?: UserInfoWrapper;
  nuovo?: boolean;
  cloneUtentiGruppo: RiferimentoUtente[] = [];
  cloneTipologiePratiche: TipoPratica[] = [];
  loading = 0;
  loadingError: any = null;

  gruppoForm!: FormGroup;
  utentiGruppo: RiferimentoUtente[] = [];
  tipologiePratiche: TipoPratica[] = [];
  dirty = false;

  columnsUtenti: ICosmoTableColumn[] = [{
    label: 'Codice fiscale', field: 'codiceFiscale', canSort: false, canHide: false, canFilter: true, defaultFilter: true
  }, {
    label: 'Nome', field: 'nome', canSort: false, canFilter: true, defaultFilter: true
  }, {
    label: 'Cognome', field: 'cognome', canSort: false, canFilter: true, defaultFilter: true
  }, {
    name: 'azioni', label: 'Azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.XXS
  },
  ];

  columnsTipiPratiche: ICosmoTableColumn[] = [{
    label: 'Codice', field: 'codice', canSort: false, canHide: false, canFilter: true, defaultFilter: true
  }, {
    label: 'Descrizione', name: 'descrizione', field: 'descrizione', canSort: false, canFilter: true, defaultFilter: true
  }, {
    label: 'Azioni', name: 'azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.SMALL
  },
  ];

  @ViewChild('tableUtentiAggiunti') tableUtentiAggiunti?: CosmoTableComponent;
  @ViewChild('tableUtentiEnte') tableUtentiEnte?: CosmoTableComponent;
  @ViewChild('tableTipologiePraticheAggiunte') tableTipologiePraticheAggiunte?: CosmoTableComponent;
  @ViewChild('tableGruppiPratiche') tableGruppiPratiche?: CosmoTableComponent;

  constructor(
    private route: ActivatedRoute,
    private securityService: SecurityService,
    private gruppiService: GruppiService,
    private utentiService: UtentiService,
    private tipiPraticheService: GestioneTipiPraticheService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private router: Router
  ) { }

  get isAggiungi(): boolean {
    return this.nuovo ?? true;
  }

  get isModifica(): boolean {
    return !this.isAggiungi;
  }

  // @HostListener allows us to also guard against browser refresh, close, etc.
  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    // insert logic to check if there are pending changes here;
    // returning true will navigate without confirmation
    // returning false will show a confirm dialog before navigating away
    if (this.dirty || this.gruppoForm?.dirty) {
      return false;
    }
    return true;
  }

  ngOnInit(): void {
    combineLatest([
      this.securityService.getCurrentUser(),
      this.route.params,
      this.route.data,
    ]).subscribe(latest => {
      this.refresh(
        Utils.require(latest[0], 'utente corrente'),
        !(latest[1].id),
        (latest[1].id ? +latest[1].id : undefined)
      );
    });
  }

  refresh(user: UserInfoWrapper, nuovo: boolean, idGruppo?: number): void {
    this.loading++;
    this.loadingError = null;

    of({ nuovo, idGruppo, user, }).pipe(
      mergeMap(data => {
        if (data.nuovo || !data.idGruppo) {
          return of({ ...data, gruppo: undefined });
        }
        return this.gruppiService.getGruppo(data.idGruppo).pipe(
          map(gruppoResponse => {
            return {
              ...data,
              gruppo: Utils.require(gruppoResponse.gruppo, 'gruppo'),
            };
          })
        );
      }),
    ).subscribe(data => {
      this.idGruppo = data.idGruppo;
      this.user = data.user;
      this.gruppo = data.gruppo;
      this.nuovo = data.nuovo;
      this.utentiGruppo = data.gruppo?.utenti ?? [];
      this.tipologiePratiche = data.gruppo?.tipologiePratiche ?? [];
      this.cloneUtentiGruppo = this.utentiGruppo.map(x => Object.assign({}, x));
      this.cloneTipologiePratiche = this.tipologiePratiche.map(x => Object.assign({}, x));

      this.initForm();
      this.loading--;
    }, failure => {
      this.loadingError = failure;
      this.loading--;
    });
  }

  private initForm() {
    let nome = '';
    let descrizione = '';
    let codice = '';

    if (this.gruppo) {
      nome = this.gruppo?.nome as string;
      descrizione = this.gruppo?.descrizione as string;
      codice = this.gruppo?.codice as string;
    }

    this.gruppoForm = new FormGroup({
      nome: new FormControl(nome, [Validators.required, Validators.maxLength(100)]),
      codice: new FormControl(codice, [Validators.required, Validators.maxLength(100)], [
        this.checkConflictingField('codice', 'eqic')
      ]),
      descrizione: new FormControl(descrizione, [Validators.required, Validators.maxLength(1000)]),
    });
  }

  tornaIndietro() {
    // this.router.navigate(['back'], { relativeTo: this.route });
    window.history.back();
  }

  displayInvalid(name: string): boolean {
    if (!this.gruppoForm) {
      return false;
    }
    const control = this.resolveControl(name);
    return control.touched && control.invalid;
  }

  hasError(name: string, type: string): any {
    if (!this.gruppoForm) {
      return false;
    }
    const control = this.resolveControl(name);
    return control.errors && control.errors[type] ?
      (control.errors[type] ?? true) : false;
  }

  hasValue(name: string): boolean {
    if (!this.gruppoForm) {
      return false;
    }
    const control = this.resolveControl(name);
    return Utils.isNotBlank(control.value);
  }

  private resolveControl(name: string): AbstractControl {
    let actual: any = this.gruppoForm;
    for (const token of name.split('.')) {
      const newControl = actual.controls[token];
      if (!newControl) {
        throw new Error('Controllo non trovato nel form: ' + name);
      }
      actual = newControl;
    }
    return actual as AbstractControl;
  }

  isSelezionatoU(utente: Utente): boolean {
    if (!this.utentiGruppo?.length) {
      return false;
    }
    return !!this.utentiGruppo.find(u => u.id === utente.id);
  }

  isSelezionatoTP(tipoPratica: TipoPratica): boolean {
    if (!this.tipologiePratiche?.length) {
      return false;
    }
    return !!this.tipologiePratiche.find(tp => tp.codice === tipoPratica.codice);
  }

  selezionaU(utente: Utente): void {
    this.dirty = true;
    this.utentiGruppo.push({
      ...utente
    });
    setTimeout(() => {
      if (this.tableUtentiAggiunti) {
        this.tableUtentiAggiunti.refresh();
      }
    }, 1);
  }

  selezionaTP(tipoPratica: TipoPratica): void {
    this.dirty = true;
    this.tipologiePratiche.push({
      ...tipoPratica
    });
    setTimeout(() => {
      if (this.tableTipologiePraticheAggiunte) {
        this.tableTipologiePraticheAggiunte.refresh();
      }
    }, 1);
  }

  rimuoviU(utente: Utente): void {
    this.dirty = true;
    const toRemove = this.utentiGruppo.find(u => u.id === utente.id);
    if (!toRemove) {
      return;
    }
    this.utentiGruppo.splice(this.utentiGruppo.indexOf(toRemove), 1);

    setTimeout(() => {
      if (this.tableUtentiAggiunti) {
        this.tableUtentiAggiunti.refresh();
      }
    }, 1);
  }

  rimuoviTP(tipoPratica: TipoPratica): void {
    this.dirty = true;
    const toRemove = this.tipologiePratiche.find(tp => tp.codice === tipoPratica.codice);
    if (!toRemove) {
      return;
    }
    this.tipologiePratiche.splice(this.tipologiePratiche.indexOf(toRemove), 1);

    setTimeout(() => {
      if (this.tableTipologiePraticheAggiunte) {
        this.tableTipologiePraticheAggiunte.refresh();
      }
    }, 1);
  }

  dataProviderUtentiEnte = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const output: Observable<ICosmoTablePageResponse> = this.securityService.getCurrentUser().pipe(
      mergeMap(utente => {
        const payload: any = {
          page: input.page ?? 0,
          size: input.size ?? 10,
          sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
            '-' : '+' + input.sort[0]?.property) : undefined,
          filter: {
            idEnte: {
              eq: utente.ente?.id
            },
          }
        };
        if (Utils.isNotBlank(input.query)) {
          payload.filter.fullText = { ci: input.query?.trim() };
        }
        return this.utentiService.getUtenti(JSON.stringify(payload));
      }),
      map(response => {
        return {
          content: response.utenti ?? [],
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

  dataProviderTipiPratiche = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
    const output: Observable<ICosmoTablePageResponse> = this.securityService.getCurrentUser().pipe(
      delay(environment.httpMockDelay),
      mergeMap(utente => {
        const payload = {
          page: input.page ?? 0,
          size: input.size ?? 10,
          sort: input.sort?.length ? (input.sort[0]?.direction === CosmoTableSortDirection.DESCENDING ?
            '-' : '+' + input.sort[0]?.property) : undefined,
            ...this.getFilterPayload(input, utente),
        };
        return this.tipiPraticheService.getTipiPratica(JSON.stringify(payload));
      }),
      map(response => {
        return {
          content: response.tipiPratiche ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.tipiPratiche?.length,
          size: response.pageInfo?.pageSize,
          totalElements: response.pageInfo?.totalElements,
          totalPages: response.pageInfo?.totalPages,
        };
      })
    );
    return output;
  }

  getFilterPayload(input: ICosmoTablePageRequest, utente: UserInfoWrapper): any {
    const output: any = {
      filter: {
        idEnte: {
        eq: utente.ente?.id
      }}
    };
    if (input.query) {
      output.filter.fullText = {
        ci: input.query
      };
    }
    return output;
  }


  buildPayloadCreazione(): CreaGruppoRequest {
    this.assertValidForm();
    const raw = this.gruppoForm.getRawValue();

    const out: CreaGruppoRequest = {
      nome: raw.nome,
      descrizione: raw.descrizione,
      codice: raw.codice,
      idUtenti: this.utentiGruppo.map(u => u.id).filter(u => !!u) as number[],
      codiciTipologiePratiche: this.tipologiePratiche.map(tp => tp.codice).filter(tp => !!tp) as string[]
    };

    return out;
  }

  buildPayloadModifica(): AggiornaGruppoRequest {
    return {
      ...this.buildPayloadCreazione()
    };
  }

  assertValidForm() {
    if (!this.gruppoForm.valid) {
      throw new Error('Dati inseriti non validi');
    }
  }

  onSubmit() {
    this.assertValidForm();
    if (this.nuovo) {
      this.submitCrea();
    } else {
      this.submitModifica();
    }
  }

  submitCrea() {
    this.gruppiService.creaGruppo(this.buildPayloadCreazione()).subscribe(
      result => {
        this.clearDirty();
        let messaggio = this.translateService.instant('gruppi.dialogs.creato.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.gruppo?.nome ?? '']);
        this.modalService.info(this.translateService.instant('gruppi.dialogs.creato.titolo'),
          messaggio).then(
            () => {
              this.tornaIndietro();
            }
          ).catch(() => { });
      },
      error => {
        this.modalService.simpleError(Utils.toMessage(error), error.error.errore).then().catch(() => { });
      }
    );
  }

  submitModifica() {
    if (!this.idGruppo) {
      return;
    }

    this.gruppiService.aggiornaGruppo(this.idGruppo, this.buildPayloadModifica()).subscribe(
      result => {
        this.clearDirty();
        let messaggio = this.translateService.instant('gruppi.dialogs.modificato.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.gruppo?.nome ?? '']);
        this.modalService.info(this.translateService.instant('gruppi.dialogs.modificato.titolo'),
          messaggio).then(
            () => {
              this.tornaIndietro();
            }
          ).catch(() => { });
      },
      error => {
        this.modalService.simpleError(Utils.toMessage(error), error.error.errore).then().catch(() => { });
      }
    );
  }

  clearDirty(): void {
    this.dirty = false;
    if (this.gruppoForm) {
      this.gruppoForm.markAsPristine();
    }
  }

  pulisciCampi() {
    this.utentiGruppo = this.cloneUtentiGruppo.map(x => Object.assign({}, x));
    this.tipologiePratiche = this.cloneTipologiePratiche.map(x => Object.assign({}, x));
    setTimeout(() => {
      if (this.tableUtentiAggiunti) {
        this.tableUtentiAggiunti.refresh();
      }
    }, 1);
    this.initForm();
  }

  displayValidating(name: string): boolean {
    if (!this.gruppoForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.touched || control.dirty) && (control as FormControl).status === 'PENDING';
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
        fields: 'id,nome,' + fieldName,
        filter: {
          idEnte: {
            eq: Utils.require(this.user?.ente?.id, 'idEnte')
          }
        },
      };

      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.gruppiService.getGruppi(filter)),
        map(response => {

          if (response.gruppi?.length && (!this.gruppo || (response.gruppi[0] as any)[fieldName] !== (this.gruppo as any)[fieldName])) {
            return {
              conflict: {
                field: fieldName,
                otherValue: (response.gruppi[0] as any)[fieldName],
                otherId: response.gruppi[0].id,
                otherName: response.gruppi[0].nome,
              }
            };
          }
          return null;
        })
      );
    };
  }

  gestioneTags(u: Utente) {

    this.router.navigate(['gestioneTags', u.id], { relativeTo: this.route });
  }

  isUserOnDB(u: Utente) {
    return this.cloneUtentiGruppo.find(ug => ug.id === u.id);
  }

}
