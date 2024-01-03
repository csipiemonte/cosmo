/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Type } from '@angular/core';

export interface WidgetConfig {
    widget: Type<any> ;
    colsMin: number;
    colsMax: number;
    enabled: boolean;
    name: string;
    descrizione: string;
    cols?: number;
}
