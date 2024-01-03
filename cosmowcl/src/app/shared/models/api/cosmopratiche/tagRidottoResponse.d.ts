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
import { TipoTag } from './tipoTag.d';


export interface TagRidottoResponse { 
    codice: string;
    descrizione?: string;
    tipoTag: TipoTag;
    warning?: string;
}

