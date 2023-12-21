/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import javax.validation.Valid;
import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnTaskFeignClient;
import it.csi.cosmo.cosmocmmn.dto.rest.AssegnaTaskRequest;
import it.csi.cosmo.cosmocmmn.dto.rest.AssegnaTaskResponse;

/**
 *
 *
 */
@Component
public class CosmoCmmnTaskTestClient extends ParentTestClient implements CosmoCmmnTaskFeignClient {

  /**
   *
   */
  public CosmoCmmnTaskTestClient() {
    // Auto-generated constructor stub
  }

  @Override
  public AssegnaTaskResponse postTaskAssegna(String arg0, @Valid AssegnaTaskRequest arg1) {
    notMocked();
    return null;
  }

}
