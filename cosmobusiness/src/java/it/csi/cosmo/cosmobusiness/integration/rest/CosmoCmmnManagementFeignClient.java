/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmobusiness.integration.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.flowable.rest.service.api.RestActionRequest;
import it.csi.cosmo.common.feignclient.model.FeignClient;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadletterJobResponseWrapper;


/**
 *
 */

@FeignClient(
    value = "${" + ParametriApplicativo.DISCOVERY_SERVER_LOCATION_KEY
    + "}/api/proxy/cmmn",
    interceptors = CosmoBusinessFeignClientConfiguration.class,
    configurator = CosmoBusinessCmmnFeignClientConfiguration.class)
public interface CosmoCmmnManagementFeignClient {

  // ----------------------GET----------------------

  @GET
  @Path("/process/management/deadletter-jobs")
  DeadletterJobResponseWrapper getDeadletterJobs(
      @QueryParam("processInstanceId") String processInstanceId,
      @QueryParam("processDefinitionId") String processDefinitionId,
      @QueryParam("tenantId") String tenantId, @QueryParam("sort") String sort,
      @QueryParam("size") String size);

  @POST
  @Path("/process/management/deadletter-jobs/{jobId}")
  void moveDeadletterJobs(@PathParam("jobId") String jobId, RestActionRequest action);

}
