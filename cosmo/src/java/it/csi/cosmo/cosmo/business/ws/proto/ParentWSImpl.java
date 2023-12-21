/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.ws.proto;

import java.io.IOException;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmo.business.rest.proto.ParentApiImpl;
import it.csi.cosmo.cosmo.business.service.proto.ParentWebSocketService;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;

public abstract class ParentWSImpl<O> extends ParentApiImpl {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.WEBSOCKET_LOG_CATEGORY, this.getClass().getSimpleName());

  protected abstract ParentWebSocketService<O> getService();

  @OnMessage
  public Object handleMessage(Session session, String message) {
    logger.debug("handleMessage", "received websocket data message");
    return getService().handleRawMessage(session, message);
  }

  @OnOpen
  public void handleConnectionOpened(Session session, EndpointConfig config) throws IOException {
    logger.debug("handleConnectionOpened", "opened websocket connection");

    HttpSession httpSession =
        (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

    getService().handleRawConnectionOpened(session, httpSession, config);
  }

  @OnClose
  public void handleConnectionClosed(Session session, CloseReason reason) {
    logger.debug("handleConnectionClosed", "closed websocket connection");
    getService().handleRawConnectionClosed(session, reason);
  }

}
