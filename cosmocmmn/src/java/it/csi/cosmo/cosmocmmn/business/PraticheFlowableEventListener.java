/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BoundaryEvent;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.common.engine.impl.cfg.TransactionState;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableActivityCancelledEvent;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntityImpl;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.variable.api.event.FlowableVariableEvent;
import it.csi.cosmo.common.dto.rest.process.ProcessEngineEventDTO;
import it.csi.cosmo.common.dto.rest.process.ProcessInstanceDTO;
import it.csi.cosmo.common.dto.rest.process.ProcessInstanceVariableEventDTO;
import it.csi.cosmo.common.dto.rest.process.TaskIdentityLinkDTO;
import it.csi.cosmo.common.dto.rest.process.TaskInstanceDTO;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmocmmn.business.service.EventSenderService;
import it.csi.cosmo.cosmocmmn.util.FlowableVariableType;
import it.csi.cosmo.cosmocmmn.util.logger.LogCategory;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerFactory;

public class PraticheFlowableEventListener extends AbstractFlowableEngineEventListener {

  public static final String STATE_CHANGE = "stateChange";
  private static final String ACTIVITY_CANCELLED = "activityCancelled";
  private static final String TASK_CREATED = "taskCreated";
  private static final String TASK_COMPLETED = "taskCompleted";
  private static final String TASK_ASSIGNED = "taskAssigned";
  private static final String PROCESS_COMPLETED = "processCompleted";
  private static final String PROCESS_CREATED = "processCreated";
  private static final String PROCESS_CANCELLED = "processCancelled";
  private static final String PROCESS_COMPLETED_WITH_TERMINATE_END = "processCompletedWithTerminateEnd";
  private static final String PROCESS_COMPLETED_WITH_ERROR_END = "processCompletedWithErrorEnd";
  private static final String VARIABLE_CREATED = "variableCreated";
  private static final String VARIABLE_UPDATED = "variableUpdated";
  private static final String VARIABLE_DELETED = "variableDeleted";

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  ProcessEngineConfiguration processEngineConfiguration = null;

  EventSenderService senderService = null;

  public PraticheFlowableEventListener(ProcessEngineConfiguration processEngineConfiguration,
      EventSenderService senderService) {
    this.processEngineConfiguration = processEngineConfiguration;
    this.senderService = senderService;
  }

  @Override
  protected void processCompletedWithErrorEnd(FlowableEngineEntityEvent event) {
    logEvent(event);
    processProcessEvent(PROCESS_COMPLETED_WITH_ERROR_END, event);
  }

  @Override
  protected void processCompletedWithTerminateEnd(FlowableEngineEntityEvent event) {
    logEvent(event);
    processProcessEvent(PROCESS_COMPLETED_WITH_TERMINATE_END, event);
  }

  @Override
  protected void processCreated(FlowableEngineEntityEvent event) {
    logEvent(event);
    processProcessEvent(PROCESS_CREATED, event);
  }

  @Override
  protected void processCompleted(FlowableEngineEntityEvent event) {
    logEvent(event);
    processProcessEvent(PROCESS_COMPLETED, event);
  }

  @Override
  protected void taskAssigned(FlowableEngineEntityEvent event) {
    logEvent(event);
    processTaskEvent(TASK_ASSIGNED, event);
  }

  @Override
  protected void taskCompleted(FlowableEngineEntityEvent event) {
    logEvent(event);
    processTaskEvent(TASK_COMPLETED, event);
  }

  @Override
  protected void activityCancelled(FlowableActivityCancelledEvent event) {
    logEvent(event);
    processActivityCancelled(ACTIVITY_CANCELLED, event);
  }

  @Override
  protected void taskCreated(FlowableEngineEntityEvent event) {
    logEvent(event);
    processTaskEvent(TASK_CREATED, event);
  }

  @Override
  protected void entityDeleted(FlowableEngineEntityEvent event) {
    entityInitialized(event);
  }

  @Override
  protected void entityInitialized(FlowableEngineEntityEvent event) {
    if (event.getEntity() == null) {
      return;
    }
    if (!(event.getEntity() instanceof IdentityLinkEntityImpl)) {
      return;
    }

    IdentityLinkEntityImpl entity = (IdentityLinkEntityImpl) event.getEntity();
    if (StringUtils.isEmpty(entity.getTaskId())) {
      return;
    }

    if (!"candidate".equals(entity.getType())) {
      return;
    }

    processIdentityEntityUpdate(event, entity);
  }

  protected void processIdentityEntityUpdate(FlowableEngineEntityEvent event,
      IdentityLinkEntityImpl entity) {
    HistoricTaskInstance taskParent = processEngineConfiguration.getHistoryService()
        .createHistoricTaskInstanceQuery().taskId(entity.getTaskId()).singleResult();
    String processInstanceId = taskParent.getProcessInstanceId();

    HistoricProcessInstance p = null;
    if (processInstanceId != null) {
      p = processEngineConfiguration.getHistoryService().createHistoricProcessInstanceQuery()
          .includeProcessVariables().processInstanceId(processInstanceId).singleResult();
    }

    Task t = processEngineConfiguration.getTaskService().createTaskQuery()
        .taskId(entity.getTaskId()).list().stream().findFirst().orElse(null);

    if (t != null || p != null) {
      ProcessEngineEventDTO mapped = map(TASK_ASSIGNED, event, p, t);
      handle(mapped);
    }
  }

