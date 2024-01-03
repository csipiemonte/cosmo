/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { AttivitaAssegnazione } from './attivita-assegnazione.model';

export interface OutputAssegnazione {
    attivitaAssegnazione: AttivitaAssegnazione[];
    stato: string;
}
