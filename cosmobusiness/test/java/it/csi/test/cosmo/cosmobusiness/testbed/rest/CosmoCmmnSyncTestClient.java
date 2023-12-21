/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import java.util.List;
import java.util.Map;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnSyncFeignClient;

/**
 *
 */

@Component
public class CosmoCmmnSyncTestClient extends ParentTestClient
    implements CosmoCmmnSyncFeignClient {

  /**
   * 
   */
  public CosmoCmmnSyncTestClient() {
    // Auto-generated constructor stub
  }

  @Override
  public Map<String, Object> postProcessInstance(ProcessInstanceCreateRequest payload) {
    notMocked();
    return null;
  }

  @Override
  public Map<String, Object> putProcessInstanceVariables(String processInstanceId,
      List<RestVariable> variablesIn) {
    notMocked();
    return null;
  }

}
