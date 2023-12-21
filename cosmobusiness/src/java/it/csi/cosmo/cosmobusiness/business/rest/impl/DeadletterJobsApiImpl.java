/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.DeadletterJobsApi;
import it.csi.cosmo.cosmobusiness.business.service.ManagementService;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJobAction;

/**
 *
 */

public class DeadletterJobsApiImpl extends ParentApiImpl implements DeadletterJobsApi {

  @Autowired
  private ManagementService managementService;

  @Override
  public Response getDeadletterJobs(SecurityContext securityContext) {
    return Response.ok(managementService.getDeadLetterJobs()).build();
  }

  @Override
  public Response postDeadletterJobsJobId(String jobId, DeadLetterJobAction body,
      SecurityContext securityContext) {
    managementService.moveDeadLetterJob(jobId, body);
    return Response.ok().build();
  }

}
