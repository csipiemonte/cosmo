/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * firma.v1
 * API per la gestione della firma digitale
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { AttivitaEseguibileMassivamente } from './attivitaEseguibileMassivamente.d';
import { DocumentiTask } from './documentiTask.d';
import { CertificatoFirma } from './certificatoFirma.d';


export interface EsecuzioneMultiplaFirmaRequest { 
    certificato?: CertificatoFirma;
    otp?: string;
    tasks?: Array<AttivitaEseguibileMassivamente>;
    documentiTask?: Array<DocumentiTask>;
    note?: string;
    lockMgmt?: boolean;
    mandareAvantiProcesso: boolean;
}

