/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
export interface Commento {
    id: number;
    cfAutore: string;
    nomeAutore: string ;
    cognomeAutore: string ;
    messaggio: string;
    timestamp: Date;
}

export interface PaginaCommenti {
    elementi: Commento[];
}
