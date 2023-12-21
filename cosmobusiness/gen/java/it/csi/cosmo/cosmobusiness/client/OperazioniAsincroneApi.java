/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

import it.csi.cosmo.cosmobusiness.dto.rest.*;

import it.csi.cosmo.cosmobusiness.dto.rest.CreaOperazioneAsincronaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Esito;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/operazioni-asincrone")  
public interface OperazioniAsincroneApi  {
   
    @DELETE @Path("/{uuid}")  @Produces({ "application/json" })
    public void deleteOperazioneAsincrona( @PathParam("uuid") String uuid);

    @GET @Path("/{uuid}")  @Produces({ "application/json" })
    public OperazioneAsincrona getOperazioneAsincrona( @PathParam("uuid") String uuid);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public OperazioneAsincrona postOperazioneAsincrona( @Valid CreaOperazioneAsincronaRequest body);

    @PUT @Path("/{uuid}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public OperazioneAsincrona putOperazioneAsincrona( @PathParam("uuid") String uuid,  @Valid OperazioneAsincrona body);

}
