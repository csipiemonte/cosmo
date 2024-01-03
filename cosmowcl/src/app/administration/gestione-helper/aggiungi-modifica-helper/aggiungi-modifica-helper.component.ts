/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { forkJoin, Observable, of } from 'rxjs';
import { debounceTime, delay, distinctUntilChanged, finalize, map, switchMap } from 'rxjs/operators';
import { Constants } from 'src/app/shared/constants/constants';
import { CodiceModale } from 'src/app/shared/models/api/cosmonotifications/codiceModale';
import { CodicePagina } from 'src/app/shared/models/api/cosmonotifications/codicePagina';
import { CodiceTab } from 'src/app/shared/models/api/cosmonotifications/codiceTab';
import { Helper } from 'src/app/shared/models/api/cosmonotifications/helper';
import { CustomForm } from 'src/app/shared/models/api/cosmopratiche/customForm';
import { ConfigurazioniService } from 'src/app/shared/services/configurazioni.service';
import { HelperService } from 'src/app/shared/services/helper.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Utils } from 'src/app/shared/utilities/utilities';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-aggiungi-modifica-helper',
  templateUrl: './aggiungi-modifica-helper.component.html',
  styleUrls: ['./aggiungi-modifica-helper.component.scss']
})
export class AggiungiModificaHelperComponent implements OnInit {
  loading = 0;
  loadingError: any | null = null;
  helperForm!: FormGroup;
  helper?: Helper | null = null;
  toolbar: string[];
  maxSize = 0;
  codiciPagina?: CodicePagina[];
  codicePaginaSelezionato: CodicePagina | null = null;
  showCodiciTabs = false;
  showCodiciForms = false;
  showCodiciModalsPage = false;
  showCodiciModalsTab = false;
  codiciTabs?: CodiceTab[];
  codiciModalsPage?: CodiceModale[];
  codiciModalsPageTab?: CodiceModale[];
  codiceTabSelezionato: CodiceTab | null = null;
  codiceFormSelezionato: CustomForm | null = null;
  codiceModalePaginaSelezionato: CodiceModale | null = null;
  codiceModaleTabSelezionato: CodiceModale | null = null;
  modaliPagina?: CodiceModale[];
  modaliTab?: CodiceModale[];

  constructor(
    private route: ActivatedRoute,
    private helperService: HelperService,
    private configurazioniService: ConfigurazioniService,
    private translateService: TranslateService,
    private modalService: ModalService) {

      this.toolbar = ['|', 'insertTable', 'uploadImage', 'blockQuote'];

    }

    ngOnInit(): void {
      this.loading ++;
      this.loadingError = null;

      forkJoin({
        maxSize: this.configurazioniService.getConfigurazioneByChiave(Constants.PARAMETRI.HELPER.HTML_MAX_SIZE),
        codiciPagina: this.helperService.getCodiciPagina(),
      }).pipe(
        finalize(() => {
          this.route.params.subscribe(params => {
            if (params && params.id) {
              this.helperService.get(params.id).subscribe( h => {
                if (h) {
                  this.helper = h;

                  this.helperService.getCodiciTab(h.codicePagina?.codice ?? '').subscribe(res => {
                    if (res && res.length > 0) {
                      this.codiciTabs = res;
                      this.showCodiciTabs = true;
                    }
                    if (h.codiceTab && h.codiceTab.codice === 'custom-form') {
                      this.codiceFormSelezionato = h.codiceForm ?? null;
                      this.showCodiciForms = true;
                    }

                    if (h.codiceModale) {
                      const requestPayload: any = {
                        filter: {
                          codicePagina: {eq: h.codicePagina?.codice },
                          codiceTab: {eq: h.codiceTab?.codice}
                          }};
                      this.helperService.getCodiciModale(JSON.stringify(requestPayload)).subscribe(mod => {
                        if (!mod[0].codiceTab) {
                          this.codiceModalePaginaSelezionato = h.codiceModale ?? null;
                          this.codiciModalsPage = mod;
                          this.showCodiciModalsPage = true;
                          this.showCodiciModalsTab = false;
                          this.showCodiciTabs = false;

                        } else {
                          this.codiceModaleTabSelezionato = h.codiceModale ?? null;
                          this.codiciModalsPageTab = mod;
                          this.showCodiciModalsTab = true;
                          this.showCodiciModalsPage = false;
                        }
                        this.initForm();
                        this.loading--;
                      });
                    } else {
                      this.initForm();
                      this.loading--;
                    }
                  });
                }
              });
            } else {
              this.initForm();
              this.loading--;
            }
          });
        }),
      ).subscribe(result => {
          this.maxSize = +(result.maxSize ?? 0);
          this.codiciPagina = result.codiciPagina ?? [];

      });
    }


