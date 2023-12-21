/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/fruitori")  
public interface FruitoriApi  {
   
    @DELETE @Path("/evento/{idEvento}") @Consumes({ "application/json" }) @Produces({ "application/json", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse",  })
    public void deleteFruitoriEvento( @Size(min=1,max=255) @PathParam("idEvento") String idEvento,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid EliminaEventoFruitoreRequest body);

    @DELETE @Path("/pratiche-esterne/{idPraticaExt}") @Consumes({ "application/json" }) @Produces({ "application/json", "eliminaPraticaEsternaFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "conflictSampleResponse",  })
    public EliminaPraticaEsternaFruitoreResponse deletePraticheEsterneIdFruitori( @PathParam("idPraticaExt") String idPraticaExt,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid EliminaPraticaEsternaFruitoreRequest body);

    @GET @Path("/pratiche/{idPratica}")  @Produces({ "application/json", "getPraticaFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse",  })
    public GetPraticaFruitoreResponse getFruitoriPratica( @Size(min=1,max=255) @PathParam("idPratica") String idPratica,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor);

    @POST @Path("/evento") @Consumes({ "application/json" }) @Produces({ "application/json", "creaEventoFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse", "conflictSampleResponse",  })
    public CreaEventoFruitoreResponse postFruitoriEvento(  @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid CreaEventoFruitoreRequest body);

    @POST @Path("/pratiche/{idPratica}/segnala") @Consumes({ "application/json" }) @Produces({ "application/json", "inviaSegnaleFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse", "conflictSampleResponse",  })
    public InviaSegnaleFruitoreResponse postFruitoriSegnale( @Size(min=1,max=255) @PathParam("idPratica") String idPratica,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid InviaSegnaleFruitoreRequest body);

    @POST @Path("/pratiche-esterne") @Consumes({ "application/json" }) @Produces({ "application/json", "creaPraticaEsternaFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "conflictSampleResponse",  })
    public CreaPraticaEsternaFruitoreResponse postPraticheEsterneFruitori(  @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid CreaPraticaEsternaFruitoreRequest body);

    @POST @Path("/processo") @Consumes({ "application/json" }) @Produces({ "application/json", "avviaProcessoFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse", "conflictSampleResponse",  })
    public AvviaProcessoFruitoreResponse postProcessoFruitore(  @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid AvviaProcessoFruitoreRequest body);

    @PUT @Path("/evento/{idEvento}") @Consumes({ "application/json" }) @Produces({ "application/json", "aggiornaEventoFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse", "conflictSampleResponse",  })
    public AggiornaEventoFruitoreResponse putFruitoriEvento( @Size(min=1,max=255) @PathParam("idEvento") String idEvento,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid AggiornaEventoFruitoreRequest body);

    @PUT @Path("/pratiche-esterne/{idPraticaExt}") @Consumes({ "application/json" }) @Produces({ "application/json", "aggiornaPraticaEsternaFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "conflictSampleResponse",  })
    public AggiornaPraticaEsternaFruitoreResponse putPraticheEsterneIdFruitori( @PathParam("idPraticaExt") String idPraticaExt,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid AggiornaPraticaEsternaFruitoreRequest body);

}
