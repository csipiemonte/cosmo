/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.rest.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.flowable.rest.service.api.runtime.task.TaskActionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;

@RestController
@RequestMapping("/sync")
public class SyncController {

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

  @PostMapping("/runtime/process-instances")
  public Map<String, Object> postProcessInstance(
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

    var instance = runtimeService.createProcessInstanceBuilder()
        .businessKey(payload.getBusinessKey()).tenantId(payload.getTenantId())
        .processDefinitionKey(payload.getProcessDefinitionKey()).variables(variables).start();

    Map<String, Object> output = new HashMap<>();
    output.put("processInstanceId", instance.getId());
    return output;
  }

  @PutMapping("/runtime/process-instances/{processInstanceId}/variables")
  public Map<String, Object> putProcessInstanceVariables(
      @PathVariable("processInstanceId") String processInstanceId,
      @RequestBody List<RestVariable> variablesIn) {

    RuntimeService runtimeService = pec.getRuntimeService();

    Map<String, Object> variables = new HashMap<>();
    if (variablesIn != null) {
      variablesIn.forEach(v -> {
        var value = v.getValue();
        if (value instanceof Map<?, ?> || value instanceof Collection<?>) {
          // import as json
          value = ObjectUtils.getDataMapper().convertValue(value, JsonNode.class);
        }
        variables.put(v.getName(), value);
      });
    }

    runtimeService.setVariables(processInstanceId, variables);

    Map<String, Object> output = new HashMap<>();
    output.put("processInstanceId", processInstanceId);
    return output;
  }

  @PostMapping("/runtime/tasks/{taskId}/update")
  public Map<String, Object> updateTask(@PathVariable("taskId") String taskId,
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

    for (Entry<String, Object> entry : variables.entrySet()) {
      var value = entry.getValue();
      if (value instanceof Map<?, ?> || value instanceof Collection<?>) {
        // import as json
        value = ObjectUtils.getDataMapper().convertValue(value, JsonNode.class);
      }
      taskService.setVariable(taskId, entry.getKey(), value);
    }

    Map<String, Object> output = new HashMap<>();
    return output;
  }

}
