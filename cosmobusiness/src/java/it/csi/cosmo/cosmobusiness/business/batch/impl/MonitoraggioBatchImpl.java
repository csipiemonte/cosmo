/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.batch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.cosmobusiness.business.batch.MonitoraggioBatch;
import it.csi.cosmo.cosmobusiness.business.service.MonitoraggioService;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;

@Service
public class MonitoraggioBatchImpl extends ParentBatchImpl implements MonitoraggioBatch {
  
  @Autowired
  private MonitoraggioService monitoraggioService;

  @Override
  public boolean isEnabled() {
    return configurazioneService != null && configurazioneService
        .requireConfig(ParametriApplicativo.BATCH_MONITORAGGIO_ENABLE).asBool();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void execute(BatchExecutionContext context) {
    String method = "execute";

    logger.info(method, "esecuzione batch di monitoraggio");
    
    monitoraggioService.monitoraggioPratiche(context);
    
    logger.info(method, "termine batch di monitoraggio");
  }
}
