/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { FieldType } from '../field-type.enum';
import { Option } from './option.model';

export interface FormField {
    fieldType: FieldType;
    id: string;
    name: string | null;
    type: string;
    value: string | null;
    required: boolean;
    readOnly: boolean;
    placeholder: string | null;
    hasEmptyValue?: boolean;
    options?: Option[];
}
