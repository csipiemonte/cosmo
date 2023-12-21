/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest;

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
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/pratiche")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface PraticheApi  {
   
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deletePraticheId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @DELETE
    @Path("/{id}/commenti/{idCommento}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deletePraticheIdCommentiIdCommento( @PathParam("idCommento") String idCommento, @PathParam("id") String id,@Context SecurityContext securityContext);
    @DELETE
    @Path("/variabili/{processInstanceId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deletePraticheVariabiliProcessInstanceId( @PathParam("processInstanceId") String processInstanceId, @NotNull @QueryParam("variableName") String variableName,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}/commenti")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getCommenti( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{idPratica}/attivita/{idAttivita}/attivazione-sistema-esterno/payload")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticaAttivitaAsePayload( @PathParam("idPratica") Long idPratica, @PathParam("idAttivita") Long idAttivita,@Context SecurityContext securityContext);
    @GET
    @Path("/history-process/{processInstanceId}/variabili")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheHistoryVariables( @PathParam("processInstanceId") String processInstanceId, @QueryParam("includeProcessVariables") Boolean includeProcessVariables,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}/commenti/{idCommento}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheIdCommentiIdCommento( @PathParam("idCommento") String idCommento, @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}/elaborazione/contesto")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheIdElaborazioneContesto( @PathParam("id") Long id,@Context SecurityContext securityContext);
    @GET
    @Path("/{idPratica}/info-per-smistamento/{idDocumento}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheIdInfo( @PathParam("idPratica") String idPratica, @PathParam("idDocumento") String idDocumento,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}/storico-task")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheIdStoricoTask( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}/task")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheIdTask( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/stato/{idPraticaExt}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheStatoIdPraticaExt( @PathParam("idPraticaExt") String idPraticaExt,@Context SecurityContext securityContext);
    @GET
    @Path("/task/{idTask}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheTaskIdTask( @PathParam("idTask") String idTask,@Context SecurityContext securityContext);
    @GET
    @Path("/task/{idTask}/subtasks")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheTaskIdTaskSubtasks( @PathParam("idTask") String idTask,@Context SecurityContext securityContext);
    @GET
    @Path("/variabili/{processInstanceId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheVariabiliProcessInstanceId( @PathParam("processInstanceId") String processInstanceId,@Context SecurityContext securityContext);
    @PATCH
    @Path("/variabili/{processInstanceId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response patchPraticheVariabiliProcessInstanceId( @PathParam("processInstanceId") String processInstanceId, List<VariabileProcesso> body,@Context SecurityContext securityContext);
    @POST
    @Path("/{id}/commenti")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postCommenti( @PathParam("id") String id, Commento body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/assegna")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticaAssegna( @PathParam("idPratica") Long idPratica, AssegnaAttivitaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/attivita/{idAttivita}/attivazione-sistema-esterno/invia")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticaAttivitaAseInvia( @PathParam("idPratica") Long idPratica, @PathParam("idAttivita") Long idAttivita,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/attivita/{idAttivita}/assegna")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticaAttivitaAssegna( @PathParam("idPratica") Long idPratica, @PathParam("idAttivita") Long idAttivita, AssegnaAttivitaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/attivita/{idAttivita}/assegnami")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticaAttivitaAssegnami( @PathParam("idPratica") Long idPratica, @PathParam("idAttivita") Long idAttivita,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/attivita/{idAttivita}/conferma")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticaAttivitaConferma( @PathParam("idPratica") Long idPratica, @PathParam("idAttivita") Long idAttivita, Task body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/attivita/{idAttivita}/salva")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticaAttivitaSalva( @PathParam("idPratica") Long idPratica, @PathParam("idAttivita") Long idAttivita, Task body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/chiamata-esterna/invia")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticaChiamataEstermaInvia( @PathParam("idPratica") Long idPratica, InviaChiamataEsternaRequest body,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPratiche( CreaPraticaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/{id}/elaborazione")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticheIdElaborazione( @PathParam("id") Long id, GetElaborazionePraticaRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPraticheId( @PathParam("id") String id, Pratica body, @QueryParam("aggiornaAttivita") Boolean aggiornaAttivita,@Context SecurityContext securityContext);
    @PUT
    @Path("/variabili/{processInstanceId}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPraticheVariabiliProcessInstanceId( @PathParam("processInstanceId") String processInstanceId, List<VariabileProcesso> body,@Context SecurityContext securityContext);
}
