/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.rest.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.runtime.Execution;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.flowable.rest.service.api.runtime.task.TaskActionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import it.csi.cosmo.cosmocmmn.business.service.AsyncTaskService;
import it.csi.cosmo.cosmocmmn.dto.rest.InviaSegnaleProcessoRequest;
import it.csi.cosmo.cosmocmmn.util.logger.LogCategory;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerFactory;

@RestController
@RequestMapping("/async")
public class AsyncController {

  /**
   * 
   */
  private static final String FIELD_OUTCOME = "Outcome";

  /**
   * 
   */
  private static final String FIELD_TRANSIENT_VARIABLES = "TransientVariables";

  @Autowired
  private ProcessEngineConfiguration pec;

  @Autowired
  private AsyncTaskService asyncTaskService;

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @PostMapping("/runtime/process-instances")
  public OperazioneAsincrona postProcessInstance(
      @RequestBody ProcessInstanceCreateRequest payload) {

    ValidationUtils.require(payload.getBusinessKey(), "BusinessKey");
    ValidationUtils.require(payload.getTenantId(), "TenantId");
    ValidationUtils.require(payload.getProcessDefinitionKey(), "ProcessDefinitionKey");
    ValidationUtils.notSupported(payload.getProcessDefinitionId(), "ProcessDefinitionId");
    ValidationUtils.notSupported(payload.getOutcome(), FIELD_OUTCOME);
    ValidationUtils.notSupported(payload.getTransientVariables(), FIELD_TRANSIENT_VARIABLES);

    RuntimeService runtimeService = pec.getRuntimeService();

    Map<String, Object> variables = new HashMap<>();
    if (payload.getVariables() != null) {
      payload.getVariables().forEach(v -> {
        var value = v.getValue();
        if (value instanceof Map<?, ?> || value instanceof Collection<?>) {
          // import as json
          value = ObjectUtils.getDataMapper().convertValue(value, JsonNode.class);
        }
        variables.put(v.getName(), v.getValue());
      });
    }

    var future = asyncTaskService.start("Avvio del processo", task -> {

      var instance = runtimeService.createProcessInstanceBuilder()
          .businessKey(payload.getBusinessKey()).tenantId(payload.getTenantId())
          .processDefinitionKey(payload.getProcessDefinitionKey()).variables(variables).start();

      return instance.getId();
    });

    return referenceOperazioneAsincrona(future);
  }

  @PostMapping("/runtime/tasks/{taskId}/update")
  public OperazioneAsincrona updateTask(@PathVariable("taskId") String taskId,
      @RequestBody TaskActionRequest payload) {

    ValidationUtils.require(taskId, "taskId");
    ValidationUtils.notSupported(payload.getAssignee(), "Assignee");
    ValidationUtils.notSupported(payload.getFormDefinitionId(), "FormDefinitionId");
    ValidationUtils.notSupported(payload.getOutcome(), FIELD_OUTCOME);
    ValidationUtils.notSupported(payload.getTransientVariables(), FIELD_TRANSIENT_VARIABLES);

    TaskService taskService = pec.getTaskService();

    Map<String, Object> variables = new HashMap<>();
    if (payload.getVariables() != null) {
      payload.getVariables().forEach(v -> variables.put(v.getName(), v.getValue()));
    }

    var future = asyncTaskService.start("Aggiornamento del task", task -> {

      task.step("Aggiornamento delle variabili", step -> {
        // taskService.setVariables(taskId, variables);
        for (Entry<String, Object> entry : variables.entrySet()) {
          var value = entry.getValue();
          if (value instanceof Map<?, ?> || value instanceof Collection<?>) {
            // import as json
            value = ObjectUtils.getDataMapper().convertValue(value, JsonNode.class);
          }
          taskService.setVariable(taskId, entry.getKey(), value);
        }
      });

      return taskId;
    });

    return referenceOperazioneAsincrona(future);
  }

