/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.rest.impl;

import java.util.Map;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.dto.common.ServiceStatusDTO;
import it.csi.cosmo.common.dto.common.ServiceStatusEnum;
import it.csi.cosmo.cosmonotifications.business.rest.StatusApi;
import it.csi.cosmo.cosmonotifications.business.service.MonitoringService;
import it.csi.cosmo.cosmonotifications.dto.rest.ServiceStatus;


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
        status.getResponseTime() != 0 ? status.getResponseTime().intValue()
            : null);
    serviceStatus.setProduct(map.get("prodotto").toString());
    serviceStatus.setComponent(map.get("componente").toString());
    serviceStatus.setEnviroment(map.get("ambiente").toString());
    serviceStatus.setVersion(map.get("versione").toString());

    if (status.getStatus() == ServiceStatusEnum.UP) {
      return Response.ok(serviceStatus).build();
    } else {
      return Response.status(Status.SERVICE_UNAVAILABLE).entity(serviceStatus).build();
    }
  }

}
