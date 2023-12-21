/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmoauthorization.business.rest.FruitoriApi;
import it.csi.cosmo.cosmoauthorization.business.service.FruitoriService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaEndpointFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaSchemaAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEndpointFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaSchemaAutenticazioneFruitoreRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CredenzialiAutenticazioneFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.EndpointFruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.Fruitore;
import it.csi.cosmo.cosmoauthorization.dto.rest.FruitoriResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.SchemaAutenticazioneFruitore;

/**
 *
 */

public class FruitoriApiImpl extends ParentApiImpl implements FruitoriApi {


  @Autowired
  private FruitoriService fruitoriService;

  @Secured("ADMIN_COSMO")
  @Override
  public Response deleteFruitoriId(Long id, SecurityContext securityContext) {
    fruitoriService.deleteFruitore(id);
    return Response.noContent().build();
  }

  @Override
  public Response getFruitori(String filter, SecurityContext securityContext) {
    FruitoriResponse fruitori = fruitoriService.getFruitori(filter);
    return Response.ok(fruitori).build();
  }

  @Override
  public Response getFruitoriId(Long id, SecurityContext securityContext) {
    Fruitore fruitore = fruitoriService.getFruitore(id);
    return Response.ok(fruitore).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response postFruitori(CreaFruitoreRequest body, SecurityContext securityContext) {
    Fruitore fruitore = fruitoriService.createFruitore(body);
    return Response.status(201).entity(fruitore).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response putFruitori(Long id, AggiornaFruitoreRequest body,
      SecurityContext securityContext) {
    Fruitore fruitore = fruitoriService.putFruitore(id, body);
    return Response.ok(fruitore).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response deleteEndpointFruitore(Long idFruitore, Long idEndpoint,
      SecurityContext securityContext) {

    fruitoriService.deleteEndpointFruitore(idFruitore, idEndpoint);
    return Response.noContent().build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response deleteSchemaAuthFruitore(Long idFruitore, Long idSchemaAutenticazione,
      SecurityContext securityContext) {

    fruitoriService.deleteSchemaAuthFruitore(idFruitore, idSchemaAutenticazione);
    return Response.noContent().build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response postEndpointsFruitori(Long idFruitore, CreaEndpointFruitoreRequest body,
      SecurityContext securityContext) {

    EndpointFruitore fruitore = fruitoriService.postEndpointsFruitori(idFruitore, body);
    return Response.status(201).entity(fruitore).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response postSchemiAuthFruitori(Long idFruitore,
      CreaSchemaAutenticazioneFruitoreRequest body,
      SecurityContext securityContext) {

    SchemaAutenticazioneFruitore fruitore =
        fruitoriService.postSchemiAuthFruitori(idFruitore, body);
    return Response.status(201).entity(fruitore).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response putEndpointFruitore(Long idFruitore, Long idEndpoint,
      AggiornaEndpointFruitoreRequest body, SecurityContext securityContext) {

    EndpointFruitore fruitore = fruitoriService.putEndpointFruitore(idFruitore, idEndpoint, body);
    return Response.ok(fruitore).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response putSchemaAuthFruitore(Long idFruitore, Long idSchemaAutenticazione,
      AggiornaSchemaAutenticazioneFruitoreRequest body, SecurityContext securityContext) {

    SchemaAutenticazioneFruitore fruitore =
        fruitoriService.putSchemaAuthFruitore(idFruitore, idSchemaAutenticazione, body);
    return Response.ok(fruitore).build();
  }

  @Secured(hasScopes = "BE_ORCHESTRATOR")
  @Override
  public Response getFruitoriAmiApiManagerId(String apiManagerId, SecurityContext securityContext) {
    Fruitore fruitore = fruitoriService.getFruitoreApiManagerId(apiManagerId);
    return Response.ok(fruitore).build();
  }

  @Secured(hasScopes = "BE_ORCHESTRATOR")
  @Override
  public Response postFruitoriAutentica(CredenzialiAutenticazioneFruitore body,
      SecurityContext securityContext) {
    Fruitore fruitore = fruitoriService.postFruitoriAutentica(body);
    return Response.ok(fruitore).build();
  }
}
