/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.GetPraticaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InvioCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulaCallbackStatoPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulazioneCallbackResponse;

public interface CallbackStatoPraticaService {

  GetPraticaFruitoreResponse getStatoPratica(Long idPratica);

  AggiornaStatoPraticaRequest getStatoPraticaPerCallback(Long idPratica);

  InvioCallbackResponse postCallbackStatoPraticaInvia(InviaCallbackStatoPraticaRequest body);

  SchedulazioneCallbackResponse postCallbackStatoPraticaSchedula(
      SchedulaCallbackStatoPraticaRequest body);
}
