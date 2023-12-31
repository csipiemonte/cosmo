/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * session.v2
 * Servizi per la gestione della sessione
 *
 * The version of the OpenAPI document: 2.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { UserInfoProfilo } from './userInfoProfilo.d';
import { UserInfoEnte } from './userInfoEnte.d';
import { UserInfoGruppo } from './userInfoGruppo.d';
import { Preferenza } from './preferenza.d';


export interface UserInfo { 
    anonimo?: boolean;
    nome?: string;
    cognome?: string;
    codiceFiscale?: string;
    email?: string;
    telefono?: string;
    ente?: UserInfoEnte;
    profilo?: UserInfoProfilo;
    gruppi?: Array<UserInfoGruppo>;
    preferenze?: Array<Preferenza>;
    fineValidita?: string;
    hashIdentita?: string;
    accessoDiretto?: boolean;
}

