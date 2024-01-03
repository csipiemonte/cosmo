/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.EsportaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.ImportExportDTO.ImportaTipoPraticaRequest;


@Path("/import-export")
public interface ImportExportApi  {

  @POST
  @Path("/importa")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Response importaPratica(
      ImportaTipoPraticaRequest body, 
      @Context HttpServletRequest request
  );

  @POST
  @Path("/esporta")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Response esporta(
      EsportaTipoPraticaRequest body,
      @Context SecurityContext securityContext
  );

  @GET
  @Path("/opzioni-esportazione-tenant")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Response getOpzioniEsportazioneTenant(
      @QueryParam("codiceTipoPratica") String codiceTipoPratica,
      @Context HttpServletRequest request);

}
