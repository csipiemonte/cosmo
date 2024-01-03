/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal, NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { CosmoTableColumnSize, CosmoTableComponent, CosmoTableSortDirection, ICosmoTableColumn,
  ICosmoTablePageRequest, ICosmoTablePageResponse, ICosmoTableReloadContext,
  ICosmoTableStoreAdapter, ICosmoTableStoreAdapterSaveContext } from 'ngx-cosmo-table';
import { NGXLogger } from 'ngx-logger';
import { forkJoin, merge, Observable, of, Subject } from 'rxjs';
import { debounceTime, delay, distinctUntilChanged, filter, map, mergeMap } from 'rxjs/operators';
import { VariabileDiFiltro } from 'src/app/shared/models/api/cosmopratiche/variabileDiFiltro';
import { UserInfoWrapper } from 'src/app/shared/models/user/user-info';
import { EntiService } from 'src/app/shared/services/enti.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { SecurityService } from 'src/app/shared/services/security.service';
import { VariabiliDiFiltroService } from 'src/app/shared/services/variabili-di-filtro.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';
import { Constants } from '../../shared/constants/constants';
import { GestioneTipiPraticheService } from '../gestione-tipi-pratiche/gestione-tipi-pratiche.service';
import { TipoPratica } from 'src/app/shared/models/api/cosmopratiche/tipoPratica';
import { Ente } from 'src/app/shared/models/api/cosmoauthorization/ente';

@Component({
  selector: 'app-variabili-di-filtro-tipologie-pratiche',
  templateUrl: './variabili-di-filtro-tipologie-pratiche.component.html',
  styleUrls: ['./variabili-di-filtro-tipologie-pratiche.component.scss']
})
export class VariabiliDiFiltroTipologiePraticheComponent implements OnInit {

  constructor(
    private logger: NGXLogger,
    private route: ActivatedRoute,
    private router: Router,
    private securityService: SecurityService,
    private modalService: ModalService,
    private translateService: TranslateService,
    private enteService: EntiService,
    private tipiPraticheService: GestioneTipiPraticheService,
    private variabiliDiFiltroService: VariabiliDiFiltroService,
    private modal: NgbModal,
  ) {
    this.logger.debug('creating component ' + this.COMPONENT_NAME);
  }

  COMPONENT_NAME = 'VariabiliDiFiltroTipologiePraticheComponent';

  @ViewChild('instanceEnti', { static: true }) instanceEnti!: NgbTypeahead;
  focusEnti$ = new Subject<string>();
  clickEnti$ = new Subject<string>();

  @ViewChild('instanceTipoPratiche', { static: true }) instanceTipoPratiche!: NgbTypeahead;
  focusTipoPratiche$ = new Subject<string>();
  clickTipoPratiche$ = new Subject<string>();

  filterForm !: FormGroup;
  loading = true;
  loadingError: any | null = null;
  isConfiguratore = false;

  tipologiePratica: TipoPratica[] = [];
  enti: Ente[] = [];
  activeFilter: any = null;

  columns: ICosmoTableColumn[] = [{
      name: 'label', label: 'Label filtro', field: 'label', canSort: false, canHide: false, canFilter: true,
    }, {
      name: 'nome', label: 'Nome Variabile', field: 'nome', canSort: false, canHide: false, canFilter: true,
    }, {
      name: 'ente', label: 'Ente', field: 'ente', canSort: false, canHide: false,  canFilter: true,
      valueExtractor: row => row.tipoPratica.ente?.nome ?? '--',
    }, {
      name: 'tipoPratica', label: 'Tipologia pratica', field: 'tipoPratica', canSort: false, canHide: false, canFilter: true,
      valueExtractor: row => row.tipoPratica?.descrizione ?? '--',
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
            expand: payload?.status?.expandedItemIdentifiers?.find(o => true) ?? null,
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
           expandedItemIdentifiers: params.expand ? [parseInt(params.expand, 10)] : null
          } as any;
          return out;
        })
      );
    }
  };

  @ViewChild('table') table: CosmoTableComponent | null = null;

  formatterEnte = (result: Ente) => result.nome;
  formatterTipoPratica = (result: TipoPratica) => result.descrizione;

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
          labelFiltro: new FormControl(null),
          tipoPratica: new FormControl(null),
          ente: new FormControl({value: this.isConfiguratore ? this.enti.find(x => x.id === response.utente.ente?.id) : null,
             disabled: this.isConfiguratore})
        }, [], []);

        this.filterForm.controls.tipoPratica?.valueChanges.subscribe(
          elem => {
            const raw = this.filterForm.getRawValue();
            if (elem && raw.ente && raw.ente.id &&
               !this.tipologiePratica?.find(x => x?.descrizione === elem?.descrizione && raw?.ente.id === x.ente?.id)){
              this.filterForm.get('ente')?.patchValue(null);
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

  modifica(elem: VariabileDiFiltro) {
    this.router.navigate(['modifica', elem.id], { relativeTo: this.route });
  }

  elimina(elem: VariabileDiFiltro) {
    this.modalService.scegli(
      this.translateService.instant('tipi_pratiche.dialogs.elimina.titolo'),
      [{
        testo: Utils.parseAndReplacePlaceholders(
          this.translateService.instant('common.eliminazione_messaggio'), [elem.nome ?? 'questa variablie di filtro']),
        classe: 'text-danger'
      }], [
        {testo: 'Elimina', valore: true, classe: 'btn-outline-danger', icona: 'far fa-trash-alt'},
        {testo: 'Annulla', dismiss: true, defaultFocus: true}
      ]
    ).then(() => {
      this.variabiliDiFiltroService.delete(Utils.require(elem.id)).subscribe(() => {
        this.refresh(false);
      }, failure => this.modalService.simpleError(Utils.toMessage(failure), failure.error.errore));
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
      this.logger.debug('loading page with search params variabili di filtro', payload);
      return this.variabiliDiFiltroService.getVariabiliDiFiltro(JSON.stringify(payload));
  }),
      map(response => {
      return {
          content: response.variabiliDiFiltro ?? [],
          number: response.pageInfo?.page,
          numberOfElements: response.variabiliDiFiltro?.length,
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
      output.filter.nomeEnte = {
        eq: utente.ente?.nome
      };
    }
    if (this.activeFilter?.labelFiltro){
      output.filter.labelFiltro = {
        ci: this.activeFilter.labelFiltro
      };
    }
    if (utente.profilo?.codice !== Constants.CODICE_PROFILO_CONFIGURAZIONE && this.activeFilter?.ente && this.activeFilter?.ente?.nome){
      output.filter.nomeEnte = {
        ci: this.activeFilter.ente.nome
      };
    }
    if (this.activeFilter?.tipoPratica && this.activeFilter?.tipoPratica?.descrizione){
      output.filter.descrizioneTipoPratica = {
        ci: this.activeFilter.tipoPratica.descrizione
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

  azzera(): void {

    this.filterForm.get('ente')?.patchValue(null);
    this.filterForm.get('tipoPratica')?.patchValue(null);
    this.filterForm.get('labelFiltro')?.patchValue(null);
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

  goBack(){
    this.securityService.getCurrentUser().subscribe(utente => {
      if (utente && utente.profilo && utente.profilo?.codice === Constants.CODICE_PROFILO_CONFIGURAZIONE){
        this.router.navigate(['/configurazione']);
      }else{
        this.router.navigate(['/amministrazione']);
      }
    });
  }

}
