/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { TipoDocumento } from 'src/app/shared/models/api/cosmoecm/tipoDocumento';

export interface TipiDocumentoObbligatori extends TipoDocumento{
  obbligatorio?: string;
  codicePadre?: string;
}
