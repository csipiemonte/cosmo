/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaCallbackStatoPraticaRequest;

@Path("/test")
public interface TestApi {

  @GET
  @Produces({"application/json"})
  public Response testAsync();

  @Path("/callback/{idPratica}")
  @GET
  @Produces({"application/json"})
  public Response testCallback(@PathParam("idPratica") Long idPratica);

  @Path("/callback")
  @POST
  @Produces({"application/json"})
  public Response testCallbackSend(InviaCallbackStatoPraticaRequest request);
}
