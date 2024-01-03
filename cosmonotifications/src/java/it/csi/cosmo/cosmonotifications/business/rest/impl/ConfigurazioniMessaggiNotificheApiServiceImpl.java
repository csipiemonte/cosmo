/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmonotifications.business.rest.ConfigurazioniMessaggiNotificheApi;
import it.csi.cosmo.cosmonotifications.business.service.ConfigurazioniMessaggiNotificheService;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotificaRequest;

/**
 *
 */

public class ConfigurazioniMessaggiNotificheApiServiceImpl extends ParentApiImpl
implements ConfigurazioniMessaggiNotificheApi {

  @Autowired
  private ConfigurazioniMessaggiNotificheService configurazioniMessaggiNotificheService;

  @Secured("ADMIN_COSMO")
  @Override
  public Response deleteConfigurazioniMessaggiNotifiche(Long id,
      SecurityContext securityContext) {
    return Response
        .ok(configurazioniMessaggiNotificheService.eliminaConfigurazioneMessaggio(Long.valueOf(id)))
        .build();
  }

  @Override
  public Response getConfigurazioniMessaggiNotifiche(String filter,
      SecurityContext securityContext) {
    return Response.ok(configurazioniMessaggiNotificheService.getConfigurazioniMessaggi(filter))
        .build();
  }

  @Override
  public Response getConfigurazioniMessaggiNotificheId(Long id, SecurityContext securityContext) {
    return Response
        .ok(configurazioniMessaggiNotificheService.getConfigurazioneMessaggioId(Long.valueOf(id)))
        .build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response postConfigurazioniMessaggiNotifiche(ConfigurazioneMessaggioNotificaRequest body,
      SecurityContext securityContext) {
    return Response.ok(configurazioniMessaggiNotificheService.creaConfigurazioneMessaggio(body))
        .build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response putConfigurazioniMessaggiNotifiche(Long id,
      ConfigurazioneMessaggioNotificaRequest body,
      SecurityContext securityContext) {
    return Response
        .ok(configurazioniMessaggiNotificheService.modificaConfigurazioneMessaggio(Long.valueOf(id),
            body))
        .build();
  }


}
