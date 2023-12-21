/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

import it.csi.cosmo.cosmobusiness.dto.rest.*;


import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaEventoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.AvviaProcessoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AvviaProcessoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaEventoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.Esito;
import it.csi.cosmo.cosmobusiness.dto.rest.GetPraticaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreResponse;

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
   
    @DELETE
    @Path("/evento/{idEvento}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteFruitoriEvento( @Size(min=1,max=255) @PathParam("idEvento") String idEvento,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, EliminaEventoFruitoreRequest body,@Context SecurityContext securityContext);
    @DELETE
    @Path("/pratiche-esterne/{idPraticaExt}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deletePraticheEsterneIdFruitori( @PathParam("idPraticaExt") String idPraticaExt,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, EliminaPraticaEsternaFruitoreRequest body,@Context SecurityContext securityContext);
    @GET
    @Path("/pratiche/{idPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFruitoriPratica( @Size(min=1,max=255) @PathParam("idPratica") String idPratica,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@Context SecurityContext securityContext);
    @POST
    @Path("/evento")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFruitoriEvento(@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, CreaEventoFruitoreRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/pratiche/{idPratica}/segnala")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFruitoriSegnale( @Size(min=1,max=255) @PathParam("idPratica") String idPratica,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, InviaSegnaleFruitoreRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/pratiche-esterne")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticheEsterneFruitori(@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, CreaPraticaEsternaFruitoreRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/processo")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postProcessoFruitore(@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, AvviaProcessoFruitoreRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/evento/{idEvento}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putFruitoriEvento( @Size(min=1,max=255) @PathParam("idEvento") String idEvento,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, AggiornaEventoFruitoreRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/pratiche-esterne/{idPraticaExt}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPraticheEsterneIdFruitori( @PathParam("idPraticaExt") String idPraticaExt,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, AggiornaPraticaEsternaFruitoreRequest body,@Context SecurityContext securityContext);
}
