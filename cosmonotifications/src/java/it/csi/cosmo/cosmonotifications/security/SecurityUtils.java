/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.security;

import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmonotifications.business.service.ClientService;
import it.csi.cosmo.cosmonotifications.business.service.UserService;

/**
 * Utils per gestire l'utenza corrente
 */

public interface SecurityUtils {

  public static UserInfoDTO getUtenteCorrente() {
    return UserService.getInstance().getUtenteCorrente();
  }

  public static UserInfoDTO requireUtenteCorrente() {
    UserInfoDTO user = UserService.getInstance().getUtenteCorrente();
    if (user != null) {
      return user;
    } else {
      throw new UnauthorizedException("User authentication is required");
    }
  }


  public static ClientInfoDTO getClientCorrente() {
    return ClientService.getInstance().getClientCorrente();
  }

}
