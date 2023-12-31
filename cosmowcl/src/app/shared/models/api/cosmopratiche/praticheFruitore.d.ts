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
import { TagRidotto } from './tagRidotto.d';
import { AttivitaFruitore } from './attivitaFruitore.d';


export interface PraticheFruitore { 
    idPraticaExt: string;
    apiManager: string;
    oggetto: string;
    statoPratica?: string;
    tipoPratica: string;
    riassunto?: string;
    dataCreazione?: string;
    utenteCreazione?: string;
    attivita?: Array<AttivitaFruitore>;
    tag?: Array<TagRidotto>;
}

