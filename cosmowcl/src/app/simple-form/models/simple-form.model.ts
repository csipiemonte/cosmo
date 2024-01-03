/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { FormField } from './form-field.model';

export interface SimpleForm {
    id: string;
    name: string;
    key: string;
    version: number;
    url: string;
    fields: FormField[];
}
