/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.rest.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmo.business.rest.WebSocketProxyApi;
import it.csi.cosmo.cosmo.business.rest.proto.ParentApiImpl;
import it.csi.cosmo.cosmo.business.service.WebSocketProxyService;
import it.csi.cosmo.cosmo.dto.ws.WebSocketEventPost;
import it.csi.cosmo.cosmo.dto.ws.WebSocketPost;
import it.csi.cosmo.cosmo.security.Scopes;


public class WebSocketProxyApiImpl extends ParentApiImpl implements WebSocketProxyApi {

  @Autowired
  public WebSocketProxyService proxyService;

  @Secured(testOnly = true)
  @Override
  public Response proxyPost(String channel, WebSocketPost payload) {
    HttpServletRequest request = RequestUtils.getCurrentRequest().orElseThrow();

    proxyService.post(request, channel, payload);

    return ok("Message POSTed to target WebSocket channel");
  }

  @Secured(hasScopes = Scopes.PUSH_NOTIFICATIONS)
  @Override
  public Response proxyPostNotifications(WebSocketPost payload) {
    HttpServletRequest request = RequestUtils.getCurrentRequest().orElseThrow();

    proxyService.post(request, "notifications", payload);

    return ok("Message POSTed to target WebSocket channel");
  }

  @Secured(hasScopes = Scopes.PUSH_EVENTS)
  @Override
  public Response proxyPostEvent(WebSocketEventPost payload) {
    HttpServletRequest request = RequestUtils.getCurrentRequest().orElseThrow();

    proxyService.postEvent(request, "notifications", payload);

    return ok("Message POSTed to target WebSocket channel as event");
  }

}