  @PostMapping("/runtime/tasks/{taskId}/complete")
  public OperazioneAsincrona completeTask(@PathVariable("taskId") String taskId,
      @RequestBody TaskActionRequest payload) {

    ValidationUtils.require(taskId, "taskId");
    ValidationUtils.notSupported(payload.getAssignee(), "Assignee");
    ValidationUtils.notSupported(payload.getFormDefinitionId(), "FormDefinitionId");
    ValidationUtils.notSupported(payload.getOutcome(), FIELD_OUTCOME);
    ValidationUtils.notSupported(payload.getTransientVariables(), FIELD_TRANSIENT_VARIABLES);

    TaskService taskService = pec.getTaskService();

    Map<String, Object> variables = new HashMap<>();
    if (payload.getVariables() != null) {
      payload.getVariables().forEach(v -> variables.put(v.getName(), v.getValue()));
    }

    var future = asyncTaskService.start("Aggiornamento del task", task -> {

      task.step("Aggiornamento delle variabili", step -> {
        // taskService.setVariables(taskId, variables);
        for (Entry<String, Object> entry : variables.entrySet()) {
          var value = entry.getValue();
          if (value instanceof Map<?, ?> || value instanceof Collection<?>) {
            // import as json
            value = ObjectUtils.getDataMapper().convertValue(value, JsonNode.class);
          }
          taskService.setVariable(taskId, entry.getKey(), value);
        }
      });

      task.step("Completamento del task", step -> {
        try {
          taskService.complete(taskId);
        } catch (Throwable e) { // NOSONAR
          if (e instanceof BpmnError) { // NOSONAR
            var bmpnError = (BpmnError) e;
            if (bmpnError.getMessage().startsWith("No catching boundary event")) {
              logger.warn("completeTask", e.getMessage());

              if (bmpnError.getCause() != null) {
                e = bmpnError.getCause();
              } else {
                logger.error("completeTask", bmpnError.getMessage(), bmpnError);
                e = new InternalServerException(bmpnError.getErrorCode());
              }
            }
          }
          logger.error("completeTask", "error in async complete task step: " + e.getMessage());
          throw ExceptionUtils.toChecked(e);
        }
      });

      return taskId;
    });

    return referenceOperazioneAsincrona(future);
  }

  @PostMapping("/runtime/process/{processId}/signal")
  public OperazioneAsincrona inviaSegnaleProcesso(@PathVariable("processId") String processId,
      @RequestBody InviaSegnaleProcessoRequest payload) {

    final var methodName = "inviaSegnaleProcesso";

    ValidationUtils.require(processId, "processId");
    ValidationUtils.require(payload, "payload");
    ValidationUtils.validaAnnotations(payload);

    RuntimeService runtimeService = pec.getRuntimeService();

    var processInstances =
        runtimeService.createProcessInstanceQuery().processInstanceId(processId).active().list();

    if (processInstances.isEmpty()) {
      String msg = "Nessun processo attivo corrispondente all'identificativo fornito";
      logger.warn(methodName, msg);
      throw new ConflictException(msg);
    }

    if (processInstances.size() > 1) {
      String msg = "Troppi processi (" + processInstances.size() + ") corrispondenti al processId "
          + processId;
      logger.warn(methodName, msg);
      throw new InternalServerException(msg);
    }

    var processInstance = processInstances.get(0);
    var waitingExecutions =
        runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
            .messageEventSubscriptionName(payload.getCodiceSegnale()).list();

    var future = asyncTaskService.start("Invio del segnale al processo", task -> {

      logger.info(methodName, "iniziata elaborazione asincrona del segnale {} per il processo {}",
          payload.getCodiceSegnale(), processId);

      if (waitingExecutions.isEmpty()) {
        logger.info(methodName, "nessuna esecuzione in attesa nel processo {} per il segnale {}",
            processId, payload.getCodiceSegnale());
        return null;
      }

      logger.info(methodName, "{} esecuzioni in attesa nel processo {} per il segnale {}",
          waitingExecutions.size(), processId, payload.getCodiceSegnale());

      for (Execution execution : waitingExecutions) {
        task.step(
            "Invio segnale " + payload.getCodiceSegnale() + " all'execution " + execution.getId(),
            step -> {
              logger.info(methodName, "invio del segnale {} all'esecuzione {} nel processo {}",
                  payload.getCodiceSegnale(), execution.getId(), processId);

              runtimeService.messageEventReceived(payload.getCodiceSegnale(), execution.getId());

              logger.info(methodName, "inviato il segnale {} all'esecuzione {} nel processo {}",
                  payload.getCodiceSegnale(), execution.getId(), processId);

            });
      }

      logger.info(methodName, "terminata elaborazione asincrona del segnale {} per il processo {}",
          payload.getCodiceSegnale(), processId);

      return null;
    });

    return referenceOperazioneAsincrona(future);
  }

  private OperazioneAsincrona referenceOperazioneAsincrona(LongTaskFuture<?> future) {
    OperazioneAsincrona output = new OperazioneAsincrona();
    output.setStato(future.getTask().getStatus().name());
    output.setNome(future.getTask().getName());
    output.setUuid(future.getTaskId());
    return output;
  }
}
