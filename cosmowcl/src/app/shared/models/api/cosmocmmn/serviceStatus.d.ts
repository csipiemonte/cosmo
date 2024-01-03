/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * common
 * API per ottenere lo stato dell\'applicativo ed i modelli comuni a tutte le API
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


/**
 * Model che contiene i campi che vengono restituiti per avere informazioni sullo stato dell\'applicativo
 */
export interface ServiceStatus { 
    component?: string;
    details?: object;
    enviroment?: string;
    message?: string;
    name?: string;
    product?: string;
    responseTime?: number;
    status: string;
    version?: string;
}

