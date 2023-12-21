/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest;

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
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/profili")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface ProfiliApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteProfiliId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getProfiliId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getRuoli( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postProfili( Profilo body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putProfiliId( @PathParam("id") String id, Profilo body,@Context SecurityContext securityContext);
}
