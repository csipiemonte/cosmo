/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ExecutionActionRequest;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.rest.service.api.runtime.task.TaskQueryRequest;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.springframework.stereotype.Component;
import it.csi.cosmo.cosmobusiness.dto.rest.ExecutionWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.HistoricVariablesPage;
import it.csi.cosmo.cosmobusiness.dto.rest.IdentityLink;
import it.csi.cosmo.cosmobusiness.dto.rest.ProcessInstanceWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.dto.rest.TaskResponseWrapper;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;

/**
 *
 */

/**
 *
 *
 */
@Component
public class CosmoCmmnTestClient extends ParentTestClient implements CosmoCmmnFeignClient {

  /**
   *
   */
  public CosmoCmmnTestClient() {
    // Auto-generated constructor stub
  }

  @Override
  public ProcessInstanceResponse getProcessInstance(String processInstanceId) {
    notMocked();
    return null;
  }

  @Override
  public byte[] getSerializedVariable(String processInstanceId, String variableName) {
    notMocked();
    return null; // NOSONAR
  }

  @Override
  public TaskResponseWrapper getTasks(String assignee, String dueBefore, String dueAfter,
      String sort, String size, String start, String tenantId) {
    notMocked();
    return null;
  }

  @Override
  public TaskResponseWrapper getPraticheTasksProcessInstanceId(String processInstanceId) { // NOSONAR
    notMocked();
    return null;
  }

  @Override
  public RestVariable[] getProcessInstanceVariables(String processInstanceId) { // NOSONAR

    RestVariable var1 = new RestVariable();
    var1.setName("var1");
    var1.setValue("value of variable 1");

    RestVariable var2 = new RestVariable();
    var2.setName("var2");
    var2.setValue(123);

    RestVariable var3 = new RestVariable();
    var3.setName("var3");
    var3.setValue(null);

    RestVariable var4 = new RestVariable();
    var4.setName("var4");
    var4.setValue(Arrays.asList("item1", "item2", "item3"));

    Map<String, Object> map = new HashMap<>();
    map.put("key1", 123);
    map.put("key2", "value2");
    RestVariable var5 = new RestVariable();
    var5.setName("var5");
    var5.setValue(map);

    return new RestVariable[] {var1, var2, var3, var4, var5};
  }

  @Override
  public TaskResponse getTaskId(String id) {
    notMocked();
    return null;
  }


  @Override
  public List<Map<String, String>> getPraticheIdStoricoTask(String processInstanceId) { // NOSONAR
    notMocked();
    return null; // NOSONAR
  }

  @Override
  public TaskResponseWrapper getPraticheIdStoricoAttivitaTask(String processInstanceId,
      String activityType) {
    notMocked();
    return null;
  }

  @Override
  public ProcessInstanceWrapper getProcessInstancesByBusinessKey(String businessKey) {
    notMocked();
    return null;
  }

  @Override
  public ExecutionWrapper getExecutions(String messageEventSubscriptionName,
      String processInstanceId) {
    notMocked();
    return null;
  }

  @Override
  public TaskResponse postTask(Task task) {

    TaskResponse response = new TaskResponse();
    response.setId("999999");

    return response;
  }

  @Override
  public Map<String, Object> postTaskId(String id, Task task) {
    notMocked();
    return null;
  }

  @Override
  public ProcessInstanceResponse postPratica(Map<String, Object> body) {
    notMocked();
    return null;
  }

  @Override
  public TaskResponseWrapper postQueryTask(TaskQueryRequest task) {
    notMocked();
    return null;
  }

  @Override
  public TaskResponse putTask(String id, Task task) { // NOSONAR
    notMocked();
    return null;
  }

  @Override
  public RestVariable[] putProcessInstanceVariables(String processInstanceId,
      List<RestVariable> variables) {
    notMocked();
    return null; // NOSONAR
  }

  @Override
  public void putPraticaIdPraticaExt(String idPraticaExt, Map<String, Object> jsonPayload) {
    notMocked();

  }

  @Override
  public ExecutionWrapper putExecution(String executionId, ExecutionActionRequest request) {
    notMocked();
    return null;
  }

  @Override
  public ProcessInstanceResponse postProcessInstance(ProcessInstanceCreateRequest body) {
    notMocked();
    return null;
  }

  @Override
  public void deleteProcessInstanceVariables(String processInstanceId, String variableName) {
    notMocked();

  }

  @Override
  public RestVariable[] postTaskVariables(String id, List<RestVariable> variables) {
    notMocked();
    return null;
  }


  @Override
  public TaskResponse getHistoricTaskId(String taskId) {
    notMocked();
    return null;
  }

  @Override
  public RestVariable[] getTaskVariables(String taskId) {

    notMocked();
    return null;
  }

  @Override
  public ProcessInstanceWrapper getHistoricProcessInstances(String processInstanceId,
      Boolean includeProcessVariables) {

    notMocked();
    return null;
  }

  @Override
  public HistoricVariablesPage getHistoricVariableInstances(String processInstanceId) {
    notMocked();
    return null;
  }

  @Override
  public ProcessInstanceWrapper getHistoricProcessInstancesByBusinessKey(String businessKey) {

    notMocked();
    return null;
  }

  @Override
  public IdentityLink[] getIdentityLinksByTaskId(String taskId) {
    notMocked();
    return null;
  }

  @Override
  public TaskResponseWrapper getTasksByProcessInstanceId(String processInstanceId) {
    notMocked();
    return null;
  }

  @Override
  public TaskResponse deleteTask(String id) {
    notMocked();
    return null;
  }

  @Override
  public RestVariable getProcessInstanceVariable(String processInstanceId, String variableName) {
    notMocked();
    return null;
  }

  @Override
  public byte[] getSerializedHistoryVariable(String processInstanceId, String variableName) {
    notMocked();
    return null;
  }

}
