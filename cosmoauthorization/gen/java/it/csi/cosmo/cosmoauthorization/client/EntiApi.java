/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;

import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaEnteRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEnteRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.EnteResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.EntiResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/enti")  
public interface EntiApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public EnteResponse deleteEntiId( @PathParam("id") Long id);

    @GET   @Produces({ "application/json" })
    public EntiResponse getEnti(  @QueryParam("filter") String filter);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public EnteResponse getEntiId( @PathParam("id") Long id);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public EnteResponse postEnti( @NotNull @Valid CreaEnteRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public EnteResponse putEnti( @PathParam("id") Long id,  @NotNull @Valid AggiornaEnteRequest body);

}
