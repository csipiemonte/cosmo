/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.service;

import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheFruitoreResponse;

public interface FruitoriService {

  CreaPraticaFruitoreResponse postPratiche(CreaPraticaFruitoreRequest body);

  AggiornaRelazionePraticaResponse creaAggiornaPraticheInRelazione(String idPraticaExt,
      AggiornaRelazionePraticaRequest body);

  PraticheFruitoreResponse getPratiche(String filter);
}
