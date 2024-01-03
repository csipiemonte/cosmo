/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.AggiornaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.ContenutiDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.Documenti;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoInvioStiloRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistamentoResponse;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RelazioniDocumentoDuplicato;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediApposizioneSigilloRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediSmistamentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.dto.rest.VerificaTipologiaDocumentiSalvati;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/documenti")  
public interface DocumentiApi  {
   
    @DELETE @Path("/{idDocumento}/contenuti/{idContenuto}")  @Produces({ "application/json" })
    public void deleteContenutoDocumentoId( @PathParam("idDocumento") Integer idDocumento,  @PathParam("idContenuto") Integer idContenuto);

    @DELETE @Path("/{id}")  @Produces({ "application/json" })
    public void deleteDocumentoId( @PathParam("id") Integer id);

    @POST @Path("/{idPraticaDa}/duplica-documenti/{idPraticaA}")  @Produces({ "application/json" })
    public RiferimentoOperazioneAsincrona duplicaDocumenti( @PathParam("idPraticaDa") Integer idPraticaDa,  @PathParam("idPraticaA") Integer idPraticaA);

    @POST @Path("/{idDocumento}/duplica-documento-in/{idPraticaA}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public RelazioniDocumentoDuplicato duplicaDocumento( @PathParam("idDocumento") Integer idDocumento,  @PathParam("idPraticaA") Integer idPraticaA,  @Valid RelazioniDocumentoDuplicato body);

    @GET @Path("/{idDocumento}/contenuti")  @Produces({ "application/json" })
    public ContenutiDocumento getContenuti( @PathParam("idDocumento") Integer idDocumento);

    @GET @Path("/{idDocumento}/contenuti/{idContenuto}")  @Produces({ "application/octet-stream", "application/json" })
    public void getContenutoId( @PathParam("idDocumento") Integer idDocumento,  @PathParam("idContenuto") Integer idContenuto,   @QueryParam("preview") Boolean preview);

    @GET @Path("/{idDocumento}/contenuti/signed")  @Produces({ "application/octet-stream", "application/json" })
    public void getContenutoIdSigned( @PathParam("idDocumento") String idDocumento,  @NotNull  @QueryParam("signature_key") String signatureKey,  @NotNull  @QueryParam("signature") String signature);

    @GET @Path("/invio-stilo")  @Produces({ "application/json" })
    public List<Documento> getDocumentiInvioStilo( @NotNull  @QueryParam("filter") String filter);

    @GET   @Produces({ "application/json" })
    public DocumentiResponse getDocumento(  @QueryParam("filter") String filter,   @QueryParam("export") Boolean export);

    @GET @Path("/{id}")  @Produces({ "application/json" })
    public Documento getDocumentoId( @PathParam("id") Integer id);

    @GET @Path("/{idDocumento}/contenuti/{idContenuto}/content")  
    public void getIndexContent( @PathParam("idDocumento") Integer idDocumento,  @PathParam("idContenuto") Integer idContenuto);

    @GET @Path("/tipologie-documenti-salvati")  @Produces({ "application/json" })
    public List<VerificaTipologiaDocumentiSalvati> getTipologieDocumentiSalvati( @NotNull  @QueryParam("tipologieDocumenti") String tipologieDocumenti);

    @POST @Path("/{idDocumento}/esito-invio-stilo") @Consumes({ "application/json" }) 
    public void postDocumentiIdDocumentoEsitoInvioStilo( @PathParam("idDocumento") String idDocumento,  @Valid EsitoInvioStiloRequest body);

    @POST @Path("/{idDocumento}/invio-documento-stilo")  
    public void postDocumentiIdDocumentoInvioDocumentoStilo( @PathParam("idDocumento") String idDocumento,  @NotNull  @QueryParam("idUd") Long idUd);

    @POST @Path("/{idDocumento}/smistamento") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public EsitoSmistamentoResponse postDocumentiIdDocumentoSmistamento( @PathParam("idDocumento") String idDocumento,  @Valid EsitoSmistaDocumentoRequest body);

    @POST @Path("/{idPratica}/esposizione") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public PreparaEsposizioneDocumentiResponse postDocumentiIdPraticaEsposizione( @PathParam("idPratica") Long idPratica,  @Valid PreparaEsposizioneDocumentiRequest body);

    @POST @Path("/{idPratica}/richiediApposizioneSigillo") @Consumes({ "application/json" }) 
    public void postDocumentiIdPraticaRichiediApposizioneSigillo( @PathParam("idPratica") String idPratica,  @Valid RichiediApposizioneSigilloRequest body);

    @POST @Path("/{idPratica}/richiediSmistamento") @Consumes({ "application/json" }) 
    public void postDocumentiIdPraticaRichiediSmistamento( @PathParam("idPratica") String idPratica,  @Valid RichiediSmistamentoRequest body);

    @POST  @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Documenti postDocumento( @NotNull  @QueryParam("idPratica") Long idPratica,  @NotNull @Valid CreaDocumentiRequest documento);

    @GET @Path("/{idPraticaDa}/prepara-duplicazione/{idPraticaA}")  @Produces({ "application/json" })
    public Documenti preparaDuplicazione( @PathParam("idPraticaDa") Integer idPraticaDa,  @PathParam("idPraticaA") Integer idPraticaA,   @QueryParam("restituisciDocumenti") Boolean restituisciDocumenti);

    @PUT @Path("/{idDocumento}/smistamento") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public EsitoSmistamentoResponse putDocumentiIdDocumentoSmistamento( @PathParam("idDocumento") String idDocumento,  @Valid EsitoSmistaDocumentoRequest body);

    @PUT @Path("/{id}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Documento putDocumentoId( @PathParam("id") Integer id,  @Valid AggiornaDocumentoRequest body);

}
