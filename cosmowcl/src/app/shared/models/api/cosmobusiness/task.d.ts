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


/**
 * Modello per definizone task interfaccia flowable
 */
export interface Task { 
    id?: string;
    action?: string;
    assignee?: string;
    createTime?: string;
    delegationState?: string;
    description?: string;
    dueDate?: string;
    execution?: string;
    name?: string;
    category?: string;
    owner?: string;
    parentTaskId?: string;
    priority?: number;
    processDefinition?: string;
    processInstance?: string;
    suspended?: boolean;
    taskDefinitionKey?: string;
    url?: string;
    tenantId?: string;
    formKey?: string;
    processInstanceId?: string;
    variables?: Array<object>;
    cancellationDate?: string;
    subtasks?: Array<Task>;
}

