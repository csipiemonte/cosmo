/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.rest;

import it.csi.cosmo.cosmo.dto.rest.*;


import it.csi.cosmo.cosmo.dto.rest.CompleteUploadDocumentiZipSessionRequest;
import it.csi.cosmo.cosmo.dto.rest.CompleteUploadDocumentiZipSessionResponse;
import it.csi.cosmo.cosmo.dto.rest.CompleteUploadSessionRequest;
import it.csi.cosmo.cosmo.dto.rest.CompleteUploadSessionResponse;
import it.csi.cosmo.cosmo.dto.rest.CreateUploadSessionRequest;
import it.csi.cosmo.cosmo.dto.rest.CreateUploadSessionResponse;
import java.io.File;
import it.csi.cosmo.cosmo.dto.rest.FileDocumentiZipUnzipRequest;
import it.csi.cosmo.cosmo.dto.rest.FileUploadPraticheResult;
import it.csi.cosmo.cosmo.dto.rest.FileUploadResult;
import it.csi.cosmo.cosmo.dto.rest.InfoFile;

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
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/file")
@Consumes({ "application/json" })
@Produces({ "application/json" })


public interface FileApi  {
   
    @DELETE
    @Path("/pratiche")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteFilePratiche( @NotNull @QueryParam("path") String path,@Context SecurityContext securityContext);
    @DELETE
    @Path("/upload-session/{sessionUUID}/cancel")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response deleteFileUploadSessionCancel( @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID,@Context SecurityContext securityContext);
    @GET
    @Path("/pratiche")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response getFilePratiche( @NotNull @QueryParam("path") String path,@Context SecurityContext securityContext);
    @GET
    @Path("/{uploadUUID}")
    @Consumes({ "application/json" })
    @Produces({ "application/octet-stream" })

    public Response getFileTemp( @Size(min=1,max=255) @PathParam("uploadUUID") String uploadUUID,@Context SecurityContext securityContext);
    @POST
    @Path("/documentizip/upload-session")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFileDocumentiZipUploadSession( CreateUploadSessionRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/documentizip/upload-session/{sessionUUID}/complete")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFileDocumentiZipUploadSessionComplete( @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID, CompleteUploadDocumentiZipSessionRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/documentizip/upload-session/{sessionUUID}/parts/{partNumber}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })

    public Response postFileDocumentiZipUploadSessionPart(MultipartFormDataInput input, @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID, @PathParam("partNumber") Long partNumber,@HeaderParam("X-Request-Id") String xRequestId,@Context SecurityContext securityContext);
    @POST
    @Path("/documentizip/unzip-file")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFilePraticheUnzipFile( FileDocumentiZipUnzipRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/pratiche/upload")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })

    public Response postFilePraticheUpload(MultipartFormDataInput input,@HeaderParam("X-Request-Id") String xRequestId,@Context SecurityContext securityContext);
    @POST
    @Path("/upload")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })

    public Response postFileUpload(MultipartFormDataInput input,@HeaderParam("X-Request-Id") String xRequestId,@Context SecurityContext securityContext);
    @POST
    @Path("/upload-session")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFileUploadSession( CreateUploadSessionRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/upload-session/{sessionUUID}/complete")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })

    public Response postFileUploadSessionComplete( @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID, CompleteUploadSessionRequest body,@Context SecurityContext securityContext);
    @POST
    @Path("/upload-session/{sessionUUID}/parts/{partNumber}")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })

    public Response postFileUploadSessionPart(MultipartFormDataInput input, @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID, @PathParam("partNumber") Long partNumber,@HeaderParam("X-Request-Id") String xRequestId,@Context SecurityContext securityContext);
}
