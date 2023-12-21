/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.client;

import it.csi.cosmo.cosmoauthorization.dto.rest.*;

import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsterna;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsternaConValidita;
import it.csi.cosmo.cosmoauthorization.dto.rest.Esito;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsternaConValidita;
import java.util.List;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/applicazione-esterna")  
public interface ApplicazioneEsternaApi  {
   
    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public void deleteApplicazioneEsternaId( @PathParam("id") String id);

    @DELETE @Path("/{idApplicazione}/funzionalita/{idFunzionalita}")  @Produces({ "application/json" })
    public void deleteApplicazioneEsternaIdApplicazioneFunzionalitaIdFunzionalita( @PathParam("idApplicazione") String idApplicazione,  @PathParam("idFunzionalita") String idFunzionalita);

    @DELETE @Path("/{id}/associazione-ente")  @Produces({ "application/json" })
    public void deleteApplicazioneEsternaIdAssociazioneEnte( @PathParam("id") String id);

    @GET   @Produces({ "application/json" })
    public List<ApplicazioneEsterna> getApplicazioneEsterna( @NotNull  @QueryParam("configurata") Boolean configurata);

    @GET @Path("/all")  @Produces({ "application/json" })
    public List<ApplicazioneEsternaConValidita> getApplicazioneEsternaAll();

    @GET @Path("/associazione-ente")  @Produces({ "application/json" })
    public List<ApplicazioneEsternaConValidita> getApplicazioneEsternaAssociazioneEnte( @NotNull  @QueryParam("enteAssociato") Boolean enteAssociato);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public ApplicazioneEsterna getApplicazioneEsternaId( @PathParam("id") String id);

    @GET @Path("/{idApplicazione}/funzionalita")  @Produces({ "application/json" })
    public List<FunzionalitaApplicazioneEsternaConValidita> getApplicazioneEsternaIdApplicazioneFunzionalita( @PathParam("idApplicazione") String idApplicazione);

    @GET @Path("/{id}/associazione-ente")  @Produces({ "application/json" })
    public ApplicazioneEsternaConValidita getApplicazioneEsternaIdAssociazioneEnte( @PathParam("id") String id);

    @GET @Path("/{idApplicazione}/funzionalita/{idFunzionalita}")  @Produces({ "application/json" })
    public FunzionalitaApplicazioneEsternaConValidita getApplicazioneEsternaIdFunzionalitaId( @PathParam("idApplicazione") String idApplicazione,  @PathParam("idFunzionalita") String idFunzionalita);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public ApplicazioneEsterna postApplicazioneEsterna( @Valid ApplicazioneEsterna body);

    @POST @Path("/{idApplicazione}/funzionalita") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public FunzionalitaApplicazioneEsternaConValidita postApplicazioneEsternaIdFunzionalita( @PathParam("idApplicazione") String idApplicazione,  @Valid FunzionalitaApplicazioneEsternaConValidita body);

    @PUT @Path("/associazione-utente") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public List<ApplicazioneEsterna> putApplicazioneEsternaAssociazioneUtente( @Valid List<ApplicazioneEsterna> body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public ApplicazioneEsterna putApplicazioneEsternaId( @PathParam("id") String id,  @Valid ApplicazioneEsterna body);

    @PUT @Path("/{id}/associazione-ente") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public ApplicazioneEsternaConValidita putApplicazioneEsternaIdAssociazioneEnte( @PathParam("id") String id,  @Valid ApplicazioneEsternaConValidita body);

    @PUT @Path("/{idApplicazione}/funzionalita/{idFunzionalita}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public FunzionalitaApplicazioneEsternaConValidita putApplicazioneEsternaIdFunzionalitaId( @PathParam("idApplicazione") String idApplicazione,  @PathParam("idFunzionalita") String idFunzionalita,  @Valid FunzionalitaApplicazioneEsternaConValidita body);

}
