/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
export interface Attivita {
  assegnatario: string;
  nome: string;
  dataOraInizio: Date | null;
  dataOraFine: Date | null;
  stato: string;
}
