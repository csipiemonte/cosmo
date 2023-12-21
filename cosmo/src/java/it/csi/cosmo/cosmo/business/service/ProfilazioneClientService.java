/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.service;

import javax.servlet.http.HttpServletRequest;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.cosmo.business.service.impl.ProfilazioneClientServiceImpl;
import it.csi.cosmo.cosmo.util.listener.SpringApplicationContextHelper;

/**
 * Servizio per la gestione della profilazione del client
 */
public interface ProfilazioneClientService {

  static ProfilazioneClientService getInstance () {
    return (ProfilazioneClientService) SpringApplicationContextHelper.getBean ( ProfilazioneClientServiceImpl.class );
  }

  ClientInfoDTO caricaClientInfo(HttpServletRequest request);

  ClientInfoDTO caricaClientInfoTest(HttpServletRequest request);
}
