/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { FormDefinition } from './form-definition.model';

export interface FormDefinitionResponse {
    data: FormDefinition[];
    total: number;
    start: number;
    sort: string;
    order: string;
    size: number;
}
