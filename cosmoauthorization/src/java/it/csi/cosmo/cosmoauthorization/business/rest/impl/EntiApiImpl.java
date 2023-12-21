/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmoauthorization.business.rest.EntiApi;
import it.csi.cosmo.cosmoauthorization.business.service.EntiService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaEnteRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEnteRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.EnteResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.EntiResponse;

/**
 *
 */

public class EntiApiImpl extends ParentApiImpl implements EntiApi {

  @Autowired
  private EntiService entiService;

  @Secured("ADMIN_COSMO")
  @Override
  public Response deleteEntiId(Long id, SecurityContext securityContext) {
    entiService.deleteEnte(id);
    return Response.noContent().build();
  }

  @Override
  public Response getEnti(String filter, SecurityContext securityContext) {
    EntiResponse entiResponse = entiService.getEnti(filter);
    return Response.ok(entiResponse).build();
  }

  @Override
  public Response getEntiId(Long id, SecurityContext securityContext) {
    EnteResponse enteResponse = new EnteResponse();
    enteResponse.setEnte(entiService.getEnte(id));
    return Response.ok(enteResponse).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response postEnti(CreaEnteRequest ente, SecurityContext securityContext) {

    EnteResponse enteResponse = new EnteResponse();
    enteResponse.setEnte(entiService.createEnte(ente));
    return Response.status(201).entity(enteResponse).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response putEnti(Long id, AggiornaEnteRequest ente, SecurityContext securityContext) {
    EnteResponse enteResponse = new EnteResponse();
    enteResponse.setEnte(entiService.updateEnte(id, ente));
    return Response.ok(enteResponse).build();
  }

}
