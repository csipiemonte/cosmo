/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service.proto;

import java.io.IOException;
import java.util.function.Predicate;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmo.dto.ws.WebSocketJointSession;

/**
 *
 */

public interface ParentWebSocketService<O> {

  Object handleRawMessage(Session session, String name);

  void handleRawConnectionOpened(Session session, HttpSession httpSession, EndpointConfig config)
      throws IOException;

  void handleRawConnectionClosed(Session session, CloseReason reason);

  void onConnectionOpened(WebSocketJointSession session)
      throws IOException;

  void onConnectionClosed(WebSocketJointSession session, CloseReason reason);

  void broadcast(O payload, Predicate<UserInfoDTO> userFilter);

  void broadcastEvent(String event, Object payload, Predicate<UserInfoDTO> userFilter);
}
