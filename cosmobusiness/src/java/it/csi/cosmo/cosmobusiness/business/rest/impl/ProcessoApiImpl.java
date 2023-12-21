/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.ProcessoApi;
import it.csi.cosmo.cosmobusiness.business.service.ProcessService;
import it.csi.cosmo.cosmobusiness.dto.rest.AvanzamentoProcessoRequest;

/**
 *
 */

public class ProcessoApiImpl extends ParentApiImpl implements ProcessoApi {

  @Autowired
  private ProcessService processoService;

  @Override
  public Response postAvanzaProcesso(AvanzamentoProcessoRequest body,
      SecurityContext securityContext) {
    processoService.avanzaProcesso(body);
    return Response.noContent().build();
  }



}
