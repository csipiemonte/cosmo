/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.client;

import it.csi.cosmo.cosmonotifications.dto.rest.*;

import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.Esito;

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
   
    @POST @Path("/notifiche") @Consumes({ "application/json" }) @Produces({ "application/json", "creaNotificaFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse", "conflictSampleResponse",  })
    public CreaNotificaFruitoreResponse postNotificaFruitore( @NotNull @Valid CreaNotificaFruitoreRequest notifica,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor);

}
