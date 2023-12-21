/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;


import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;

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

@Path("/settings")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface SettingsApi  {
   
    @POST
    @Path("/organization")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response createPreferenzeEnte( @NotNull @QueryParam("idEnte") Integer idEnte, Preferenza body,@Context SecurityContext securityContext);
    @GET
    @Path("/organization")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPreferenzeEnte( @NotNull @QueryParam("idEnte") Integer idEnte, @NotNull @QueryParam("versione") String versione,@Context SecurityContext securityContext);
    @PUT
    @Path("/organization")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPreferenzeEnte( @NotNull @QueryParam("idEnte") Integer idEnte, Preferenza body,@Context SecurityContext securityContext);
}