  tornaIndietro() {
    window.history.back();
  }

  onSubmit() {
    const payload: Helper = {
      codicePagina: undefined,
      codiceTab: undefined,
      codiceModale: undefined,
      html: ''
    };
    const raw = this.helperForm.getRawValue();
    payload.codicePagina = {codice: raw.codicePagina.codice, descrizione: raw.codicePagina.descrizione};
    if (!!raw.codiceTab) {
      payload.codiceTab = {codice: raw.codiceTab.codice};
    }
    if (!!raw.codiceForm && !!raw.codiceForm.entity) {
      payload.codiceForm = {codice: raw.codiceForm.entity.codice};
    }
    if (!!raw.codiceModaleTab) {
      payload.codiceModale = { codice: raw.codiceModaleTab.codice };
    }
    if (!!raw.codiceModalePagina) {
      payload.codiceModale = { codice: raw.codiceModalePagina.codice };
    }

    payload.html = raw.html;

    if (this.helper) {
      this.helperService.update(this.helper.id ?? 0, payload).subscribe(result => {
        if (result) {
          delay(environment.httpMockDelay);
          let messaggio = this.translateService.instant('helper.dialogs.modificato.messaggio');
          messaggio = Utils.parseAndReplacePlaceholders(messaggio, [raw.codicePagina?.descrizione ?? '']);
          this.modalService.info(this.translateService.instant(
            'helper.dialogs.modificato.titolo'), messaggio).then(
              () => {
                this.tornaIndietro();
              }, err => {
                this.modalService.simpleError(Utils.toMessage(err), err.error.errore);
          });
        }
      });
    }
    else {
      this.helperService.create(payload).subscribe(result => {
        if (result) {
          delay(environment.httpMockDelay);
          let messaggio = this.translateService.instant('helper.dialogs.creato.messaggio');
          messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.codicePagina?.descrizione ?? '']);
          this.modalService.info(this.translateService.instant(
            'helper.dialogs.creato.titolo'), messaggio).then(
              () => {
                this.tornaIndietro();
              }, err => {
                this.modalService.simpleError(Utils.toMessage(err), err.error.errore);
          });
        }
      });
    }
  }

  private initForm() {
    let codicePagina: CodicePagina | null = null;
    let html = '';
    let codiceTab: CodiceTab | null = null;
    let codiceForm: CustomForm | null = null;
    let codiceModalePagina: CodiceModale | null = null;
    let codiceModaleTab: CodiceModale | null = null;
    if (this.helper) {
      codicePagina = this.helper.codicePagina ?? null;
      html = this.helper.html ?? '';
      codiceTab = this.helper.codiceTab ?? null;
      codiceForm = this.helper.codiceForm ?? null;
      codiceModalePagina = this.helper.codiceModale ?? null;
      codiceModaleTab = this.helper.codiceModale ?? null;
      this.codicePaginaSelezionato = this.codiciPagina?.find(cp => cp.codice === codicePagina?.codice ) ?? null;
      this.codiceTabSelezionato = this.codiciTabs?.find(ct => ct.codice === codiceTab?.codice ) ?? null;
      this.codiceModalePaginaSelezionato = this.codiciModalsPage?.find(cmp => cmp.codice === codiceModalePagina?.codice) ?? null;
      this.codiceModaleTabSelezionato = this.codiciModalsPageTab?.find(cmpt => cmpt.codice === codiceModaleTab?.codice) ?? null;

    }

    this.helperForm = new FormGroup({
      codicePagina: new FormControl({value: codicePagina, disabled: !this.isNuovo}, [
        Validators.required
      ], [
         this.checkConflictingFieldCodicePagina('codicePagina', 'eqic')
      ]),
      codiceTab: new FormControl({value: codiceTab, disabled: !this.isNuovo}),
      codiceForm: new FormControl({value: { entity: codiceForm }, disabled: !this.isNuovo}),
      codiceModalePagina: new FormControl({value: codiceModalePagina, disabled: !this.isNuovo}),
      codiceModaleTab: new FormControl({value: codiceModaleTab, disabled: !this.isNuovo}),
      html: new FormControl({value: html, disabled: false}, [
        Validators.maxLength(this.maxSize),
        Validators.required
      ]),
    });

  }

  private checkConflictingFieldCodicePagina(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const requestPayload: any = {
        fields: 'id,codiceTab,codiceModale,' + fieldName,
        filter: { codicePagina: this.helper?.codicePagina?.codice ? { ne: this.helper.codicePagina?.codice } : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v.codice;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.helperService.getHelpers(filter)),
        map(response => {
          if (response.helpers?.length) {
            if (response.helpers.find(x => x.codiceTab == null && x.codiceModale == null)) {
              return {
                conflict: { field: fieldName, other: response.helpers[0] }
              };
            }
          }
          return null;
        })
      );
    };
  }

  private checkConflictingFieldCodiceTab(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const codicePaginaFilter = this.codicePaginaSelezionato?.codice ?
        this.codicePaginaSelezionato?.codice : this.helper?.codicePagina?.codice ?
          this.helper?.codicePagina?.codice : undefined;

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,codicePagina,codiceForm,codiceModale, ' + fieldName,
        sort: 'codiceModale DESC',
        filter: {
          codicePagina: codicePaginaFilter ? {eq: codicePaginaFilter } : undefined,
          codiceTab: this.helper?.codiceTab?.codice ? { eq: this.helper.codiceTab?.codice } : undefined,
          codiceModale: this.codiceModaleTabSelezionato?.codice ? {eq: this.codiceModaleTabSelezionato?.codice } : undefined},
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v.codice;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.helperService.getHelpers(filter)),
        map(response => {
          response.helpers = response?.helpers?.filter(x => x.codiceModale == null);
          if (response.helpers?.length &&
            !response.helpers.find((x: Helper) => x.codiceTab?.codice === 'custom-form')) {
            return {
              conflict: { field: fieldName, other: response.helpers[0] }
            };
          }
          return null;
        })
      );
    };
  }

  private checkConflictingFieldCodiceForm(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const codicePaginaFilter = this.codicePaginaSelezionato?.codice ?
        this.codicePaginaSelezionato?.codice : this.helper?.codicePagina?.codice ?
          this.helper?.codicePagina?.codice : undefined;

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,codicePagina,codiceTab,' + fieldName,
        filter: {
          codicePagina: codicePaginaFilter ? {eq: codicePaginaFilter } : undefined,
          codiceTab: this.helper?.codiceTab?.codice ? { eq: this.helper.codiceTab?.codice } : undefined,
          codiceForm: this.helper?.codiceForm?.codice ? { eq: this.helper.codiceForm.codice} : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v.entity.codice;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.helperService.getHelpers(filter)),
        map(response => {

          if (response.helpers?.length) {
            return {
              conflict: { field: fieldName, other: response.helpers[0] }
            };
          }
          return null;
        })
      );
    };
  }

  private checkConflictingFieldModaleTab(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const codiceModaleFilter = this.codiceModaleTabSelezionato?.codice ?
        this.codiceModaleTabSelezionato?.codice : this.helper?.codiceModale?.codice ?
          this.helper?.codiceModale?.codice : undefined;

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,codicePagina,codiceTab,' + fieldName,
        filter: {
          codicePagina: this.helper?.codicePagina?.codice ? {eq: this.helper.codicePagina?.codice } : undefined,
          codiceTab: this.helper?.codiceTab?.codice ? { eq: this.helper.codiceTab?.codice } : undefined,
          codiceModale: codiceModaleFilter ? { eq: codiceModaleFilter} : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v.codice;
      requestPayload.filter[fieldName] = fieldFilter;
      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.helperService.getHelpers(filter)),
        map(response => {
          if (response.helpers?.length) {
            return {
              conflict: { field: fieldName, other: response.helpers[0] }
            };
          }
          return null;
        })
      );
    };
  }

  private checkConflictingFieldModalePagina(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const codiceModaleFilter = this.codiceModalePaginaSelezionato?.codice ?
        this.codiceModalePaginaSelezionato?.codice : this.helper?.codiceModale?.codice ?
          this.helper?.codiceModale?.codice : undefined;

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,codicePagina,codiceTab,' + fieldName,
        filter: {
          codicePagina: this.helper?.codicePagina?.codice ? {eq: this.helper.codicePagina?.codice } : undefined,
          codiceTab: this.helper?.codiceTab?.codice ? { eq: this.helper.codiceTab?.codice } : undefined,
          codiceModale: codiceModaleFilter ? { eq: codiceModaleFilter} : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v.codice;
      requestPayload.filter[fieldName] = fieldFilter;
      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.helperService.getHelpers(filter)),
        map(response => {
          if (response.helpers?.length) {
            return {
              conflict: { field: fieldName, other: response.helpers[0] }
            };
          }
          return null;
        })
      );
    };
  }

  hasValue(name: string): boolean {
    if (!this.helperForm) {
      return false;
    }
    const control = this.resolveControl(name);

    if (!control) {
      return false;
    }
    return Utils.isNotBlank(control.value);
  }

  hasError(name: string, type: string): any {
    return this.getError(name, type) !== null;
  }

  getError(name: string, type: string): any {
    if (!this.helperForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return control.errors && control.errors[type] ?
      (control.errors[type] ?? true) : null;
  }

  displayInvalid(name: string): boolean {
    if (!this.helperForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.dirty || control.touched) && control.invalid;
  }

  displayValidating(name: string): boolean {
    if (!this.helperForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.touched || control.dirty) && (control as FormControl).status === 'PENDING';
  }

  private resolveControl(name: string): AbstractControl | undefined {
    let actual: any = this.helperForm;
    for (const token of name.split('.')) {
      const newControl = actual.controls[token];
      if (!newControl) {
        return undefined;
      }
      actual = newControl;
    }
    return actual as AbstractControl;
  }

  get isNuovo(): boolean {
    return !this.helper;
  }

  fillModalsPageList() {
    this.codiciModalsPage = this.modaliPagina;
    this.helperForm.setControl('codiceModalePagina', new FormControl({value: '', disabled: !this.isNuovo}, [],
    [this.checkConflictingFieldModalePagina('codiceModale', 'eqic')]));
  }

  fillModalsPageTabList() {
    this.codiciModalsPageTab = this.modaliTab;
    this.helperForm.setControl('codiceModaleTab', new FormControl({value: '', disabled: !this.isNuovo}, [],
    [this.checkConflictingFieldModaleTab('codiceModale', 'eqic')]));
  }

  fillTabsList() {
    this.helperService.getCodiciTab(this.codicePaginaSelezionato?.codice ?? '').subscribe( codiciTabs => {
      if (codiciTabs && codiciTabs.length > 0) {
        this.showCodiciTabs = true;
        this.codiciTabs = codiciTabs;
        this.helperForm.setControl('codiceTab', new FormControl({value: '', disabled: !this.isNuovo}, [
          Validators.required
        ], [this.checkConflictingFieldCodiceTab('codiceTab', 'eqic')
        ]));
      } else {
        this.showCodiciTabs = false;
        this.showCodiciForms = false;
        this.helperForm.removeControl('codiceTab');
        this.helperForm.removeControl('codiceForm');
        this.codiceTabSelezionato = null;
        this.codiceFormSelezionato = null;
      }
    });
  }

  fillFormsList() {
    if (this.codiceTabSelezionato?.codice === 'custom-form') {
      this.showCodiciForms = true;
      this.helperForm.setControl('codiceForm', new FormControl({value: '', disabled: !this.isNuovo}, [
        Validators.required
      ], [this.checkConflictingFieldCodiceForm('codiceForm', 'eqic')
      ]));

    } else {
      this.showCodiciForms = false;
      this.helperForm.removeControl('codiceForm');
      this.codiceFormSelezionato = null;
    }
  }

  aggiornaVisibiltaFiltriModali(target: any) {
    if (this.isNuovo) {
      switch (target.name) {
        case 'codicePagina':
          this.showCodiciForms = false;
          this.showCodiciModalsPage = false;
          this.showCodiciModalsTab = false;
          this.showCodiciTabs = false;
          this.codiceFormSelezionato = null;
          this.codiceModalePaginaSelezionato = null;
          this.codiceModaleTabSelezionato = null;
          this.codiceTabSelezionato = null;
          this.helperForm.controls.codiceModalePagina.setValue(null);
          const requestPayload: any = {
            filter: {
              codicePagina: {eq: this.codicePaginaSelezionato?.codice }
            }
          };
          this.helperService.getCodiciModale(JSON.stringify(requestPayload)).subscribe(modals => {
            this.modaliPagina = modals.filter(m => !m.codiceTab);
            this.fillTabsList();
            if (this.modaliPagina.length > 0) {
              this.fillModalsPageList();
            }
            this.codiciModalsPage = this.modaliPagina?.filter(cm => cm.codicePagina === this.codicePaginaSelezionato?.codice);
            if (this.codiciModalsPage && this.codiciModalsPage?.length > 0) {
              this.showCodiciModalsPage = true;
            } else {
              this.showCodiciModalsPage = false;
              this.helperForm.get('codiceModalePagina')?.setErrors(null);
              this.codiceModalePaginaSelezionato = null;
              this.helperForm.controls.codiceModalePagina.setValue(null);
            }
          });
          break;
        case 'codiceTab':
          this.showCodiciModalsPage = false;
          this.codiceModalePaginaSelezionato = null;
          this.helperForm.controls.codiceModalePagina.setValue(null);
          const payload: any = {
            filter: {
              codicePagina: {eq: this.codicePaginaSelezionato?.codice },
              codiceTab: {eq: this.codiceTabSelezionato?.codice}
            }
          };
          this.helperService.getCodiciModale(JSON.stringify(payload)).subscribe(modals => {
            this.modaliTab = modals.filter(m => m.codiceTab);
            if (this.modaliTab.length > 0) {
              this.fillModalsPageTabList();
            }
            this.codiciModalsPageTab = this.modaliTab?.filter(cm => cm.codiceTab === this.codiceTabSelezionato?.codice);
            if (this.codiciModalsPageTab && this.codiciModalsPageTab?.length > 0) {
              this.showCodiciModalsTab = true;
            } else {
              this.showCodiciModalsTab = false;
              this.helperForm.get('codiceModaleTab')?.setErrors(null);
              this.codiceModaleTabSelezionato = null;
              this.helperForm.controls.codiceModaleTab.setValue(null);
            }
            this.fillFormsList();
            });

          break;
        case 'codiceModalePagina':
          this.showCodiciModalsTab = false;
          this.showCodiciTabs = false;
          this.helperForm.get('codicePagina')?.setErrors(null);
          this.helperForm.get('codiceTab')?.setErrors(null);
          break;
        case 'codiceModaleTab':
          this.showCodiciModalsPage = false;
          this.helperForm.controls.codiceModalePagina.setValue(null);
          this.helperForm.get('codiceTab')?.setErrors(null);
          this.codiceModalePaginaSelezionato = null;
          break;
      }
    }
  }

  pulisciCampi() {
    this.showCodiciTabs = false;
    this.showCodiciModalsPage = false;
    this.showCodiciModalsTab = false;
    this.showCodiciForms = false;
    this.codicePaginaSelezionato = null;
    this.codiceTabSelezionato = null;
    this.codiceModalePaginaSelezionato = null;
    this.codiceModaleTabSelezionato = null;
    this.codiceFormSelezionato = null;
    this.helperForm.reset();
  }
}
