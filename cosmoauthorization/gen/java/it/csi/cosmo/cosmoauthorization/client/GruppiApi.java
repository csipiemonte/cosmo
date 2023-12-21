/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;

import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;
import it.csi.cosmo.cosmoauthorization.dto.rest.GruppiResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.GruppoResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoGruppo;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/gruppi")  
public interface GruppiApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public void deleteGruppi( @PathParam("id") Long id);

    @GET   @Produces({ "application/json" })
    public GruppiResponse getGruppi(  @QueryParam("filter") String filter);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public GruppoResponse getGruppiId( @PathParam("id") Long id);

    @GET @Path("/utente-corrente")  @Produces({ "application/json" })
    public List<RiferimentoGruppo> getGruppiUtenteCorrente();

    @GET @Path("/codice/{codice}")  @Produces({ "application/json" })
    public GruppoResponse getGruppoCodice( @PathParam("codice") String codice);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public GruppoResponse postGruppi( @Valid CreaGruppoRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public GruppoResponse putGruppi( @PathParam("id") Long id,  @Valid AggiornaGruppoRequest body);

}
