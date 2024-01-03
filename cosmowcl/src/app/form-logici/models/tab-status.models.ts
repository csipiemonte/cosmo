/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Observable } from 'rxjs';

export interface TabBadge {
    text?: string;
    class?: string;
    icon?: string;
    tooltip?: string;
}

export type TabLifecycleCallback = boolean | void | Observable<boolean | void>;
