/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.service;

import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotifica;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioniMessaggiNotificheResponse;

/**
 *
 */

public interface ConfigurazioniMessaggiNotificheService {

  ConfigurazioneMessaggioNotifica creaConfigurazioneMessaggio(
      ConfigurazioneMessaggioNotificaRequest messaggio);

  ConfigurazioneMessaggioNotifica eliminaConfigurazioneMessaggio(Long idMessaggio);

  ConfigurazioneMessaggioNotifica modificaConfigurazioneMessaggio(Long idMessaggio,
      ConfigurazioneMessaggioNotificaRequest messaggio);

  ConfigurazioneMessaggioNotifica getConfigurazioneMessaggioId(Long idMessaggio);

  ConfigurazioniMessaggiNotificheResponse getConfigurazioniMessaggi(String filter);

}
