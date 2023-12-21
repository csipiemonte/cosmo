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

@Path("/preferenze-utente")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface PreferenzeUtenteApi  {
   
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response createPreferenzeUtente( Preferenza body,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPreferenzeUtente(@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPreferenzeUtenteId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @PUT
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPreferenzeUtente( Preferenza body,@Context SecurityContext securityContext);
}
