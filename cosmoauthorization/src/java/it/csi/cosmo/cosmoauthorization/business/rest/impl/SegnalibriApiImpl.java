/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoauthorization.business.rest.SegnalibriApi;
import it.csi.cosmo.cosmoauthorization.business.service.SegnalibroService;

/**
 *
 */

public class SegnalibriApiImpl extends ParentApiImpl implements SegnalibriApi {

  @Autowired
  private SegnalibroService segnalibroService;

  @Override
  public Response getTipiSegnalibri(SecurityContext securityContext) {
    return Response.ok(segnalibroService.getTipiSegnalibri()).build();
  }

}
