/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * firma.v1
 * API per la gestione della firma digitale
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { EsitoVerificaFirma } from './esitoVerificaFirma.d';


/**
 * Model che contiene i campi relativi a infoVerificaFirma
 */
export interface InfoVerificaFirma { 
    codiceFiscaleFirmatario?: string;
    dataVerificaFirma?: string;
    dataApposizione?: string;
    dataApposizioneMarcaturaTemporale?: string;
    firmatario?: string;
    organizzazione?: string;
    infoVerificaFirme?: Array<InfoVerificaFirma>;
    esito: EsitoVerificaFirma;
    codiceErrore?: string;
}
