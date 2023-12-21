/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmo.business.rest.WhoamiApi;
import it.csi.cosmo.cosmo.business.rest.proto.ParentApiImpl;
import it.csi.cosmo.cosmo.dto.rest.WhoAmI;
import it.csi.cosmo.cosmo.security.SecurityUtils;


public class WhoAmIApiImpl extends ParentApiImpl implements WhoamiApi {

  @Secured(permitAll = true)
  @Override
  public Response getWhoami(SecurityContext securityContext) {
    WhoAmI output = new WhoAmI();

    output.setUtente(SecurityUtils.getUtenteCorrente());
    output.setApplicativo(SecurityUtils.getClientCorrente());

    return Response.ok(output).build();
  }

}
