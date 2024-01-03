/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmonotifications.business.rest.FruitoriApi;
import it.csi.cosmo.cosmonotifications.business.service.FruitoriService;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreResponse;

public class FruitoriApiServiceImpl extends ParentApiImpl implements FruitoriApi {

  @Autowired
  FruitoriService fruitoriService;

  @Override
  public Response postNotificaFruitore(CreaNotificaFruitoreRequest notifica, String xRequestId,
      String xForwardedFor,
      SecurityContext securityContext) {

    CreaNotificaFruitoreResponse n = fruitoriService.creaNotificaFruitore(notifica);
    return Response.status(200).entity(n).build();
  }

}
