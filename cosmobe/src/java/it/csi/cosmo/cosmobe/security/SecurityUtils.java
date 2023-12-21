/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.security;

import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.cosmobe.business.service.ClientService;

/**
 * Utils per gestire l'utenza corrente
 */

public interface SecurityUtils {

  static ClientInfoDTO getClientCorrente() {
    return ClientService.getInstance().getClientCorrente();
  }

  static ClientInfoDTO requireClientCorrente() {
    ClientInfoDTO client = ClientService.getInstance().getClientCorrente();
    if (client != null && !client.getAnonimo()) {
      return client;
    } else {
      throw new UnauthorizedException("Client authentication is required");
    }
  }
}
