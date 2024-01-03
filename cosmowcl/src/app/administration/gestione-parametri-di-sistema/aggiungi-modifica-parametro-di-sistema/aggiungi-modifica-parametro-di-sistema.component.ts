/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, HostListener, OnInit } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, of } from 'rxjs';
import { ComponentCanDeactivate } from 'src/app/shared/guards/can-deactivate.guard';
import { ModalService } from 'src/app/shared/services/modal.service';
import { TranslateService } from '@ngx-translate/core';
import { GestioneParametriDiSistemaService } from 'src/app/shared/services/gestione-parametri-di-sistema.service';
import { ParametroDiSistema } from 'src/app/shared/models/api/cosmoauthorization/parametroDiSistema';
import { debounceTime, delay, distinctUntilChanged, map, switchMap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Utils } from '../../../shared/utilities/utilities';
import { AggiornaParametroDiSistemaRequest } from 'src/app/shared/models/api/cosmoauthorization/aggiornaParametroDiSistemaRequest';
import { CreaParametroDiSistemaRequest } from 'src/app/shared/models/api/cosmoauthorization/creaParametroDiSistemaRequest';
import { Constants } from 'src/app/shared/constants/constants';

@Component({
  selector: 'app-aggiungi-modifica-parametro-di-sistema',
  templateUrl: './aggiungi-modifica-parametro-di-sistema.component.html',
  styleUrls: ['./aggiungi-modifica-parametro-di-sistema.component.scss']
})
export class AggiungiModificaParametroDiSistemaComponent implements OnInit, ComponentCanDeactivate {

  psForm!: FormGroup;
  ps: ParametroDiSistema | null = null;
  dragAndDropIsTouched = false;
  chiaveParametroSistema?: string;

  constructor(
    private route: ActivatedRoute,
    private gestioneParametriDiSistemaService: GestioneParametriDiSistemaService,
    private modalService: ModalService,
    private translateService: TranslateService,
  ) { }
  // @HostListener allows us to also guard against browser refresh, close, etc.
  @HostListener('window:beforeunload')
  canDeactivate(): Observable<boolean> | boolean {
    // insert logic to check if there are pending changes here;
    // returning true will navigate without confirmation
    // returning false will show a confirm dialog before navigating away
    if (this.dragAndDropIsTouched || this.psForm?.dirty) {
      return false;
    }
    return true;
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params && params.chiave) {
        this.chiaveParametroSistema = params.chiave;
        this.gestioneParametriDiSistemaService.getParametroDiSistema(params.chiave).subscribe(ps => {
          if (ps) {
            this.ps = ps;
            this.initForm();
          }
        });
      } else {
        this.initForm();
      }
    });
  }

  tornaIndietro() {
    window.history.back();
  }

  initForm() {
    let chiave = '';
    let valore = '';
    let descrizione = '';

    if (this.ps) {
      chiave = this.ps.chiave ?? '';
      valore = this.ps.valore ?? '';
      descrizione = this.ps.descrizione ?? '';
    }

    this.psForm = new FormGroup({
      chiave: new FormControl({ value: chiave, disabled: !this.isNuovo }, [
        Validators.required,
        Validators.maxLength(20),
      ], [
        this.checkConflictingFieldChiave('chiave', 'eqic')
      ]),
      valore: new FormControl({ value: valore, disabled: false }, [
        Validators.required,
        Validators.maxLength(255),
      ]),
      descrizione: new FormControl({ value: descrizione, disabled: false }, [
        Validators.maxLength(500),
      ]),
    });
  }
  checkConflictingFieldChiave(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{ [key: string]: any } | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'chiave,descrizione,' + fieldName,
        filter: { chiave: this.ps ? { ne: this.ps.chiave } : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.gestioneParametriDiSistemaService.getParametriDiSistema(filter)),
        map(response => {
          if (response.parametriDiSistema?.length) {
            return {
              conflict: { field: fieldName, other: response.parametriDiSistema[0] }
            };
          }
          return null;
        })
      );
    };
  }

  get isNuovo(): boolean {
    return !this.ps;
  }

  hasValue(name: string): boolean {
    if (!this.psForm) {
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
    if (!this.psForm) {
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
    if (!this.psForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.dirty || control.touched) && control.invalid;
  }

  displayValidating(name: string): boolean {
    if (!this.psForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.touched || control.dirty) && (control as FormControl).status === 'PENDING';
  }

  private resolveControl(name: string): AbstractControl | undefined {
    let actual: any = this.psForm;
    for (const token of name.split('.')) {
      const newControl = actual.controls[token];
      if (!newControl) {
        return undefined;
      }
      actual = newControl;
    }
    return actual as AbstractControl;
  }

  onSubmit() {

    const linkAssistenza = Constants.EMAIL_ASSISTENZA;
    let messaggio = this.translateService.instant('gestione_parametri_di_sistema.messaggio_riavvio');
    messaggio = Utils.parseAndReplacePlaceholders(messaggio, [linkAssistenza, linkAssistenza]);


    this.modalService.htmlModal(this.translateService.instant('gestione_parametri_di_sistema.form_gestione_parametri_di_sistema'),
    messaggio).then(
      () => {
        if (this.ps) {
          this.submitAggiorna();
        }
        else {
          this.submitCrea();
        }
      }
    ).catch(() => { });
  }

  submitCrea() {
    this.gestioneParametriDiSistemaService.create(this.buildPayloadCreazione()).subscribe(
      result => {
        this.clearDirty();
        let messaggio = this.translateService.instant('gestione_parametri_di_sistema.dialogs.creato.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result?.chiave ?? '']);
        this.modalService.info(this.translateService.instant('gestione_parametri_di_sistema.dialogs.creato.titolo'),
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

  submitAggiorna() {
    if (!this.chiaveParametroSistema) {
      return;
    }
    this.gestioneParametriDiSistemaService.update(this.chiaveParametroSistema, this.buildPayloadModifica()).subscribe(
      result => {
        this.clearDirty();
        let messaggio = this.translateService.instant('gestione_parametri_di_sistema.dialogs.modificato.messaggio');
        messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.chiave ?? '']);
        this.modalService.info(this.translateService.instant('gestione_parametri_di_sistema.dialogs.modificato.titolo'),
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

  buildPayloadCreazione(): CreaParametroDiSistemaRequest {
    if (!this.psForm) {
      throw new Error('Form non trovato');
    }
    const raw = this.psForm.getRawValue();

    const payload: CreaParametroDiSistemaRequest = {
      chiave: raw.chiave,
      valore: raw.valore,
      descrizione: raw.descrizione
    };

    return payload;
  }

  buildPayloadModifica(): AggiornaParametroDiSistemaRequest {
    if (!this.psForm) {
      throw new Error('Form non trovato');
    }
    const raw = this.psForm.getRawValue();

    const payload: AggiornaParametroDiSistemaRequest = {
      chiave: raw.chiave,
      valore: raw.valore,
      descrizione: raw.descrizione
    };

    return payload;
  }

  clearDirty(): void {
    this.dragAndDropIsTouched = false;
    if (this.psForm) {
      this.psForm.markAsPristine();
    }
  }

}
