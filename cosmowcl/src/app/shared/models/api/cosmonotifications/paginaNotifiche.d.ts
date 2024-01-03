/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * notifications
 * api per cosmo notifica 
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { PageInfo } from './pageInfo.d';
import { Notifica } from './notifica.d';


/**
 * modello del servizio di notifica
 */
export interface PaginaNotifiche { 
    elementi?: Array<Notifica>;
    info?: PageInfo;
    totaleNonLette?: number;
}

