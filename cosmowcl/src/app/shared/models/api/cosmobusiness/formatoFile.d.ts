/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * cosmobusiness
 * Api per cosmobusiness
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { FormatoFileProfiloFeqTipoFirma } from './formatoFileProfiloFeqTipoFirma.d';


/**
 * Model che contiene informazioni relative al formato del file
 */
export interface FormatoFile { 
    codice?: string;
    descrizione?: string;
    icona?: string;
    mimeType?: string;
    supportaAnteprima?: boolean;
    formatoFileProfiloFeqTipoFirma?: Array<FormatoFileProfiloFeqTipoFirma>;
}

