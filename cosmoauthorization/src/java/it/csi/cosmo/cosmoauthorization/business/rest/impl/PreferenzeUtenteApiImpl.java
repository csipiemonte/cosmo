/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoauthorization.business.rest.PreferenzeUtenteApi;
import it.csi.cosmo.cosmoauthorization.business.service.PreferenzeUtenteService;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;


public class PreferenzeUtenteApiImpl extends ParentApiImpl implements PreferenzeUtenteApi {

  @Autowired
  private PreferenzeUtenteService preferenzeUtenteService;

  @Override
  public Response getPreferenzeUtente(SecurityContext securityContext) {
    Preferenza preferenzeUtente =
        preferenzeUtenteService.getPreferenzeUtente();
    if (preferenzeUtente != null) {
      return Response.ok(preferenzeUtente).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response getPreferenzeUtenteId(String id, SecurityContext securityContext) {
    Preferenza preferenzeUtente = preferenzeUtenteService.getPreferenzeUtente(id);
    if (preferenzeUtente != null) {
      return Response.ok(preferenzeUtente).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response createPreferenzeUtente(Preferenza body,
      SecurityContext securityContext) {

    Preferenza preferenzeUtente =
        preferenzeUtenteService.createPreferenzeUtente(body);
    if (preferenzeUtente != null) {
      return Response.ok(preferenzeUtente).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response putPreferenzeUtente(Preferenza body, SecurityContext securityContext) {
    Preferenza preferenzeUtente =
        preferenzeUtenteService.updatePreferenzeUtente(body);
    if (preferenzeUtente != null) {
      return Response.ok(preferenzeUtente).build();
    } else {
      return Response.noContent().build();
    }
  }


}
