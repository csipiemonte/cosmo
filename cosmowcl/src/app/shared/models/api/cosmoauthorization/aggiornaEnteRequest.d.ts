/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * cw-profilazione
 * API dei servizi di profilazione usati dal common workspace
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { CreaEnteRequestNuoviUtentiAmministratori } from './creaEnteRequestNuoviUtentiAmministratori.d';
import { CreaEnteRequestUtentiAmministratori } from './creaEnteRequestUtentiAmministratori.d';


export interface AggiornaEnteRequest { 
    nome: string;
    codiceProfiloDefault?: string;
    utentiAmministratori?: Array<CreaEnteRequestUtentiAmministratori>;
    nuoviUtentiAmministratori?: Array<CreaEnteRequestNuoviUtentiAmministratori>;
}

