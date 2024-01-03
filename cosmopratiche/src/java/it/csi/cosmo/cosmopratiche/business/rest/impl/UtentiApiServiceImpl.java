/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmopratiche.business.rest.UtentiApi;
import it.csi.cosmo.cosmopratiche.business.service.UtentiService;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;


public class UtentiApiServiceImpl extends ParentApiImpl implements UtentiApi {

  @Autowired
  UtentiService u;

  // ATTIVO
  @Secured(authenticated = true)
  @Override
  public Response putUtentiPratichePreferiteIdPratica(String idPratica,
      SecurityContext securityContext) {
    Pratica p = u.putUtentiPratichePreferiteIdPratica(idPratica);
    if (p != null) {
      return Response.ok(p).build();
    } else {
      return Response.noContent().build();
    }
  }

  // ATTIVO
  @Secured(authenticated = true)
  @Override
  public Response deleteUtentiPratichePreferiteIdPratica(String idPratica,
      SecurityContext securityContext) {
    Void del = u.deleteUtentiPratichePreferiteIdPratica(idPratica);
    if (del != null) {
      return Response.ok(del).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Secured(authenticated = true)
  @Override
  public Response getUtentiPraticheCondivise(Integer offset, Integer limit,
      SecurityContext securityContext) {

    throw new InternalServerException("DISMISSING ENDPOINT");
  }

  @Secured(authenticated = true)
  @Override
  public Response getUtentiPraticheDaLavorare(Integer offset, Integer limit,
      SecurityContext securityContext) {

    throw new InternalServerException("DISMISSING ENDPOINT");
  }

  @Secured(authenticated = true)
  @Override
  public Response getUtentiPratichePreferite(Integer offset, Integer limit,
      SecurityContext securityContext) {

    throw new InternalServerException("DISMISSING ENDPOINT");
  }

  @Secured(authenticated = true)
  @Override
  public Response getUtentiPratichePreferiteIdPratica(String idPratica,
      SecurityContext securityContext) {

    throw new InternalServerException("DISMISSING ENDPOINT");
  }

  @Secured(authenticated = true)
  @Override
  public Response getUtentiPraticheCondiviseIdpratica(String idpratica,
      SecurityContext securityContext) {

    throw new InternalServerException("DISMISSING ENDPOINT");
  }

  @Secured(authenticated = true)
  @Override
  public Response deleteUtentiPraticheCondiviseIdpratica(String idpratica,
      SecurityContext securityContext) {

    throw new InternalServerException("DISMISSING ENDPOINT");
  }

  @Secured(authenticated = true)
  @Override
  public Response putUtentiPraticheCondiviseIdpratica(String idpratica,
      SecurityContext securityContext) {

    throw new InternalServerException("DISMISSING ENDPOINT");
  }

}
