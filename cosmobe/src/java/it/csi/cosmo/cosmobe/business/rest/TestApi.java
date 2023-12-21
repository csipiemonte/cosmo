/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.business.rest;

import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import it.csi.cosmo.common.security.model.Secured;


/**
 * Risorsa RestEasy per il reperimento di informazioni sullo stato
 */
@Path("/")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface TestApi {

  @Secured(testOnly = true)
  @GET
  @Path("/file/temp/{uploadUUID}")
  @Consumes({"application/json"})
  @Produces({"application/octet-stream"})
  public Response getFileTemp(@Size(min = 1, max = 255) @PathParam("uploadUUID") String uploadUUID,
      @Context SecurityContext securityContext);
}
