/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.client;

import it.csi.cosmo.cosmoecm.dto.rest.*;

import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiLinkFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentiFruitore;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentiLinkFruitore;

import java.util.List;
import java.util.Map;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/fruitori")  
public interface FruitoriApi  {
   
    @GET @Path("/documenti/{idDocumento}/contenuto")  @Produces({ "application/json" })
    public Object getContenutoFruitore( @PathParam("idDocumento") String idDocumento);

    @POST @Path("/documenti") @Consumes({ "application/json" }) @Produces({ "application/json", "creaDocumentiFruitoreSampleResponse", "badRequestSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse", "conflictSampleResponse",  })
    public EsitoCreazioneDocumentiFruitore postDocumentoFruitore( @NotNull @Valid CreaDocumentiFruitoreRequest documento,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor);

    @POST @Path("/documenti/link") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public EsitoCreazioneDocumentiLinkFruitore postFruitoriDocumentiLink(  @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid CreaDocumentiLinkFruitoreRequest body);

}
