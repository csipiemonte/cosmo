/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * @deprecated utilizzare le classi generate sotto src/shared/models/api
 */
export interface Funzionalita{
    nomeFunzionalita: string;
    parametri: Parametro[];

}
/**
 * @deprecated utilizzare le classi generate sotto src/shared/models/api
 */
export interface Parametro{
    nome: string;
    valore: string;
}
