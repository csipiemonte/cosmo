/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;


import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteCampiTecnici;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtentiResponse;

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
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteUtentiId( @Pattern(regexp="^[0-9]+$") @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtenti( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/cf/{codiceFiscale}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiCodiceFiscale( @Pattern(regexp="^[A-Za-z]{6}[0-9]{2}[A-Za-z]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}[A-Za-z]{1}$") @PathParam("codiceFiscale") String codiceFiscale,@Context SecurityContext securityContext);
    @GET
    @Path("/ente")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiEnte( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/{idUtente}/ente/{idEnte}/validita")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiEntiValiditaId( @PathParam("idUtente") String idUtente, @PathParam("idEnte") String idEnte,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiId( @Pattern(regexp="^[0-9]+$") @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/utente-corrente")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getUtentiUtenteCorrente(@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postUtenti( UtenteCampiTecnici body,@Context SecurityContext securityContext);
    @PUT
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putUtenti( UtenteCampiTecnici body,@Context SecurityContext securityContext);
}
