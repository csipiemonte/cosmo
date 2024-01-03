/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/batch")
public interface BatchApi {

  @GET
  @Path("/fs2index")
  @Produces({"application/json"})
  public Response launchFilesystem2IndexBatch();

}
