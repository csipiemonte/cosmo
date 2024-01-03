/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { HistoryEntry } from 'src/app/shared/models/router-history/historyEntry';
import { NavigationStart, NavigationEnd } from '@angular/router';

export interface RouterHistory {
  history: HistoryEntry[];
  currentIndex: number;
  event: NavigationStart | NavigationEnd | null;
  trigger?: 'imperative' | 'popstate' | 'hashchange';
  id: number;
  idToRestore?: number;
}
