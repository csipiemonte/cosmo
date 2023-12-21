/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@SuppressWarnings("unused")
@Path("/file")  
public interface FileApi  {
   
    @DELETE @Path("/pratiche")  
    public void deleteFilePratiche( @NotNull  @QueryParam("path") String path);

    @DELETE @Path("/upload-session/{sessionUUID}/cancel")  
    public void deleteFileUploadSessionCancel( @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID);

    @GET @Path("/pratiche")  @Produces({ "application/json" })
    public List<InfoFile> getFilePratiche( @NotNull  @QueryParam("path") String path);

    @GET @Path("/{uploadUUID}")  
    public void getFileTemp( @Size(min=1,max=255) @PathParam("uploadUUID") String uploadUUID);

    @POST @Path("/documentizip/upload-session") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CreateUploadSessionResponse postFileDocumentiZipUploadSession( @Valid CreateUploadSessionRequest body);

    @POST @Path("/documentizip/upload-session/{sessionUUID}/complete") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CompleteUploadDocumentiZipSessionResponse postFileDocumentiZipUploadSessionComplete( @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID,  @Valid CompleteUploadDocumentiZipSessionRequest body);

    @POST @Path("/documentizip/upload-session/{sessionUUID}/parts/{partNumber}") @Consumes({ "multipart/form-data" }) 
    public void postFileDocumentiZipUploadSessionPart(MultipartFormDataInput input,  @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID,  @PathParam("partNumber") Long partNumber,   @HeaderParam("X-Request-Id") String xRequestId);

    @POST @Path("/documentizip/unzip-file") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public FileUploadResult postFilePraticheUnzipFile( @Valid FileDocumentiZipUnzipRequest body);

    @POST @Path("/pratiche/upload") @Consumes({ "multipart/form-data" }) @Produces({ "application/json" })
    public FileUploadPraticheResult postFilePraticheUpload(MultipartFormDataInput input,   @HeaderParam("X-Request-Id") String xRequestId);

    @POST @Path("/upload") @Consumes({ "multipart/form-data" }) @Produces({ "application/json" })
    public FileUploadResult postFileUpload(MultipartFormDataInput input,   @HeaderParam("X-Request-Id") String xRequestId);

    @POST @Path("/upload-session") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CreateUploadSessionResponse postFileUploadSession( @Valid CreateUploadSessionRequest body);

    @POST @Path("/upload-session/{sessionUUID}/complete") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public CompleteUploadSessionResponse postFileUploadSessionComplete( @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID,  @Valid CompleteUploadSessionRequest body);

    @POST @Path("/upload-session/{sessionUUID}/parts/{partNumber}") @Consumes({ "multipart/form-data" }) 
    public void postFileUploadSessionPart(MultipartFormDataInput input,  @Size(min=1,max=255) @PathParam("sessionUUID") String sessionUUID,  @PathParam("partNumber") Long partNumber,   @HeaderParam("X-Request-Id") String xRequestId);

}
