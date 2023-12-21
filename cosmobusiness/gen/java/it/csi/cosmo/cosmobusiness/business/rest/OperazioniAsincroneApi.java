/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

import it.csi.cosmo.cosmobusiness.dto.rest.*;


import it.csi.cosmo.cosmobusiness.dto.rest.CreaOperazioneAsincronaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Esito;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;

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

@Path("/operazioni-asincrone")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface OperazioniAsincroneApi  {
   
    @DELETE
    @Path("/{uuid}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteOperazioneAsincrona( @PathParam("uuid") String uuid,@Context SecurityContext securityContext);
    @GET
    @Path("/{uuid}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getOperazioneAsincrona( @PathParam("uuid") String uuid,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postOperazioneAsincrona( CreaOperazioneAsincronaRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{uuid}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putOperazioneAsincrona( @PathParam("uuid") String uuid, OperazioneAsincrona body,@Context SecurityContext securityContext);
}
