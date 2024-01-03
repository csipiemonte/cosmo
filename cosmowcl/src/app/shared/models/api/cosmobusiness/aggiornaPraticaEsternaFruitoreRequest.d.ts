/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * cosmobusiness Fruitori
 * Api per i fruitori esterni
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { AggiornaPraticaEsternaAttivitaFruitoreRequest } from './aggiornaPraticaEsternaAttivitaFruitoreRequest.d';


export interface AggiornaPraticaEsternaFruitoreRequest { 
    codiceIpaEnte: string;
    tipoPratica?: string;
    stato?: string;
    riassunto?: string;
    linkPratica?: string;
    attivita?: Array<AggiornaPraticaEsternaAttivitaFruitoreRequest>;
    dataFinePratica?: string;
}

