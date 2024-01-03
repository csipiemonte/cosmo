/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.rest;

import it.csi.cosmo.cosmonotifications.dto.rest.*;


import it.csi.cosmo.cosmonotifications.dto.rest.Esito;
import it.csi.cosmo.cosmonotifications.dto.rest.Notifica;
import it.csi.cosmo.cosmonotifications.dto.rest.PaginaNotifiche;

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

@Path("/utenti")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface UtentiApi  {
   
    @GET
    @Path("/notifiche/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getNotificationsId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/notifiche")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiIdUtenteNotifiche( @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit,@Context SecurityContext securityContext);
    @PUT
    @Path("/notifiche/tutte")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putNotificationsAll(@Context SecurityContext securityContext);
    @PUT
    @Path("/notifiche/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putNotificationsId( @PathParam("id") String id, Notifica body,@Context SecurityContext securityContext);
}
