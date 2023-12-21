/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;


import java.math.BigDecimal;
import it.csi.cosmo.cosmoauthorization.dto.rest.ConfigurazioneEnte;

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

@Path("/configurazioni-ente")

@Produces({ "application/json" })


public interface ConfigurazioniEnteApi  {
   
    @DELETE
    @Path("/{chiave}")
    
    @Produces({ "application/json" })

    public Response deleteConfigurazioneEnte( @PathParam("chiave") String chiave, @QueryParam("idEnte") BigDecimal idEnte,@Context SecurityContext securityContext);
    @GET
    @Path("/{chiave}")
    
    @Produces({ "application/json" })

    public Response getConfigurazioneEnte( @PathParam("chiave") String chiave, @QueryParam("idEnte") BigDecimal idEnte,@Context SecurityContext securityContext);
    @GET
    
    
    @Produces({ "application/json" })

    public Response getConfigurazioniEnte( @QueryParam("idEnte") BigDecimal idEnte,@Context SecurityContext securityContext);
    @POST
    
    
    @Produces({ "application/json" })

    public Response postConfigurazioneEnte( ConfigurazioneEnte body, @QueryParam("idEnte") BigDecimal idEnte,@Context SecurityContext securityContext);
    @PUT
    @Path("/{chiave}")
    
    @Produces({ "application/json" })

    public Response putConfigurazioneEnte( @PathParam("chiave") String chiave, ConfigurazioneEnte body, @QueryParam("idEnte") BigDecimal idEnte,@Context SecurityContext securityContext);
}
