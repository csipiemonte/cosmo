/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
export interface RouterHistoryInfo {
  url: string;
  data: { descrizione: string, root?: boolean, isTab?: boolean};
  filter?: any;
}
