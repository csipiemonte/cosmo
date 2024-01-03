/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/fruitori")  
public interface FruitoriApi  {
   
    @GET @Path("/pratiche")  @Produces({ "application/json" })
    public PraticheFruitoreResponse getFruitoriPratiche( @NotNull  @QueryParam("filter") String filter,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor);

    @POST @Path("/pratiche") @Consumes({ "application/json" }) @Produces({ "application/json", "creaPraticaFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "conflictSampleResponse",  })
    public CreaPraticaFruitoreResponse postPraticheFruitori(  @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid CreaPraticaFruitoreRequest body);

    @PUT @Path("/pratiche/{idPraticaExt}/in-relazione") @Consumes({ "application/json" }) @Produces({ "application/json", "AggiornaRelazionePraticaSampleResponse", "badRequestSampleResponse", "unathorizedSampleResponse", "forbiddenSampleResponse",  })
    public AggiornaRelazionePraticaResponse putFruitoriPraticheIdPraticaExt( @PathParam("idPraticaExt") String idPraticaExt,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid AggiornaRelazionePraticaRequest body);

}
