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


export interface Processo { 
    id?: string;
    url?: string;
    name?: string;
    businessKey?: string;
    suspended?: boolean;
    ended?: boolean;
    processDefinitionId?: string;
    processDefinitionUrl?: string;
    processDefinitionName?: string;
    processDefinitionDescription?: string;
    activityId?: string;
    startUserId?: string;
    startTime?: string;
    tenantId?: string;
    completed?: boolean;
}

