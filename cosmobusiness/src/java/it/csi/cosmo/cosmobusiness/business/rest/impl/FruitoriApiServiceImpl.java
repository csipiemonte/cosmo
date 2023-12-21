/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.FruitoriApi;
import it.csi.cosmo.cosmobusiness.business.service.FruitoriService;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaEventoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.AvviaProcessoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AvviaProcessoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaEventoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.GetPraticaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreResponse;

public class FruitoriApiServiceImpl extends ParentApiImpl implements FruitoriApi {

  @Autowired
  private FruitoriService fruitoriService;

  @Override
  public Response postProcessoFruitore(String xRequestId, String xForwardedFor,
      AvviaProcessoFruitoreRequest body, SecurityContext securityContext) {

    AvviaProcessoFruitoreResponse result = fruitoriService.avviaProcesso(body);
    return Response.status(201).entity(result).build();
  }

  @Override
  public Response postFruitoriEvento(String xRequestId, String xForwardedFor,
      CreaEventoFruitoreRequest body, SecurityContext securityContext) {

    CreaEventoFruitoreResponse result = fruitoriService.creaEvento(body);
    return Response.status(201).entity(result).build();
  }

  @Override
  public Response putFruitoriEvento(String idEvento, String xRequestId, String xForwardedFor,
      AggiornaEventoFruitoreRequest body, SecurityContext securityContext) {

    AggiornaEventoFruitoreResponse result = fruitoriService.aggiornaEvento(idEvento, body);
    return Response.status(200).entity(result).build();
  }

  @Override
  public Response deleteFruitoriEvento(String idEvento, String xRequestId, String xForwardedFor,
      EliminaEventoFruitoreRequest request, SecurityContext securityContext) {

    fruitoriService.eliminaEvento(idEvento, request);
    return Response.status(204).build();
  }

  @Override
  public Response getFruitoriPratica(String idPratica, String xRequestId, String xForwardedFor,
      SecurityContext securityContext) {

    GetPraticaFruitoreResponse result = fruitoriService.getPratica(idPratica);
    return Response.status(200).entity(result).build();
  }

  @Override
  public Response postFruitoriSegnale(String idPratica, String xRequestId, String xForwardedFor,
      InviaSegnaleFruitoreRequest body, SecurityContext securityContext) {

    InviaSegnaleFruitoreResponse result = fruitoriService.inviaSegnaleProcesso(idPratica, body);
    return Response.status(200).entity(result).build();
  }

  @Override
  public Response deletePraticheEsterneIdFruitori(String idPraticaExt, String xRequestId,
      String xForwardedFor, EliminaPraticaEsternaFruitoreRequest body,
      SecurityContext securityContext) {

    EliminaPraticaEsternaFruitoreResponse response =
        fruitoriService.deletePraticheEsterne(idPraticaExt, body);
    return Response.status(200).entity(response).build();
  }

  @Override
  public Response postPraticheEsterneFruitori(String xRequestId, String xForwardedFor,
      CreaPraticaEsternaFruitoreRequest body, SecurityContext securityContext) {

    CreaPraticaEsternaFruitoreResponse response = fruitoriService.postPraticheEsterne(body);
    return Response.status(201).entity(response).build();
  }

  @Override
  public Response putPraticheEsterneIdFruitori(String idPraticaExt, String xRequestId,
      String xForwardedFor, AggiornaPraticaEsternaFruitoreRequest body,
      SecurityContext securityContext) {

    AggiornaPraticaEsternaFruitoreResponse response =
        fruitoriService.putPraticheEsterne(idPraticaExt, body);
    return Response.status(200).entity(response).build();
  }

}
