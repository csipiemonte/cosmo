/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.client;

import it.csi.cosmo.cosmobusiness.dto.rest.*;

import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Esito;
import it.csi.cosmo.cosmobusiness.dto.rest.FormTask;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.GetPayloadAttivazioneSistemaEsternoResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InformazioniPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaChiamataEsternaRequest;
import java.util.List;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaCommenti;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaTask;
import it.csi.cosmo.cosmobusiness.dto.rest.Pratica;
import it.csi.cosmo.cosmobusiness.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabileProcesso;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabiliProcessoResponse;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/pratiche")  
public interface PraticheApi  {
   
    @DELETE @Path("/{id}")  
    public void deletePraticheId( @PathParam("id") String id);

    @DELETE @Path("/{id}/commenti/{idCommento}")  
    public void deletePraticheIdCommentiIdCommento( @PathParam("idCommento") String idCommento,  @PathParam("id") String id);

    @DELETE @Path("/variabili/{processInstanceId}")  
    public void deletePraticheVariabiliProcessInstanceId( @PathParam("processInstanceId") String processInstanceId,  @NotNull  @QueryParam("variableName") String variableName);

    @GET @Path("/{id}/commenti")  @Produces({ "application/json" })
    public PaginaCommenti getCommenti( @PathParam("id") String id);

    @GET @Path("/{idPratica}/attivita/{idAttivita}/attivazione-sistema-esterno/payload")  @Produces({ "application/json" })
    public GetPayloadAttivazioneSistemaEsternoResponse getPraticaAttivitaAsePayload( @PathParam("idPratica") Long idPratica,  @PathParam("idAttivita") Long idAttivita);

    @GET @Path("/history-process/{processInstanceId}/variabili")  @Produces({ "application/json" })
    public VariabiliProcessoResponse getPraticheHistoryVariables( @PathParam("processInstanceId") String processInstanceId,   @QueryParam("includeProcessVariables") Boolean includeProcessVariables);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public Pratica getPraticheId( @PathParam("id") String id);

    @GET @Path("/{id}/commenti/{idCommento}")  @Produces({ "application/json" })
    public Commento getPraticheIdCommentiIdCommento( @PathParam("idCommento") String idCommento,  @PathParam("id") String id);

    @GET @Path("/{id}/elaborazione/contesto")  @Produces({ "application/json" })
    public Object getPraticheIdElaborazioneContesto( @PathParam("id") Long id);

    @GET @Path("/{idPratica}/info-per-smistamento/{idDocumento}")  @Produces({ "application/json" })
    public InformazioniPratica getPraticheIdInfo( @PathParam("idPratica") String idPratica,  @PathParam("idDocumento") String idDocumento);

    @GET @Path("/{id}/storico-task")  @Produces({ "application/json" })
    public PaginaTask getPraticheIdStoricoTask( @PathParam("id") String id);

    @GET @Path("/{id}/task")  @Produces({ "application/json" })
    public PaginaTask getPraticheIdTask( @PathParam("id") String id);

    @GET @Path("/stato/{idPraticaExt}")  @Produces({ "application/json" })
    public RiassuntoStatoPratica getPraticheStatoIdPraticaExt( @PathParam("idPraticaExt") String idPraticaExt);

    @GET @Path("/task/{idTask}")  @Produces({ "application/json" })
    public FormTask getPraticheTaskIdTask( @PathParam("idTask") String idTask);

    @GET @Path("/task/{idTask}/subtasks")  @Produces({ "application/json" })
    public PaginaTask getPraticheTaskIdTaskSubtasks( @PathParam("idTask") String idTask);

    @GET @Path("/variabili/{processInstanceId}")  @Produces({ "application/json" })
    public VariabiliProcessoResponse getPraticheVariabiliProcessInstanceId( @PathParam("processInstanceId") String processInstanceId);

    @PATCH @Path("/variabili/{processInstanceId}") @Consumes({ "application/json" }) 
    public void patchPraticheVariabiliProcessInstanceId( @PathParam("processInstanceId") String processInstanceId,  @Valid List<VariabileProcesso> body);

    @POST @Path("/{id}/commenti") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Commento postCommenti( @PathParam("id") String id,  @Valid Commento body);

    @POST @Path("/{idPratica}/assegna") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public AssegnaAttivitaResponse postPraticaAssegna( @PathParam("idPratica") Long idPratica,  @Valid AssegnaAttivitaRequest body);

    @POST @Path("/{idPratica}/attivita/{idAttivita}/attivazione-sistema-esterno/invia")  @Produces({ "application/json" })
    public RiferimentoOperazioneAsincrona postPraticaAttivitaAseInvia( @PathParam("idPratica") Long idPratica,  @PathParam("idAttivita") Long idAttivita);

    @POST @Path("/{idPratica}/attivita/{idAttivita}/assegna") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public AssegnaAttivitaResponse postPraticaAttivitaAssegna( @PathParam("idPratica") Long idPratica,  @PathParam("idAttivita") Long idAttivita,  @Valid AssegnaAttivitaRequest body);

    @POST @Path("/{idPratica}/attivita/{idAttivita}/assegnami")  @Produces({ "application/json" })
    public AssegnaAttivitaResponse postPraticaAttivitaAssegnami( @PathParam("idPratica") Long idPratica,  @PathParam("idAttivita") Long idAttivita);

    @POST @Path("/{idPratica}/attivita/{idAttivita}/conferma") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RiferimentoOperazioneAsincrona postPraticaAttivitaConferma( @PathParam("idPratica") Long idPratica,  @PathParam("idAttivita") Long idAttivita,  @Valid Task body);

    @POST @Path("/{idPratica}/attivita/{idAttivita}/salva") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RiferimentoOperazioneAsincrona postPraticaAttivitaSalva( @PathParam("idPratica") Long idPratica,  @PathParam("idAttivita") Long idAttivita,  @Valid Task body);

    @POST @Path("/{idPratica}/chiamata-esterna/invia") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RiferimentoOperazioneAsincrona postPraticaChiamataEstermaInvia( @PathParam("idPratica") Long idPratica,  @Valid InviaChiamataEsternaRequest body);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public OperazioneAsincrona postPratiche( @Valid CreaPraticaRequest body);

    @POST @Path("/{id}/elaborazione") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Object postPraticheIdElaborazione( @PathParam("id") Long id,  @Valid GetElaborazionePraticaRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Pratica putPraticheId( @PathParam("id") String id,   @QueryParam("aggiornaAttivita") Boolean aggiornaAttivita,  @Valid Pratica body);

    @PUT @Path("/variabili/{processInstanceId}") @Consumes({ "application/json" }) 
    public void putPraticheVariabiliProcessInstanceId( @PathParam("processInstanceId") String processInstanceId,  @Valid List<VariabileProcesso> body);

}
