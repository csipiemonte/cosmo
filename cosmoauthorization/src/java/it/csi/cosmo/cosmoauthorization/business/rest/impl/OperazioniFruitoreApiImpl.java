/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoauthorization.business.rest.OperazioniFruitoreApi;
import it.csi.cosmo.cosmoauthorization.business.service.FruitoriService;
import it.csi.cosmo.cosmoauthorization.dto.rest.OperazioneFruitore;

/**
 *
 */

public class OperazioniFruitoreApiImpl extends ParentApiImpl implements OperazioniFruitoreApi {

  @Autowired
  private FruitoriService fruitoriService;

  @Override
  public Response getOperazioniFruitore(SecurityContext securityContext) {
    List<OperazioneFruitore> response = fruitoriService.getOperazioniFruitore();
    return Response.ok(response).build();
  }

}
