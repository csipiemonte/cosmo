/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

import it.csi.cosmo.cosmopratiche.dto.rest.*;

import it.csi.cosmo.cosmopratiche.dto.rest.PaginaPratiche;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/utenti")  
public interface UtentiApi  {
   
    @DELETE @Path("/pratiche/condivise/{idpratica}")  
    public void deleteUtentiPraticheCondiviseIdpratica( @PathParam("idpratica") String idpratica);

    @DELETE @Path("/pratiche/preferite/{idPratica}")  
    public void deleteUtentiPratichePreferiteIdPratica( @PathParam("idPratica") String idPratica);

    @GET @Path("/pratiche/condivise")  @Produces({ "application/json" })
    public PaginaPratiche getUtentiPraticheCondivise(  @QueryParam("offset") Integer offset,   @QueryParam("limit") Integer limit);

    @GET @Path("/pratiche/condivise/{idpratica}")  @Produces({ "application/json" })
    public Pratica getUtentiPraticheCondiviseIdpratica( @PathParam("idpratica") String idpratica);

    @GET @Path("/pratiche/da-lavorare")  @Produces({ "application/json" })
    public PaginaPratiche getUtentiPraticheDaLavorare(  @QueryParam("offset") Integer offset,   @QueryParam("limit") Integer limit);

    @GET @Path("/pratiche/preferite")  @Produces({ "application/json" })
    public PaginaPratiche getUtentiPratichePreferite(  @QueryParam("offset") Integer offset,   @QueryParam("limit") Integer limit);

    @GET @Path("/pratiche/preferite/{idPratica}")  @Produces({ "application/json" })
    public Pratica getUtentiPratichePreferiteIdPratica( @PathParam("idPratica") String idPratica);

    @PUT @Path("/pratiche/condivise/{idpratica}")  @Produces({ "application/json" })
    public Pratica putUtentiPraticheCondiviseIdpratica( @PathParam("idpratica") String idpratica);

    @PUT @Path("/pratiche/preferite/{idPratica}")  @Produces({ "application/json" })
    public Pratica putUtentiPratichePreferiteIdPratica( @PathParam("idPratica") String idPratica);

}
