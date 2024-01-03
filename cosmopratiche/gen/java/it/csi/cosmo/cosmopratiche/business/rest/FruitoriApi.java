/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.Esito;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheFruitoreResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/fruitori")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface FruitoriApi  {
   
    @GET
    @Path("/pratiche")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFruitoriPratiche( @NotNull @QueryParam("filter") String filter,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@Context SecurityContext securityContext);
    @POST
    @Path("/pratiche")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticheFruitori(@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, CreaPraticaFruitoreRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/pratiche/{idPraticaExt}/in-relazione")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putFruitoriPraticheIdPraticaExt( @PathParam("idPraticaExt") String idPraticaExt, AggiornaRelazionePraticaRequest body,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@Context SecurityContext securityContext);
}
