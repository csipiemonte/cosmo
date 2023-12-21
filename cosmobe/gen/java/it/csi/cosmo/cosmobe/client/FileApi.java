/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@SuppressWarnings("unused")
@Path("/file")  
public interface FileApi  {
   
    @DELETE @Path("/upload-session/{sessionUUID}/cancel")  @Produces({ "application/json", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse", "conflictSampleResponse",  })
    public void cancelUploadSession( @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor);

    @POST @Path("/upload-session/{sessionUUID}/complete") @Consumes({ "application/json" }) @Produces({ "application/json", "completeUploadSessionSampleResponse", "notFoundSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "conflictSampleResponse",  })
    public CompleteUploadSessionResponse completeUploadSession( @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid CompleteUploadSessionRequest body);

    @POST @Path("/upload-session") @Consumes({ "application/json" }) @Produces({ "application/json", "createUploadSessionSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse",  })
    public CreateUploadSessionResponse createUploadSession(  @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor,  @Valid CreateUploadSessionRequest body);

    @POST @Path("/upload") @Consumes({ "multipart/form-data" }) @Produces({ "application/json", "uploadFileSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse", "conflictSampleResponse",  })
    public FileUploadResult postFileUpload(MultipartFormDataInput input,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor);

    @POST @Path("/upload-file-utenti") @Consumes({ "multipart/form-data" }) @Produces({ "application/json", "uploadFileSampleResponse", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse", "conflictSampleResponse",  })
    public FileUploadExcelResult postUploadFileUtenti(MultipartFormDataInput input,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor);

    @POST @Path("/upload-session/{sessionUUID}/parts/{partNumber}") @Consumes({ "multipart/form-data" }) @Produces({ "application/json", "unauthorizedSampleResponse", "forbiddenSampleResponse", "notFoundSampleResponse", "conflictSampleResponse",  })
    public void uploadSessionPart(MultipartFormDataInput input,  @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID,  @PathParam("partNumber") Long partNumber,   @HeaderParam("X-Request-Id") String xRequestId,   @HeaderParam("X-Forwarded-For") String xForwardedFor);

}
