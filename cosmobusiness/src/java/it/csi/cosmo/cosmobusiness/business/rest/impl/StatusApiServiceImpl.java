/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.common.exception.ManagedException;
import it.csi.cosmo.cosmo.dto.rest.ServiceStatus;
import it.csi.cosmo.cosmobusiness.business.rest.StatusApi;
import it.csi.cosmo.cosmobusiness.business.service.MonitoringService;


public class StatusApiServiceImpl extends ParentApiImpl implements StatusApi {

  @Autowired
  public MonitoringService monitoringService;

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


    if (status.getStatus() == ServiceStatusEnum.UP) {
      return Response.ok(serviceStatus).build();
    } else {
      throw ManagedException.withResponse(HttpStatus.SERVICE_UNAVAILABLE, serviceStatus);
    }
  }

}
