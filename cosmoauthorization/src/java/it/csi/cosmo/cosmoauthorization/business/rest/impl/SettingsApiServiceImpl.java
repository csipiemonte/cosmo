/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoauthorization.business.rest.SettingsApi;
import it.csi.cosmo.cosmoauthorization.business.service.PreferenzeEnteService;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;

public class SettingsApiServiceImpl extends ParentApiImpl implements SettingsApi {

  @Autowired
  private PreferenzeEnteService preferenzeEnteService;

  @Override
  public Response getPreferenzeEnte(Integer idEnte, String versione,
      SecurityContext securityContext) {
    Preferenza preferenzeEnte = preferenzeEnteService.getPreferenzeEnte(idEnte, versione);
    if (preferenzeEnte == null) {
      return Response.noContent().build();
    } else {
      return Response.ok(preferenzeEnte).build();
    }
  }


  @Override
  public Response putPreferenzeEnte(Integer idEnte, Preferenza body,
      SecurityContext securityContext) {
    Preferenza preferenzeEnte = preferenzeEnteService.updatePreferenzeEnte(idEnte, body);
    if (preferenzeEnte != null) {
      return Response.ok(preferenzeEnte).build();
    } else {
      return Response.noContent().build();
    }
  }


  @Override
  public Response createPreferenzeEnte(Integer idEnte, Preferenza body,
      SecurityContext securityContext) {
    Preferenza preferenzeEnte = preferenzeEnteService.createPreferenzeEnte(idEnte, body);
    if (preferenzeEnte != null) {
      return Response.ok(preferenzeEnte).build();
    } else {
      return Response.noContent().build();
    }
  }

}
