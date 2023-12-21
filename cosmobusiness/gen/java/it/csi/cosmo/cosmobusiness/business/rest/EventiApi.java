/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

import it.csi.cosmo.cosmobusiness.dto.rest.*;


import it.csi.cosmo.cosmobusiness.dto.rest.Esito;
import it.csi.cosmo.cosmobusiness.dto.rest.Evento;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaEventi;

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

@Path("/eventi")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface EventiApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteEventiId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getEventi( @QueryParam("nome") String nome, @QueryParam("utente") String utente, @QueryParam("dtCreazionePrima") String dtCreazionePrima, @QueryParam("dtCreazioneDopo") String dtCreazioneDopo, @QueryParam("dtScadenzaPrima") String dtScadenzaPrima, @QueryParam("dtScadenzaDopo") String dtScadenzaDopo, @QueryParam("sospeso") Boolean sospeso, @QueryParam("ente") String ente, @QueryParam("inizio") Integer inizio, @QueryParam("dimensionePagina") Integer dimensionePagina,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getEventiId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postEventi( Evento body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putEventiId( @PathParam("id") String id, Evento body,@Context SecurityContext securityContext);
}