  @Override
  protected void processCancelled(FlowableCancelledEvent event) {
    logEvent(event);

    HistoricProcessInstance p = null;
    Task t = null;

    p = processEngineConfiguration.getHistoryService().createHistoricProcessInstanceQuery()
        .includeProcessVariables().processInstanceId(event.getProcessInstanceId()).singleResult();

    ProcessEngineEventDTO mapped = map(PROCESS_CANCELLED, event, p, t);

    handle(mapped);
  }

  private void processProcessEvent(String eventType, FlowableEngineEntityEvent event) {
    HistoricProcessInstance p = null;
    Task t = null;
    Object entity = event.getEntity();

    ExecutionEntity te = entity instanceof ExecutionEntity ? ((ExecutionEntity) entity) : null;
    if (te != null) {
      p = processEngineConfiguration.getHistoryService().createHistoricProcessInstanceQuery()
          .includeProcessVariables().processInstanceId(te.getProcessInstanceId()).singleResult();

    } else {
      p = processEngineConfiguration.getHistoryService().createHistoricProcessInstanceQuery()
          .includeProcessVariables().processInstanceId(event.getProcessInstanceId()).singleResult();
    }

    ProcessEngineEventDTO mapped = map(eventType, event, p, t);
    handle(mapped);
  }

  private void processTaskEvent(String eventType, FlowableEngineEntityEvent event) {

    HistoricProcessInstance p = null;
    Task t = null;
    Object entity = event.getEntity();
    String processInstanceId = null;

    TaskEntity te = entity instanceof TaskEntity ? ((TaskEntity) entity) : null;
    if (te != null) {
      processInstanceId = te.getProcessInstanceId();
      if (te.getProcessInstanceId() == null && te.getParentTaskId() != null) {
        HistoricTaskInstance taskParent = processEngineConfiguration.getHistoryService()
            .createHistoricTaskInstanceQuery().taskId(te.getParentTaskId()).singleResult();
        processInstanceId = taskParent.getProcessInstanceId();
      }
    }

    if (processInstanceId != null) {
      p = processEngineConfiguration.getHistoryService().createHistoricProcessInstanceQuery()
          .includeProcessVariables().processInstanceId(processInstanceId).singleResult();
    }

    t = entity instanceof Task ? ((Task) entity) : null;

    if (t != null || p != null) {
      ProcessEngineEventDTO mapped = map(eventType, event, p, t);
      handle(mapped);
    }
  }

  private void processActivityCancelled(String eventType,
      FlowableActivityCancelledEvent event) {

    TaskInfo t = null;
    HistoricProcessInstance p = null;

    Object cause = event.getCause();
    String processInstanceId = event.getProcessInstanceId();
    String executionId = event.getExecutionId();

    BoundaryEvent be = cause instanceof BoundaryEvent ? ((BoundaryEvent) cause) : null;
    UserTask ue = cause instanceof UserTask ? ((UserTask) cause) : null;

    if ((be != null && event.getActivityId().equals(be.getAttachedToRefId()))
        || (ue != null && event.getActivityId().equals(ue.getId())) && processInstanceId != null
        && executionId != null) {

      logger.info("processActivityCancelled", "Event with ID: " + event.getActivityId());

      p =
          processEngineConfiguration.getHistoryService()
          .createHistoricProcessInstanceQuery()
          .includeProcessVariables()
          .processInstanceId(processInstanceId)
          .singleResult();

      t = processEngineConfiguration.getTaskService()
          .createTaskQuery()
          .processInstanceId(processInstanceId)
          .executionId(executionId)
          .taskDefinitionKey(event.getActivityId())
          .singleResult();

      if (t == null) {
        t = processEngineConfiguration.getHistoryService()
            .createHistoricTaskInstanceQuery()
            .processInstanceId(processInstanceId)
            .executionId(executionId)
            .taskDefinitionKey(event.getActivityId())
            .singleResult();
      }
    }

    if (t != null && p != null) {
      ProcessEngineEventDTO mapped = map(eventType, event, p, t);
      handle(mapped);
    }
  }

  @Override
  protected void variableCreated(FlowableVariableEvent event) {

    logEvent(event);


    if (event.getProcessInstanceId() != null) {

      HistoricProcessInstance p = processEngineConfiguration.getHistoryService()
          .createHistoricProcessInstanceQuery().includeProcessVariables()
          .processInstanceId(event.getProcessInstanceId()).singleResult();

      ProcessInstanceVariableEventDTO mapped = map(VARIABLE_CREATED, event, p);

      handle(mapped);
    }

  }

