/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoauthorization.business.rest.AutorizzazioniFruitoreApi;
import it.csi.cosmo.cosmoauthorization.business.service.AutorizzazioniFruitoreService;

/**
 *
 */

public class AutorizzazioniFruitoriApiImpl extends ParentApiImpl
    implements AutorizzazioniFruitoreApi {

  @Autowired
  private AutorizzazioniFruitoreService autorizzazioniFruitoreService;

  @Override
  public Response getAuthFruitore(String filter, SecurityContext securityContext) {
    return Response.ok(autorizzazioniFruitoreService.getAutorizzazioniFruitore(filter)).build();
  }

}
