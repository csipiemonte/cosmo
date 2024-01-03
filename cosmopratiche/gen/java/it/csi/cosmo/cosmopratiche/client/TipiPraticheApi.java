/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/tipi-pratiche")  
public interface TipiPraticheApi  {
   
    @DELETE @Path("/{codice}")  
    public void deleteTipiPratiche( @PathParam("codice") String codice);

    @GET   @Produces({ "application/json" })
    public TipiPraticheResponse getTipiPratiche(  @QueryParam("filter") String filter);

    @GET @Path("/{codice}")  @Produces({ "application/json" })
    public TipoPratica getTipoPratica( @PathParam("codice") String codice);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public TipoPratica postTipiPratiche( @Valid CreaTipoPraticaRequest body);

    @PUT @Path("/{codice}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public TipoPratica putTipiPratiche( @PathParam("codice") String codice,  @Valid AggiornaTipoPraticaRequest body);

}
