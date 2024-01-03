/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
/*
 * Copyright 2001-2019 CSI Piemonte. All Rights Reserved.
 *
 * This software is proprietary information of CSI Piemonte. Use is subject to license terms.
 *
 */

package it.csi.cosmo.cosmoecm.integration.rest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.client.IndexApi;
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
/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/soap/index",
    interceptors = CosmoEcmM2MFeignClientConfiguration.class,
    configurator = CosmoSoapFeignClientConfiguration.class)
public interface CosmoSoapIndexFeignClient extends IndexApi {

  @Override
  @PUT
  @Path("/aggiorna")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Entity aggiorna(@Valid Entity body);

  @Override
  @PUT
  @Path("/copia-nodo")
  @Produces({"application/json"})
  public String copiaNodo(@NotNull @QueryParam("sourceIdentifierFrom") String sourceIdentifierFrom,
      @QueryParam("sourceIdentifierTo") String sourceIdentifierTo);

  @Override
  @POST
  @Path("/file/{parentIdentifier}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Entity creaFile(@PathParam("parentIdentifier") String parentIdentifier,
      @Valid ContenutoEntity body);

  @Override
  @DELETE
  @Path("/cancella/{identifier}")
  public void deleteIdentifier(@PathParam("identifier") String identifier);

  @Override
  @PUT
  @Path("/estrai-busta/{targetContainerIdentifier}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Entity estraiBusta(
      @PathParam("targetContainerIdentifier") String targetContainerIdentifier, @Valid Entity body);

  @Override
  @GET
  @Path("/cartella/{uuidOrPath}")
  @Produces({"application/json"})
  public Folder findFolder(@PathParam("uuidOrPath") String uuidOrPath);

  @Override
  @POST
  @Path("/share")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  SharedLink share(@Valid CondivisioniRequest var1);

  @Override
  @GET
  @Path("/share/{sourceIdentifier}")
  @Produces({"application/json"})
  ListShareDetail shareId(@PathParam("sourceIdentifier") String var1);
  @Override
  @GET
  @Path("/file/{parentIdentifier}")
  @Produces({"application/json"})
  public Entity getFile(@PathParam("parentIdentifier") String parentIdentifier, @QueryParam("withContent") Boolean withContent);

  @Override
  @GET
  @Path("/info-formato-file/{identifier}")
  @Produces({"application/json"})
  public FileFormatInfo getInfoFormatoFile(@PathParam("identifier") String identifier);

  @Override
  @POST
  @Path("/cartella/{uuidOrPath}")
  public String createFolder(@PathParam("uuidOrPath") String uuidOrPath);

  @Override
  @PUT
  @Path("/sposta")
  public void sposta(@NotNull @QueryParam("source") String source,
      @NotNull @QueryParam("targetContainer") String targetContainer);

  @Override
  @POST
  @Path("/verifica-firma/{sourceIdentifier}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public VerifyReport verificaFirma(@PathParam("sourceIdentifier") String sourceIdentifier,
      @Valid SignatureVerificationParameters body);

  @Override
  @POST
  @Path("/file")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  public Entity creaFileIndex(@Valid CreaFileRequest body);
}
