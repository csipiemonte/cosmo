/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackAggiornamentoDeliberaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackCreazioneDeliberaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InvioCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulazioneCallbackResponse;

public interface CallbackDelibereService {

  InvioCallbackResponse postCallbackAggiornamentoDeliberaInvia(
      InviaCallbackAggiornamentoDeliberaRequest body);

  SchedulazioneCallbackResponse postCallbackAggiornamentoDeliberaSchedula(
      InviaCallbackAggiornamentoDeliberaRequest body);

  InvioCallbackResponse postCallbackCreazioneDeliberaInvia(
      InviaCallbackCreazioneDeliberaRequest body);

  SchedulazioneCallbackResponse postCallbackCreazioneDeliberaSchedula(
      InviaCallbackCreazioneDeliberaRequest body);
}
