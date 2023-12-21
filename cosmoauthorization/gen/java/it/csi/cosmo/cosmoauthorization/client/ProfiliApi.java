/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;

import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;
import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiliResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiloResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/profili")  
public interface ProfiliApi  {
   
    @DELETE @Path("/{id}")  
    public void deleteProfiliId( @PathParam("id") String id);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public ProfiloResponse getProfiliId( @PathParam("id") String id);

    @GET   @Produces({ "application/json" })
    public ProfiliResponse getRuoli(  @QueryParam("filter") String filter);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public ProfiloResponse postProfili( @Valid Profilo body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public ProfiloResponse putProfiliId( @PathParam("id") String id,  @Valid Profilo body);

}
