/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.TipiPraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;

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

@Path("/tipi-pratiche")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface TipiPraticheApi  {
   
    @DELETE
    @Path("/{codice}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteTipiPratiche( @PathParam("codice") String codice,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTipiPratiche( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/{codice}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTipoPratica( @PathParam("codice") String codice,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postTipiPratiche( CreaTipoPraticaRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{codice}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putTipiPratiche( @PathParam("codice") String codice, AggiornaTipoPraticaRequest body,@Context SecurityContext securityContext);
}
