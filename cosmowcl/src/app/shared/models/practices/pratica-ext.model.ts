/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Attivita } from '../api/cosmobusiness/attivita';
import { Processo } from '../api/cosmobusiness/processo';
/**
 * @deprecated utilizzare le classi generate sotto src/shared/models/api
 */
export interface PraticaExtResponse {

  attivita?: Attivita;
  processo: Processo;
}
