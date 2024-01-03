/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.CartelleApi;
import it.csi.cosmo.cosmoecm.business.service.CartellaService;
import it.csi.cosmo.cosmoecm.dto.rest.Cartella;

public class CartelleApiImpl extends ParentApiImpl implements CartelleApi {

  @Autowired
  private CartellaService cartellaService;

  @Override
  public Response deletePratiche(Cartella body, SecurityContext securityContext) {
    cartellaService.cancellaCartellaIndex(body);

    return Response.noContent().build();
  }

  @Override
  public Response postPratiche(Cartella body, SecurityContext securityContext) {
    Cartella output = cartellaService.creaCartellaIndex(body);

    if (output == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(output).build();
    }
  }

}
