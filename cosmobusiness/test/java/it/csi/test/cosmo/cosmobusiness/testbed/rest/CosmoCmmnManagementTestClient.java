/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import org.flowable.rest.service.api.RestActionRequest;
import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadletterJobResponseWrapper;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnManagementFeignClient;

/**
 *
 */
@Component
public class CosmoCmmnManagementTestClient extends ParentTestClient implements CosmoCmmnManagementFeignClient{

  @Override
  public DeadletterJobResponseWrapper getDeadletterJobs(String processInstanceId,
      String processDefinitionId, String tenantId, String sort, String size) {
    notMocked();
    return null;
  }

  @Override
  public void moveDeadletterJobs(String jobId, RestActionRequest action) {
    notMocked();

  }

}
