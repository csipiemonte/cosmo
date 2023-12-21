/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.rest.impl;

import java.math.BigDecimal;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmoauthorization.business.rest.ConfigurazioniEnteApi;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneEnteService;
import it.csi.cosmo.cosmoauthorization.dto.rest.ConfigurazioneEnte;

/**
 *
 */

public class ConfigurazioniEnteApiImpl extends ParentApiImpl implements ConfigurazioniEnteApi {

  @Autowired
  private ConfigurazioneEnteService configurazioneEnteService;

  @Secured("ADMIN_COSMO")
  @Override
  public Response deleteConfigurazioneEnte(String chiave, BigDecimal idEnte,
      SecurityContext securityContext) {
    configurazioneEnteService.deleteConfigurazioneEnte(idEnte, chiave);
    return Response.noContent().build();
  }

  @Override
  public Response getConfigurazioneEnte(String chiave, BigDecimal idEnte,
      SecurityContext securityContext) {
    return Response.ok(configurazioneEnteService.getConfigurazioneEnte(idEnte, chiave)).build();
  }

  @Override
  public Response getConfigurazioniEnte(BigDecimal idEnte, SecurityContext securityContext) {
    return Response.ok(configurazioneEnteService.getConfigurazioniEnte(idEnte)).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response postConfigurazioneEnte(ConfigurazioneEnte body, BigDecimal idEnte,
      SecurityContext securityContext) {
    return Response.ok(configurazioneEnteService.postConfigurazioneEnte(idEnte, body)).build();
  }

  @Secured("ADMIN_COSMO")
  @Override
  public Response putConfigurazioneEnte(String chiave, ConfigurazioneEnte body, BigDecimal idEnte,
      SecurityContext securityContext) {
    return Response.ok(configurazioneEnteService.putConfigurazioneEnte(idEnte, chiave, body))
        .build();
  }

}
