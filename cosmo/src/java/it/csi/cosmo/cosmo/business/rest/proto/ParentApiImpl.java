/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.rest.proto;

import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.dto.rest.Esito;


public abstract class ParentApiImpl extends SpringBeanAutowiringSupport {

  @Autowired
  public ConfigurazioneService configurazioneService;

  protected ConfigurazioneService getConfigurazioneService() {
    return this.configurazioneService;
  }

  protected Response ok(String message) {

    Esito output = new Esito();
    output.setCode("OK");
    output.setStatus(200);
    output.setTitle(message);

    return Response.ok(output).build();
  }
}
