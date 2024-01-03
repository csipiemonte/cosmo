/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.rest;

import it.csi.cosmo.cosmonotifications.dto.rest.*;


import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotifica;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioniMessaggiNotificheResponse;

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

@Path("/configurazioni-messaggi-notifiche")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface ConfigurazioniMessaggiNotificheApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteConfigurazioniMessaggiNotifiche( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getConfigurazioniMessaggiNotifiche( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getConfigurazioniMessaggiNotificheId( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postConfigurazioniMessaggiNotifiche( ConfigurazioneMessaggioNotificaRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putConfigurazioniMessaggiNotifiche( @PathParam("id") Long id, ConfigurazioneMessaggioNotificaRequest body,@Context SecurityContext securityContext);
}
