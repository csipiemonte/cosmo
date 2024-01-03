/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/variabili-filtro")  
public interface VariabiliFiltroApi  {
   
    @DELETE @Path("/{id}")  
    public void deleteVariabiliFiltroId( @PathParam("id") String id);

    @GET   @Produces({ "application/json" })
    public VariabiliDiFiltroResponse getVariabiliFiltro(  @QueryParam("filter") String filter);

    @GET @Path("/formati")  @Produces({ "application/json" })
    public List<FormatoVariabileDiFiltro> getVariabiliFiltroFormati();

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public VariabileDiFiltro getVariabiliFiltroId( @PathParam("id") String id);

    @GET @Path("/tipiFiltro")  @Produces({ "application/json" })
    public List<TipoFiltro> getVariabiliFiltroTipiFiltro();

    @GET @Path("/tipo-pratica/{codice}")  @Produces({ "application/json" })
    public List<VariabileDiFiltro> getVariabiliFiltroTipoPraticaCodiceTipoPratica( @PathParam("codice") String codice);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public VariabileDiFiltro postVariabiliFiltro( @Valid VariabileDiFiltro body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public VariabileDiFiltro putVariabiliFiltroId( @PathParam("id") String id,  @Valid VariabileDiFiltro body);

}