  @Override
  protected void variableUpdatedEvent(FlowableVariableEvent event) {
    logEvent(event);


    if (event.getProcessInstanceId() != null) {

      HistoricProcessInstance p = processEngineConfiguration.getHistoryService()
          .createHistoricProcessInstanceQuery().includeProcessVariables()
          .processInstanceId(event.getProcessInstanceId()).singleResult();

      ProcessInstanceVariableEventDTO mapped = map(VARIABLE_UPDATED, event, p);

      handle(mapped);
    }
  }

  @Override
  protected void variableDeletedEvent(FlowableVariableEvent event) {
    logEvent(event);


    if (event.getProcessInstanceId() != null) {

      HistoricProcessInstance p = processEngineConfiguration.getHistoryService()
          .createHistoricProcessInstanceQuery().includeProcessVariables()
          .processInstanceId(event.getProcessInstanceId()).singleResult();

      if (p.getEndTime() == null) {
        ProcessInstanceVariableEventDTO mapped = map(VARIABLE_DELETED, event, p);

        handle(mapped);
      }

    }
  }

  private void handle(ProcessInstanceVariableEventDTO event) {
    this.senderService.sendVariableEventToCosmo(event);
  }

  protected void handle(ProcessEngineEventDTO event) {
    this.senderService.sendEventToCosmo(event);
  }

  private ProcessEngineEventDTO map(String eventType, FlowableEngineEvent ev,
      HistoricProcessInstance p, TaskInfo t) {
    var output = new ProcessEngineEventDTO();

    OffsetDateTime adesso = OffsetDateTime.now();

    output.setTimestamp(adesso);
    output.setMessageType(eventType != null ? eventType : ev.getType().name());

    Map<String, Object> mappedVariables = new HashMap<>();

    if (p != null) {
      var mappedProcess = new ProcessInstanceDTO();
      mappedProcess.setBusinessKey(p.getBusinessKey());
      mappedProcess.setTenantId(p.getTenantId());
      mappedProcess.setProcessDefinitionKey(p.getProcessDefinitionKey());
      mappedProcess.setId(p.getId());

      if (p.getProcessVariables() != null) {
        p.getProcessVariables().forEach(mappedVariables::put);
      }

      mappedProcess.setVariables(mappedVariables);
      output.setProcess(mappedProcess);
    }

    if (t != null) {
      var mappedTask = new TaskInstanceDTO();
      mappedTask.setId(t.getId());
      mappedTask.setName(t.getName());
      mappedTask.setDescription(t.getDescription());
      mappedTask.setParentTaskId(t.getParentTaskId());
      mappedTask.setAssignee(t.getAssignee());
      mappedTask.setFormKey(t.getFormKey());

      if (t.getIdentityLinks() != null) {
        mappedTask.setIdentityLinks(t.getIdentityLinks().stream().map(link -> {
          var out = new TaskIdentityLinkDTO();
          out.setType(link.getType());
          out.setUserId(link.getUserId());
          out.setGroupId(link.getGroupId());
          return out;
        }).collect(Collectors.toCollection(HashSet::new)));
      }

      output.setTask(mappedTask);
    }

    return output;
  }

  private ProcessInstanceVariableEventDTO map(String eventType, FlowableVariableEvent event,
      HistoricProcessInstance p) {

    var output = new ProcessInstanceVariableEventDTO();
    output.setBusinessKey(p.getBusinessKey());
    output.setVariableName(event.getVariableName());
    output.setVariableType(event.getVariableType().getTypeName());
    output.setMessageType(eventType != null ? eventType : event.getType().name());
    if (eventType != null && !eventType.equalsIgnoreCase(VARIABLE_DELETED)) {
      switch (FlowableVariableType.fromCodice(event.getVariableType().getTypeName())) {
        case LONGJSONTYPE:
        case LONGSTRINGTYPE:
        case SERIALIZABLETYPE:
          output.setBytearrayVariableValue( event.getVariableValue().toString().getBytes());
          break;
        case BYTEARRAYTYPE:
          output.setBytearrayVariableValue((byte[]) event.getVariableValue());
          break;
        case LONGTYPE:
        case SHORTTYPE:
        case INTEGERTYPE:
          output.setLongVariableValue(Long.valueOf(event.getVariableValue().toString()));
          break;
        case DOUBLETYPE:
          output.setDoubleVariableValue(Double.valueOf(event.getVariableValue().toString()));
          break;
        case NULLTYPE:
          break;
        default:
          output.setTextVariableValue(event.getVariableValue().toString());

      }
    }

    return output;
  }


  private void logEvent(FlowableEngineEvent ev) {
    if (logger.isDebugEnabled()) {
      try {
        logger.debug("event", "< " + ev.getType() + " - " + ObjectUtils.toJson(ev));
      } catch (Exception e) {
        logger.debug("event", "< " + ev.getType() + " - " + ev.toString() + " ("
            + e.getClass().getSimpleName() + ")");
      }
    }
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
