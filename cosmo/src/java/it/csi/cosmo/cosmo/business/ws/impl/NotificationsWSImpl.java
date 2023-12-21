/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.ws.impl;

import javax.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmo.business.service.NotificationsWebSocketService;
import it.csi.cosmo.cosmo.business.service.proto.ParentWebSocketService;
import it.csi.cosmo.cosmo.business.ws.GetHttpSessionConfigurator;
import it.csi.cosmo.cosmo.business.ws.proto.ParentWSImpl;

@ServerEndpoint(value = "/websocket/notifications", configurator = GetHttpSessionConfigurator.class)
public class NotificationsWSImpl extends ParentWSImpl<Object> {

  @Autowired
  private NotificationsWebSocketService service;

  @Override
  protected ParentWebSocketService<Object> getService() {
    return service;
  }

}
