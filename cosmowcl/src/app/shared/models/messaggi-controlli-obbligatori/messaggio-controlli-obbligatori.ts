/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
export interface MessaggioControlliObbligatori {
  tipoMessaggio: TipoMessaggio;
  messaggio: string;
  tipiDocumento?: string[];
}

export enum TipoMessaggio {
  ERROR = 1,
  WARNING = 0
}
