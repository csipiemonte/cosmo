/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * documento
 * API per la gestione dei documenti
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { PageInfo } from './pageInfo.d';
import { RaggruppamentoFormatoFile } from './raggruppamentoFormatoFile.d';


export interface RaggruppamentoFormatiFileResponse { 
    formatiFile?: Array<RaggruppamentoFormatoFile>;
    pageInfo?: PageInfo;
}

