/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, Validators, FormControl, AbstractControl, AsyncValidatorFn, ValidatorFn } from '@angular/forms';
import { CustomFormService } from '../../../shared/services/customForm.service';
import { CustomForm } from 'src/app/shared/models/api/cosmopratiche/customForm';
import { Observable, of } from 'rxjs';
import { Utils } from '../../../shared/utilities/utilities';
import { ActivatedRoute } from '@angular/router';
import { debounceTime, delay, distinctUntilChanged, map, switchMap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { TranslateService } from '@ngx-translate/core';
import { ModalService } from '../../../shared/services/modal.service';
import { ClipboardService } from 'ngx-clipboard';

@Component({
  selector: 'app-aggiungi-modifica-custom-form',
  templateUrl: './aggiungi-modifica-custom-form.component.html',
  styleUrls: ['./aggiungi-modifica-custom-form.component.scss']
})
export class AggiungiModificaCustomFormComponent implements OnInit {

  cfForm!: FormGroup;
  cf?: CustomForm | null = null;
  codiceCustomForm!: string;
  codiceCopied = false;

  constructor(
    private route: ActivatedRoute,
    private customFormService: CustomFormService,
    private translateService: TranslateService,
    private modalService: ModalService,
    private clipboardService: ClipboardService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params && params.id) {
        this.codiceCustomForm = params.id;
        this.customFormService.get(params.id).subscribe( cf => {
          if (cf) {
            this.cf = cf;
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

  private initForm() {
    let codice = '';
    let descrizione = '';
    let customForm = '';

    if (this.cf) {
      codice = this.cf.codice;
      descrizione = this.cf.descrizione ?? '';
      customForm =
       this.cf.customForm ?? '';
    }

    const validators: ValidatorFn = this.json();

    this.cfForm = new FormGroup({
      codice: new FormControl({value: codice, disabled: !this.isNuovo}, [
        Validators.required,
        Validators.maxLength(30),
      ], [
         this.checkConflictingFieldCodice('codice', 'eqic')
      ]),
      descrizione: new FormControl({value: descrizione, disabled: false}, [
        Validators.maxLength(100),
      ]),
      customForm: new FormControl({value: customForm, disabled: false}, [
        Validators.required,
        validators
      ]),
    });
  }

  get isNuovo(): boolean {
    return !this.cf;
  }

  checkConflictingFieldCodice(fieldName: string, clause: 'eq' | 'eqic' = 'eq'): AsyncValidatorFn {
    return (input: AbstractControl): Observable<{[key: string]: any} | null> => {
      const v = (input as FormControl).value;
      if (!v) {
        return of(null);
      }

      const requestPayload: any = {
        page: 0,
        size: 1,
        fields: 'id,descrizione,' + fieldName,
        filter: { codice: this.cf ? { ne: this.cf.codice } : undefined },
      };
      const fieldFilter: any = {};
      fieldFilter[clause] = v;
      requestPayload.filter[fieldName] = fieldFilter;

      return of(JSON.stringify(requestPayload)).pipe(
        debounceTime(500),
        distinctUntilChanged(),
        delay(environment.httpMockDelay),
        switchMap(filter => this.customFormService.getCustomForms(filter)),
        map(response => {
          if (response.customForms?.length) {
            return {
              conflict: { field: fieldName, other: response.customForms[0] }
            };
          }
          return null;
        })
      );
    };
  }

  hasValue(name: string): boolean {
    if (!this.cfForm) {
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
    if (!this.cfForm) {
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
    if (!this.cfForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.dirty || control.touched) && control.invalid;
  }

  displayValidating(name: string): boolean {
    if (!this.cfForm) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      return false;
    }
    return (control.touched || control.dirty) && (control as FormControl).status === 'PENDING';
  }

  private resolveControl(name: string): AbstractControl | undefined {
    let actual: any = this.cfForm;
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
    const payload: CustomForm = {
      codice: '', customForm: '', descrizione: ''
    };
    const raw = this.cfForm.getRawValue();
    payload.codice = raw.codice;
    if (raw.descrizione.length > 0) {
      payload.descrizione = raw.descrizione;
    }
    payload.customForm = raw.customForm;
    if (this.codiceCustomForm) {
      this.customFormService.update(this.codiceCustomForm, payload).subscribe(result => {
        if (result) {
          delay(environment.httpMockDelay);
          let messaggio = this.translateService.instant('custom_form.dialogs.modificato.messaggio');
          messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.codice ?? '']);
          this.modalService.info(this.translateService.instant('custom_form.dialogs.modificato.titolo'), messaggio)
            .then(() => {
              this.tornaIndietro();
            });
        }
      }, err => {
        this.modalService.simpleError(Utils.toMessage(err.error.title), err.error.errore);
      });
    }
    else {
      this.customFormService.create(payload).subscribe(result => {
        if (result) {
          delay(environment.httpMockDelay);
          let messaggio = this.translateService.instant('custom_form.dialogs.creato.messaggio');
          messaggio = Utils.parseAndReplacePlaceholders(messaggio, [result.codice ?? '']);
          this.modalService.info(this.translateService.instant('custom_form.dialogs.creato.titolo'), messaggio)
            .then(() => {
              this.tornaIndietro();
            });
        }
      }, err => {
        this.modalService.simpleError(Utils.toMessage(err.error.title), err.error.errore);
      });
    }
  }

  copyCodice(): void {
    if (!this.cf || !this.cf.codice?.length) {
      return;
    }
    this.clipboardService.copy(this.cf.codice);
    this.codiceCopied = true;
    setTimeout(() => this.codiceCopied = false, 3000);
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


}
