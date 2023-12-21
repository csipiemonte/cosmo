/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service.impl;

import java.io.IOException;
import org.springframework.stereotype.Service;
import it.csi.cosmo.cosmo.business.service.NotificationsWebSocketService;
import it.csi.cosmo.cosmo.business.service.proto.ParentWebSocketServiceImpl;
import it.csi.cosmo.cosmo.dto.ws.WebSocketChannel;
import it.csi.cosmo.cosmo.dto.ws.WebSocketEvent;
import it.csi.cosmo.cosmo.dto.ws.WebSocketJointSession;

@WebSocketChannel("notifications")
@Service
public class NotificationsWebSocketServiceImpl
extends ParentWebSocketServiceImpl<String, Object> implements NotificationsWebSocketService {

  @Override
  public Class<String> getInputClass() {
    return String.class;
  }

  @Override
  public Class<Object> getOutputClass() {
    return Object.class;
  }

  @Override
  public String onMessage(WebSocketJointSession session, String message) {
    String name =
        session.getPrincipal() != null ? session.getPrincipal().getNome() : "Guest";

        logger.debug("onMessage", "MESSAGE: " + message + " from " + name);
        return null;
  }

  @Override
  public void onConnectionOpened(WebSocketJointSession session) throws IOException {
    String name = session.getPrincipal() != null ? session.getPrincipal().getNome() : "Guest";

    logger.debug("onConnectionOpened", "CONNECTION OPENED from " + name);
  }

  @Override
  public void onEvent(WebSocketJointSession jointSession, WebSocketEvent<Object> event) {
    logger.debug("onEvent", "EVENT: " + event.getEvent());
  }

}
