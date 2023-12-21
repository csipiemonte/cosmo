/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJobAction;
import it.csi.cosmo.cosmobusiness.dto.rest.DeadLetterJobsResponse;

/**
 *
 */

public interface ManagementService {

  DeadLetterJobsResponse getDeadLetterJobs();

  void moveDeadLetterJob(String jobId, DeadLetterJobAction action);
}
