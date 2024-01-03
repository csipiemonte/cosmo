/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { EnteCertificatore } from '../api/cosmoauthorization/enteCertificatore';
import { ProfiloFEQ } from '../api/cosmoauthorization/profiloFEQ';
import {
  SceltaMarcaTemporale,
} from '../api/cosmoauthorization/sceltaMarcaTemporale';
import {
  TipoCredenzialiFirma,
} from '../api/cosmoauthorization/tipoCredenzialiFirma';
import { TipoOTP } from '../api/cosmoauthorization/tipoOTP';

export interface ValorePreferenzeEnte {
  header: string;
  logo: string;
  logoFooter: string;
  logoFooterCentrale: string;
  logoFooterDestra: string;
  iconaFea?: string;
  dimensioneMassimaAllegatiEmail?: number;
  widgets?: string[];
  isWidgetModificabile?: boolean;
  impostazioniFirma?: {
    enteCertificatore?: EnteCertificatore,
    tipoCredenzialiFirma?: TipoCredenzialiFirma,
    tipoOTP?: TipoOTP,
    profiloFEQ?: ProfiloFEQ,
    sceltaMarcaTemporale?: SceltaMarcaTemporale
  };
}
