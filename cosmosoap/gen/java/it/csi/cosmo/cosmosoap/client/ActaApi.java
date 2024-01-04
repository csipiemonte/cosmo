/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.client;

import it.csi.cosmo.cosmosoap.dto.rest.*;

import it.csi.cosmo.cosmosoap.dto.rest.Classificazioni;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoDocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciResponse;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSemplici;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSempliciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSempliciResponse;
import it.csi.cosmo.cosmosoap.dto.rest.Esito;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentiRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Protocollo;
import it.csi.cosmo.cosmosoap.dto.rest.RegistrazioniClassificazioni;
import it.csi.cosmo.cosmosoap.dto.rest.Titolario;
import it.csi.cosmo.cosmosoap.dto.rest.VociTitolario;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/acta")  
public interface ActaApi  {
   
    @GET @Path("/classificazioni/{idDocumentoSemplice}")  @Produces({ "application/json" })
    public Classificazioni getClassificazioniIdDocumentoSemplice( @PathParam("idDocumentoSemplice") String idDocumentoSemplice,  @NotNull  @QueryParam("idIdentita") String idIdentita);

    @GET @Path("/contenuto-primario/{id}")  @Produces({ "application/json" })
    public ContenutoDocumentoFisico getContenutoPrimarioId( @PathParam("id") String id,  @NotNull  @QueryParam("idIdentita") String idIdentita);

    @GET @Path("/documenti-fisici/{idDocumentoSemplice}")  @Produces({ "application/json" })
    public DocumentiFisiciResponse getDocumentiFisiciByidDocumentoSemplice( @PathParam("idDocumentoSemplice") String idDocumentoSemplice,  @NotNull  @QueryParam("idIdentita") String idIdentita);

    @PUT @Path("/documenti-fisici-map") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public DocumentiFisiciMap getDocumentiFisiciMap( @NotNull  @QueryParam("idIdentita") String idIdentita,  @Valid ImportaDocumentiRequest body);

    @GET @Path("/documenti-semplici")  @Produces({ "application/json" })
    public DocumentiSempliciResponse getDocumentiSemplici( @NotNull  @QueryParam("idIdentita") String idIdentita,   @QueryParam("parolaChiave") String parolaChiave,   @QueryParam("idClassificazione") String idClassificazione);

    @PUT @Path("/documenti-semplici-map") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public DocumentiSempliciMap getDocumentiSempliciMap( @NotNull  @QueryParam("idIdentita") String idIdentita,  @Valid ImportaDocumentiRequest body);

    @GET @Path("/documenti-semplici-pageable")  @Produces({ "application/json" })
    public DocumentiSemplici getDocumentiSempliciPageable( @NotNull  @QueryParam("filter") String filter,  @NotNull  @QueryParam("idIdentita") String idIdentita,  @NotNull  @QueryParam("perProtocollo") Boolean perProtocollo);

    @GET @Path("/identita-disponibili")  @Produces({ "application/json" })
    public IdentitaActaResponse getIdentitaDisponibili();

    @GET @Path("/protocollo/{id}")  @Produces({ "application/json" })
    public Protocollo getProtocolloId( @PathParam("id") String id,  @NotNull  @QueryParam("idIdentita") String idIdentita);

    @GET @Path("/registrazioni")  @Produces({ "application/json" })
    public RegistrazioniClassificazioni getRegistrazioni( @NotNull  @QueryParam("idIdentita") String idIdentita,   @QueryParam("filter") String filter);

    @GET @Path("/ricerca-per-indice-classificazione-esteso")  @Produces({ "application/json" })
    public String getRicercaPerIndiceClassificazioneEstesa(  @QueryParam("identita") String identita,   @QueryParam("indiceClassificazioneEsteso") String indiceClassificazioneEsteso);

    @GET @Path("/ricerca-titolario")  @Produces({ "application/json" })
    public Titolario getRicercaTitolario( @NotNull  @QueryParam("codice") String codice,  @NotNull  @QueryParam("idIdentita") String idIdentita);

    @GET @Path("/ricerca-alberatura-voci-pageable")  @Produces({ "application/json" })
    public VociTitolario ricercaAlberaturaVociPageable( @NotNull  @QueryParam("idIdentita") String idIdentita,  @NotNull  @QueryParam("chiaveTitolario") String chiaveTitolario,  @NotNull  @QueryParam("filter") String filter,   @QueryParam("chiavePadre") String chiavePadre);

}
