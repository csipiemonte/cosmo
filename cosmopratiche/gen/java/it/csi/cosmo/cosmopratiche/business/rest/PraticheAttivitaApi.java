/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

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

@Path("/pratiche-attivita")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface PraticheAttivitaApi  {
   
    @GET
    @Path("/{idAttivita}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheAttivitaIdAttivita( @PathParam("idAttivita") String idAttivita,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticheAttivita( Pratica body,@Context SecurityContext securityContext);
    @PUT
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPraticheAttivita( Pratica body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{idAttivita}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPraticheAttivitaIdAttivita( @PathParam("idAttivita") String idAttivita,@Context SecurityContext securityContext);
}
