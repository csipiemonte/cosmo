/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmosoap.business.rest.IndexApi;
import it.csi.cosmo.cosmosoap.business.service.Api2IndexService;
import it.csi.cosmo.cosmosoap.dto.rest.CondivisioniRequest;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoEntity;
import it.csi.cosmo.cosmosoap.dto.rest.CreaFileRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.SignatureVerificationParameters;

/**
 *
 */

public class IndexApiImpl extends ParentApiImpl implements IndexApi {

  @Autowired
  private Api2IndexService api2IndexService;

  @Override
  public Response aggiorna(Entity body, SecurityContext securityContext) {
    return Response.ok(api2IndexService.aggiorna(body)).build();
  }

  @Override
  public Response copiaNodo(String sourceIdentifierFrom, String sourceIdentifierTo,
      SecurityContext securityContext) {
    return Response.ok(api2IndexService.copyNode(sourceIdentifierFrom, sourceIdentifierTo)).build();
  }

  @Override
  public Response creaFile(String parentIdentifier, ContenutoEntity body,
      SecurityContext securityContext) {
    return Response
        .ok(api2IndexService.create(parentIdentifier, body.getEntity(), body.getContent())).build();
  }

  @Override
  public Response deleteIdentifier(String identifier, SecurityContext securityContext) {
    api2IndexService.delete(identifier);
    return Response.noContent().build();
  }

  @Override
  public Response estraiBusta(String targetContainerIdentifier, Entity body,
      SecurityContext securityContext) {
    return Response.ok(api2IndexService.estraiBusta(body, targetContainerIdentifier)).build();
  }

  @Override
  public Response share(CondivisioniRequest body, SecurityContext securityContext) {
    return Response
        .ok(api2IndexService.share(body.getOptions(), body.getSourceIdentifier(), body.getEntity()))
        .build();
  }

  @Override
  public Response shareId(String sourceIdentifier, SecurityContext securityContext) {
    return Response.ok(api2IndexService.getShares(sourceIdentifier)).build();
  }

  @Override
  public Response getInfoFormatoFile(String identifier, SecurityContext securityContext) {
    return Response.ok(api2IndexService.getFileFormatInfo(identifier)).build();
  }

  @Override
  public Response sposta(String source, String targetContainer, SecurityContext securityContext) {
    api2IndexService.move(source, targetContainer);
    return Response.ok().build();
  }

  @Override
  public Response verificaFirma(String sourceIdentifier, SignatureVerificationParameters body,
      SecurityContext securityContext) {
    return Response.ok(api2IndexService.verificaFirma(sourceIdentifier, body)).build();
  }

  @Override
  public Response createFolder(String uuidOrPath, SecurityContext securityContext) {
    return Response.ok(api2IndexService.createFolder(uuidOrPath)).build();
  }

  @Override
  public Response findFolder(String uuidOrPath, SecurityContext securityContext) {
    return Response.ok(api2IndexService.findFolder(uuidOrPath)).build();
  }

  @Override
  public Response creaFileIndex(CreaFileRequest body, SecurityContext securityContext) {
    return Response
        .ok(api2IndexService.creaFileIndex(body)).build();
  }

  @Override
  public Response getFile(String parentIdentifier, Boolean withContent,
      SecurityContext securityContext) {
    return Response.ok(api2IndexService.find(parentIdentifier, withContent)).build();
  }


}
