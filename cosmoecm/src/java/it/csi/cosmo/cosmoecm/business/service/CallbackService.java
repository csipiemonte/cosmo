/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.Map;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulazioneCallbackResponse;

/**
 *
 */

public interface CallbackService {

  SchedulazioneCallbackResponse schedulaInvioAsincrono(OperazioneFruitore operazione,
      Long idFruitore, Object payload, Map<String, Object> parametri, String codiceSegnale);

}
