/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmobusiness.integration.rest;

import java.util.List;
import java.util.Map;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;


/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/cmmn/process/sync",
    interceptors = CosmoBusinessFeignClientConfiguration.class,
    configurator = CosmoBusinessCmmnFeignClientConfiguration.class)
public interface CosmoCmmnSyncFeignClient {

  @POST
  @Path("/runtime/process-instances")
  Map<String, Object> postProcessInstance(ProcessInstanceCreateRequest payload);

  @PUT
  @Path("/runtime/process-instances/{processInstanceId}/variables")
  Map<String, Object> putProcessInstanceVariables(
      @PathParam("processInstanceId") String processInstanceId, List<RestVariable> variablesIn);
}
