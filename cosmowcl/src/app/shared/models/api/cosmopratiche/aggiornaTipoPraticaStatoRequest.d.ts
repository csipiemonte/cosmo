/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * Pratiche
 * API per la gestione pratiche 
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


/**
 * Model che descrive la richiesta di aggiornamento di uno stato associato ad un tipo pratica
 */
export interface AggiornaTipoPraticaStatoRequest { 
    codice: string;
    descrizione: string;
    classe?: string;
}

