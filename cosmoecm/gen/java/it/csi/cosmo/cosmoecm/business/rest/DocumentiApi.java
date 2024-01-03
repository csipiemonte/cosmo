/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

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
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/documenti")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface DocumentiApi  {
   
    @DELETE
    @Path("/{idDocumento}/contenuti/{idContenuto}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteContenutoDocumentoId( @PathParam("idDocumento") Integer idDocumento, @PathParam("idContenuto") Integer idContenuto,@Context SecurityContext securityContext);
    @DELETE
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteDocumentoId( @PathParam("id") Integer id,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPraticaDa}/duplica-documenti/{idPraticaA}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response duplicaDocumenti( @PathParam("idPraticaDa") Integer idPraticaDa, @PathParam("idPraticaA") Integer idPraticaA,@Context SecurityContext securityContext);
    @POST
    @Path("/{idDocumento}/duplica-documento-in/{idPraticaA}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response duplicaDocumento( @PathParam("idDocumento") Integer idDocumento, @PathParam("idPraticaA") Integer idPraticaA, RelazioniDocumentoDuplicato body,@Context SecurityContext securityContext);
    @GET
    @Path("/{idDocumento}/contenuti")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getContenuti( @PathParam("idDocumento") Integer idDocumento,@Context SecurityContext securityContext);
    @GET
    @Path("/{idDocumento}/contenuti/{idContenuto}")
    @Consumes({ "application/json" })
    @Produces({ "application/octet-stream", "application/json" })

    public Response getContenutoId( @PathParam("idDocumento") Integer idDocumento, @PathParam("idContenuto") Integer idContenuto, @QueryParam("preview") Boolean preview,@Context SecurityContext securityContext);
    @GET
    @Path("/{idDocumento}/contenuti/signed")
    @Consumes({ "application/json" })
    @Produces({ "application/octet-stream", "application/json" })

    public Response getContenutoIdSigned( @PathParam("idDocumento") String idDocumento, @NotNull @QueryParam("signature_key") String signatureKey, @NotNull @QueryParam("signature") String signature,@Context SecurityContext securityContext);
    @GET
    @Path("/invio-stilo")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDocumentiInvioStilo( @NotNull @QueryParam("filter") String filter,@Context SecurityContext securityContext);
    @GET
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDocumento( @QueryParam("filter") String filter, @QueryParam("export") Boolean export,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getDocumentoId( @PathParam("id") Integer id,@Context SecurityContext securityContext);
    @GET
    @Path("/{idDocumento}/contenuti/{idContenuto}/content")
    @Consumes({ "application/json" })
    @Produces({ "application/octet-stream" })

    public Response getIndexContent( @PathParam("idDocumento") Integer idDocumento, @PathParam("idContenuto") Integer idContenuto,@Context SecurityContext securityContext);
    @GET
    @Path("/tipologie-documenti-salvati")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getTipologieDocumentiSalvati( @NotNull @QueryParam("tipologieDocumenti") String tipologieDocumenti,@Context SecurityContext securityContext);
    @POST
    @Path("/{idDocumento}/esito-invio-stilo")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postDocumentiIdDocumentoEsitoInvioStilo( @PathParam("idDocumento") String idDocumento, EsitoInvioStiloRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idDocumento}/invio-documento-stilo")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postDocumentiIdDocumentoInvioDocumentoStilo( @PathParam("idDocumento") String idDocumento, @NotNull @QueryParam("idUd") Long idUd,@Context SecurityContext securityContext);
    @POST
    @Path("/{idDocumento}/smistamento")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postDocumentiIdDocumentoSmistamento( @PathParam("idDocumento") String idDocumento, EsitoSmistaDocumentoRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/esposizione")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postDocumentiIdPraticaEsposizione( @PathParam("idPratica") Long idPratica, PreparaEsposizioneDocumentiRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/richiediApposizioneSigillo")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postDocumentiIdPraticaRichiediApposizioneSigillo( @PathParam("idPratica") String idPratica, RichiediApposizioneSigilloRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/{idPratica}/richiediSmistamento")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postDocumentiIdPraticaRichiediSmistamento( @PathParam("idPratica") String idPratica, RichiediSmistamentoRequest body,@Context SecurityContext securityContext);
    @POST
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postDocumento( CreaDocumentiRequest documento, @NotNull @QueryParam("idPratica") Long idPratica,@Context SecurityContext securityContext);
    @GET
    @Path("/{idPraticaDa}/prepara-duplicazione/{idPraticaA}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response preparaDuplicazione( @PathParam("idPraticaDa") Integer idPraticaDa, @PathParam("idPraticaA") Integer idPraticaA, @QueryParam("restituisciDocumenti") Boolean restituisciDocumenti,@Context SecurityContext securityContext);
    @PUT
    @Path("/{idDocumento}/smistamento")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putDocumentiIdDocumentoSmistamento( @PathParam("idDocumento") String idDocumento, EsitoSmistaDocumentoRequest body,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response putDocumentoId( @PathParam("id") Integer id, AggiornaDocumentoRequest body,@Context SecurityContext securityContext);
}
