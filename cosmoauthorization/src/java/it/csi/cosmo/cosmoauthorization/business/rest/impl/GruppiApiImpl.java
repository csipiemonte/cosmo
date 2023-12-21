/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmoauthorization.business.rest.GruppiApi;
import it.csi.cosmo.cosmoauthorization.business.service.GruppiService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.GruppiResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.GruppoResponse;

/**
 *
 */

public class GruppiApiImpl extends ParentApiImpl implements GruppiApi {

  @Autowired
  private GruppiService gruppiService;

  @Override
  public Response getGruppi(String filter, SecurityContext securityContext) {
    GruppiResponse gruppiResponse = gruppiService.getGruppi(filter);
    return Response.ok(gruppiResponse).build();
  }

  @Override
  public Response getGruppiId(Long id, SecurityContext securityContext) {
    GruppoResponse gruppoResponse = new GruppoResponse();
    gruppoResponse.setGruppo(gruppiService.getGruppo(id));
    return Response.ok(gruppoResponse).build();
  }

  @Override
  public Response getGruppoCodice(String codice, SecurityContext securityContext) {
    GruppoResponse gruppoResponse = new GruppoResponse();
    gruppoResponse.setGruppo(gruppiService.getGruppoPerCodice(codice));
    return Response.ok(gruppoResponse).build();
  }

  @Secured("ADMIN_ENTE")
  @Override
  public Response postGruppi(CreaGruppoRequest body, SecurityContext securityContext) {
    GruppoResponse gruppoResponse = new GruppoResponse();
    gruppoResponse.setGruppo(gruppiService.createGruppo(body));
    return Response.status(201).entity(gruppoResponse).build();
  }

  @Secured("ADMIN_ENTE")
  @Override
  public Response putGruppi(Long id, AggiornaGruppoRequest body, SecurityContext securityContext) {
    GruppoResponse gruppoResponse = new GruppoResponse();
    gruppoResponse.setGruppo(gruppiService.updateGruppo(id, body));
    return Response.ok(gruppoResponse).build();
  }

  @Secured("ADMIN_ENTE")
  @Override
  public Response deleteGruppi(Long id, SecurityContext securityContext) {
    gruppiService.deleteGruppo(id);
    return Response.noContent().build();
  }

  @Override
  public Response getGruppiUtenteCorrente(SecurityContext securityContext) {
    return Response.ok(gruppiService.getGruppiUtenteCorrente()).build();
  }

}
