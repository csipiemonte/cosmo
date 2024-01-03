/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmonotifications.business.rest.NotificheGlobaliApi;
import it.csi.cosmo.cosmonotifications.business.service.NotificationsService;
import it.csi.cosmo.cosmonotifications.dto.rest.NotificheGlobaliRequest;

@SuppressWarnings("unused")
public class NotificheGlobaliApiServiceImpl extends ParentApiImpl implements NotificheGlobaliApi {

  @Autowired
  NotificationsService s;

  @Override
  public Response postNotificheGlobali(NotificheGlobaliRequest body,
      SecurityContext securityContext) {
    s.sendNotifications(body);
    return Response.ok().status(201).build();
  }

}
