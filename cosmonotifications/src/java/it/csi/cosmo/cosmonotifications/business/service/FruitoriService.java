/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.service;

import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreResponse;

public interface FruitoriService {

  /**
   * @param notifica
   * @return
   */
  CreaNotificaFruitoreResponse creaNotificaFruitore(CreaNotificaFruitoreRequest notifica);


}
