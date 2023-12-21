/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest.impl;



import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoauthorization.business.rest.ImpostazioniFirmaApi;
import it.csi.cosmo.cosmoauthorization.business.service.ImpostazioniFirmaService;
import it.csi.cosmo.cosmoauthorization.dto.rest.ImpostazioniFirma;

public class ImpostazioniFirmaApiServiceImpl extends ParentApiImpl implements ImpostazioniFirmaApi {

  @Autowired
  private ImpostazioniFirmaService impostazioniFirmaService;

  @Override
  public Response getImpostazioniFirma(SecurityContext securityContext) {
    ImpostazioniFirma impostazioniFirma = impostazioniFirmaService.getImpostazioniFirma();
    if (impostazioniFirma == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(impostazioniFirma).build();
    }
  }
}

