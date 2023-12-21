/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.dto.rest.process.ProcessEngineEventDTO;
import it.csi.cosmo.common.dto.rest.process.ProcessInstanceVariableEventDTO;
import it.csi.cosmo.cosmobusiness.business.service.ProcessEventService;
import it.csi.cosmo.cosmobusiness.business.service.ProcessVariableEventService;
import it.csi.cosmo.cosmobusiness.client.ProcessEventApi;

/**
 *
 */

public class ProcessEventApiImpl extends ParentApiImpl implements ProcessEventApi {

  @Autowired
  private ProcessEventService processEventService;

  @Autowired
  private ProcessVariableEventService processVariableEventService;

  @Override
  public Response postProcessEvent(ProcessEngineEventDTO body) {

    processEventService.process(body);
    return Response.status(200).build();
  }

  @Override
  public Response postProcessVariableEvent(ProcessInstanceVariableEventDTO body) {
    processVariableEventService.process(body);
    return Response.status(200).build();
  }

}
