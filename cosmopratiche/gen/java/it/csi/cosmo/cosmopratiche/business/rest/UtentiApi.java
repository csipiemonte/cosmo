/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.PaginaPratiche;
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

@Path("/utenti")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface UtentiApi  {
   
    @DELETE
    @Path("/pratiche/condivise/{idpratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteUtentiPraticheCondiviseIdpratica( @PathParam("idpratica") String idpratica,@Context SecurityContext securityContext);
    @DELETE
    @Path("/pratiche/preferite/{idPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteUtentiPratichePreferiteIdPratica( @PathParam("idPratica") String idPratica,@Context SecurityContext securityContext);
    @GET
    @Path("/pratiche/condivise")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiPraticheCondivise( @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit,@Context SecurityContext securityContext);
    @GET
    @Path("/pratiche/condivise/{idpratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiPraticheCondiviseIdpratica( @PathParam("idpratica") String idpratica,@Context SecurityContext securityContext);
    @GET
    @Path("/pratiche/da-lavorare")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiPraticheDaLavorare( @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit,@Context SecurityContext securityContext);
    @GET
    @Path("/pratiche/preferite")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiPratichePreferite( @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit,@Context SecurityContext securityContext);
    @GET
    @Path("/pratiche/preferite/{idPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiPratichePreferiteIdPratica( @PathParam("idPratica") String idPratica,@Context SecurityContext securityContext);
    @PUT
    @Path("/pratiche/condivise/{idpratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putUtentiPraticheCondiviseIdpratica( @PathParam("idpratica") String idpratica,@Context SecurityContext securityContext);
    @PUT
    @Path("/pratiche/preferite/{idPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putUtentiPratichePreferiteIdPratica( @PathParam("idPratica") String idPratica,@Context SecurityContext securityContext);
}
