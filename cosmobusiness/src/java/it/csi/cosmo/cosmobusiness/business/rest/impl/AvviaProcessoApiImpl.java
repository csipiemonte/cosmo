/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.AvviaProcessoApi;
import it.csi.cosmo.cosmobusiness.business.service.PracticeService;

/**
 *
 */

public class AvviaProcessoApiImpl extends ParentApiImpl implements AvviaProcessoApi{
  
  @Autowired
  private PracticeService practiceService;

  @Override
  public Response postAvviaProcessoIdPratica(String idPratica, SecurityContext securityContext) {
    
    return Response.ok(practiceService.avviaProcesso(idPratica)).build();
  }
}
