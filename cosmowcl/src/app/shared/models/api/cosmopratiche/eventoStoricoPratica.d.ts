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
import { RiferimentoFruitore } from './riferimentoFruitore.d';
import { RiferimentoAttivita } from './riferimentoAttivita.d';
import { RiferimentoUtente } from './riferimentoUtente.d';
import { RiferimentoGruppo } from './riferimentoGruppo.d';


/**
 * 
 */
export interface EventoStoricoPratica { 
    tipo: string;
    timestamp: string;
    attivita?: RiferimentoAttivita;
    utente?: RiferimentoUtente;
    fruitore?: RiferimentoFruitore;
    utenteCoinvolto?: RiferimentoUtente;
    gruppoCoinvolto?: RiferimentoGruppo;
    descrizione?: string;
}

