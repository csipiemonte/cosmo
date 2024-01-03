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
import { UseCase } from './useCase.d';


/**
 * Ruolo di un utente all\'interno dell\'applicativo
 */
export interface Profilo { 
    id?: number;
    codice: string;
    descrizione?: string;
    useCases?: Array<UseCase>;
}

