/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Input, Directive } from '@angular/core';
import { AbstractControl, FormGroup } from '@angular/forms';
import { FieldNames } from '../field-names';
import { FormField } from '../models/form-field.model';

@Directive()
export class ParentControl {
    @Input()
    formField!: FormField;

    @Input()
    controls: FormGroup | null = null;

    @Input()
    controlIndex = 0;

    readonly FieldNames = FieldNames;

    getUniqueId(): string {
        const stringArr = [];
        for (let i = 0; i < 4; i++){
          // tslint:disable-next-line:no-bitwise
          const S4 = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
          stringArr.push(S4);
        }
        return stringArr.join('-');
      }

    get formControl(): AbstractControl | null {
      let control: AbstractControl | null = null;
      if (this.controls && this.controlIndex) {
        control = this.controls.controls[this.controlIndex];
      }
      return control;
    }
}
