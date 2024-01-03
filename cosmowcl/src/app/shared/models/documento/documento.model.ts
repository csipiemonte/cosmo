/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import { AnteprimaContenutoDocumento } from '../api/cosmoecm/anteprimaContenutoDocumento';
import { ContenutoDocumento } from '../api/cosmoecm/contenutoDocumento';
import { Documento } from '../api/cosmoecm/documento';
import { PageInfo } from '../api/cosmoecm/pageInfo';

export interface DocumentoDTO extends Documento {
    contenutoEffettivo?: ContenutoDocumento;
    contenutoTemporaneo?: ContenutoDocumento;
    contenutoOriginale?: ContenutoDocumento;
    contenutoSbustato?: ContenutoDocumento;
    contenutoFirmato?: ContenutoDocumento;
    anteprimaEffettiva?: AnteprimaContenutoDocumento;
    ultimoContenutoFirmaDigitale?: ContenutoDocumento;
    ultimoContenutoFEA?: ContenutoDocumento;
    ultimoContenutoSigilloElettronico?: ContenutoDocumento;

}

export interface DocumentiDTOResponse {
    documenti?: Array<DocumentoDTO>;
    pageInfo?: PageInfo;
}

export enum TipoContenutoDocumentoEnum {
    TEMPORANEO = 'TEMPORANEO',
    ORIGINALE = 'ORIGINALE',
    SBUSTATO = 'SBUSTATO',
    FIRMATO = 'FIRMATO'
}

export enum TipoContenutoDocumentoFirmatoEnum {
    FEA = 'firma_elettronica_avanzata',
    FIRMA_DIGITALE = 'firma_digitale',
    SIGILLO_ELETTRONICO = 'sigillo_elettronico'
}
