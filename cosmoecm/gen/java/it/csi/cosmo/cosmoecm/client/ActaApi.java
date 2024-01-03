/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.DocumentoFisicoActa;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.IdentitaUtente;
import it.csi.cosmo.cosmoecm.dto.rest.ImportaDocumentiActaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.dto.rest.TitolarioActa;
import it.csi.cosmo.cosmoecm.dto.rest.VociTitolarioActa;

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
   
    @GET @Path("/documenti/ricerca-con-indice-classificazione-esteso-aggregazione")  @Produces({ "application/json" })
    public String getActaDocumentiRicercaConIndiceClassificazioneEstesoAggregazione( @NotNull  @QueryParam("identita") String identita,  @NotNull  @QueryParam("indiceClassificazioneEsteso") String indiceClassificazioneEsteso);

    @GET @Path("/documenti")  @Produces({ "application/json" })
    public RiferimentoOperazioneAsincrona getDocumentiActa( @NotNull  @QueryParam("identita") String identita,   @QueryParam("filter") String filter);

    @GET @Path("/documenti/{id}/documenti-fisici")  @Produces({ "application/json" })
    public List<DocumentoFisicoActa> getDocumentiFisiciActa( @PathParam("id") String id,  @NotNull  @QueryParam("identita") String identita);

    @GET @Path("/identita-utente")  @Produces({ "application/json" })
    public List<IdentitaUtente> getIdentitaUtente();

    @GET @Path("/ricerca-alberatura-voci-pageable")  @Produces({ "application/json" })
    public VociTitolarioActa getRicercaAlberaturaVociPageable( @NotNull  @QueryParam("idIdentita") String idIdentita,  @NotNull  @QueryParam("chiaveTitolario") String chiaveTitolario,  @NotNull  @QueryParam("filter") String filter,   @QueryParam("chiavePadre") String chiavePadre);

    @GET @Path("/ricerca-titolario")  @Produces({ "application/json" })
    public TitolarioActa getRicercaTitolario( @NotNull  @QueryParam("codice") String codice,  @NotNull  @QueryParam("idIdentita") String idIdentita);

    @POST @Path("/documenti/importa") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RiferimentoOperazioneAsincrona importaDocumentiActa( @NotNull  @QueryParam("identita") String identita,  @Valid ImportaDocumentiActaRequest body);

}
