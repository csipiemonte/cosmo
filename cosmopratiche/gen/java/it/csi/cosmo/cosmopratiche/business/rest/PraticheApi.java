/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest;

import it.csi.cosmo.cosmopratiche.dto.rest.*;


import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import java.math.BigDecimal;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.CondivisionePratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaCondivisionePraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.Esito;
import java.util.List;
import it.csi.cosmo.cosmopratiche.dto.rest.PaginaTask;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticaInRelazione;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheNoLinkResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoCaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoRelazionePraticaPratica;

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
    @Path("/file/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deletePraticheFileId( @PathParam("id") String id,@Context SecurityContext securityContext);
    @DELETE
    @Path("/{idPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deletePraticheIdPratica( @PathParam("idPratica") String idPratica,@Context SecurityContext securityContext);
    @DELETE
    @Path("/{idPratica}/condivisioni/{idCondivisione}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deletePraticheIdPraticaCondivisioneIdCondivisione( @PathParam("idPratica") Long idPratica, @PathParam("idCondivisione") Long idCondivisione,@Context SecurityContext securityContext);
    @GET
    @Path("/{idPratica}/attivita")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getAttivitaIdPratica( @PathParam("idPratica") String idPratica,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPratiche( @QueryParam("filter") String filter, @QueryParam("export") Boolean export,@Context SecurityContext securityContext);
    @GET
    @Path("/file")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheFile( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/file/caricamento-in-bozza")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheFileCaricamentoInBozza( @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    @Path("/file/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheFileId( @PathParam("id") String id, @NotNull @QueryParam("filter") String filter, @QueryParam("export") Boolean export,@Context SecurityContext securityContext);
    @GET
    @Path("/file/path/{dataInserimento}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheFilePath( @PathParam("dataInserimento") String dataInserimento,@Context SecurityContext securityContext);
    @GET
    @Path("/file/stati-caricamento")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheFileStatiCaricamento(@Context SecurityContext securityContext);
    @GET
    @Path("/{idPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheIdPratica( @PathParam("idPratica") String idPratica, @QueryParam("annullata") Boolean annullata,@Context SecurityContext securityContext);
    @GET
    @Path("/{idPratica}/diagramma")
    @Consumes({ "application/json" })
    @Produces({ "application/octet-stream" })

    public Response getPraticheIdPraticaDiagramma( @PathParam("idPratica") Integer idPratica,@Context SecurityContext securityContext);
    @GET
    @Path("/{idPratica}/in-relazione")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheIdPraticaRelazioni( @PathParam("idPratica") Integer idPratica,@Context SecurityContext securityContext);
    @GET
    @Path("/{idPratica}/storico")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheIdPraticaStorico( @PathParam("idPratica") Integer idPratica,@Context SecurityContext securityContext);
    @GET
    @Path("/no-link")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheNoLink(@Context SecurityContext securityContext);
    @GET
    @Path("/stato")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheStato( @Size(max=100) @QueryParam("tipo_pratica") String tipoPratica,@Context SecurityContext securityContext);
    @GET
    @Path("/stato/{idPraticaExt}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheStatoIdPraticaExt( @PathParam("idPraticaExt") String idPraticaExt,@Context SecurityContext securityContext);
    @GET
    @Path("/task/{idTask}/form")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheTaskIdTaskForm( @PathParam("idTask") String idTask,@Context SecurityContext securityContext);
    @GET
    @Path("/task/{idTask}/subtasks")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheTaskIdTaskSubtasks( @PathParam("idTask") String idTask,@Context SecurityContext securityContext);
    @GET
    @Path("/tipi-relazione-pratica-pratica")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getPraticheTipiRelazionePraticaPratica(@Context SecurityContext securityContext);
    @GET
    @Path("/visibilita")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getVisibilitaPraticaById( @NotNull @QueryParam("idPratica") Long idPratica,@Context SecurityContext securityContext);
    @GET
    @Path("/visibilita/task")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getVisibilitaPraticaByTask( @NotNull @QueryParam("idTask") String idTask,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPratiche( CreaPraticaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/file")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticheFile( CaricamentoPraticaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/condivisioni")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticheIdPraticaCondivisioni( @PathParam("idPratica") Long idPratica, CreaCondivisionePraticaRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/storico")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postPraticheIdPraticaStorico( @PathParam("idPratica") Integer idPratica, StoricoPraticaRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/file/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPraticheFileId( @PathParam("id") String id, CaricamentoPraticaRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/file/path/cancellato")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPraticheFilePathCancellato( @NotNull @QueryParam("path") String path,@Context SecurityContext securityContext);
    @PUT
    @Path("/{idPratica}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPraticheIdPratica( @PathParam("idPratica") String idPratica, Pratica body, @QueryParam("aggiornaTask") Boolean aggiornaTask,@Context SecurityContext securityContext);
    @PUT
    @Path("/{idPratica}/{tipoRelazione}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putPraticheInRelazione( @PathParam("idPratica") String idPratica, @PathParam("tipoRelazione") String tipoRelazione, List<BigDecimal> body,@Context SecurityContext securityContext);
}
