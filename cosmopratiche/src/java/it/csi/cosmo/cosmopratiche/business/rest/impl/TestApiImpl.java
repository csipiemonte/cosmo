/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmopratiche.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPratica;

@Path("/test")
@Consumes({"application/json"})
@Produces({"application/json"})
public class TestApiImpl extends ParentApiImpl {

  @Autowired
  private StoricoPraticaService storicoPraticaService;

  @GET
  @Path("/storico/{idPratica}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Response getStoricoAttivita(@PathParam("idPratica") Long idpratica) {
    StoricoPratica result = storicoPraticaService.getStoricoAttivita(idpratica);
    return Response.ok(result).build();
  }

}
