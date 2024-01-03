/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmonotifications.business.rest.NotificheApi;
import it.csi.cosmo.cosmonotifications.business.service.NotificationsService;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheResponse;

@SuppressWarnings("unused")
public class NotificheApiServiceImpl extends ParentApiImpl implements NotificheApi {

  @Autowired
  NotificationsService s;

  @Override
  public Response postNotifications(CreaNotificheRequest body, SecurityContext securityContext) {
    CreaNotificheResponse n = s.postNotifications(body);
    return Response.status(201).entity(n).build();
  }


}
