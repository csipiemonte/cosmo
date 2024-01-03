/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * @deprecated utilizzare le classi generate sotto src/shared/models/api
 */
export interface Notification {
    id: number;
    fruitore: { id: number, nomeApp?: string, url?: string };
    pratica?: {id: number , fruitore: any};
    descrizione?: string;
    arrivo: Date;
    scadenza?: Date;
    lettura?: Date;
    etichette: any;
}
