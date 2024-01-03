/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
export interface FormDefinition {
    id: string;
    url: string;
    name: string;
    key: string;
    description: string;
    version: number;
    resourceName: string;
    deploymentId: string;
    tenantId: string;
}
