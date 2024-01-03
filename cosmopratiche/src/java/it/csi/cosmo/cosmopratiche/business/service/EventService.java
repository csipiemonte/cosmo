/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketTargetSelector;

public interface EventService {

  void broadcastEvent(String eventType, Object payload, WebSocketTargetSelector... targets);

}
