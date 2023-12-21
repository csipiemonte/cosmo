/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.cosmoauthorization.business.rest.ProfiliApi;
import it.csi.cosmo.cosmoauthorization.business.service.ProfiliService;
import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiliResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiloResponse;

/**
 *
 */

public class ProfiliApiImpl extends ParentApiImpl implements ProfiliApi {

  @Autowired
  private ProfiliService profiliService;

  @Override
  public Response getProfiliId(String id, SecurityContext securityContext) {
    ProfiloResponse profiloResponse = new ProfiloResponse();
    profiloResponse.setProfilo(profiliService.getProfilo(id));
    if (profiloResponse.getProfilo() != null) {
      return Response.ok(profiloResponse).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response getRuoli(String filter, SecurityContext securityContext) {
    ProfiliResponse profiliResponse = profiliService.getProfili(filter);
    if (CollectionUtils.isEmpty(profiliResponse.getProfili())) {
      return Response.noContent().build();
    } else {
      return Response.ok(profiliResponse).build();
    }
  }

  @Override
  public Response postProfili(Profilo profilo, SecurityContext securityContext) {
    ProfiloResponse profiloResponse = new ProfiloResponse();
    profiloResponse.setProfilo(profiliService.createProfilo(profilo));
    if (profiloResponse.getProfilo() != null) {
      return Response.ok(profilo).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response putProfiliId(String id, Profilo body, SecurityContext securityContext) {
    ProfiloResponse profiloResponse = new ProfiloResponse();
    profiloResponse.setProfilo(profiliService.updateProfilo(id, body));

    return Response.ok(profiloResponse).build();
  }

  @Override
  public Response deleteProfiliId(String id, SecurityContext securityContext) {
    profiliService.deleteProfilo(id);
    return Response.noContent().build();
  }
}
