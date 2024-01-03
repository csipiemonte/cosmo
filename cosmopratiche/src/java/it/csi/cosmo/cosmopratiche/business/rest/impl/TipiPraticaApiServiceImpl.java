/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.rest.impl;



import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmopratiche.business.rest.TipiPraticheApi;
import it.csi.cosmo.cosmopratiche.business.service.TipiPraticheService;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaTipoPraticaRequest;


/**
 *
 */

public class TipiPraticaApiServiceImpl extends ParentApiImpl implements TipiPraticheApi {

  @Autowired
  TipiPraticheService p;

  @Override
  public Response getTipiPratiche(String filter, SecurityContext securityContext) {
    return Response.ok(p.getTipiPratica(filter)).build();
  }

  @Override
  public Response getTipoPratica(String codice, SecurityContext securityContext) {
    return Response.ok(p.getTipoPratica(codice)).build();
  }

  @Secured(hasAnyAuthority = {"ADMIN_COSMO", "CONF"})
  @Override
  public Response deleteTipiPratiche(String codice, SecurityContext securityContext) {
    p.deleteTipoPratica(codice);
    return Response.noContent().build();
  }

  @Secured(hasAnyAuthority = {"ADMIN_COSMO", "CONF"})
  @Override
  public Response postTipiPratiche(CreaTipoPraticaRequest body, SecurityContext securityContext) {
    return Response.status(201).entity(p.postTipoPratica(body)).build();
  }

  @Secured(hasAnyAuthority = {"ADMIN_COSMO", "CONF"})
  @Override
  public Response putTipiPratiche(String codice, AggiornaTipoPraticaRequest body, SecurityContext securityContext) {
    return Response.ok(p.putTipoPratica(codice, body)).build();
  }

}
