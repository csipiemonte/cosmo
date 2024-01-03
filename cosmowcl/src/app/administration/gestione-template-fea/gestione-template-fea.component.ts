/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import { Component, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import {CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection, ICosmoTableColumn, ICosmoTablePageRequest,
  ICosmoTablePageResponse, ICosmoTableReloadContext, ICosmoTableStoreAdapter, ICosmoTableStoreAdapterSaveContext } from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { Observable, Subject, forkJoin, merge, of } from 'rxjs';
import { debounceTime, delay, distinctUntilChanged, filter, map, mergeMap } from 'rxjs/operators';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';
import { TipoPratica } from 'src/app/shared/models/api/cosmoecm/tipoPratica';
import { EntiService } from 'src/app/shared/services/enti.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { GestioneTipiPraticheService } from '../gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { Constants } from 'src/app/shared/constants/constants';
import { TipiDocumentiService } from 'src/app/shared/services/tipi-documenti/tipi-documenti.service';
import { TipoDocumento } from 'src/app/shared/models/api/cosmobusiness/tipoDocumento';
import { environment } from 'src/environments/environment';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { TemplateFeaService } from 'src/app/shared/services/template-fea.service';
import { TemplateFea } from 'src/app/shared/models/api/cosmoecm/templateFea';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { Utils } from 'src/app/shared/utilities/utilities';

@Component({
  selector: 'app-gestione-template-fea',
  templateUrl: './gestione-template-fea.component.html',
  styleUrls: ['./gestione-template-fea.component.scss']
})
export class GestioneTemplateFeaComponent implements OnInit {

  constructor(
    private logger: NGXLogger,
    private route: ActivatedRoute,
    private router: Router,
    private securityService: SecurityService,
    private enteService: EntiService,
    private tipiPraticheService: GestioneTipiPraticheService,
    private tipiDocumentiService: TipiDocumentiService,
    private templateFeaService: TemplateFeaService,
    private modalService: ModalService,
    private translateService: TranslateService
  ) {
    this.logger.debug('creating component ' + this.COMPONENT_NAME);
  }

  COMPONENT_NAME = 'GestioneTemplateFeaComponent';
  loading = true;
  loadingError: any | null = null;
  isConfiguratore = false;
  filterForm !: FormGroup;
  tipologiePratica: TipoPratica[] = [];
  tipologieDocumento: TipoDocumento[] = [];
  enti: Ente[] = [];
  activeFilter: any = null;

  @ViewChild('instanceEnti', { static: true }) instanceEnti!: NgbTypeahead;
  focusEnti$ = new Subject<string>();
  clickEnti$ = new Subject<string>();
  @ViewChild('instanceTipoPratiche', { static: true }) instanceTipoPratiche!: NgbTypeahead;
  focusTipoPratiche$ = new Subject<string>();
  clickTipoPratiche$ = new Subject<string>();
  @ViewChild('instanceTipoDocumenti', { static: true }) instanceTipoDocumenti!: NgbTypeahead;
  focusTipoDocumenti$ = new Subject<string>();
  clickTipoDocumenti$ = new Subject<string>();

  columns: ICosmoTableColumn[] = [{
    name: 'ente', label: 'Ente', field: 'ente', canSort: false, canHide: false,  canFilter: true,
    valueExtractor: row => row.ente?.nome ?? '--',
  }, {
    name: 'tipoPratica', label: 'Tipologia pratica', field: 'tipoPratica', canSort: false, canHide: false, canFilter: true,
    valueExtractor: row => row.tipoPratica?.descrizione ?? '--',
  }, {
    name: 'tipoDocumento', label: 'Tipologia documento', field: 'tipoDocumento', canSort: false, canHide: false, canFilter: true,
    valueExtractor: row => row.tipoDocumento?.descrizione ?? '--',
  }, {
    name: 'descrizione', label: 'Descrizione template', field: 'descrizione', canSort: false, canHide: false, canFilter: true,
  }, {
    name: 'azioni', label: 'Azioni',
    canHide: false,
    canSort: false,
    applyTemplate: true,
    size: CosmoTableColumnSize.SMALL
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

  @ViewChild('table') table: CosmoTableComponent | null = null;

  formatterEnte = (result: Ente) => result.nome;
  formatterTipoPratica = (result: TipoPratica) => result.descrizione;
  formatterTipoDocumento = (result: TipoDocumento) => result.descrizione;

  ngOnInit(): void {
    this.logger.debug('initializing component ' + this.COMPONENT_NAME);
    this.loading = true;
    forkJoin({
      enti: this.enteService.getEnti(JSON.stringify({})),
      tipiPratiche: this.tipiPraticheService.listTipiPraticaByEnte(undefined, undefined, true),
      utente: this.securityService.getCurrentUser()
    }).subscribe(
      response => {
        this.enti = response.enti.enti ?? [];
        this.tipologiePratica = response.tipiPratiche ?? [];
        if (response.utente && response.utente.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
          this.isConfiguratore = true;
        }

        this.filterForm = new FormGroup({
          descrizioneTemplate: new FormControl(null),
          tipoPratica: new FormControl(null),
          tipoDocumento: new FormControl(null),
          ente: new FormControl({value: this.isConfiguratore ? this.enti.find(ente => ente.id === response.utente.ente?.id) : null,
             disabled: this.isConfiguratore})
        }, [], []);

        this.filterForm.controls.tipoPratica?.valueChanges.subscribe(
          elem => {
            const raw = this.filterForm.getRawValue();
            if (elem && raw.ente && raw.ente.id &&
               !this.tipologiePratica?.find(x => x?.descrizione === elem?.descrizione && raw?.ente.id === x.ente?.id)){
              this.filterForm.get('ente')?.patchValue(null);
            }
            this.filterForm.get('tipoDocumento')?.patchValue(null);
            this.tipologieDocumento = [];
            if (elem && elem.codice ) {
              this.tipiDocumentiService.getTipiDocumentiAll(elem.codice).subscribe(
                td => {
                  this.tipologieDocumento = td;
                }
              );
            }
          }
        );

        this.filterForm.controls.ente?.valueChanges.subscribe(
          elem => {
            const raw = this.filterForm.getRawValue();
            if (raw.tipoPratica && raw.tipoPratica.descrizione && elem && elem.id &&
               !this.tipologiePratica?.find(x => x?.descrizione === raw.tipoPratica?.descrizione && elem?.id === x.ente?.id)){
              this.filterForm.get('tipoPratica')?.patchValue(null);
            }
          }
        );
        this.refresh();
        this.loading = false;
      }, error => {
        this.loading = false;
        this.loadingError = error.error;
      }
      );
  }

  refresh(inBackground = false) {
    if (this.table) {
      this.table.refresh(inBackground);
    }
  }

  aggiungi() {
    this.router.navigate(['nuovo'], { relativeTo: this.route });
  }

  modifica(elem: TemplateFea) {
    this.router.navigate(['modifica', elem.id], { relativeTo: this.route });
  }

  elimina(elem: TemplateFea) {
    this.modalService.scegli(
      this.translateService.instant('tipi_pratiche.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [elem.descrizione ?? 'questo template fea']),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.templateFeaService.delete(Utils.require(elem.id)).subscribe(() => {
        this.refresh(false);
      },
      failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
    }).catch(() => {});
  }

  dataProvider = (input: ICosmoTablePageRequest, context?: ICosmoTableReloadContext) => {
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
      this.logger.debug('loading page with search params ', payload);
      return this.templateFeaService.search(JSON.stringify(payload));
  }),
      map(response => {
      return {
          content: response.templateFea ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.templateFea?.length,
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
      filter: {}
    };
    if (utente.ente?.id && utente.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
      output.filter.idEnte = {
        eq: utente.ente?.id
      };
    }
    if (this.activeFilter?.descrizioneTemplate){
      output.filter.descrizione = {
        ci: this.activeFilter.descrizioneTemplate
      };
    }
    if (utente.profilo?.codice !== Constants.CODICE_PROFILO_CONFIGURAZIONE && this.activeFilter?.ente && this.activeFilter?.ente?.nome){
      output.filter.idEnte = {
        eq: this.activeFilter.ente.id
      };
    }
    if (this.activeFilter?.tipoPratica && this.activeFilter?.tipoPratica?.codice){
      output.filter.codiceTipoPratica = {
        ci: this.activeFilter.tipoPratica.codice
      };
    }
    if (this.activeFilter?.tipoDocumento && this.activeFilter?.tipoDocumento?.codice){
      output.filter.codiceTipoDocumento = {
        ci: this.activeFilter.tipoDocumento.codice
      };
    }

    return output;
  }

  searchEnti = (text$: Observable<string>) => {

    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());

    const clicksWithClosedPopup$ = this.clickEnti$.pipe(
      filter(() => this.instanceEnti && !this.instanceEnti.isPopupOpen()),
      map(o => null)
    );

    const inputFocus$ = this.focusEnti$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      mergeMap((term: string | null) => {
        return of(this.enti ?
          this.enti.filter(v => (!(term?.length) || v.nome.toLowerCase().indexOf(term.toLowerCase()) > -1)  &&
          (this.filterForm?.get('tipoPratica')?.value === null ||
          (this.filterForm?.get('ente')?.value !== null && this.filterForm?.get('tipoPratica')?.value !== null) ||
          this.tipologiePratica?.find(x => x?.descrizione === this.filterForm?.get('tipoPratica')?.value.descrizione &&
          v?.id === x.ente?.id)))
            .sort((c1, c2) => (c1.nome ?? '').localeCompare(c2.nome ?? ''))
            .slice(0, 10)
          : []);
      })
    );
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
        return of(this.tipologiePratica ?
          this.tipologiePratica.filter(v => (!(term?.length) || v.descrizione
          && v.descrizione.toLowerCase().indexOf(term.toLowerCase()) > -1)
          && (this.filterForm?.get('ente')?.value === null ||
          (this.filterForm?.get('ente')?.value !== null && this.filterForm?.get('tipoPratica')?.value !== null)
          || this.filterForm?.get('ente')?.value?.id === v.ente?.id))
          .filter((v, i, a) => v.descrizione && a.findIndex(elem => elem.descrizione && elem.descrizione === v.descrizione) === i)
          .sort((c1, c2) => (c1.descrizione ?? '').localeCompare(c2.descrizione ?? ''))
          .slice(0, 10)
          : []);
      })
    );
  }

  searchTipiDoc = (text$: Observable<string>) => {
    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());

    const clicksWithClosedPopup$ = this.clickTipoDocumenti$.pipe(
      filter(() => this.instanceTipoDocumenti && !this.instanceTipoDocumenti.isPopupOpen()),
      map(o => null)
    );

    const inputFocus$ = this.focusTipoDocumenti$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      mergeMap((term: string | null) => {
        return of(this.tipologieDocumento ?
          this.tipologieDocumento.filter(v => (!(term?.length) || v.codice.toLowerCase().indexOf(term.toLowerCase()) > -1))
            .sort((c1, c2) => (c1.codice ?? '').localeCompare(c2.codice ?? ''))
            .slice(0, 10)
          : []);
      })
    );
  }

  focusOutEnte(value: any){

    const val = this.filterForm.get('ente')?.value;
    if (value !== val?.nome){
      this.filterForm.get('ente')?.patchValue(null);
    }
  }

  focusOutTipoPratica(value: any){

    const val = this.filterForm.get('tipoPratica')?.value;
    if (value !== val?.descrizione){
      this.filterForm.get('tipoPratica')?.patchValue(null);
    }
  }

  focusOutTipoDocumento(value: any){

    const val = this.filterForm.get('tipoDocumento')?.value;
    if (value !== val?.descrizione){
      this.filterForm.get('tipoDocumento')?.patchValue(null);
    }
  }

  azzera(): void {
    this.filterForm.get('ente')?.patchValue(null);
    this.filterForm.get('tipoPratica')?.patchValue(null);
    this.filterForm.get('tipoDocumento')?.patchValue(null);
    this.filterForm.get('descrizioneTemplate')?.patchValue(null);
    this.activeFilter = this.filterForm.getRawValue();

    if (this.table) {
      this.table.reset();
    }
  }

  filtra(): void {
    this.activeFilter = this.filterForm.getRawValue();

    if (this.table) {
      this.table.reset();
    }

  }

  hasValue(name: string): any {
    if (!this.filterForm) {
      return false;
    }
    const control = this.resolveControl(name);
    return !!control.value;
  }

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

  displayInvalid(name: string): boolean {
    if (!this.filterForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return (control.dirty || control.touched) && control.invalid;
  }

  isTipoDocFilterReadOnly(): boolean {
    return this.tipologieDocumento.length === 0;
  }

}
