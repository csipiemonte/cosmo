/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest;

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
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/fruitori")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface FruitoriApi  {
   
    @GET
    @Path("/documenti/{idDocumento}/contenuto")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getContenutoFruitore( @PathParam("idDocumento") String idDocumento,@Context SecurityContext securityContext);
    @POST
    @Path("/documenti")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postDocumentoFruitore( CreaDocumentiFruitoreRequest documento,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@Context SecurityContext securityContext);
    @POST
    @Path("/documenti/link")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFruitoriDocumentiLink(@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, CreaDocumentiLinkFruitoreRequest body,@Context SecurityContext securityContext);
}
