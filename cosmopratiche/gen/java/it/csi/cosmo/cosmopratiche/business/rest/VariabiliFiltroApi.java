/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.FormatoVariabileDiFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabileDiFiltro;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabiliDiFiltroResponse;

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

@Path("/variabili-filtro")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface VariabiliFiltroApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteVariabiliFiltroId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getVariabiliFiltro( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/formati")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getVariabiliFiltroFormati(@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getVariabiliFiltroId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/tipiFiltro")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getVariabiliFiltroTipiFiltro(@Context SecurityContext securityContext);
    @GET
    @Path("/tipo-pratica/{codice}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getVariabiliFiltroTipoPraticaCodiceTipoPratica( @PathParam("codice") String codice,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postVariabiliFiltro( VariabileDiFiltro body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putVariabiliFiltroId( @PathParam("id") String id, VariabileDiFiltro body,@Context SecurityContext securityContext);
}
