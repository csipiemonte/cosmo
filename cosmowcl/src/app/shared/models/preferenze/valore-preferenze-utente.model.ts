/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import { EnteCertificatore } from '../api/cosmoauthorization/enteCertificatore';
import { ProfiloFEQ } from '../api/cosmoauthorization/profiloFEQ';
import { SceltaMarcaTemporale } from '../api/cosmoauthorization/sceltaMarcaTemporale';
import { TipoCredenzialiFirma } from '../api/cosmoauthorization/tipoCredenzialiFirma';
import { TipoOTP } from '../api/cosmoauthorization/tipoOTP';
import { RicezioneNotifiche } from '../api/cosmonotifications/ricezioneNotifiche';
import { PreferenzeWidgets } from './preferenze-widgets.model';

export interface ValorePreferenzeUtente {
  maxPageSize?: number;
  home?: { widgets: PreferenzeWidgets[] };
  impostazioniFirma?: {
    enteCertificatore?: EnteCertificatore,
    tipoCredenzialiFirma?: TipoCredenzialiFirma,
    tipoOTP?: TipoOTP,
    profiloFEQ?: ProfiloFEQ,
    sceltaMarcaTemporale?: SceltaMarcaTemporale
  };
  ricezioneNotifiche?: {
    email: RicezioneNotifiche,
    cosmo: RicezioneNotifiche
  };
  segnalibri?: [{
      tipo?: string;
      nome?: string;
      segnalibro?: any;
  }];
  posizioneToast?: string;
}
