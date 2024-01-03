/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * documento
 * API per la gestione documenti da parte dei fruitori esterni
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


/**
 * Model che contiene i campi relativi al documento da creare tramite link
 */
export interface CreaDocumentoLinkFruitoreRequest { 
    id: string;
    idPadre?: string;
    codiceTipo: string;
    titolo?: string;
    descrizione?: string;
    autore?: string;
    nomeFile: string;
    link: string;
}

