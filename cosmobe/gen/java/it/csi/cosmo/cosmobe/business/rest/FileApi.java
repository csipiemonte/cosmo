/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.business.rest;

import it.csi.cosmo.cosmobe.dto.rest.*;


import it.csi.cosmo.cosmobe.dto.rest.CompleteUploadSessionRequest;
import it.csi.cosmo.cosmobe.dto.rest.CompleteUploadSessionResponse;
import it.csi.cosmo.cosmobe.dto.rest.CreateUploadSessionRequest;
import it.csi.cosmo.cosmobe.dto.rest.CreateUploadSessionResponse;
import it.csi.cosmo.cosmobe.dto.rest.Esito;
import java.io.File;
import it.csi.cosmo.cosmobe.dto.rest.FileUploadExcelResult;
import it.csi.cosmo.cosmobe.dto.rest.FileUploadResult;

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
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/file")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface FileApi  {
   
    @DELETE
    @Path("/upload-session/{sessionUUID}/cancel")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response cancelUploadSession( @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@Context SecurityContext securityContext);
    @POST
    @Path("/upload-session/{sessionUUID}/complete")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response completeUploadSession( @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID, CompleteUploadSessionRequest body,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@Context SecurityContext securityContext);
    @POST
    @Path("/upload-session")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response createUploadSession(@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor, CreateUploadSessionRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/upload")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })

    public Response postFileUpload(MultipartFormDataInput input,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@Context SecurityContext securityContext);
    @POST
    @Path("/upload-file-utenti")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })

    public Response postUploadFileUtenti(MultipartFormDataInput input,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@Context SecurityContext securityContext);
    @POST
    @Path("/upload-session/{sessionUUID}/parts/{partNumber}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })

    public Response uploadSessionPart(MultipartFormDataInput input, @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID, @PathParam("partNumber") Long partNumber,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@Context SecurityContext securityContext);
}
