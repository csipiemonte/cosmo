/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.service;

import it.csi.cosmo.common.dto.rest.process.ProcessEngineEventDTO;
import it.csi.cosmo.common.dto.rest.process.ProcessInstanceVariableEventDTO;

public interface EventSenderService {

  /**
   * @param event
   */
  void sendEventToCosmo(ProcessEngineEventDTO event);

  void sendVariableEventToCosmo(ProcessInstanceVariableEventDTO event);

}
