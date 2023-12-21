/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmobusiness.business.rest.OperazioniAsincroneApi;
import it.csi.cosmo.cosmobusiness.business.service.AsyncTaskService;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaOperazioneAsincronaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;

public class OperazioniAsincroneApiServiceImpl extends ParentApiImpl
    implements OperazioniAsincroneApi {

  @Autowired
  private AsyncTaskService service;

  @Override
  public Response getOperazioneAsincrona(String uuid, SecurityContext securityContext) {
    OperazioneAsincrona result = service.getOperazioneAsincrona(uuid);

    String ifNoneMatchHeader = RequestUtils.getCurrentRequest().map(
        request -> RequestUtils.streamHeaders(request, "If-None-Match").findFirst().orElse(null))
        .orElse(null);

    String eTag = getETag(result);

    if (!StringUtils.isBlank(ifNoneMatchHeader) && eTag.equals(ifNoneMatchHeader)) {
      return Response.status(304).build();
    }

    return Response.ok(result).header("ETag", eTag).build();
  }

  @Override
  public Response deleteOperazioneAsincrona(String uuid, SecurityContext securityContext) {
    service.deleteOperazioneAsincrona(uuid);
    return Response.status(204).build();
  }

  protected String getETag(OperazioneAsincrona op) {
    return op.getUuid() + "/" + op.getVersione();
  }

  @Override
  public Response postOperazioneAsincrona(CreaOperazioneAsincronaRequest body,
      SecurityContext securityContext) {

    OperazioneAsincrona created = service.postOperazioneAsincrona(body);
    return Response.status(201).entity(created).build();
  }

  @Override
  public Response putOperazioneAsincrona(String uuid, OperazioneAsincrona body,
      SecurityContext securityContext) {
    OperazioneAsincrona updated = service.putOperazioneAsincrona(uuid, body);
    return Response.ok(updated).build();
  }
}
