/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/pratiche")  
public interface PraticheApi  {
   
    @DELETE @Path("/file/{id}")  
    public void deletePraticheFileId( @PathParam("id") String id);

    @DELETE @Path("/{idPratica}")  
    public void deletePraticheIdPratica( @PathParam("idPratica") String idPratica);

    @DELETE @Path("/{idPratica}/condivisioni/{idCondivisione}")  
    public void deletePraticheIdPraticaCondivisioneIdCondivisione( @PathParam("idPratica") Long idPratica,  @PathParam("idCondivisione") Long idCondivisione);

    @GET @Path("/{idPratica}/attivita")  @Produces({ "application/json" })
    public List<Attivita> getAttivitaIdPratica( @PathParam("idPratica") String idPratica);

    @GET   @Produces({ "application/json" })
    public PraticheResponse getPratiche(  @QueryParam("filter") String filter,   @QueryParam("export") Boolean export);

    @GET @Path("/file")  @Produces({ "application/json" })
    public CaricamentoPraticheResponse getPraticheFile(  @QueryParam("filter") String filter);

    @GET @Path("/file/caricamento-in-bozza")  @Produces({ "application/json" })
    public CaricamentoPraticheResponse getPraticheFileCaricamentoInBozza(  @QueryParam("filter") String filter);

    @GET @Path("/file/{id}")  @Produces({ "application/json" })
    public CaricamentoPraticheResponse getPraticheFileId( @PathParam("id") String id,  @NotNull  @QueryParam("filter") String filter,   @QueryParam("export") Boolean export);

    @GET @Path("/file/path/{dataInserimento}")  @Produces({ "application/json" })
    public List<String> getPraticheFilePath( @PathParam("dataInserimento") String dataInserimento);

    @GET @Path("/file/stati-caricamento")  @Produces({ "application/json" })
    public StatoCaricamentoPratica getPraticheFileStatiCaricamento();

    @GET @Path("/{idPratica}")  @Produces({ "application/json" })
    public Pratica getPraticheIdPratica( @PathParam("idPratica") String idPratica,   @QueryParam("annullata") Boolean annullata);

    @GET @Path("/{idPratica}/diagramma")  
    public void getPraticheIdPraticaDiagramma( @PathParam("idPratica") Integer idPratica);

    @GET @Path("/{idPratica}/in-relazione")  @Produces({ "application/json" })
    public List<PraticaInRelazione> getPraticheIdPraticaRelazioni( @PathParam("idPratica") Integer idPratica);

    @GET @Path("/{idPratica}/storico")  @Produces({ "application/json" })
    public StoricoPratica getPraticheIdPraticaStorico( @PathParam("idPratica") Integer idPratica);

    @GET @Path("/no-link")  @Produces({ "application/json" })
    public PraticheNoLinkResponse getPraticheNoLink();

    @GET @Path("/stato")  @Produces({ "application/json" })
    public List<StatoPratica> getPraticheStato( @Size(max=100)  @QueryParam("tipo_pratica") String tipoPratica);

    @GET @Path("/stato/{idPraticaExt}")  @Produces({ "application/json" })
    public RiassuntoStatoPratica getPraticheStatoIdPraticaExt( @PathParam("idPraticaExt") String idPraticaExt);

    @GET @Path("/task/{idTask}/form")  @Produces({ "application/json" })
    public SimpleForm getPraticheTaskIdTaskForm( @PathParam("idTask") String idTask);

    @GET @Path("/task/{idTask}/subtasks")  @Produces({ "application/json" })
    public PaginaTask getPraticheTaskIdTaskSubtasks( @PathParam("idTask") String idTask);

    @GET @Path("/tipi-relazione-pratica-pratica")  @Produces({ "application/json" })
    public List<TipoRelazionePraticaPratica> getPraticheTipiRelazionePraticaPratica();

    @GET @Path("/visibilita")  @Produces({ "application/json" })
    public Pratica getVisibilitaPraticaById( @NotNull  @QueryParam("idPratica") Long idPratica);

    @GET @Path("/visibilita/task")  @Produces({ "application/json" })
    public Pratica getVisibilitaPraticaByTask( @NotNull  @QueryParam("idTask") String idTask);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Pratica postPratiche( @Valid CreaPraticaRequest body);

    @POST @Path("/file") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CaricamentoPratica postPraticheFile( @Valid CaricamentoPraticaRequest body);

    @POST @Path("/{idPratica}/condivisioni") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CondivisionePratica postPraticheIdPraticaCondivisioni( @PathParam("idPratica") Long idPratica,  @Valid CreaCondivisionePraticaRequest body);

    @POST @Path("/{idPratica}/storico") @Consumes({ "application/json" }) 
    public void postPraticheIdPraticaStorico( @PathParam("idPratica") Integer idPratica,  @Valid StoricoPraticaRequest body);

    @PUT @Path("/file/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CaricamentoPratica putPraticheFileId( @PathParam("id") String id,  @Valid CaricamentoPraticaRequest body);

    @PUT @Path("/file/path/cancellato")  
    public void putPraticheFilePathCancellato( @NotNull  @QueryParam("path") String path);

    @PUT @Path("/{idPratica}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Pratica putPraticheIdPratica( @PathParam("idPratica") String idPratica,   @QueryParam("aggiornaTask") Boolean aggiornaTask,  @Valid Pratica body);

    @PUT @Path("/{idPratica}/{tipoRelazione}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Pratica putPraticheInRelazione( @PathParam("idPratica") String idPratica,  @PathParam("tipoRelazione") String tipoRelazione,  @Valid List<BigDecimal> body);

}
