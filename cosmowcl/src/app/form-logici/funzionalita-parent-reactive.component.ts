/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Injector, Directive } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidatorFn } from '@angular/forms';
import { NGXLogger } from 'ngx-logger';
import { combineLatest, Observable, of } from 'rxjs';
import { delay, mergeMap, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Utils } from '../shared/utilities/utilities';
import { FunzionalitaParentComponent } from './funzionalita-parent.component';

@Directive()
export abstract class FunzionalitaParentReactiveComponent<INPUT, LOADED, OUTPUT> extends FunzionalitaParentComponent {

  public form?: FormGroup;

  public loading = 0;
  public loadingError: any | null = null;

  public debug = environment.debug;

  protected inputData!: INPUT;
  protected loadedData!: LOADED;

  constructor(public injector: Injector, protected logger: NGXLogger) {
    super(injector);
  }

  public get somethingDirty(): boolean {
    if (this.form?.dirty || this.dirty || this.checkDirty()) {
      return true;
    }
    return false;
  }

  // override to customize
  protected reloadOnRouteParams(): boolean {
    return true;
  }

  // override to set input triggers for reload (e.g. route params, ...)
  protected getInputData(): Observable<INPUT | null> {
    return of(null);
  }

  // override to build payload
  protected abstract buildPayload(formValue: any): OUTPUT;

  // override to check form validity outside of main form
  protected checkValidity(): boolean {
    return true;
  }

  // override to check form dirtiness outside of main form
  protected checkDirty(): boolean {
    return false;
  }

  // implement to submit form data
  protected abstract onSubmit(payload: OUTPUT): Observable<any>;

  // override to clean data outside of form
  protected onReset(): Observable<void> | void {
    // NOP
  }

  // implement to build the form
  protected abstract buildForm(inputData?: INPUT, loadedData?: LOADED): FormGroup | Observable<FormGroup>;

  // override to load more data when loading form
  protected loadData(inputData?: INPUT): Observable<LOADED> {
    return of(inputData as any as LOADED);
  }

  public submit() {
    this.onSubmitInternal();
  }

  onSubmitInternal() {
    if (!this.allValid) {
      this.logger.warn('cannot submit - data is not valid');
      return;
    }

    this.onSubmit(this.buildInternalPayload()).subscribe(() => {
      this.clearDirtyInternal();
    });
  }

  private clearDirtyInternal(): void {
    if (this.form) {
      this.form.markAsPristine();
    }
    this.dirty = false;
    this.markPristine();
  }

  get allValid(): boolean {
    if (!this.form?.valid) {
      return false;
    }
    try {
      if (!this.checkValidity()) {
        return false;
      }
    } catch (err) {
      this.logger.warn('error checking for form validity');
      return false;
    }
    return true;
  }

  reset(): Observable<void> {
    let formResponse = this.buildForm(this.inputData ?? undefined, this.loadedData ?? undefined);
    if (!(formResponse instanceof Observable)) {
      formResponse = of(formResponse);
    }
    return formResponse.pipe(
      tap(form => {
        this.form = form;
      }),
      mergeMap(() => {
        let onReset = this.onReset();
        if (!(onReset instanceof Observable)) {
          onReset = of(onReset);
        }
        return onReset;
      })
    );
  }

  ngOnInit(): void {
    const aggregatedInputData = combineLatest([
      this.getInputData()
    ]);

    aggregatedInputData.pipe(
      tap(() => {
        this.loading++;
        this.loadingError = null;
      }),
      delay(environment.httpMockDelay),
      mergeMap(inputData => {
        this.inputData = inputData[0] as INPUT;
        return this.loadData(this.inputData);
      }),
    ).subscribe(loadedData => {
      this.loadedData = loadedData;
      this.reset().subscribe(() => {
        this.loading--;
        this.sendData({});
      }, () => {
        this.loading--;
        this.sendData({});
      });
    }, failure => {
      this.loadingError = failure;
      this.loading--;
      this.sendData({});
    });
  }

  public getPayload(): OUTPUT {
    return this.buildInternalPayload();
  }

  private buildInternalPayload(): OUTPUT {
    if (!this.form) {
      throw new Error('Form non trovato');
    }
    return this.buildPayload(this.form.getRawValue());
  }

  isCtrlValid(name: string): boolean {
    if (!this.form) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return control.valid;
  }

  isInvalid(name: string): boolean {
    if (!this.form) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return control.invalid;
  }

  isTouched(name: string): boolean {
    if (!this.form) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return control.touched;
  }

  isDirty(name: string): boolean {
    if (!this.form) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return control.dirty;
  }

  isValidating(name: string): boolean {
    if (!this.form) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return (control as FormControl).status === 'PENDING';
  }

  displayValidating(name: string): boolean {
    if (!this.form) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return (control.touched || control.dirty) && (control as FormControl).status === 'PENDING';
  }

  displayInvalid(name: string): boolean {
    if (!this.form) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return (control.dirty || control.touched) && control.invalid;
  }

  getError(name: string | null, type: string): any {
    if (!this.form) {
      return false;
    }

    if (!name?.length) {
      return this.form.errors && this.form.errors[type] ?
        (this.form.errors[type] ?? true) : null;
    }

    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return control.errors && control.errors[type] ?
      (control.errors[type] ?? true) : null;
  }

  hasError(name: string | null, type: string): any {
    return this.getError(name, type) !== null;
  }

  hasValue(name: string): boolean {
    if (!this.form) {
      return false;
    }
    const control = this.resolveControl(name);
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return false;
    }
    return Utils.isNotBlank(control.value);
  }

  getValue(name: string): any {
    if (!this.form) {
      return null;
    }
    const control = this.resolveControl(name) as FormControl;
    if (!control) {
      this.logger.warn('no control found for path ' + name);
      return null;
    }
    return control?.value;
  }

  getControl(name: string): AbstractControl | undefined {
    if (!this.form) {
      return undefined;
    }
    return this.resolveControl(name) ?? undefined;
  }

  private resolveControl(name: string): AbstractControl | undefined {
    let actual: any = this.form;
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

  protected requiredIf(condition: (() => boolean)): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {

      if (condition === null || condition === undefined) {
        return null;
      }
      if (condition() !== true) {
        return null;
      }
      const value = input.value;

      if (value === null || value === undefined || (typeof value === 'string' && value.trim().length < 1)) {
        return {
          required: true
        };
      }
      return null;
    };
  }

  protected requireValueIf(requiredValue: any, condition: (() => boolean)): ValidatorFn {
    return (input: AbstractControl): { [key: string]: any } | null => {

      if (condition === null || condition === undefined) {
        return null;
      }
      if (condition() !== true) {
        return null;
      }
      const value = input.value;

      if (value === null || value === undefined || (typeof value === 'string' && value.trim().length < 1)) {
        return null;
      }

      let result;
      if (typeof requiredValue === 'function') {
        result = requiredValue(value);
      } else {
        result = (value === requiredValue);
      }

      if (!result) {
        return {
          requireValueIf: true
        };
      }
      return null;
    };
  }
}
