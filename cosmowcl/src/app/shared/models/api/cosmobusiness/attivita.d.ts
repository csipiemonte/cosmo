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
import { FunzionalitaAttivita } from './funzionalitaAttivita.d';
import { CampiTecnici } from './campiTecnici.d';
import { Assegnazione } from './assegnazione.d';


export interface Attivita { 
    id?: number;
    idPratica?: number;
    linkAttivita?: string;
    linkAttivitaEsterna?: string;
    nome?: string;
    descrizione?: string;
    evento?: string;
    assegnazione?: Array<Assegnazione>;
    parent?: string;
    dataCancellazione?: string;
    campiTecnici?: CampiTecnici;
    formKey?: string;
    esterna?: boolean;
    funzionalita?: Array<FunzionalitaAttivita>;
    gruppoAssegnatario?: string;
    hasChildren?: boolean;
}

