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
import { StatoPraticaFruitore } from './statoPraticaFruitore.d';
import { AttivitaFruitore } from './attivitaFruitore.d';
import { UtenteFruitore } from './utenteFruitore.d';
import { DocumentoFruitore } from './documentoFruitore.d';
import { TipoPraticaFruitore } from './tipoPraticaFruitore.d';
import { MessaggioFruitore } from './messaggioFruitore.d';


/**
 * Tutti i contenuti della pratica aggiornati alla data odierna
 */
export interface AggiornaStatoPraticaRequest { 
    id: string;
    codiceIpaEnte?: string;
    tipo?: TipoPraticaFruitore;
    oggetto?: string;
    stato?: StatoPraticaFruitore;
    riassunto?: string;
    utenteCreazione?: UtenteFruitore;
    dataCreazione?: string;
    dataFine?: string;
    dataCambioStato?: string;
    dataAggiornamento?: string;
    /**
     * metadati della pratica in formato libero
     */
    metadati?: object;
    /**
     * array di tutte le attività della pratica in ordine cronologico, le attività concluse hanno dataFine not null
     */
    attivita?: Array<AttivitaFruitore>;
    /**
     * array in ordine cronologico dei commenti della pratica
     */
    commenti?: Array<MessaggioFruitore>;
    /**
     * array dei documenti della pratica
     */
    documenti?: Array<DocumentoFruitore>;
}

