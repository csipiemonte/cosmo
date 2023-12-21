/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import it.csi.cosmo.cosmo.dto.rest.Esito;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketEventPostRequest;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketPostRequest;

@Path("/websocket")
public interface WebSocketProxyApi {

  @Path("/{channel}")
  @POST
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Esito post(@PathParam("channel") String channel, WebSocketPostRequest payload);

  @Path("/event")
  @POST
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Esito postEvent(WebSocketEventPostRequest payload);

}
