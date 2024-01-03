/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmopratiche.business.rest.FruitoriApi;
import it.csi.cosmo.cosmopratiche.business.service.FruitoriService;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreResponse;

public class FruitoriApiServiceImpl extends ParentApiImpl implements FruitoriApi {

  @Autowired
  FruitoriService fruitoriService;

  @Override
  public Response postPraticheFruitori(String xRequestId, String xForwardedFor,
      CreaPraticaFruitoreRequest body, SecurityContext securityContext) {

    CreaPraticaFruitoreResponse response = fruitoriService.postPratiche(body);
    return Response.status(201).entity(response).build();
  }


  @Override
  public Response putFruitoriPraticheIdPraticaExt(String idPraticaExt,
      AggiornaRelazionePraticaRequest body, String xRequestId, String xForwardedFor,
      SecurityContext securityContext) {
    AggiornaRelazionePraticaResponse response =
        fruitoriService.creaAggiornaPraticheInRelazione(idPraticaExt, body);
    return Response.ok(response).build();
  }


  @Override
  public Response getFruitoriPratiche(String filter,String xRequestId,
      String xForwardedFor, SecurityContext securityContext) {
    return Response.ok(fruitoriService.getPratiche(filter)).build();
  }
}
