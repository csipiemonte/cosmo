/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmonotifications.business.rest.UtentiApi;
import it.csi.cosmo.cosmonotifications.business.service.NotificationsService;
import it.csi.cosmo.cosmonotifications.dto.rest.Notifica;
import it.csi.cosmo.cosmonotifications.dto.rest.PaginaNotifiche;

@SuppressWarnings("unused")
public class UtentiApiServiceImpl extends ParentApiImpl implements UtentiApi {

  @Autowired
  NotificationsService s;

  @Override
  public Response getNotificationsId(String id, SecurityContext securityContext) {
    Notifica n = s.getNotificationsId(id);
    if (n != null) {
      return Response.ok(n).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response getUtentiIdUtenteNotifiche(Integer offset, Integer limit,
      SecurityContext securityContext) {
    PaginaNotifiche n = s.getNotifications(offset, limit);
    if (n != null) {
      return Response.ok(n).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Override
  public Response putNotificationsId(String id, Notifica body, SecurityContext securityContext) {
    Notifica n = s.putNotificationsId(id, body);
    if (n != null) {
      return Response.ok(n).build();
    } else {
      return Response.noContent().build();
    }
  }

  @Secured(authenticated = true)
  @Override
  public Response putNotificationsAll(SecurityContext securityContext) {
    s.putNotificationsAll();
    return Response.noContent().build();
  }
}
