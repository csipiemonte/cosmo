/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { CustomFormField } from './custom-form-field';

export interface CustomFormComponentElement {
  components: CustomFormField[];
  width: number;
  offset: number;
  push: number;
  pull: number;
}
