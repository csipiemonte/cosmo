/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;

import java.math.BigDecimal;
import it.csi.cosmo.cosmoauthorization.dto.rest.ConfigurazioneEnte;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/configurazioni-ente")  
public interface ConfigurazioniEnteApi  {
   
    @DELETE @Path("/{chiave}")  
    public void deleteConfigurazioneEnte( @PathParam("chiave") String chiave,   @QueryParam("idEnte") BigDecimal idEnte);

    @GET @Path("/{chiave}")  @Produces({ "application/json" })
    public ConfigurazioneEnte getConfigurazioneEnte( @PathParam("chiave") String chiave,   @QueryParam("idEnte") BigDecimal idEnte);

    @GET   @Produces({ "application/json" })
    public List<ConfigurazioneEnte> getConfigurazioniEnte(  @QueryParam("idEnte") BigDecimal idEnte);

    @POST   @Produces({ "application/json" })
    public ConfigurazioneEnte postConfigurazioneEnte(  @QueryParam("idEnte") BigDecimal idEnte,  @Valid ConfigurazioneEnte body);

    @PUT @Path("/{chiave}")  @Produces({ "application/json" })
    public ConfigurazioneEnte putConfigurazioneEnte( @PathParam("chiave") String chiave,   @QueryParam("idEnte") BigDecimal idEnte,  @Valid ConfigurazioneEnte body);

}
