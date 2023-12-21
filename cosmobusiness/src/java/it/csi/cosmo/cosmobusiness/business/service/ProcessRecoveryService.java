/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.common.security.model.UserInfoDTO;

public interface ProcessRecoveryService {

  /**
   * @param idPratica
   * @param actor
   * @return
   */
  boolean recover(Long idPratica, UserInfoDTO actor);
}
