/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.TestApi;
import it.csi.cosmo.cosmoecm.business.service.EventService;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;


public class TestApiImpl extends ParentApiImpl implements TestApi {

  @Autowired
  public EventService eventService;

  @Override
  public Response testEvent() {

    eventService.broadcastEvent("testEvent", SecurityUtils.getClientCorrente());
    return Response.ok("done").build();
  }


}
