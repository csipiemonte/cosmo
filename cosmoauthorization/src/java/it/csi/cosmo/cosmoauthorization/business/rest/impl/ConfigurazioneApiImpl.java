/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.cosmoauthorization.business.rest.ConfigurazioneApi;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametriResponse;

/**
 *
 */

public class ConfigurazioneApiImpl extends ParentApiImpl implements ConfigurazioneApi {

  @Override
  public Response getConfigurazioneParametro(String chiave, SecurityContext securityContext) {

    ParametriResponse parametriResponse = super.configurazioneService.getConfigAPI(chiave);

    if (CollectionUtils.isEmpty(parametriResponse.getParametri())) {
      return Response.noContent().build();
    } else {
      return Response.ok(parametriResponse).build();
    }
  }

}
