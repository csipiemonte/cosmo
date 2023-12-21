/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.rest.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmo.business.rest.ParametriApi;
import it.csi.cosmo.cosmo.business.rest.proto.ParentApiImpl;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.config.ParametriApplicativo.ExposurePolicy;
import it.csi.cosmo.cosmo.dto.rest.Parametro;


/**
 * Resource implementation for table 'ParametroApplicativo'
 */
public class ParametriApiImpl extends ParentApiImpl implements ParametriApi {

  @Secured(permitAll = true)
  @Override
  public Response getParametri(SecurityContext securityContext) {
    List<Parametro> records = Arrays.stream(ParametriApplicativo.values())
        .filter(value -> value.getPolicyEsposizione() == ExposurePolicy.EXTERNAL)
        .map(configurazioneService::getConfig)
        .map(dto -> {
          Parametro p = new Parametro();
          p.setChiave(dto.getKey());
          p.setValore(dto.getValue());
          return p;
        })
        .collect(Collectors.toList());

    if ( !CollectionUtils.isEmpty(records)) {
      return Response.ok ( records ).build ();
    } else {
      return Response.status ( Status.NO_CONTENT ).build ();
    }
  }

}
