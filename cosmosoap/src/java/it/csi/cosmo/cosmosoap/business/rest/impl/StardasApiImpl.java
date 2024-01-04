/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmosoap.business.rest.StardasApi;
import it.csi.cosmo.cosmosoap.business.service.StardasService;
import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoRequest;

/**
 *
 */

public class StardasApiImpl extends ParentApiImpl implements StardasApi {

  @Autowired
  private StardasService stardasService;

  @Override
  public Response smistamentoDocumento(SmistaDocumentoRequest body,
      SecurityContext securityContext) {
    return Response.ok(stardasService.smistaDocumento(body)).build();
  }

  @Override
  public Response statoRichiesta(GetStatoRichiestaRequest body, SecurityContext securityContext) {
    return Response.ok(stardasService.getStatoRichiesta(body)).build();
  }

}
