/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.client;

import it.csi.cosmo.cosmonotifications.dto.rest.*;

import it.csi.cosmo.cosmonotifications.dto.rest.Esito;
import it.csi.cosmo.cosmonotifications.dto.rest.Notifica;
import it.csi.cosmo.cosmonotifications.dto.rest.PaginaNotifiche;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/utenti")  
public interface UtentiApi  {
   
    @GET @Path("/notifiche/{id}")  @Produces({ "application/json" })
    public Notifica getNotificationsId( @PathParam("id") String id);

    @GET @Path("/notifiche")  @Produces({ "application/json" })
    public PaginaNotifiche getUtentiIdUtenteNotifiche(  @QueryParam("offset") Integer offset,   @QueryParam("limit") Integer limit);

    @PUT @Path("/notifiche/tutte")  @Produces({ "application/json" })
    public void putNotificationsAll();

    @PUT @Path("/notifiche/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Notifica putNotificationsId( @PathParam("id") String id,  @Valid Notifica body);

}
