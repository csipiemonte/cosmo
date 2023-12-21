/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.impl.cfg.TransactionState;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableActivityCancelledEvent;
import org.flowable.engine.delegate.event.FlowableActivityEvent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.cosmocmmn.business.service.FaultInjectionService;
import it.csi.cosmo.cosmocmmn.business.service.impl.FaultInjectionServiceImpl;
import it.csi.cosmo.cosmocmmn.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmocmmn.util.logger.LogCategory;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerFactory;

public class FaultInjectionEventListener extends AbstractFlowableEngineEventListener {

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  private FaultInjectionService getService() {
    return (FaultInjectionServiceImpl) SpringApplicationContextHelper
        .getBean(FaultInjectionServiceImpl.class);
  }

  private void throwPossibleFault(String hookPoint) {
    var fault = getService().checkFault(hookPoint);
    if (fault.isPresent()) {
      logger.info("throwPossibleFault", "throwing injected fault for hookPoint " + hookPoint);
      throw ExceptionUtils.toChecked(fault.get());
    } else {
      logger.debug("throwPossibleFault", "no injected fault for hookPoint " + hookPoint);
    }
  }

  @Override
  protected void processCreated(FlowableEngineEntityEvent event) {
    throwPossibleFault("processCreated:" + event.getProcessDefinitionId());
  }

  @Override
  protected void processCompleted(FlowableEngineEntityEvent event) {
    throwPossibleFault("processCompleted:" + event.getProcessDefinitionId());
  }

  @Override
  protected void taskCreated(FlowableEngineEntityEvent event) {
    throwPossibleFault(
        "taskCreated:" + event.getProcessDefinitionId() + "/" + event.getExecutionId());
  }

  @Override
  protected void taskAssigned(FlowableEngineEntityEvent event) {
    throwPossibleFault(
        "taskAssigned:" + event.getProcessDefinitionId() + "/" + event.getExecutionId());
  }

  @Override
  protected void taskCompleted(FlowableEngineEntityEvent event) {
    throwPossibleFault(
        "taskCompleted:" + event.getProcessDefinitionId() + "/" + event.getExecutionId());
  }

  @Override
  protected void activityStarted(FlowableActivityEvent event) {
    throwPossibleFault(
        "activityStarted:" + event.getProcessDefinitionId() + "/" + event.getActivityId());
  }

  @Override
  protected void activityCompleted(FlowableActivityEvent event) {
    throwPossibleFault(
        "activityCompleted:" + event.getProcessDefinitionId() + "/" + event.getActivityId());
  }

  @Override
  protected void activityCancelled(FlowableActivityCancelledEvent event) {
    throwPossibleFault(
        "activityCancelled:" + event.getProcessDefinitionId() + "/" + event.getActivityId());
  }

  @Override
  public boolean isFireOnTransactionLifecycleEvent() {
    return true;
  }

  @Override
  public boolean isFailOnException() {
    return true;
  }

  @Override
  public String getOnTransaction() {
    return TransactionState.COMMITTING.name();
  }
}
