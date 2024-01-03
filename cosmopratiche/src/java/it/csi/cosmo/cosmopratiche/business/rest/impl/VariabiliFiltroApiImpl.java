/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmopratiche.business.rest.VariabiliFiltroApi;
import it.csi.cosmo.cosmopratiche.business.service.VariabiliFiltroService;
import it.csi.cosmo.cosmopratiche.dto.rest.VariabileDiFiltro;

/**
 *
 */

public class VariabiliFiltroApiImpl extends ParentApiImpl implements VariabiliFiltroApi {

  @Autowired
  private VariabiliFiltroService variabiliFiltroService;


  @Override
  public Response deleteVariabiliFiltroId(String id, SecurityContext securityContext) {
    variabiliFiltroService.deleteVariabiliFiltroId(id);
    return Response.noContent().build();

  }

  @Override
  public Response getVariabiliFiltro(String filter, SecurityContext securityContext) {
    return Response.ok(variabiliFiltroService.getVariabiliFiltro(filter)).build();
  }

  @Override
  public Response getVariabiliFiltroId(String id, SecurityContext securityContext) {
    VariabileDiFiltro v = variabiliFiltroService.getVariabiliFiltroId(id);
    return Response.ok(v).build();
  }

  @Override
  public Response postVariabiliFiltro(VariabileDiFiltro body, SecurityContext securityContext) {

    VariabileDiFiltro v = variabiliFiltroService.postVariabiliFiltro(body);
    return Response.status(201).entity(v).build();

  }

  @Override
  public Response putVariabiliFiltroId(String id, VariabileDiFiltro body,
      SecurityContext securityContext) {
    VariabileDiFiltro v = variabiliFiltroService.putVariabiliFiltroId(id, body);
    return Response.ok(v).build();

  }

  @Override
  public Response getVariabiliFiltroFormati(SecurityContext securityContext) {
    return Response.ok(variabiliFiltroService.getVariabiliFiltroFormati()).build();
  }

  @Override
  public Response getVariabiliFiltroTipiFiltro(SecurityContext securityContext) {
    return Response.ok(variabiliFiltroService.getVariabiliFiltroTipiFiltro()).build();
  }

  @Override
  public Response getVariabiliFiltroTipoPraticaCodiceTipoPratica(String codice,
      SecurityContext securityContext) {
    return Response.ok(variabiliFiltroService.getVariabiliFiltroTipoPratica(codice)).build();
  }

}
