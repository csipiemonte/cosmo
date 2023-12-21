/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.CustomCallbackApi;
import it.csi.cosmo.cosmobusiness.business.service.CustomCallbackService;

/**
 *
 */

public class CustomCallbackApiImpl extends ParentApiImpl implements CustomCallbackApi {

  @Autowired
  private CustomCallbackService customCallbackService;

  @Override
  public Response getCustomCallback(String apiManagerId, String codiceDescrittivo,
      String processInstanceId, SecurityContext securityContext) {
    return Response
        .ok(customCallbackService.getCustomEndopoint(apiManagerId, codiceDescrittivo,
            processInstanceId))
        .build();
  }
}
