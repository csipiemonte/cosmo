/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business;

import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.common.engine.impl.interceptor.EngineConfigurationConstants;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.configurator.IdmEngineConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CosmoIdmEngineConfigurator extends IdmEngineConfigurator {

  @Autowired
  IdmIdentityService idmIdentityService;

  @Override
  public void configure(AbstractEngineConfiguration engineConfiguration) {
    super.configure(engineConfiguration);
    getIdmEngineConfiguration(engineConfiguration).setIdmIdentityService(idmIdentityService);
  }

  protected static IdmEngineConfiguration getIdmEngineConfiguration(
      AbstractEngineConfiguration engineConfiguration) {
    return (IdmEngineConfiguration) engineConfiguration.getEngineConfigurations()
        .get(EngineConfigurationConstants.KEY_IDM_ENGINE_CONFIG);
  }
}
