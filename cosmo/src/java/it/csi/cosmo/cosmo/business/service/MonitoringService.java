/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service;

import it.csi.cosmo.common.dto.common.ServiceStatusDTO;

public interface MonitoringService {

  /**
   *
   * @return
   */
  ServiceStatusDTO getServiceStatus();
}
