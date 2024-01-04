/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.client;

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
import javax.ws.rs.*;
import javax.validation.constraints.*;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Path("/index")  
public interface IndexApi  {
   
    @PUT @Path("/aggiorna") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Entity aggiorna( @Valid Entity body);

    @PUT @Path("/copia-nodo")  @Produces({ "application/json" })
    public String copiaNodo( @NotNull  @QueryParam("sourceIdentifierFrom") String sourceIdentifierFrom,   @QueryParam("sourceIdentifierTo") String sourceIdentifierTo);

    @POST @Path("/file/{parentIdentifier}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Entity creaFile( @PathParam("parentIdentifier") String parentIdentifier,  @Valid ContenutoEntity body);

    @POST @Path("/file") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Entity creaFileIndex( @Valid CreaFileRequest body);

    @POST @Path("/cartella/{uuidOrPath}")  @Produces({ "application/json" })
    public String createFolder( @PathParam("uuidOrPath") String uuidOrPath);

    @DELETE @Path("/cancella/{identifier}")  
    public void deleteIdentifier( @PathParam("identifier") String identifier);

    @PUT @Path("/estrai-busta/{targetContainerIdentifier}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public Entity estraiBusta( @PathParam("targetContainerIdentifier") String targetContainerIdentifier,  @Valid Entity body);

    @GET @Path("/cartella/{uuidOrPath}")  @Produces({ "application/json" })
    public Folder findFolder( @PathParam("uuidOrPath") String uuidOrPath);

    @GET @Path("/file/{parentIdentifier}")  @Produces({ "application/json" })
    public Entity getFile( @PathParam("parentIdentifier") String parentIdentifier,   @QueryParam("withContent") Boolean withContent);

    @GET @Path("/info-formato-file/{identifier}")  @Produces({ "application/json" })
    public FileFormatInfo getInfoFormatoFile( @PathParam("identifier") String identifier);

    @POST @Path("/share") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public SharedLink share( @Valid CondivisioniRequest body);

    @GET @Path("/share/{sourceIdentifier}")  @Produces({ "application/json" })
    public ListShareDetail shareId( @PathParam("sourceIdentifier") String sourceIdentifier);

    @PUT @Path("/sposta")  
    public void sposta( @NotNull  @QueryParam("source") String source,  @NotNull  @QueryParam("targetContainer") String targetContainer);

    @POST @Path("/verifica-firma/{sourceIdentifier}") @Consumes({ "application/json" }) @Produces({ "application/json" })
    public VerifyReport verificaFirma( @PathParam("sourceIdentifier") String sourceIdentifier,  @Valid SignatureVerificationParameters body);

}
