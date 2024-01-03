/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * cosmobusiness-formlogici.v1
 * API per la gestione dei form logici
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


export interface TipologiaParametroFormLogico { 
    codice: string;
    descrizione?: string;
    obbligatorio?: boolean;
    tipo?: string;
    schema?: string;
    valoreDefault?: string;
}

