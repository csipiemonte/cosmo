/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.business.rest;

import it.csi.cosmo.cosmosoap.dto.rest.*;


import it.csi.cosmo.cosmosoap.dto.rest.CondivisioniRequest;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoEntity;
import it.csi.cosmo.cosmosoap.dto.rest.CreaFileRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.FileFormatInfo;
import it.csi.cosmo.cosmosoap.dto.rest.Folder;
import it.csi.cosmo.cosmosoap.dto.rest.ListShareDetail;
import it.csi.cosmo.cosmosoap.dto.rest.SharedLink;
import it.csi.cosmo.cosmosoap.dto.rest.SignatureVerificationParameters;
import it.csi.cosmo.cosmosoap.dto.rest.VerifyReport;

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

@Path("/index")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface IndexApi  {
   
    @PUT
    @Path("/aggiorna")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response aggiorna( Entity body,@Context SecurityContext securityContext);
    @PUT
    @Path("/copia-nodo")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response copiaNodo( @NotNull @QueryParam("sourceIdentifierFrom") String sourceIdentifierFrom, @QueryParam("sourceIdentifierTo") String sourceIdentifierTo,@Context SecurityContext securityContext);
    @POST
    @Path("/file/{parentIdentifier}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response creaFile( @PathParam("parentIdentifier") String parentIdentifier, ContenutoEntity body,@Context SecurityContext securityContext);
    @POST
    @Path("/file")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response creaFileIndex( CreaFileRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/cartella/{uuidOrPath}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response createFolder( @PathParam("uuidOrPath") String uuidOrPath,@Context SecurityContext securityContext);
    @DELETE
    @Path("/cancella/{identifier}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteIdentifier( @PathParam("identifier") String identifier,@Context SecurityContext securityContext);
    @PUT
    @Path("/estrai-busta/{targetContainerIdentifier}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response estraiBusta( @PathParam("targetContainerIdentifier") String targetContainerIdentifier, Entity body,@Context SecurityContext securityContext);
    @GET
    @Path("/cartella/{uuidOrPath}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response findFolder( @PathParam("uuidOrPath") String uuidOrPath,@Context SecurityContext securityContext);
    @GET
    @Path("/file/{parentIdentifier}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFile( @PathParam("parentIdentifier") String parentIdentifier, @QueryParam("withContent") Boolean withContent,@Context SecurityContext securityContext);
    @GET
    @Path("/info-formato-file/{identifier}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getInfoFormatoFile( @PathParam("identifier") String identifier,@Context SecurityContext securityContext);
    @POST
    @Path("/share")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response share( CondivisioniRequest body,@Context SecurityContext securityContext);
    @GET
    @Path("/share/{sourceIdentifier}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response shareId( @PathParam("sourceIdentifier") String sourceIdentifier,@Context SecurityContext securityContext);
    @PUT
    @Path("/sposta")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response sposta( @NotNull @QueryParam("source") String source, @NotNull @QueryParam("targetContainer") String targetContainer,@Context SecurityContext securityContext);
    @POST
    @Path("/verifica-firma/{sourceIdentifier}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response verificaFirma( @PathParam("sourceIdentifier") String sourceIdentifier, SignatureVerificationParameters body,@Context SecurityContext securityContext);
}
