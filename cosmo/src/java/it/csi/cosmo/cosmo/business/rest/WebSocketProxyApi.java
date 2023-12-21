/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import it.csi.cosmo.cosmo.dto.ws.WebSocketEventPost;
import it.csi.cosmo.cosmo.dto.ws.WebSocketPost;


/**
 * Risorsa RestEasy per il reperimento di informazioni sullo stato
 */
@Path("/")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface WebSocketProxyApi {

  @POST
  @Path("/websocket/notifications")
  public Response proxyPostNotifications(WebSocketPost request);

  @POST
  @Path("/websocket/event")
  public Response proxyPostEvent(WebSocketEventPost request);

  @POST
  @Path("/websocket/{channel}")
  public Response proxyPost(@PathParam("channel") String channel, WebSocketPost payload);

}
