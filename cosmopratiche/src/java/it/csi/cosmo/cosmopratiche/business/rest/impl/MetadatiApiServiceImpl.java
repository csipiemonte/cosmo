/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmopratiche.business.rest.MetadatiApi;
import it.csi.cosmo.cosmopratiche.business.service.MetadatiService;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;

public class MetadatiApiServiceImpl extends ParentApiImpl implements MetadatiApi {

  @Autowired
  private MetadatiService metadatiService;

  @Override
  public Response putMetadatiIdPratica(String idPratica, SecurityContext securityContext) {

    Pratica pratica = metadatiService.aggiornaMetadatiPratica(idPratica);

    if (pratica != null) {
      return Response.ok(pratica).build();
    }
    return Response.noContent().build();
  }

  @Override
  public Response putMetadatiIdPraticaVariabiliProcesso(String idPratica,
      SecurityContext securityContext) {

    Pratica pratica = metadatiService.aggiornaVariabiliProcesso(idPratica);

    if (pratica != null) {
      return Response.ok(pratica).build();
    }
    return Response.noContent().build();
  }

}
