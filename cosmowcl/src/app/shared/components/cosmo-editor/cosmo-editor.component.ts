/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Component, ElementRef, forwardRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Subject } from 'rxjs';

// import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import * as ClassicEditor from 'src/app/shared/cosmo-ckeditor5/ckeditor.js';

@Component({
  selector: 'app-cosmo-editor',
  templateUrl: './cosmo-editor.component.html',
  styleUrls: ['./cosmo-editor.component.scss'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => CosmoEditorComponent),
    multi: true
  }]
})
export class CosmoEditorComponent implements OnInit, OnDestroy, ControlValueAccessor {

  @Input() disabled?: boolean;
  @Input() maxlength?: number;
  @Input() maxlengthByte?: number;
  @Input() placeholder?: string;
  @Input() customToolbar?: string[];

  @Input() internalControlValue: string | null = null;

  @ViewChild('nativeControl') nativeControl: ElementRef | null = null;

  public editor = ClassicEditor;

  public editorConfig = {toolbar: { items: [] as string[], shouldNotGroupWhenFull: true }, placeholder: '', fontColor: {}};

  focus$ = new Subject<string>();
  click$ = new Subject<string>();

  constructor() {
    this.editorConfig = {
      // toolbar: [ 'bold', 'italic', 'underline', '|', 'fontColor' ],
      toolbar: {
        items: [
          'heading', '|',
          'fontsize', '|',
          'fontColor', '|',
          'bold', 'italic', 'underline', '|',
          'link', '|',
          'bulletedList', '|',
          'undo', 'redo'
        ],
        shouldNotGroupWhenFull: true
      },
      placeholder: this.placeholder ?? 'Inserisci il testo qui',
      fontColor: {
        colors: [
          { color: '#00FFFF', label: 'Azzurro' },
          { color: '#000000', label: 'Nero' },
          { color: '#0000FF', label: 'Blu' },
          { color: '#FF00FF', label: 'Fucsia' },
          { color: '#808080', label: 'Grigio' },
          { color: '#008000', label: 'Verde' },
          { color: '#00FF00', label: 'Verde fluo' },
          { color: '#800000', label: 'Marrone' },
          { color: '#000080', label: 'Blu' },
          { color: '#808000', label: 'Oliva' },
          { color: '#800080', label: 'Viola' },
          { color: '#FF0000', label: 'Rosso' },
          { color: '#C0C0C0', label: 'Argento' },
          { color: '#008080', label: 'Verde acqua' },
          { color: '#FFFF00', label: 'Giallo' },
          { color: '#FFFFFF', label: 'Bianco', hasBorder: true },
        ]
      }
    };
  }

  get isDisabled(): boolean {
    return this.disabled ?? false;
  }

  clear(): void {
    this.writeValue(null);
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

  blur($event: any) {
    if (!!this.propagateTouched) {
      this.propagateTouched($event);
    }
  }

  change($event: any) {

    this.propagateChange(this.internalControlValue);
  }

  get controlValue() {
    return this.internalControlValue;
  }

  set controlValue(val) {
    this.internalControlValue = val;
    this.propagateChange(this.internalControlValue);
  }

  writeValue(value: string | null) {
    if (value !== undefined) {
      this.internalControlValue = value;
    }
  }

  ngOnInit(): void {
     // aggiunta toolbar specifici
     if (!!this.customToolbar) {
      this.editorConfig.toolbar.items = this.editorConfig.toolbar.items.concat(this.customToolbar);
    }
  }

  ngOnDestroy(): void {
  }

  public percentualeCaratteri(): number | null {
    if (!this.maxlength) {
      return null;
    }
    const len = (this.internalControlValue ?? '').length;
    return Math.round(100.0 * len / this.maxlength);
  }

  public percentualeGrandezza(): number | null {
    if (!this.maxlengthByte) {
      return null;
    }
    const byteSize = new Blob([this.internalControlValue ?? '']).size;
    return Math.round(100.0 * byteSize / this.maxlengthByte);
  }


}
