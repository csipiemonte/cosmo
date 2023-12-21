/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.SchemiAutenticazioneApi;
import it.csi.cosmo.cosmobusiness.business.service.ChiamataEsternaService;

public class SchemiAutenticazioneApiImpl extends ParentApiImpl implements SchemiAutenticazioneApi {

  @Autowired
  private ChiamataEsternaService chiamataEsternaService;

  @Override
  public Response testSchemaAutenticazione(Long idSchemaAutenticazione,
      SecurityContext securityContext) {

    Object result =
        chiamataEsternaService.testSchemaAutenticazione(idSchemaAutenticazione);

    return Response.status(200).entity(result).build();
  }
}
