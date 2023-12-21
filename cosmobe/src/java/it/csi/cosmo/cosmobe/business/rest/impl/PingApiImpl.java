/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmo.dto.rest.Pong;
import it.csi.cosmo.cosmobe.business.rest.PingApi;


public class PingApiImpl extends ParentApiImpl implements PingApi {

  @Secured(permitAll = true)
  @Override
  public Response getPing(SecurityContext securityContext) {
    Pong response = new Pong();
    response.setPong("pong!");
    return Response.ok(response).build();
  }
}
