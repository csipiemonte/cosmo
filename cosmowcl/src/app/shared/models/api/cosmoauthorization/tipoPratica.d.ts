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
import { TipoDocumento } from './tipoDocumento.d';
import { CustomForm } from './customForm.d';
import { TipoOTP } from './tipoOTP.d';
import { ProfiloFEQ } from './profiloFEQ.d';
import { SceltaMarcaTemporale } from './sceltaMarcaTemporale.d';
import { TabsDettaglio } from './tabsDettaglio.d';
import { RiferimentoEnte } from './riferimentoEnte.d';
import { RiferimentoGruppo } from './riferimentoGruppo.d';
import { EnteCertificatore } from './enteCertificatore.d';
import { StatoPratica } from './statoPratica.d';
import { TipoCredenzialiFirma } from './tipoCredenzialiFirma.d';
import { TrasformazioneDatiPratica } from './trasformazioneDatiPratica.d';


export interface TipoPratica { 
    codice: string;
    descrizione?: string;
    processDefinitionKey?: string;
    caseDefinitionKey?: string;
    codiceApplicazioneStardas?: string;
    ente?: RiferimentoEnte;
    stati?: Array<StatoPratica>;
    tipiDocumento?: Array<TipoDocumento>;
    creabileDaInterfaccia: boolean;
    creabileDaServizio: boolean;
    trasformazioni?: Array<TrasformazioneDatiPratica>;
    customForm?: CustomForm;
    responsabileTrattamentoStardas?: string;
    overrideResponsabileTrattamento?: boolean;
    annullabile?: boolean;
    condivisibile?: boolean;
    assegnabile?: boolean;
    tabsDettaglio?: Array<TabsDettaglio>;
    codiceFruitoreStardas?: string;
    overrideFruitoreDefault?: boolean;
    gruppoCreatore?: RiferimentoGruppo;
    gruppoSupervisore?: RiferimentoGruppo;
    registrazioneStilo?: string;
    tipoUnitaDocumentariaStilo?: string;
    immagine?: string;
    enteCertificatore?: EnteCertificatore;
    tipoCredenziale?: TipoCredenzialiFirma;
    tipoOtp?: TipoOTP;
    profiloFEQ?: ProfiloFEQ;
    sceltaMarcaTemporale?: SceltaMarcaTemporale;
}

