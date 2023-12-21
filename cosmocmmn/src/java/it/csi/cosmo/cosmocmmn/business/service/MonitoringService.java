/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.service;

import it.csi.cosmo.common.dto.common.ServiceStatusDTO;

public interface MonitoringService {

  /**
   * Da informazioni riguardo lo stato dell'applicativo
   *
   * @return un dto con informazioni relative allo stato di ciascun servizio dell'applicativo
   */
  ServiceStatusDTO getServiceStatus();
}
