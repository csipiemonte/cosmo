/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.business.rest.impl;

import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmobe.business.rest.StatusApi;
import it.csi.cosmo.cosmobe.business.service.MonitoringService;
import it.csi.cosmo.cosmobe.dto.rest.ServiceStatus;


public class StatusApiImpl extends ParentApiImpl implements StatusApi {

  @Autowired
  public MonitoringService monitoringService;

  @Secured(permitAll = true)
  @Override
  public Response getStatus(SecurityContext securityContext) {
    Map<String, Object> map = getConfigurazioneService().getBuildProperties();

    ServiceStatusDTO status = monitoringService.getServiceStatus();

    ServiceStatus serviceStatus = new ServiceStatus();
    serviceStatus.setStatus(status.getStatus().name());
    serviceStatus.setName(status.getName());
    serviceStatus.setDetails(status.getDetails());
    serviceStatus.setResponseTime(
        status.getResponseTime() != null ? status.getResponseTime().intValue() : null);
    serviceStatus.setProduct(map.get("prodotto").toString());
    serviceStatus.setComponent(map.get("componente").toString());
    serviceStatus.setEnviroment(map.get("ambiente").toString());
    serviceStatus.setVersion(map.get("versione").toString());

    if (status.getStatus() != ServiceStatusEnum.UP) {
      return Response.status(HttpStatus.SERVICE_UNAVAILABLE.value()).entity(serviceStatus).build();
    } else {
      return Response.ok(serviceStatus).build();
    }
  }

}
