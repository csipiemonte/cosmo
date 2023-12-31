/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * Pratiche fruitori
 * API per la gestione pratiche da parte dei fruitori esterni
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


/**
 * Model contenente i dati relativi alla relazione pratica
 */
export interface RelazionePratica { 
    /**
     * id esterno della pratica verso cui è diretta una relazione
     */
    idPraticaExtA: string;
    /**
     * tipo della relazione
     */
    tipoRelazione: string;
    /**
     * data di inizio validità della relazione
     */
    dtInizioValidita?: string;
    /**
     * data di fine validità della relazione
     */
    dtFineValidita?: string;
}

