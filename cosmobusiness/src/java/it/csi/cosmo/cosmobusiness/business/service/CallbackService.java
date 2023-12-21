/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import java.util.Map;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.cosmobusiness.dto.EsitoTentativoInvioCallback;
import it.csi.cosmo.cosmobusiness.dto.rest.InvioCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulazioneCallbackResponse;

public interface CallbackService {

  InvioCallbackResponse inviaSincrono(OperazioneFruitore operazione, Long idFruitore,
      Object payload, Map<String, Object> parametri, String codiceSegnale);

  SchedulazioneCallbackResponse schedulaInvioAsincrono(OperazioneFruitore operazione,
      Long idFruitore, Object payload, Map<String, Object> parametri, String codiceSegnale);

  EsitoTentativoInvioCallback tentaInvioCallbackSchedulato(Long idCallback);

  void annullaCallbackSchedulato(Long idCallback);

}
