/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CustomFormComponentElement } from './custom-form-component-element';
import { CustomFormData } from './custom-form-data';
import { CustomFormValidate } from './custom-form-validate';

export interface CustomFormField {
  key: string;
  type: string;
  input: boolean;
  content?: string;
  defaultValue?: string;
  placeholder?: string;
  validate?: CustomFormValidate;
  data?: CustomFormData;
  dataSrc?: string;
  template?: string;
  valueProperty?: string;
  columns?: CustomFormComponentElement[];
  components?: CustomFormField[];
  rows?: CustomFormComponentElement[][];
  customOptions: any;
 }
