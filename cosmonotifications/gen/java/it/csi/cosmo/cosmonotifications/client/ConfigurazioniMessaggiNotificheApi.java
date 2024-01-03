/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.client;

import it.csi.cosmo.cosmonotifications.dto.rest.*;

import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotifica;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioniMessaggiNotificheResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/configurazioni-messaggi-notifiche")  
public interface ConfigurazioniMessaggiNotificheApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public ConfigurazioneMessaggioNotifica deleteConfigurazioniMessaggiNotifiche( @PathParam("id") Long id);

    @GET   @Produces({ "application/json" })
    public ConfigurazioniMessaggiNotificheResponse getConfigurazioniMessaggiNotifiche(  @QueryParam("filter") String filter);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public ConfigurazioneMessaggioNotifica getConfigurazioniMessaggiNotificheId( @PathParam("id") Long id);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public ConfigurazioneMessaggioNotifica postConfigurazioniMessaggiNotifiche( @Valid ConfigurazioneMessaggioNotificaRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public ConfigurazioneMessaggioNotifica putConfigurazioniMessaggiNotifiche( @PathParam("id") Long id,  @Valid ConfigurazioneMessaggioNotificaRequest body);

}
