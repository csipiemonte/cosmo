/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import { Parametro } from '../api/cosmobusiness/parametro';
import { Esito } from '../esito';
/**
 * @deprecated utilizzare le classi generate sotto src/shared/models/api
 */
export interface ParametriResponse {
  parametri: Parametro[];
  esito: Esito;
}
