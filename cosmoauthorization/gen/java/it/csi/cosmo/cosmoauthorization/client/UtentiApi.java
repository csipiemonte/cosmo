/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/utenti")  
public interface UtentiApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public UtenteResponse deleteUtentiId( @Pattern(regexp="^[0-9]+$") @PathParam("id") String id);

    @GET   @Produces({ "application/json" })
    public UtentiResponse getUtenti(  @QueryParam("filter") String filter);

    @GET @Path("/cf/{codiceFiscale}")  @Produces({ "application/json" })
    public UtenteResponse getUtentiCodiceFiscale( @Pattern(regexp="^[A-Za-z]{6}[0-9]{2}[A-Za-z]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}[A-Za-z]{1}$") @PathParam("codiceFiscale") String codiceFiscale);

    @GET @Path("/ente")  @Produces({ "application/json" })
    public UtentiResponse getUtentiEnte(  @QueryParam("filter") String filter);

    @GET @Path("/{idUtente}/ente/{idEnte}/validita")  @Produces({ "application/json" })
    public UtenteCampiTecnici getUtentiEntiValiditaId( @PathParam("idUtente") String idUtente,  @PathParam("idEnte") String idEnte);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public UtenteResponse getUtentiId( @Pattern(regexp="^[0-9]+$") @PathParam("id") String id);

    @GET @Path("/utente-corrente")  @Produces({ "application/json" })
    public UtenteResponse getUtentiUtenteCorrente();

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public UtenteResponse postUtenti( @Valid UtenteCampiTecnici body);

    @PUT  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public UtenteResponse putUtenti( @Valid UtenteCampiTecnici body);

}
