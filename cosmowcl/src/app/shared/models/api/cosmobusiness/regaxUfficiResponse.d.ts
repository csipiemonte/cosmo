/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * cosmobusiness callback delibere
 * Api per la gestione dei callback per creazione e aggiornamento delle delibere
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { RegaxErrore } from './regaxErrore.d';
import { RegaxUffici } from './regaxUffici.d';


/**
 * response per creazione uffici
 */
export interface RegaxUfficiResponse { 
    success?: boolean;
    version?: string;
    uffici?: RegaxUffici;
    statusCode?: number;
    error?: RegaxErrore;
}

