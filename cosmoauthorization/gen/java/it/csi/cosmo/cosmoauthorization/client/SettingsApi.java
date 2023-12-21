/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;

import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/settings")  
public interface SettingsApi  {
   
    @POST @Path("/organization") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Preferenza createPreferenzeEnte( @NotNull  @QueryParam("idEnte") Integer idEnte,  @Valid Preferenza body);

    @GET @Path("/organization")  @Produces({ "application/json" })
    public Preferenza getPreferenzeEnte( @NotNull  @QueryParam("idEnte") Integer idEnte,  @NotNull  @QueryParam("versione") String versione);

    @PUT @Path("/organization") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Preferenza putPreferenzeEnte( @NotNull  @QueryParam("idEnte") Integer idEnte,  @Valid Preferenza body);

}
