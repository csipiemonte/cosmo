/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.monitoring;

import it.csi.cosmo.common.dto.common.ServiceStatusDTO;

/**
 *
 */

public interface Monitorable {

  public ServiceStatusDTO checkStatus();
}
