/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmonotifications.business.rest.TipiNotificheApi;
import it.csi.cosmo.cosmonotifications.business.service.TipiNotificheService;

/**
 *
 */

public class TipiNotificheApiServiceImpl extends ParentApiImpl implements TipiNotificheApi {

  @Autowired
  private TipiNotificheService tipiNotificheService;

  @Override
  public Response getTipiNotifiche(SecurityContext securityContext) {
    return Response.ok(tipiNotificheService.getTipiNotifiche()).build();
  }

}
