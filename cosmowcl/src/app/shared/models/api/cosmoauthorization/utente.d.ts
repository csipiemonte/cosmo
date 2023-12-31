/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * cw-profilazione
 * API dei servizi di profilazione usati dal common workspace
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { Gruppo } from './gruppo.d';
import { AssociazioneUtenteProfilo } from './associazioneUtenteProfilo.d';
import { AssociazioneEnteUtente } from './associazioneEnteUtente.d';
import { Preferenza } from './preferenza.d';


/**
 * Utente che utilizza l\'applicativo
 */
export interface Utente { 
    id?: number;
    codiceFiscale: string;
    nome: string;
    cognome: string;
    enti: Array<AssociazioneEnteUtente>;
    profili: Array<AssociazioneUtenteProfilo>;
    preferenze?: Array<Preferenza>;
    gruppi?: Array<Gruppo>;
}

