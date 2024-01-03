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
import { RiferimentoAttivita } from './riferimentoAttivita.d';
import { RiferimentoUtente } from './riferimentoUtente.d';
import { StatoAttivita } from './statoAttivita.d';
import { RiferimentoGruppo } from './riferimentoGruppo.d';


/**
 * Model che descrive un\'attivita\' di una pratica
 */
export interface AttivitaStoricoPratica { 
    attivita: RiferimentoAttivita;
    inizio: string;
    fine?: string;
    utentiCoinvolti?: Array<RiferimentoUtente>;
    gruppiCoinvolti?: Array<RiferimentoGruppo>;
    stato?: StatoAttivita;
    esecutore?: RiferimentoUtente;
}

