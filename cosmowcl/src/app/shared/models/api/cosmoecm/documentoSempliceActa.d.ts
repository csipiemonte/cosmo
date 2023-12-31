/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/**
 * acta.v1
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { ClassificazioneActa } from './classificazioneActa.d';
import { ProtocolloActa } from './protocolloActa.d';
import { DocumentoFisicoActa } from './documentoFisicoActa.d';


/**
 * 
 */
export interface DocumentoSempliceActa { 
    id?: string;
    dbKey?: string;
    changeToken?: string;
    codiceBarre?: string;
    tipoDocFisico?: string;
    composizione?: string;
    multiplo?: boolean;
    registrato?: boolean;
    modificabile?: boolean;
    definitivo?: boolean;
    origineInterna?: boolean;
    analogico?: boolean;
    rappresentazioneDigitale?: boolean;
    daConservare?: boolean;
    prontoPerConservazione?: boolean;
    daConservareDopoIl?: string;
    daConservarePrimaDel?: string;
    conservato?: boolean;
    datiPersonali?: boolean;
    datiSensibili?: boolean;
    datiRiservati?: boolean;
    dataCreazione?: string;
    paroleChiave?: string;
    modSMSQuarantena?: boolean;
    congelato?: boolean;
    referenziabile?: boolean;
    autoreGiuridico?: Array<string>;
    autoreFisico?: Array<string>;
    scrittore?: Array<string>;
    originatore?: Array<string>;
    destinatarioGiuridico?: Array<string>;
    destinatarioFisico?: Array<string>;
    oggetto?: string;
    dataDocTopica?: string;
    dataDocCronica?: string;
    numRepertorio?: string;
    docConAllegati?: boolean;
    docAutenticato?: boolean;
    docAutenticatoCopiaAutenticata?: boolean;
    idStatoDiEfficacia?: string;
    idFormaDocumentaria?: string;
    vitalRecordCode?: string;
    idCorrente?: string;
    soggettoProduttore?: Array<string>;
    docAutentico?: boolean;
    docAutenticatoFirmaAutenticata?: boolean;
    applicativoAlimentante?: string;
    dataFinePubblicazione?: string;
    indiceClassificazioneEstesa?: string;
    idAnnotazioniList?: Array<string>;
    indiceClassificazione?: string;
    idProtocolloList?: Array<string>;
    documentiFisici?: Array<DocumentoFisicoActa>;
    protocolli?: Array<ProtocolloActa>;
    classificazioni?: Array<ClassificazioneActa>;
}

