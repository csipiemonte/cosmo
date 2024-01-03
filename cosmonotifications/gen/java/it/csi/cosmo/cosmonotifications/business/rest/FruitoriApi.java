/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.rest;

import it.csi.cosmo.cosmonotifications.dto.rest.*;


import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.Esito;

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
   
    @POST
    @Path("/notifiche")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postNotificaFruitore( CreaNotificaFruitoreRequest notifica,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@Context SecurityContext securityContext);
}
