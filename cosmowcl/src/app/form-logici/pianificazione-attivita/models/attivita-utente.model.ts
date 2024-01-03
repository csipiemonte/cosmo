/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
export interface AttivitaUtente {
    id: number;
    nome: string;
    obbligatorio: boolean;
    soloUtenti?: boolean;
    numeroUtentiMax?: number;
    attivitaDaEscludere?: number[];
    selezioneUtentiDeiGruppi?: {type: string, valore: string, tag?: {type: string, valore: string}[]}[];
    selezioneUtentiDeiTag?: {type: string, valore: string}[];
}
