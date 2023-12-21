/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

import it.csi.cosmo.cosmobusiness.dto.rest.*;

import it.csi.cosmo.cosmobusiness.dto.rest.Esito;
import it.csi.cosmo.cosmobusiness.dto.rest.Evento;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaEventi;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/eventi")  
public interface EventiApi  {
   
    @DELETE @Path("/{id}")  
    public void deleteEventiId( @PathParam("id") String id);

    @GET   @Produces({ "application/json" })
    public PaginaEventi getEventi(  @QueryParam("nome") String nome,   @QueryParam("utente") String utente,   @QueryParam("dtCreazionePrima") String dtCreazionePrima,   @QueryParam("dtCreazioneDopo") String dtCreazioneDopo,   @QueryParam("dtScadenzaPrima") String dtScadenzaPrima,   @QueryParam("dtScadenzaDopo") String dtScadenzaDopo,   @QueryParam("sospeso") Boolean sospeso,   @QueryParam("ente") String ente,   @QueryParam("inizio") Integer inizio,   @QueryParam("dimensionePagina") Integer dimensionePagina);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public Evento getEventiId( @PathParam("id") String id);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Evento postEventi( @Valid Evento body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) 
    public void putEventiId( @PathParam("id") String id,  @Valid Evento body);

}
