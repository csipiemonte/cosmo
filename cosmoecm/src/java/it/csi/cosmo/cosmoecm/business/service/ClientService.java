/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.cosmoecm.business.service.impl.ClientServiceImpl;
import it.csi.cosmo.cosmoecm.util.listener.SpringApplicationContextHelper;


/**
 * Servizio per la gestione del client
 */
public interface ClientService {

  ClientInfoDTO getClientCorrente();

  static ClientService getInstance () {
    return (ClientService) SpringApplicationContextHelper.getBean ( ClientServiceImpl.class );
  }

}
