/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

export interface UtenteGruppoDTO {
  userId?: string;
  groupId?: string;
  type: UtenteGruppoType;
}

export enum UtenteGruppoType {
  UTENTE = 'UTENTE',
  GRUPPO = 'GRUPPO'
}
