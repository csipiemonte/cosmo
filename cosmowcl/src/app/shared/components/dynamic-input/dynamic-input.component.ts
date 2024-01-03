/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AfterViewInit, Component, ElementRef, forwardRef, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { AbstractControl, ControlValueAccessor, NG_VALUE_ACCESSOR, ValidatorFn } from '@angular/forms';
import Ajv from 'ajv';

@Component({
  selector: 'app-dynamic-input',
  templateUrl: './dynamic-input.component.html',
  styleUrls: ['./dynamic-input.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => DynamicInputComponent),
    multi: true
  }]
})
export class DynamicInputComponent implements OnInit, OnDestroy, ControlValueAccessor {

  @Input() type?: string;
  @Input() schema?: string;

  @Input() internalControlValue: any = null;
  @Input() disabled?: boolean;
  @ViewChild('nativeControl') nativeControl: ElementRef | null = null;
  @ViewChild('textareaInput') textareaInput: ElementRef | null = null;

  options = {
    step: 1,
    min: 0,
    max: 10000000
  };

  constructor() { }

  get render(): boolean {
    return !!this.type;
  }

  get isDisabled(): boolean {
    return this.disabled ?? false;
  }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }

  focus() {
    if (this.nativeControl?.nativeElement) {
      this.nativeControl.nativeElement.focus();
    }
  }

  propagateChange = (_: any) => {};
  propagateTouched = (_: any) => {};

  registerOnChange(fn: any) {
    this.propagateChange = fn;
  }

  registerOnTouched(fn: any) {
    this.propagateTouched = fn;
  }

  get isObject(): boolean {
    return 'object' === this.type;
  }

  get controlValue() {
    return this.internalControlValue;
  }

  set controlValue(val) {
    this.internalControlValue = val;
    this.propagateChangeInternal(this.internalControlValue);
  }

  writeValue(value: any) {
    if (value !== undefined) {
      this.internalControlValue = value;
    }
  }

  onBlur($event: any) {
    if (!!this.propagateTouched) {
      this.propagateTouched($event);
    }
  }

  onChange($event: any) {
    this.propagateChangeInternal(this.internalControlValue);
  }

  onChangeTextarea($event: any) {
    if (this.textareaInput) {
      this.textareaInput.nativeElement.style.height = 'auto';
      this.textareaInput.nativeElement.style.height = `${this.textareaInput.nativeElement.scrollHeight}px`;
    }

    this.onChange($event);
  }

  onKeyup($event: any) {
    // NOP
  }

  resizeTextArea() {
    if (this.textareaInput) {
      this.textareaInput.nativeElement.style.height = 'auto';
      this.textareaInput.nativeElement.style.height = `${this.textareaInput.nativeElement.scrollHeight}px`;
    }
  }

  private propagateChangeInternal(val: any) {
    // if is json, validate schema
    if (this.isObject && this.schema && !this.validSchema(val)) {
      console.warn('schema validation failed in dynamic input');
      return;
    }
    this.propagateChange(this.internalControlValue);
  }

  onFocusOut(event: any): void {
    // NOP
  }

  onFocus(event: any): void {
    // NOP
  }

  onClick(event: any): void {
    // NOP
  }

  validSchema(raw: string): boolean {
    if (!this.schema) {
      throw new Error('cannot validate without schema');
    }
    const ajv = new Ajv({strict: false}); // options can be passed, e.g. {allErrors: true}
    let validate: any;
    try {
      validate = ajv.compile(JSON.parse(this.schema));
    } catch (err) {
      console.warn('invalid schema for validation', err);
      return false;
    }
    const valid = validate(this.internalControlValue);
    return valid;
  }
}
